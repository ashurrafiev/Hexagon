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
		switch(level) {
			case 1: coreHealth += 5; break;
			case 2: mapSize += 2; break;
			case 3: sentryBaseAttack++; break;
			case 4: coreAttack += 1; break;
			case 0: 
			default: sentryBaseHealth += 2; break;
		}
	}
	
}
