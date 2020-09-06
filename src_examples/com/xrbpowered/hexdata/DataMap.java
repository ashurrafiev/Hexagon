package com.xrbpowered.hexdata;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import com.xrbpowered.hexagon.Dir;
import com.xrbpowered.hexagon.MapBase;
import com.xrbpowered.hexagon.PathMap;
import com.xrbpowered.hexagon.Pathfinder;

public class DataMap extends MapBase<DataTile> implements PathMap {

	private class LinkCheckerTile {
		public int index = -1;
		public boolean visited = false;
	}
	
	private class LinkChecker extends MapBase<LinkCheckerTile> implements PathMap {
		public final Pathfinder pathfinder;
		public int index = -1;
		
		public LinkChecker() {
			super(LinkCheckerTile.class, DataMap.this.size);
			pathfinder = new Pathfinder(this);
			clear();
		}

		@Override
		protected LinkCheckerTile clearTile(int x, int y) {
			return DataMap.this.tiles[x][y]==null ? null : new LinkCheckerTile();
		}

		@Override
		public void generate() {
			clear();
		}

		@Override
		public int getMoveCost(int x, int y, Dir d) {
			int tx = x+d.dx;
			int ty = y+d.dy;
			return !isInside(tx, ty) || tiles[tx][ty]==null ? -1 : 0;
		}

		@Override
		public void clearPaths() {
		}

		@Override
		public int pathCostAt(int x, int y) {
			return tiles[x][y].visited ? 0 : -1;
		}

		@Override
		public Dir pathDirAt(int x, int y) {
			return null;
		}

		@Override
		public void setPathAt(int x, int y, Dir d, int cost) {
			tiles[x][y].index = index;
			tiles[x][y].visited = true;
		}
		
		public boolean checkAndClean() {
			index = -1;
			for(int x=0; x<size; x++)
				for(int y=0; y<size; y++) {
					if(tiles[x][y]!=null && tiles[x][y].index<0) {
						index++;
						pathfinder.updatePaths(new Point(x, y), size*size);
					}
				}
			
			int[] count = new int[index+1];
			int max = 0;
			int maxIndex = -1;
			for(int x=0; x<size; x++)
				for(int y=0; y<size; y++) {
					if(tiles[x][y]!=null) {
						int index =  tiles[x][y].index;
						if(index>=0) {
							count[index]++;
							if(count[index]>max) {
								max = count[index];
								maxIndex = index;
							}
						}
					}
				}
			
			for(int x=0; x<size; x++)
				for(int y=0; y<size; y++) {
					if(tiles[x][y]!=null && tiles[x][y].index!=maxIndex)
						DataMap.this.tiles[x][y] = null;
				}
			return count[maxIndex]>=size*size/2;
		}
	}
	
	public final Point start = new Point();
	public Player player = new Player();
	public CoreObject core = null;
	public boolean justStarted = true;
	
	private final Pathfinder distFinder;
	
	public DataMap() {
		super(DataTile.class, LevelProgression.mapSize);
		distFinder = new Pathfinder(this);
		generate();
	}

	@Override
	protected DataTile clearTile(int x, int y) {
		//return (x>y*2 || y>(x+size)/2) ? null : new DataTile();
		return (x>y+size/2 || y>x+size/2) ? null : new DataTile();
	}

	private boolean generate(Random random) {
		clear();
		
		ArrayList<Point> items = new ArrayList<>();
		
		for(int x=0; x<size; x++)
			for(int y=0; y<size; y++) {
				if(tiles[x][y]==null)
					continue;
				int t = random.nextInt(12);
				if(t<=2)
					tiles[x][y] = null;
				else if(t==3) {
					tiles[x][y].object = ItemObject.randomItem(random, true);
					items.add(new Point(x, y));
				}
				else if(t==4)
					tiles[x][y].object = Sentry.randomSentry(random);
			}
		if(!new LinkChecker().checkAndClean())
			return false;
		
		for(Iterator<Point> i = items.iterator(); i.hasNext();) {
			Point p = i.next();
			if(tiles[p.x][p.y]==null)
				i.remove();
		}
		if(items.isEmpty())
			return false;
		
		DataTile startTile = null;
		do {
			start.x = random.nextInt(size);
			start.y = random.nextInt(size);
			startTile = tiles[start.x][start.y];
		} while(startTile==null || startTile.object!=null);
		startTile.discovered = true;
		startTile.visited = true;
		
		for(Dir d : Dir.values()) {
			int tx = start.x+d.dx;
			int ty = start.y+d.dy;
			if(isInside(tx, ty) && tiles[tx][ty]!=null
					&& tiles[tx][ty].object!=null && tiles[tx][ty].object.isBlocking()) {
				tiles[tx][ty].object = null;
			}
		}
		distFinder.updatePaths(start, size*size);
		Point core = null;
		int tries = items.size()*2;
		do {
			core = items.get(random.nextInt(items.size()));
			tries--;
			if(tries<0)
				return false;
		} while(tiles[core.x][core.y].dist<size/2);
		this.core = new CoreObject();
		tiles[core.x][core.y].object = this.core;
		
		discoverArea(start);
		
		return true;
	}

	@Override
	public void generate() {
		player = new Player();
		Random random = new Random();
		while(!generate(random)) {}
		justStarted = true;
	}
	
	public boolean isInside(int x, int y) {
		return x>=0 && x<size && y>=0 && y<size;
	}
	
	public void discoverArea(Point p) {
		for(Dir d : Dir.values()) {
			int tx = p.x+d.dx;
			int ty = p.y+d.dy;
			if(isInside(tx, ty) && tiles[tx][ty]!=null)
				tiles[tx][ty].discovered = true;
		}
		updateDists();
		updateBlocks();
		justStarted = false;
	}

	public void updateBlocks() {
		for(int x=0; x<size; x++)
			for(int y=0; y<size; y++) {
				if(tiles[x][y]==null)
					continue;
				if(tiles[x][y].object!=null && tiles[x][y].object.isBlocking()) {
					tiles[x][y].blocked = false;
					continue;
				}
				boolean blocked = false;
				for(Dir d : Dir.values()) {
					int nx = x+d.dx;
					int ny = y+d.dy;
					if(isInside(nx, ny) && tiles[nx][ny]!=null) {
						DataTile tile = tiles[nx][ny];
						if(tile.discovered && tile.object!=null && tile.object.isBlocking()) {
							blocked = true;
							break;
						}
					}
				}
				tiles[x][y].blocked = blocked;
			}
	}
	
	public void updateEffects() {
		GlobalEffect.resetAll();
		for(int x=0; x<size; x++)
			for(int y=0; y<size; y++) {
				if(tiles[x][y]==null || !tiles[x][y].discovered || tiles[x][y].object==null)
					continue;
				GlobalEffect.addCount(tiles[x][y].object.globalEffect());
			}
		GlobalEffect.applyAllGlobal();
		for(int x=0; x<size; x++)
			for(int y=0; y<size; y++) {
				if(tiles[x][y]==null || !tiles[x][y].discovered || tiles[x][y].object==null)
					continue;
				GlobalEffect.applyAllToObject(tiles[x][y].object);
			}
	}

	public int updateDists() {
		LinkedList<Point> sources = new LinkedList<>();
		int count = 0;
		for(int x=0; x<size; x++)
			for(int y=0; y<size; y++) {
				if(tiles[x][y]!=null && !tiles[x][y].discovered && tiles[x][y].object!=null && tiles[x][y].object.isSource()) {
					sources.add(new Point(x, y));
					count++;
				}
			}
		distFinder.updatePaths(sources, size*size);
		return count;
	}

	@Override
	public int getMoveCost(int x, int y, Dir d) {
		int tx = x+d.dx;
		int ty = y+d.dy;
		return !isInside(tx, ty) || tiles[tx][ty]==null ? -1 : 1;
	}

	@Override
	public void clearPaths() {
		for(int x=0; x<size; x++)
			for(int y=0; y<size; y++) {
				if(tiles[x][y]!=null)
					tiles[x][y].dist = -1;
			}
	}

	@Override
	public int pathCostAt(int x, int y) {
		return tiles[x][y].dist;
	}

	@Override
	public Dir pathDirAt(int x, int y) {
		return null;
	}

	@Override
	public void setPathAt(int x, int y, Dir d, int cost) {
		tiles[x][y].dist = cost;
	}

}
