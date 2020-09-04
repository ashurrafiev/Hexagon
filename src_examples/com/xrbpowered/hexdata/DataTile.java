package com.xrbpowered.hexdata;

import java.awt.Point;

public class DataTile {

	public TileObject object = null;
	
	public boolean discovered = false;
	public boolean visited = false;
	public boolean blocked = false;
	
	public int dist = -1;
	
	public boolean interact(DataMap map, Point pos) {
		if(!discovered || blocked)
			return false;
		if(object!=null) {
			if(object.interact(map, pos)) {
				object = null;
				map.updateBlocks();
			}
			map.updateEffects();
			return true;
		}
		if(!visited) {
			visited = true;
			map.discoverArea(pos);
			map.updateEffects();
			return true;
		}
		return false;
	}
	
}
