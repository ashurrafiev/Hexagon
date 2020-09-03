package com.xrbpowered.hexdata;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

import com.xrbpowered.zoomui.GraphAssist;

public abstract class ItemObject extends TileObject {

	public static final Color colorObject = Color.WHITE;
	public static final Color colorBlocked = new Color(0x555555);
	
	public final String glyph;
	public final Color color;
	
	public ItemObject(String glyph) {
		this(glyph, colorObject);
	}

	public ItemObject(String glyph, Color color) {
		this.glyph = glyph;
		this.color = color;
	}

	@Override
	public boolean isSource() {
		return true;
	}
	
	@Override
	public void paint(GraphAssist g, boolean highlight, boolean blocked) {
		paintItem(g, glyph, blocked ? colorBlocked : color, highlight);
	}
	
	public static void paintItem(GraphAssist g, String glyph, Color color, boolean highlight) {
		g.setColor(color);
		g.graph.fillOval(-13, -13, 26, 26);
		if(highlight) {
			g.resetStroke();
			g.graph.drawOval(-16, -16, 32, 32);
		}
		if(glyph!=null) {
			g.setFont(HexDataView.fontGlyph);
			g.setColor(Color.BLACK);
			g.drawString(glyph, 0, 0, GraphAssist.CENTER, GraphAssist.CENTER);
			g.setFont(HexDataView.font);
		}
	}
	
	public static final ItemObject addStrength = new ItemObject("S") {
		@Override
		public boolean interact(DataMap map, Point pos) {
			map.player.attack += 1;
			return true;
		}
	};

	public static final ItemObject addIntegrity = new ItemObject("I") {
		@Override
		public boolean interact(DataMap map, Point pos) {
			map.player.health += 3;
			return true;
		}
	};

	public static final ItemObject surge = new ItemObject("Z") {
		@Override
		public boolean interact(DataMap map, Point pos) {
			map.player.surge.count++;
			return true;
		}
	};

	public static final ItemObject shield = new ItemObject("X") {
		@Override
		public boolean interact(DataMap map, Point pos) {
			map.player.shield.count++;
			return true;
		}
	};

	private static final ItemObject[] items = {addStrength, addIntegrity, surge, shield};
	
	public static ItemObject randomItem(Random random, boolean wildcard) {
		if(wildcard && random.nextInt(4)>0)
			return new Wildcard(random); 
		return items[random.nextInt(items.length)];
	}

}
