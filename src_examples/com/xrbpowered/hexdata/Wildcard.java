package com.xrbpowered.hexdata;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

public class Wildcard extends ItemObject {

	public static final Color colorWildcard = new Color(0xffaa00);

	public final TileObject hiddenObject;
	
	public Wildcard(Random random) {
		super(null, colorWildcard);
		if(random.nextInt(2)>0)
			hiddenObject = Sentry.randomSentry(random);
		else
			hiddenObject = randomItem(random, false);
	}

	@Override
	public boolean interact(DataMap map, Point pos) {
		map.tiles[pos.x][pos.y].object = hiddenObject;
		map.updateBlocks();
		return false;
	}

}
