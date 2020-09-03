package com.xrbpowered.hexagon;

import java.lang.reflect.Array;

public abstract class MapBase<T> {

	public final int size;
	public final T[][] tiles;
	
	@SuppressWarnings("unchecked")
	public MapBase(Class<T> type, int size) {
		this.size = size;
		this.tiles = (T[][]) Array.newInstance(type, size, size);
		this.clear();
	}

	protected abstract T clearTile(int x, int y);
	public abstract void generate();
	
	public void clear() {
		for(int x=0; x<size; x++)
			for(int y=0; y<size; y++) {
				tiles[x][y] = clearTile(x, y);
			}
	}
	
}
