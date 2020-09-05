package com.xrbpowered.hexdata;

import java.awt.Color;

import com.xrbpowered.zoomui.GraphAssist;
import com.xrbpowered.zoomui.UIContainer;

import static com.xrbpowered.hexdata.LevelProgression.*;

public class VictoryDialog extends UIContainer {

	public static final Color colorBg = new Color(0xdd222222, true);
	public static final Color colorBorder = new Color(0x777777);
	public static final Color colorText = new Color(0xaaaaaa);
	public static final Color colorLevelUp = new Color(0xffaa00);
	public static final Color colorNegXp = new Color(0x550000);

	public static final int width = 480;
	public static final int height = 280;
	
	public final ClickButton nextButton;

	private boolean victory; 
	private boolean levelUp; 
	
	public VictoryDialog(UIContainer parent) {
		super(parent);
		nextButton = new ClickButton(this, 300) {
			@Override
			public Color[] getPalette() {
				return victory ? ClickButton.paletteGreen : ClickButton.paletteRed;
			}
			@Override
			public String getLabel() {
				return levelUp ? "LEVEL UP" : victory ? "NEXT BATTLE" : "TRY AGAIN";
			}
			@Override
			public void onClick() {
				if(!HexDataGame.game.isActive()) {
					VictoryDialog.this.setVisible(false);
					xp += getXp(victory);
					int levelXp = getLevelUpXp(level);
					if(xp>=levelXp) {
						xp -= levelXp;
						nextLevel();
						HexDataGame.levelUpDialog.setVisible(true);
					}
					else {
						HexDataGame.game.newMap();
					}
					repaint();
				}
			}
		};

		setSize(width, height);
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if(visible) {
			victory = !HexDataGame.game.map().core.isAlive();
			levelUp = xp+getXp(victory) >= getLevelUpXp(level);
		}
	}
	
	@Override
	public void layout() {
		nextButton.setLocation(getWidth()/2-nextButton.getWidth()/2, getHeight()-nextButton.getHeight()-24);
		super.layout();
	}
	
	@Override
	protected void paintSelf(GraphAssist g) {
		g.fill(this, colorBg);
		g.resetStroke();
		g.border(this, colorBorder);

		g.line(30, 64, width-30, 64, colorBorder);
		
		g.setFont(HexDataView.fontGlyph);
		g.setColor(Color.WHITE);
		g.drawString(victory ? "VICTORY!" : "DEFEAT!", getWidth()/2, 64-16, GraphAssist.CENTER, GraphAssist.BOTTOM);
		int addXp = getXp(victory);
		int levelXp = getLevelUpXp(level);
		g.setColor(addXp>0 ? Color.WHITE : xp<0 ? Color.RED : colorBorder);
		g.drawString(String.format("%+dXP", addXp), getWidth()/2, 96, GraphAssist.CENTER, GraphAssist.TOP);
		
		g.setFont(HexDataView.font);
		g.setColor(colorText);
		g.drawString(String.format("XP: %d/%d", xp+addXp, levelXp), 30, 120, GraphAssist.LEFT, GraphAssist.TOP);
		
		float w = width-60;
		g.fillRect(30, 140, w, 9, levelUp ? colorLevelUp : Color.BLACK);
		float prog = levelUp ? (xp+addXp-levelXp)*w/getLevelUpXp(level+1) : (xp+addXp)*w/levelXp;
		if(prog>0)
			g.fillRect(30, 140, prog, 9, Color.WHITE);
		if(addXp<0) {
			float red = -addXp*w/levelXp;
			g.fillRect(30+prog, 140, red, 9, colorNegXp);
		}
	}
	
	@Override
	public boolean onMouseDown(float x, float y, Button button, int mods) {
		return true;
	}
	
}
