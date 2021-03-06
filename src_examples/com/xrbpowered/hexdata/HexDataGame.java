package com.xrbpowered.hexdata;

import java.awt.event.KeyEvent;

import com.xrbpowered.zoomui.KeyInputHandler;
import com.xrbpowered.zoomui.UIContainer;
import com.xrbpowered.zoomui.swing.SwingFrame;
import com.xrbpowered.zoomui.swing.SwingWindowFactory;

public class HexDataGame  extends UIContainer implements KeyInputHandler {
	
	public static HexDataView game;
	public static StatusView status;
	public static VictoryDialog victoryDialog;
	public static LevelUpDialog levelUpDialog;

	public HexDataGame(UIContainer parent) {
		super(parent);
		game = new HexDataView(this);
		status = new StatusView(this);
		victoryDialog = new VictoryDialog(this);
		victoryDialog.setVisible(false);
		levelUpDialog = new LevelUpDialog(this);
		levelUpDialog.setVisible(false);
		game.newMap();
		getBase().setFocus(this);
	}
	
	@Override
	public void layout() {
		game.getParent().setSize(getWidth(), getHeight());
		status.setSize(getWidth(), status.getHeight());
		status.setLocation(0, getHeight()-status.getHeight());
		victoryDialog.setLocation(getWidth()/2-victoryDialog.getWidth()/2, getHeight()-status.getHeight()-victoryDialog.getHeight()-36);
		levelUpDialog.setLocation(getWidth()/2-levelUpDialog.getWidth()/2, getHeight()-status.getHeight()-levelUpDialog.getHeight()-36);
		super.layout();
	}
	
	@Override
	public boolean onKeyPressed(char c, int code, int mods) {
		switch(code) {
			case KeyEvent.VK_ENTER:
				//status.newMap();
				return true;
			case KeyEvent.VK_Z:
				status.surgeButton.toggle();
				return true;
			case KeyEvent.VK_X:
				status.shieldButton.toggle();
				return true;
		}
		return false;
	}

	@Override
	public void onFocusGained() {
	}

	@Override
	public void onFocusLost() {
	}

	public static void main(String[] args) {
		LevelProgression.load();
		SwingFrame frame = new SwingFrame(SwingWindowFactory.use(), "Data Analysis", 1920, 1080, false, true) {
			@Override
			public void onClose() {
				LevelProgression.forfeit(game.map());
				LevelProgression.save();
				super.onClose();
			}
		};
		new HexDataGame(frame.getContainer());
		frame.maximize();
		frame.show();
	}

}
