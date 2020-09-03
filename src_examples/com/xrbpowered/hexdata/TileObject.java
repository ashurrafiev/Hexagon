package com.xrbpowered.hexdata;

import java.awt.Point;

import com.xrbpowered.zoomui.GraphAssist;

public abstract class TileObject {

	public boolean isBlocking() {
		return false;
	}
	
	public abstract boolean isSource();
	public abstract boolean interact(DataMap map, Point pos);
	public abstract void paint(GraphAssist g, boolean highlight, boolean blocked);
	
}
