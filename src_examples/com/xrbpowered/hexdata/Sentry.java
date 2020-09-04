package com.xrbpowered.hexdata;

import java.awt.Color;
import java.util.Random;

import com.xrbpowered.zoomui.GraphAssist;

public class Sentry extends Enemy {

	public static final Color colorEnemy = new Color(0xdd0000);
	public static final Color colorEnemyBg = new Color(0x550000);

	public final String glyph;
	
	public Sentry(String glypth, int attack, int health) {
		super(attack, health);
		this.glyph = glypth;
	}

	public Sentry(int attack, int health) {
		this(null, attack, health);
	}

	@Override
	public boolean isBlocking() {
		return true;
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
		if(glyph!=null) {
			g.setFont(HexDataView.fontGlyph);
			g.setColor(Color.RED);
			g.drawString(glyph, 0, 0, GraphAssist.CENTER, GraphAssist.CENTER);
			g.setFont(HexDataView.font);
		}
		g.setColor(GlobalEffect.catalystMultiplier>1 ? StatusView.colorModeOn : Color.WHITE);
		g.drawString(Integer.toString(getAttackModified()), 0, -16, GraphAssist.CENTER, GraphAssist.BOTTOM);
		g.setColor(Color.WHITE);
		g.drawString(Integer.toString(health), 0, 16, GraphAssist.CENTER, GraphAssist.TOP);
	}

	public static Sentry randomSentry(Random random) {
		switch(random.nextInt(10)) {
			case 2:
			case 3:
				return new Sentry(LevelProgression.sentryBaseAttack*3, LevelProgression.sentryBaseHealth); // avenger
			case 4:
			case 5:
				return new Sentry(LevelProgression.sentryBaseAttack, LevelProgression.sentryBaseHealth*3); // guardian
			case 6:
				return new Sentry("C", LevelProgression.sentryBaseAttack*2, LevelProgression.sentryBaseHealth) { // catalyst
					@Override
					public GlobalEffect globalEffect() {
						return GlobalEffect.catalyst;
					}
				};
			case 7:
				return new Sentry("N", LevelProgression.sentryBaseAttack, LevelProgression.sentryBaseHealth*2) { // inhibitor
					@Override
					public GlobalEffect globalEffect() {
						return GlobalEffect.inhibitor;
					}
				};
			case 8:
				return new Sentry("P", LevelProgression.sentryBaseAttack, LevelProgression.sentryBaseHealth) { // power
					@Override
					public GlobalEffect globalEffect() {
						return GlobalEffect.power;
					}
				};
			case 9:
				return new Sentry("R", LevelProgression.sentryBaseAttack, LevelProgression.sentryBaseHealth) { // recovery
					@Override
					public GlobalEffect globalEffect() {
						return GlobalEffect.recovery;
					}
				};
			case 0:
			case 1:
			default:
				return new Sentry(LevelProgression.sentryBaseAttack*2, LevelProgression.sentryBaseHealth*2); // defender
		}
	}

}
