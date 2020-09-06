package com.xrbpowered.hexdata;

import static com.xrbpowered.hexdata.LevelProgression.*;

import java.awt.Color;

import com.xrbpowered.zoomui.GraphAssist;
import com.xrbpowered.zoomui.UIContainer;
import com.xrbpowered.zoomui.base.UIHoverElement;

public class LevelUpDialog extends UIContainer {

	public static final Color colorBg = new Color(0xdd222222, true);
	public static final Color colorBorder = new Color(0x777777);
	public static final Color colorBorderDark = new Color(0x555555);
	public static final Color colorBorderHover = Color.WHITE;
	public static final Color colorText = new Color(0xaaaaaa);

	public static final int width = 480;
	public static final int height = 360;
	public static final int optionHeight = 40;
	public static final int optionBoxSize = 24;

	private class OptionButton extends UIHoverElement {
		public final int option;
		
		public OptionButton(int option) {
			super(LevelUpDialog.this);
			this.option = option;
			setSize(width-60, optionHeight);
		}
		
		@Override
		public boolean onMouseDown(float x, float y, Button button, int mods) {
			if(button==Button.left) {
				selectedOption = option;
				repaint();
			}
			return true;
		}
		
		@Override
		public void paint(GraphAssist g) {
			g.fill(this, Color.BLACK);
			g.resetStroke();
			g.border(this, hover ? colorBorderHover : colorBorderDark);
			g.drawRect(16, 8, optionBoxSize, optionBoxSize, Color.WHITE);
			if(selectedOption==option) {
				g.setFont(HexDataView.fontGlyph);
				g.setColor(Color.WHITE);
				g.drawString("X", 16+optionBoxSize/2, getHeight()/2, GraphAssist.CENTER, GraphAssist.CENTER);
			}
			
			g.setFont(HexDataView.font);
			g.setColor(colorText);
			g.drawString(getPerkText(level, option), 56, getHeight()/2, GraphAssist.LEFT, GraphAssist.CENTER);
		}
	}

	public final OptionButton[] perkOptions = new OptionButton[2];
	public final ClickButton nextButton;
	
	private int selectedOption = -1;

	public LevelUpDialog(UIContainer parent) {
		super(parent);
		perkOptions[0] = new OptionButton(0);
		perkOptions[1] = new OptionButton(1);
		nextButton = new ClickButton(this, "NEXT BATTLE", 300, ClickButton.paletteGreen) {
			@Override
			public boolean isEnabled() {
				return selectedOption>=0;
			}
			@Override
			public void onClick() {
				if(!HexDataGame.game.isActive() && selectedOption>=0) {
					addPerk(level, selectedOption);
					save();
					HexDataGame.game.newMap();
					LevelUpDialog.this.setVisible(false);
					repaint();
				}
			}
		};

		setSize(width, height);
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		selectedOption = -1;
	}
	
	@Override
	public void layout() {
		perkOptions[0].setLocation(30, 168);
		perkOptions[1].setLocation(30, 168+48);
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
		g.drawString("LEVEL "+level, getWidth()/2, 64-16, GraphAssist.CENTER, GraphAssist.BOTTOM);
		
		g.setFont(HexDataView.font);
		g.setColor(Color.RED);
		g.drawString("DIFFICULTY INCREASES:", 30, 96, GraphAssist.LEFT, GraphAssist.TOP);
		g.setColor(colorText);
		g.drawString(getDifficultyText(level), 30, 112, GraphAssist.LEFT, GraphAssist.TOP);
		g.setColor(Color.WHITE);
		g.drawString("SELECT PERK:", 30, 144, GraphAssist.LEFT, GraphAssist.TOP);
	}
	
	@Override
	public boolean onMouseDown(float x, float y, Button button, int mods) {
		return true;
	}

}
