package com.xrbpowered.hexagon;

import java.awt.Point;

public enum Dir {

	NW(-1, -1),
	N(0, -1),
	NE(1, 0),
	SE(1, 1),
	S(0, 1),
	SW(-1, 0);
	
	public final int dx, dy;
	
	private Dir(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public Dir cw(int turns) {
		return values()[(ordinal()+turns)%6];
	}

	public Dir ccw(int turns) {
		return values()[(ordinal()-(turns%6)+6)%6];
	}
	
	public Dir cw() {
		return cw(1);
	}
	
	public Dir ccw() {
		return ccw(1);
	}

	public Dir rev() {
		return cw(3);
	}

	public Point move(Point src) {
		return new Point(src.x+dx, src.y+dy);
	}

}
