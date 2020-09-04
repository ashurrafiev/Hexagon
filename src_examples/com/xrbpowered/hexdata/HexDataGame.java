package com.xrbpowered.hexdata;

import java.awt.event.KeyEvent;

import com.xrbpowered.zoomui.KeyInputHandler;
import com.xrbpowered.zoomui.UIContainer;
import com.xrbpowered.zoomui.swing.SwingFrame;
import com.xrbpowered.zoomui.swing.SwingWindowFactory;

public class HexDataGame  extends UIContainer implements KeyInputHandler {
	
	public static HexDataView game;
	public static StatusView status;

	public HexDataGame(UIContainer parent) {
		super(parent);
		game = new HexDataView(this);
		status = new StatusView(this);
		game.newMap();
		getBase().setFocus(this);
	}
	
	@Override
	public void layout() {
		game.getParent().setSize(getWidth(), getHeight());
		status.setSize(getWidth(), status.getHeight());
		status.setLocation(0, getHeight()-status.getHeight());
		super.layout();
	}
	
	@Override
	public boolean onKeyPressed(char c, int code, int mods) {
		switch(code) {
			case KeyEvent.VK_BACK_SPACE:
				game.newMap();
				repaint();
				break;
			case KeyEvent.VK_Z:
				status.surgeButton.toggle();
				break;
			case KeyEvent.VK_X:
				status.shieldButton.toggle();
				break;
		}
		return true;
	}

	@Override
	public void onFocusGained() {
	}

	@Override
	public void onFocusLost() {
	}
	
	public static void main(String[] args) {
		SwingFrame frame = SwingWindowFactory.use().createFrame("Data Analysis", 1200, 800);
		new HexDataGame(frame.getContainer());
		frame.maximize();
		frame.show();
	}

}
