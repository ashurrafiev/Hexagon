package com.xrbpowered.hexmaze;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.geom.Path2D;
import java.util.ArrayList;

import com.xrbpowered.hexagon.Dir;
import com.xrbpowered.hexagon.HexView;
import com.xrbpowered.hexagon.Hexagon;
import com.xrbpowered.zoomui.GraphAssist;
import com.xrbpowered.zoomui.KeyInputHandler;
import com.xrbpowered.zoomui.UIContainer;
import com.xrbpowered.zoomui.swing.SwingFrame;
import com.xrbpowered.zoomui.swing.SwingWindowFactory;

public class HexMazeGame extends HexView<HexMazeMap.Tile> implements KeyInputHandler {

	public static final int pathLimit = 10;
	
	public static final Color pawnColor = new Color(0x00aaff);
	public static final Color pathColor = new Color(0xddeeff);
	
	protected final Shape ldot;
	protected final Shape sdot;

	public ArrayList<Point> path = null;
	private Path2D pathShape = null;
	
	public HexMazeGame(UIContainer parent) {
		super(parent, new Hexagon(16), Color.BLACK);
		
		this.ldot = hex.createCircle(0.75f);
		this.sdot = hex.createCircle(0.25f);
		
		setMap(new HexMazeMap());
		map().movePawn(map().pawn, pathLimit);
		panToCenter();
		
		getBase().setFocus(this);
	}
	
	public HexMazeMap map() {
		return (HexMazeMap) map;
	}

	private static final Dir[] borders = {Dir.SW, Dir.NW, Dir.N};
	
	@Override
	protected void paintTile(GraphAssist g, int x, int y, HexMazeMap.Tile tile) {
		Point pawn = map().pawn;
		g.setColor((tile.pathDir!=null) ? pathColor : tile.type.color);
		if(getScale()>0.25f) {
			g.graph.fill(hexPath);
			for(Dir d : borders) {
				int ax = x+d.dx;
				int ay = y+d.dy;
				if(ax>=0 && ax<map.size && ay>=0 && ay<map.size) {
					HexMazeMap.Tile adj = map.tiles[ax][ay];
					if(adj!=null && tile.type==adj.type && ((tile.pathDir!=null || pawn.x==x && pawn.y==y)^(adj.pathDir!=null || pawn.x==ax && pawn.y==ay))) {
						g.pushPureStroke(true);
						g.pushAntialiasing(true);
						g.setStroke(1f);
						g.setColor(pawnColor);
						g.line(hex.lineX1(d), hex.lineY1(d), hex.lineX2(d), hex.lineY2(d));
						g.popPureStroke();
						g.popAntialiasing();
					}
				}
			}
		}
		else {
			g.graph.fill(hexPixel);
		}
		if(pawn.x==x && pawn.y==y) {
			g.pushAntialiasing(true);
			g.setColor(pawnColor);
			g.graph.fill(ldot);
			g.popAntialiasing();
		}
	}
	
	@Override
	public void paint(GraphAssist g) {
		super.paint(g);
		
		if(pathShape!=null) {
			g.pushPureStroke(true);
			g.pushAntialiasing(true);
			
			g.setColor(pawnColor);
			g.setStroke(1.5f);
			g.graph.draw(pathShape);

			Point dest = path.get(0);
			g.pushTx();
			float x0 = hex.calcx(dest.x);
			float y0 = hex.calcy(dest.x, dest.y);
			g.translate(x0, y0);
			g.setColor(pawnColor);
			g.graph.fill(sdot);
			g.popTx();
			
			g.popAntialiasing();
			g.popPureStroke();
		}
		
		if(hover!=null) {
			g.pushPureStroke(true);
			g.pushAntialiasing(true);
			g.pushTx();
			
			float x0 = hex.calcx(hover.x);
			float y0 = hex.calcy(hover.x, hover.y);
			g.translate(x0, y0);
			
			g.setColor(new Color(0xccffffff, true));
			g.setStroke(4f);
			g.graph.draw(circle);
			g.setColor(pawnColor);
			g.setStroke(2f);
			g.graph.draw(circle);
			
			g.popTx();
			g.popAntialiasing();
			g.popPureStroke();
		}
	}
	
	@Override
	public boolean onMouseDown(float x, float y, Button button, int mods) {
		if(button==Button.left && hover!=null) {
			Point pawn = map().pawn;
			if(path!=null) {
				Point dest = path.get(0);
				if(hover.x==dest.x && hover.y==dest.y) {
					map().movePawn(dest, pathLimit);
					path = null;
					pathShape = null;
					repaint();
					return true;
				}
			}
			if(hover.x!=pawn.x || hover.y!=pawn.y) {
				ArrayList<Point> path = map().pathfinder.getPath(hover);
				if(path!=null) {
					this.path = path;
					pathShape = new Path2D.Float();
					boolean first = true;
					for(Point p : path) {
						float x0 = hex.calcx(p.x);
						float y0 = hex.calcy(p.x, p.y);
						if(first)
							pathShape.moveTo(x0, y0);
						else
							pathShape.lineTo(x0, y0);
						first = false;
					}
				}
				repaint();
			}
			return true;
		}
		else
			return super.onMouseDown(x, y, button, mods);
	}

	@Override
	public boolean onKeyPressed(char c, int code, int mods) {
		switch(code) {
			case KeyEvent.VK_BACK_SPACE:
				map.generate();
				map().movePawn(map().pawn, pathLimit);
				repaint();
				break;
		}
		return true;
	}

	@Override
	public void onFocusGained() {
	}

	@Override
	public void onFocusLost() {
	}

	public static void main(String[] args) {
		SwingFrame frame = SwingWindowFactory.use(1f).createFrame("HexView", 1200, 800);
		new HexMazeGame(frame.getContainer());
		frame.maximize();
		frame.show();
	}

}
