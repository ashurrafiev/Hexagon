package com.xrbpowered.hexdata;

import java.awt.Color;
import java.awt.Point;

import com.xrbpowered.zoomui.GraphAssist;

public class CoreObject extends Enemy {

	public static final Color colorBg = new Color(0xaa000000, true);
	public static final Color colorCore = new Color(0x00dd55);
	public static final Color colorCoreBg = new Color(0x005522);
	public static final Color colorBlocked = new Color(0x555555);
	public static final Color colorBlockedBg = new Color(0x222222);

	public CoreObject() {
		super(LevelProgression.coreAttack, LevelProgression.coreHealth);
	}
	
	@Override
	public boolean isSource() {
		return true;
	}

	@Override
	public boolean interact(DataMap map, Point pos) {
		if(isAlive()) {
			super.interact(map, pos);
			if(!isAlive())
				LevelProgression.nextLevel();
		}
		return false;
	}

	@Override
	public void paint(GraphAssist g, boolean highlight, boolean blocked) {
		boolean dead = !isAlive();
		Color fg = blocked ? colorBlocked : colorCore;
		Color bg = blocked ? colorBlockedBg : colorCoreBg;
		g.setColor(colorBg);
		g.graph.fillOval(-40, -40, 80, 80);
		g.setColor(dead ? Color.BLACK : bg);
		g.graph.fillOval(-20, -20, 40, 40);
		if(!dead) {
			g.setColor(Color.BLACK);
			g.graph.fillOval(-14, -14, 28, 28);
			g.setColor(bg);
			g.graph.fillOval(-10, -10, 20, 20);
			g.setColor(fg);
			g.setStroke(1.5f);
			float d = 20 / (float)Math.sqrt(2);
			g.line(-d, -d, d, d);
			g.line(-d, d, d, -d);
			g.resetStroke();
			g.setColor(fg);
			g.graph.drawOval(-14, -14, 28, 28);
			g.graph.fillOval(-4, -4, 8, 8);
		}
		g.setStroke(2f);
		g.setColor(fg);
		g.graph.drawOval(-20, -20, 40, 40);
		g.resetStroke();
		g.setColor(highlight ? fg : Color.BLACK);
		g.graph.drawOval(-40, -40, 80, 80);
		g.setColor(GlobalEffect.catalystMultiplier>1 ? StatusView.colorModeOn : Color.WHITE);
		g.drawString(Integer.toString(getAttackModified()), 0, -24, GraphAssist.CENTER, GraphAssist.BOTTOM);
		g.setColor(dead ? fg : Color.WHITE);
		g.drawString(Integer.toString(health), 0, 24, GraphAssist.CENTER, GraphAssist.TOP);
	}


}
