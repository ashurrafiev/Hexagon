package com.xrbpowered.hexagon;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

public class Pathfinder {

	public final PathMap map;
	
	public Pathfinder(PathMap map) {
		this.map = map;
	}

	public void updatePaths(Point source, int limit) {
		LinkedList<Point> tokens = new LinkedList<>();
		tokens.add(source);
		updatePaths(tokens, limit);
	}		

	public void updatePaths(LinkedList<Point> tokens, int limit) {
		map.clearPaths();
		for(Point s : tokens) {
			map.setPathAt(s.x, s.y, null, 0);
		}
		while(!tokens.isEmpty()) {
			Point p = tokens.removeFirst();
			int pcost = map.pathCostAt(p.x, p.y);
			if(pcost>=limit)
				continue;
			for(Dir d : Dir.values()) {
				Point t = d.move(p);
				int c = map.getMoveCost(p.x, p.y, d);
				if(c<0)
					continue;
				int tcost = map.pathCostAt(t.x, t.y);
				if(tcost>=0 && tcost<=pcost+c)
					continue;
				map.setPathAt(t.x, t.y, d, pcost+c);
				tokens.add(t);
			}
		}
	}
	
	public ArrayList<Point> getPath(Point dest) {
		if(map.pathDirAt(dest.x, dest.y)==null)
			return null;
		ArrayList<Point> path = new ArrayList<>();
		for(;;) {
			path.add(dest);
			Dir d = map.pathDirAt(dest.x, dest.y);
			if(d==null)
				break;
			dest = d.rev().move(dest);
		}
		return path;
	}
	
}
