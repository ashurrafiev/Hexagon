package com.xrbpowered.hexagon;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;

import com.xrbpowered.zoomui.GraphAssist;
import com.xrbpowered.zoomui.UIContainer;
import com.xrbpowered.zoomui.UIElement;
import com.xrbpowered.zoomui.base.UIZoomView;

public abstract class HexView<T> extends UIElement {

	public final Hexagon hex;

	protected final Shape hexPath;
	protected final Shape circle;
	protected final Shape hexPixel;
	
	private static class Zoom extends UIZoomView {
		private HexView<?> view;
		private Color bgColor;
		
		private Zoom(UIContainer parent, Color bgColor) {
			super(parent);
			this.bgColor = bgColor;
		}
		
		@Override
		public boolean onMouseScroll(float x, float y, float delta, int mods) {
			scale(1.0f-delta*0.1f, 0f, 0f);
			float cx = parentToLocalX(x);
			float cy = parentToLocalY(y);
			view.updateHoverTile(cx, cy);
			repaint();
			return true;
		}
		
		@Override
		protected float parentToLocalX(float x) {
			return super.parentToLocalX(x-getWidth()/2f);
		}

		@Override
		protected float parentToLocalY(float y) {
			return super.parentToLocalY(y-getHeight()/2f);
		}
		
		@Override
		protected void applyTransform(GraphAssist g) {
			g.translate(-panX+getWidth()/2f, -panY+getHeight()/2f);
			g.scale(scale);
		}
		
		@Override
		protected void paintSelf(GraphAssist g) {
			if(bgColor!=null)
				g.fill(this, bgColor);
		}
		
		@Override
		protected void paintChildren(GraphAssist g) {
			g.pushAntialiasing(false);
			super.paintChildren(g);
			g.popAntialiasing();
		}
	}
	
	public MapBase<T> map = null;
	public Point hover = null; 
	
	public HexView(UIContainer parent, Hexagon hex, Color bgColor) {
		super(new Zoom(parent, bgColor));
		((Zoom)getParent()).view = this;
		this.hex = hex;
		this.hexPath = hex.createPath();
		this.circle = hex.createCircle(1f);
		this.hexPixel = hex.createRect();
	}
	
	public HexView<T> setMap(MapBase<T> map) {
		this.map = map;
		return this;
	}
	
	public boolean updateHoverTile(float x, float y) {
		Point m = new Point();
		hex.worldPos(m, x, y);
		loop:
		for(int ix=0; ix<=1; ix++)
			for(int iy=0; iy<=1; iy++) {
				float x0 = hex.calcx(m.x+ix); // (mx+ix)*(w+a);
				float y0 = hex.calcy(m.x+ix, m.y+iy); //(-(mx+ix)+2*(my+iy))*h;
				if(hexPath.contains(x-x0, y-y0)) {
					m.x += ix;
					m.y += iy;
					break loop;
				}
			}

		if(hover==null || hover.x!=m.x || hover.y!=m.y) {
			hover = m;
			return true;
		}
		else
			return false;
	}
	
	@Override
	public void onMouseMoved(float x, float y, int mods) {
		if(updateHoverTile(x, y))
			repaint();
	}
	
	@Override
	public void onMouseOut() {
		hover = null;
		repaint();
	}
	
	@Override
	public boolean onMouseDown(float x, float y, Button button, int mods) {
		updateHoverTile(x, y);
		return super.onMouseDown(x, y, button, mods);
	}
	
	public void panToTile(int x, int y) {
		float x0 = hex.calcx(x);
		float y0 = hex.calcy(x, y);
		((UIZoomView)getParent()).setPan(x0, y0);
	}
	
	public void panToCenter() {
		panToTile(map.size/2, map.size/2);
	}
	
	@Override
	public boolean isInside(float x, float y) {
		return true;
	}

	@Override
	public boolean isVisible(Rectangle clip) {
		return true;
	}
	
	public float getScale() {
		return ((UIZoomView)getParent()).getScale();
	}
	
	public void resetScale() {
		((UIZoomView)getParent()).setScale(1f);
	}
	
	protected abstract void paintTile(GraphAssist g, int x, int y, T tile);
	
	protected void paintTiles(GraphAssist g, Rectangle clip) {
		g.pushPureStroke(true);
		for(int x=0; x<map.size; x++)
			for(int y=0; y<map.size; y++) {
				float x0 = hex.calcx(x);
				float y0 = hex.calcy(x, y);
				if(hex.insideClip(x0, y0, clip)) {
					g.pushTx();
					g.translate(x0, y0);
					paintTile(g, x, y, map.tiles[x][y]);
					g.popTx();
				}
			}
		g.popPureStroke();
	}
	
	@Override
	public void paint(GraphAssist g) {
		if(map==null)
			return;
		Rectangle clip = g.graph.getClipBounds();
		Point m = new Point();
		hex.worldPos(m, clip.x, clip.y);
		paintTiles(g, clip);
	}
	
}
