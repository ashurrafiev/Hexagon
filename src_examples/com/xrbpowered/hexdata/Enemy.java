package com.xrbpowered.hexdata;

import java.awt.Point;

public abstract class Enemy extends TileObject {

	public int attack;
	public int health;
	
	public Enemy(int attack, int health) {
		this.attack = attack;
		this.health = health;
	}
	
	@Override
	public boolean isSource() {
		return false;
	}

	@Override
	public boolean interact(DataMap map, Point pos) {
		health -= map.player.getAttackModified();
		if(health<0)
			health = 0;
		map.player.surge.use();
		map.player.damage(attack);
		return health<=0;
	}

}
