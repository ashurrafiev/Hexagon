package com.xrbpowered.hexdata;

import java.awt.Color;
import java.awt.Point;

import com.xrbpowered.zoomui.GraphAssist;

public class ItemObject extends TileObject {

	public static final Color colorObject = new Color(0xffaa00);
	public static final Color colorBlocked = new Color(0x555555);
	
	@Override
	public boolean isSource() {
		return true;
	}
	
	@Override
	public boolean interact(DataMap map, Point pos) {
		return true;
	}

	@Override
	public void paint(GraphAssist g, boolean highlight, boolean blocked) {
		g.setColor(blocked ? colorBlocked : colorObject);
		g.graph.fillOval(-12, -12, 24, 24);
		g.setStroke(2f);
		g.graph.drawOval(-12, -12, 24, 24);
		if(highlight) {
			g.resetStroke();
			g.setColor(colorObject);
			g.graph.drawOval(-16, -16, 32, 32);
		}
	}

}
