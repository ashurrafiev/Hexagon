package com.xrbpowered.hexagon;

public interface PathMap {
	public int getMoveCost(int x, int y, Dir d);
	public void clearPaths();
	public int pathCostAt(int x, int y);
	public Dir pathDirAt(int x, int y);
	public void setPathAt(int x, int y, Dir d, int cost);
}