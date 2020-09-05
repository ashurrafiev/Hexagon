package com.xrbpowered.hexdata;

public class LevelProgression {

	public static int level = 0;
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
	
	public static String nextLevelText() {
		switch(level%5) {
			case 1: return "+5 INTEGRITY to DATA CORE";
			case 2: return "+2 map size";
			case 3: return "+1 base STRENGTH to all sentries";
			case 4: return "+5 STRENGTH to DATA CORE";
			case 0: 
			default: return "+2 base INTEGRITY to all sentries";
		}
	}
	
}
