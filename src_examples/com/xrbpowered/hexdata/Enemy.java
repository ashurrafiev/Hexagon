package com.xrbpowered.hexdata;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

import com.xrbpowered.zoomui.GraphAssist;

public class Enemy extends TileObject {

	public static final Color colorEnemy = new Color(0xdd0000);
	public static final Color colorEnemyBg = new Color(0x550000);

	public int attack = 2;
	public int health = 10;
	
	public Enemy(Random random) {
		if(random.nextBoolean())
			attack = 2+random.nextInt(3);
		else
			health = 10+random.nextInt(3)*5;
	}
	
	@Override
	public boolean isBlocking() {
		return true;
	}
	
	@Override
	public boolean isSource() {
		return false;
	}

	@Override
	public boolean interact(DataMap map, Point pos) {
		health -= map.playerAttack;
		map.damagePlayer(attack);
		return health<=0;
	}

	@Override
	public void paint(GraphAssist g, boolean highlight, boolean blocked) {
		g.setColor(colorEnemyBg);
		g.graph.fillOval(-12, -12, 24, 24);
		g.setStroke(2f);
		g.setColor(colorEnemy);
		g.graph.drawOval(-12, -12, 24, 24);
		if(highlight) {
			g.resetStroke();
			g.graph.drawOval(-16, -16, 32, 32);
		}
		g.setColor(Color.WHITE);
		g.drawString(Integer.toString(attack), 0, -16, GraphAssist.CENTER, GraphAssist.BOTTOM);
		g.drawString(Integer.toString(health), 0, 16, GraphAssist.CENTER, GraphAssist.TOP);
	}

}
