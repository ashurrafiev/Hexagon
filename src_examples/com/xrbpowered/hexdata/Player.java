package com.xrbpowered.hexdata;

public class Player {

	public static final double surgeMultiplier = 3.0;
	
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
	
	public int attack;
	public int health;
	public Mode surge;
	public Mode shield;
	
	public Player() {
		this.attack = LevelProgression.playerBaseAttack;
		this.health = LevelProgression.playerBaseHealth;
		this.surge = new Mode("SURGE", ItemObject.surge.glyph, LevelProgression.startSurge);
		this.shield = new Mode("SHIELD", ItemObject.shield.glyph, LevelProgression.startShield);
	}
	
	public int getAttackModified() {
		return (int)Math.ceil((surge.canUse() ? surgeMultiplier : 1.0)*GlobalEffect.inhibitorMultiplier*attack);
	}
	
	public void damage(int damage) {
		if(!isAlive())
			return;
		if(damage>0 && shield.canUse()) {
			shield.use();
			return;
		}
		health -= damage;
		if(health<0)
			health = 0;
		if(!isAlive())
			HexDataGame.victoryDialog.setVisible(true);
	}
	
	public boolean isAlive() {
		return health>0;
	}

}
