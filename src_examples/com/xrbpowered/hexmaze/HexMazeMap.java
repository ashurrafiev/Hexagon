package com.xrbpowered.hexmaze;

import java.awt.Color;
import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;

import com.xrbpowered.hexagon.Dir;
import com.xrbpowered.hexagon.Hexagon;
import com.xrbpowered.hexagon.MapBase;
import com.xrbpowered.hexagon.PathMap;
import com.xrbpowered.hexagon.Pathfinder;

public class HexMazeMap extends MapBase<HexMazeMap.Tile> implements PathMap {

	public static final int size = 128;
	
	public static enum TileType {
		unknown(32, false), solid(64, false), clear(255, true);
		
		public final boolean passable;
		public final Color color;
		
		private TileType(int c, boolean passable) {
			this.color = new Color(c, c, c);
			this.passable = passable;
		}
	}
	
	public static class Tile {
		public TileType type;
		public int pathCost = -1;
		public Dir pathDir = null;
		
		public Tile(TileType type) {
			this.type = type;
		}
	}
	
	public static final Random random = new Random();
	
	public final Pathfinder pathfinder;
	public Point pawn = new Point();
	
	public HexMazeMap() {
		super(Tile.class, size);
		this.pathfinder = new Pathfinder(this);
		generate();
	}
	
	public void movePawn(Point pos, int pathLimit) {
		pawn.setLocation(pos);
		pathfinder.updatePaths(pawn, pathLimit);
	}
	
	@Override
	protected Tile clearTile(int x, int y) {
		return new Tile(TileType.unknown);
	}
	
	public void generate() {
		clear();
		LinkedList<Point> tokens = new LinkedList<>();
		pawn.setLocation(size/2, size/2);
		tokens.add(pawn);
		int first = 1;
		int count = 1;
		while(!tokens.isEmpty()) {
			Point p = tokens.remove(random.nextInt(count));
			count--;
			if(p.x<0 || p.x>=size || p.y<0 || p.y>=size)
				continue;
			if(tiles[p.x][p.y].type!=TileType.unknown)
				continue;
			boolean pass = true;
			if(first>0) {
				tiles[p.x][p.y].type = TileType.clear;
				first--;
			}
			else {
				int dist = Hexagon.dist(size/2, size/2, p.x, p.y);
				float s = dist*2/(float)size;
				float prob = 0.7f*s + 0.3f*(1f-s);
				pass = random.nextFloat()>prob; // 0.3-0.7; 0.5 - avg
				tiles[p.x][p.y].type = pass ? TileType.clear : TileType.solid;
			}
			if(pass) {
				for(Dir d : Dir.values()) {
					tokens.add(d.move(p));
					count++;
				}
			}
		}
	}

	@Override
	public void clearPaths() {
		for(int x=0; x<size; x++)
			for(int y=0; y<size; y++) {
				tiles[x][y].pathCost = -1;
				tiles[x][y].pathDir = null;
			}
	}
	
	@Override
	public int getMoveCost(int x, int y, Dir d) {
		return tiles[x+d.dx][y+d.dy].type.passable ? 1 : -1;
	}
	
	@Override
	public int pathCostAt(int x, int y) {
		return tiles[x][y].pathCost;
	}
	
	@Override
	public Dir pathDirAt(int x, int y) {
		return tiles[x][y].pathDir;
	}
	
	@Override
	public void setPathAt(int x, int y, Dir d, int cost) {
		tiles[x][y].pathDir = d;
		tiles[x][y].pathCost = cost;
	}

}
