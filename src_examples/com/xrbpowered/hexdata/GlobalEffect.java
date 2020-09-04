package com.xrbpowered.hexdata;

import java.util.ArrayList;

public abstract class GlobalEffect {

	public static final double baseCatalystMultiplier = 1;
	public static final double baseInhibitorMultiplier = 0.5;
	public static final int powerIncrement = 1;
	public static final int recoveryIncrement = 1;
	
	public static ArrayList<GlobalEffect> effects = new ArrayList<>();
	
	public int count = 0;
	
	protected GlobalEffect() {
		effects.add(this);
	}
	
	public void applyGlobal() {
	}

	public void applyToEnemy(Enemy enemy) {
	}

	public void applyToObject(TileObject obj) {
		if(obj instanceof Enemy) {
			Enemy enemy = (Enemy) obj;
			if(enemy.isAlive())
				applyToEnemy(enemy);
		}
	}
	
	public static void resetAll() {
		for(GlobalEffect effect : effects)
			effect.count = 0;
	}
	
	public static void addCount(GlobalEffect effect) {
		if(effect!=null)
			effect.count++;
	}

	public static void applyAllGlobal() {
		for(GlobalEffect effect : effects)
			effect.applyGlobal();
	}

	public static void applyAllToObject(TileObject obj) {
		for(GlobalEffect effect : effects)
			effect.applyToObject(obj);
	}

	public static double catalystMultiplier = 1;
	public static double inhibitorMultiplier = 1;

	public static final GlobalEffect catalyst = new GlobalEffect() {
		@Override
		public void applyGlobal() {
			catalystMultiplier = 1.0 + count*baseCatalystMultiplier;
		}
	};
	
	public static final GlobalEffect inhibitor = new GlobalEffect() {
		@Override
		public void applyGlobal() {
			inhibitorMultiplier = Math.pow(baseInhibitorMultiplier, count);
		}
	};
	
	public static final GlobalEffect power = new GlobalEffect() {
		@Override
		public void applyToEnemy(Enemy enemy) {
			enemy.attack += count*GlobalEffect.powerIncrement;
		}
	};

	public static final GlobalEffect recovery = new GlobalEffect() {
		@Override
		public void applyToEnemy(Enemy enemy) {
			enemy.health += count*GlobalEffect.recoveryIncrement;
		}
	};

}
