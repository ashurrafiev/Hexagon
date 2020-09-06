package com.xrbpowered.hexdata;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Path2D;

import com.xrbpowered.zoomui.GraphAssist;
import com.xrbpowered.zoomui.UIContainer;
import com.xrbpowered.zoomui.base.UIHoverElement;

public class ClickButton extends UIHoverElement {

	public static final int height = 48;
	
	public static final Color[] paletteRed = {Color.WHITE, new Color(0x550000), new Color(0xdd0000)};
	public static final Color[] paletteGreen = {Color.WHITE, new Color(0x005522), new Color(0x00dd55)};
	public static final Color[] paletteBlack = {Color.WHITE, Color.BLACK, new Color(0x777777)};
	public static final Color[] paletteDisabled = {new Color(0xaaaaaa), Color.BLACK, new Color(0x777777)};
	
	public static final int colorText = 0;
	public static final int colorBg = 1;
	public static final int colorBorder = 2;
	
	public String label;
	public Color[] palette;
	
	private Shape outline;
	
	public ClickButton(UIContainer parent, String label, int width, Color[] palette) {
		super(parent);
		this.label = label;
		this.palette = palette;
		setSize(width, height);
	}

	public ClickButton(UIContainer parent, int width) {
		this(parent, null, width, null);
	}

	@Override
	public void setSize(float width, float height) {
		super.setSize(width, height);
		float delta = (float)Math.round(height*0.5/Math.tan(Math.PI/3.0));
		Path2D.Float p = new Path2D.Float();
		p.moveTo(0, height/2);
		p.lineTo(delta, 0);
		p.lineTo(width-delta, 0);
		p.lineTo(width, height/2);
		p.lineTo(width-delta, height);
		p.lineTo(delta, height);
		p.closePath();
		this.outline = p;
	}
	
	public boolean isEnabled() {
		return true;
	}
	
	public Color[] getPalette() {
		return isEnabled() ? palette : paletteDisabled;
	}

	public String getLabel() {
		return label;
	}
	
	public void onClick() {
	}
	
	@Override
	public void paint(GraphAssist g) {
		Color[] palette = getPalette();
		boolean hover = this.hover && isEnabled();
		g.setColor(hover ? palette[colorBorder] : palette[colorBg]);
		g.graph.fill(outline);
		if(!hover) {
			g.setStroke(1.5f);
			g.setColor(palette[colorBorder]);
			g.graph.draw(outline);
		}
		g.setColor(palette[colorText]);
		g.setFont(HexDataView.fontGlyph);
		g.drawString(getLabel(), getWidth()/2, getHeight()/2, GraphAssist.CENTER, GraphAssist.CENTER);
	}
	
	@Override
	public boolean onMouseDown(float x, float y, Button button, int mods) {
		if(button==Button.left && isEnabled())
			onClick();
		return true;
	}

}
