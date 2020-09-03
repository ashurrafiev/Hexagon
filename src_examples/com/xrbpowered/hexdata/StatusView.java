package com.xrbpowered.hexdata;

import java.awt.Color;

import com.xrbpowered.zoomui.GraphAssist;
import com.xrbpowered.zoomui.UIContainer;
import com.xrbpowered.zoomui.base.UIHoverElement;

public class StatusView extends UIContainer {

	public static final Color colorBg = new Color(0xaa000000, true);
	public static final Color colorBorder = new Color(0xaaaaaa);
	public static final Color colorFrame = new Color(0xcccccc);
	public static final Color colorPlayer = Color.WHITE;
	public static final Color colorPlayerBg = new Color(0x777777);
	public static final Color colorModeOn = new Color(0xffaa00);
	public static final Color colorModeOff = Color.WHITE;
	public static final Color colorModeNone = new Color(0x777777);
	
	public class ModeButton extends UIHoverElement {
		public Player.Mode mode = null;
		
		public ModeButton() {
			super(StatusView.this);
			setSize(100, 0);
		}
		
		@Override
		public void paint(GraphAssist g) {
			if(mode==null)
				return;
			
			g.pushPureStroke(true);
			g.pushAntialiasing(true);
			g.pushTx();
			g.translate(getWidth()/2, getHeight()/2);
			
			ItemObject.paintItem(g, mode.glyph,
					mode.canUse() ? colorModeOn : mode.count==0 ? colorModeNone : colorModeOff,
					hover && HexDataGame.game.isActive());
			g.setColor(colorFrame);
			g.drawString(mode.name, 0, -48, GraphAssist.CENTER, GraphAssist.BOTTOM);
			g.setColor(mode.canUse() ? colorModeOn : colorBorder);
			g.drawString(mode.enabled ? "ON" : "OFF", 0, -24, GraphAssist.CENTER, GraphAssist.BOTTOM);
			g.setColor(mode.count==0 ? Color.RED : Color.WHITE);
			g.drawString(Integer.toString(mode.count), 0, 24, GraphAssist.CENTER, GraphAssist.TOP);
			
			g.popTx();
			g.popAntialiasing();
			g.popPureStroke();
		}
		
		@Override
		public boolean onMouseDown(float x, float y, Button button, int mods) {
			if(button==Button.left) {
				mode.enabled = !mode.enabled;
				repaint();
			}
			return true;
		}
	}
	
	public final ModeButton surgeButton;
	public final ModeButton shieldButton;
	
	public Player player;
	
	public StatusView(UIContainer parent) {
		super(parent);
		this.surgeButton = new ModeButton();
		this.shieldButton = new ModeButton();
		setSize(0, 140);
	}
	
	public void setPlayer(Player player) {
		this.player = player;
		this.surgeButton.mode = player.surge;
		this.shieldButton.mode = player.shield;
	}
	
	@Override
	public void layout() {
		surgeButton.setSize(surgeButton.getWidth(), getHeight());
		surgeButton.setLocation(getWidth()/2-surgeButton.getWidth()/2-100, 0);
		shieldButton.setSize(shieldButton.getWidth(), getHeight());
		shieldButton.setLocation(getWidth()/2-shieldButton.getWidth()/2+100, 0);
		super.layout();
	}
	
	@Override
	protected void paintSelf(GraphAssist g) {
		g.fill(this, colorBg);
		g.resetStroke();
		g.hborder(this, GraphAssist.TOP, colorBorder);
		if(player==null)
			return;
		
		boolean dead = !player.isAlive();
		
		g.pushPureStroke(true);
		g.pushAntialiasing(true);
		g.pushTx();
		g.translate(getWidth()/2, getHeight()/2);
		
		g.setColor(Color.BLACK);
		g.graph.fillOval(-40, -40, 80, 80);
		if(!dead) {
			g.setColor(colorPlayerBg);
			g.graph.fillOval(-16, -16, 32, 32);
			g.setColor(colorPlayer);
			g.resetStroke();
			g.graph.drawOval(-16, -16, 32, 32);
			g.graph.drawOval(-8, -8, 16, 16);
			g.line(0, -16, 0, 16);
			g.line(-16, 0, 16, 0);
		}
		g.setStroke(2f);
		g.setColor(dead ? Color.RED : colorPlayer);
		g.graph.drawOval(-20, -20, 40, 40);
		g.resetStroke();
		g.setColor(colorFrame);
		g.graph.drawOval(-40, -40, 80, 80);
		g.drawString("STRENGTH", 0, -48, GraphAssist.CENTER, GraphAssist.BOTTOM);
		g.drawString("INTEGRITY", 0, 48, GraphAssist.CENTER, GraphAssist.TOP);
		g.setColor(player.surge.canUse() ? colorModeOn : Color.WHITE);
		g.drawString(Integer.toString(player.getAttackModified()), 0, -24, GraphAssist.CENTER, GraphAssist.BOTTOM);
		g.setColor(dead ? Color.RED : player.shield.canUse() ? colorModeOn : Color.WHITE);
		g.drawString(Integer.toString(player.health), 0, 24, GraphAssist.CENTER, GraphAssist.TOP);
		
		g.popTx();
		g.popAntialiasing();
		g.popPureStroke();
	}
	
	@Override
	public boolean onMouseDown(float x, float y, Button button, int mods) {
		return true;
	}

}
