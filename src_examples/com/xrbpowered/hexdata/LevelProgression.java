package com.xrbpowered.hexdata;

public class LevelProgression {

	public static int xp = 0;
	public static int level = 0;
	public static int battleCount = 1;
	public static int mapSize = 9;
	
	public static int playerBaseAttack = 5;
	public static int playerBaseHealth = 20;
	public static int startSurge = 1;
	public static int startShield = 1;

	public static int coreAttack = 2;
	public static int coreHealth = 15;
	public static int sentryBaseAttack = 2;
	public static int sentryBaseHealth = 5;

	public static void nextLevel() {
		level++;
		switch(level%5) {
			case 1: coreHealth += 5; break;
			case 2: mapSize += 2; break;
			case 3: sentryBaseAttack++; break;
			case 4: coreAttack += 1; break;
			case 0: 
			default: sentryBaseHealth += 2; break;
		}
	}

	public static void addPerk(int level, int option) {
		if(level%5==0) {
			switch(option) {
				case 1: startShield++; break;
				case 0:
				default: startSurge++; break;
			}
		}
		else {
			switch(option) {
				case 1: playerBaseHealth += 2; break;
				case 0:
				default: playerBaseAttack += 1; break;
			}
		}
	}

	public static int getXp(boolean victory) {
		return victory ? 20 : Math.max(-xp, -5);
	}
	
	public static int getLevelUpXp(int level) {
		return (level+1)*20;
	}
	
	public static String getDifficultyText(int level) {
		switch(level%5) {
			case 1: return "+5 INTEGRITY to DATA CORE";
			case 2: return "+2 map size";
			case 3: return "+1 base STRENGTH to all sentries";
			case 4: return "+5 STRENGTH to DATA CORE";
			case 0: 
			default: return "+2 base INTEGRITY to all sentries";
		}
	}
	
	public static String getPerkText(int level, int option) {
		switch(option) {
			case 1:
				return (level%5==0) ? "+1 extra SHIELD at start" : "+2 base INTEGRITY";
			case 0:
			default:
				return (level%5==0) ? "+1 extra SURGE at start" : "+1 base STRENGTH";
		}
	}
	
}
