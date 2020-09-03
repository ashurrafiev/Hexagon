package com.xrbpowered.hexdata;

public class Player {

	public static final int surgeMultiplier = 3;
	
	public class Mode {
		public final String name, glyph;
		
		public int count;
		public boolean enabled = false;
		
		public Mode(String name, String glyph, int count) {
			this.name = name;
			this.glyph = glyph;
			this.count = count;
		}
		
		public boolean canUse() {
			return enabled && count>0;
		}
		
		public void use() {
			if(canUse())
				count--;
		}
	}
	
	public int attack = 5;
	public int health = 25;

	public Mode surge = new Mode("SURGE", ItemObject.surge.glyph, 1);
	public Mode shield = new Mode("SHIELD", ItemObject.shield.glyph, 1);
	
	public int getAttackModified() {
		return surge.canUse() ? surgeMultiplier*attack : attack;
	}
	
	public void damage(int damage) {
		if(damage>0 && shield.canUse()) {
			shield.use();
			return;
		}
		health -= damage;
		if(health<0)
			health = 0;
	}
	
	public boolean isAlive() {
		return health>0;
	}

}
