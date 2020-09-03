package com.xrbpowered.hexdata;

import java.awt.Color;

import com.xrbpowered.zoomui.GraphAssist;
import com.xrbpowered.zoomui.UIContainer;

public class StatusView extends UIContainer {

	public static final Color colorBg = new Color(0xaa000000, true);
	public static final Color colorBorder = new Color(0xaaaaaa);
	public static final Color colorFrame = new Color(0xcccccc);
	public static final Color colorPlayer = Color.WHITE;
	public static final Color colorPlayerBg = new Color(0x777777);
	
	public final HexDataView game;
	
	public StatusView(UIContainer parent, HexDataView game) {
		super(parent);
		this.game = game;
		setSize(0, 140);
	}
	
	@Override
	protected void paintSelf(GraphAssist g) {
		g.fill(this, colorBg);
		g.resetStroke();
		g.hborder(this, GraphAssist.TOP, colorBorder);
		
		DataMap map = game.map();
		boolean dead = !map.isPlayerAlive();
		
		g.pushPureStroke(true);
		g.pushAntialiasing(true);
		g.pushTx();
		g.translate(getWidth()/2, getHeight()/2);
		
		g.setColor(Color.BLACK);
		g.graph.fillOval(-40, -40, 80, 80);
		g.setColor(dead ? Color.BLACK : colorPlayerBg);
		g.graph.fillOval(-20, -20, 40, 40);
		g.setStroke(2f);
		g.setColor(dead ? Color.RED : colorPlayer);
		g.graph.drawOval(-20, -20, 40, 40);
		g.resetStroke();
		g.setColor(colorFrame);
		g.graph.drawOval(-40, -40, 80, 80);
		g.drawString("STRENGTH", 0, -48, GraphAssist.CENTER, GraphAssist.BOTTOM);
		g.drawString("INTEGRITY", 0, 48, GraphAssist.CENTER, GraphAssist.TOP);
		g.setColor(Color.WHITE);
		g.drawString(Integer.toString(map.playerAttack), 0, -24, GraphAssist.CENTER, GraphAssist.BOTTOM);
		if(dead) g.setColor(Color.RED);
		g.drawString(Integer.toString(map.playerHealth), 0, 24, GraphAssist.CENTER, GraphAssist.TOP);
		
		g.popTx();
		g.popAntialiasing();
		g.popPureStroke();
	}

}
