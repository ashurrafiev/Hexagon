package com.xrbpowered.hexdata;

import java.awt.Color;
import java.awt.Point;

import com.xrbpowered.zoomui.GraphAssist;

public class CoreObject extends TileObject {

	public static final Color colorBg = new Color(0xaa000000, true);
	public static final Color colorCore = new Color(0x00dd55);
	public static final Color colorCoreBg = new Color(0x005522);

	public int attack = 2;
	public int health = 20;

	public boolean isAlive() {
		return health>0;
	}
	
	@Override
	public boolean isSource() {
		return true;
	}

	@Override
	public boolean interact(DataMap map, Point pos) {
		health -= map.playerAttack;
		if(health<0)
			health = 0;
		map.damagePlayer(attack);
		return false;
	}

	@Override
	public void paint(GraphAssist g, boolean highlight, boolean blocked) {
		g.setColor(colorBg);
		g.graph.fillOval(-40, -40, 80, 80);
		g.setColor(colorCoreBg);
		g.graph.fillOval(-20, -20, 40, 40);
		g.setStroke(2f);
		g.setColor(colorCore);
		g.graph.drawOval(-20, -20, 40, 40);
		if(highlight) {
			g.resetStroke();
			g.graph.drawOval(-40, -40, 80, 80);
		}
		g.setColor(Color.WHITE);
		g.drawString(Integer.toString(attack), 0, -24, GraphAssist.CENTER, GraphAssist.BOTTOM);
		if(!isAlive()) g.setColor(colorCore);
		g.drawString(Integer.toString(health), 0, 24, GraphAssist.CENTER, GraphAssist.TOP);
	}


}
