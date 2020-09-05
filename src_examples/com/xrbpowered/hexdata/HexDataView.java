package com.xrbpowered.hexdata;

import static com.xrbpowered.hexdata.LevelProgression.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;

import com.xrbpowered.hexagon.Dir;
import com.xrbpowered.hexagon.HexView;
import com.xrbpowered.hexagon.Hexagon;
import com.xrbpowered.zoomui.GraphAssist;

public class HexDataView extends HexView<DataTile> {

	public static final Font font = new Font("Consolas", Font.PLAIN, 15);
	public static final Font fontGlyph = font.deriveFont(Font.BOLD, 27);
	
	public static final Color colorTile = Color.WHITE;
	public static final Color colorLink = new Color(0xaaaaaa);
	public static final Color colorNewLink = new Color(0x555555);
	public static final Color colorBlockedLink = new Color(0x770000);
	
	public HexDataView(HexDataGame parent) {
		super(parent, new Hexagon(96), Color.BLACK);
	}
	
	public void newMap() {
		LevelProgression.battleCount++;
		setMap(new DataMap());
		HexDataGame.status.setPlayer(map().player);
		Point start = map().start;
		panToTile(start.x, start.y);
		resetScale();
	}
	
	protected DataMap map() {
		return (DataMap) map;
	}

	private static Dir[] linkd = {Dir.N, Dir.NW, Dir.SW};
	
	@Override
	protected void paintTile(GraphAssist g, int x, int y, DataTile tile) {
		if(tile==null || !tile.discovered)
			return;
		boolean highlight = hover!=null && hover.x==x && hover.y==y && !tile.blocked;
		if(tile.object!=null) {
			tile.object.paint(g, highlight, tile.blocked);
		}
		else if(tile.visited) {
			g.setColor(tile.blocked ? colorBlockedLink : colorLink);
			g.graph.fillOval(-6, -6, 12, 12);
		}
		else {
			g.setColor(highlight ? colorNewLink : Color.BLACK);
			g.graph.fillOval(-15, -15, 30, 30);
			g.setStroke(2f);
			g.setColor(highlight ? Color.WHITE : tile.blocked ? colorBlockedLink : colorLink);
			g.graph.drawOval(-15, -15, 30, 30);
			if(tile.dist>=0 && tile.dist<10) {
				g.setColor(Color.WHITE);
				g.drawString(Integer.toString(tile.dist), 0, 0, GraphAssist.CENTER, GraphAssist.CENTER);
			}
		}
	}
	
	@Override
	protected void paintTiles(GraphAssist g, Rectangle clip) {
		g.setFont(font);
		g.pushAntialiasing(true);
		
		g.setStroke(2f);
		for(int x=0; x<map.size; x++)
			for(int y=0; y<map.size; y++) {
				DataTile tile = map.tiles[x][y]; 
				if(tile==null)
					continue;
				float x0 = hex.calcx(x);
				float y0 = hex.calcy(x, y);
				for(Dir ld : linkd) {
					int lx = x+ld.dx;
					int ly = y+ld.dy;
					if(!map().isInside(lx, ly))
						continue;
					DataTile ntile = map.tiles[lx][ly];
					if(ntile==null || !ntile.discovered && !tile.discovered)
						continue;
					float x1 = hex.calcx(lx);
					float y1 = hex.calcy(lx, ly);
					if(!tile.discovered || !ntile.discovered)
						g.setColor(colorNewLink);
					else if(tile.object!=null && tile.object.isBlocking() || ntile.object!=null && ntile.object.isBlocking())
						g.setColor(colorBlockedLink);
					else
						g.setColor(colorLink);
					g.line(x0, y0, x1, y1);
				}
			}
		
		super.paintTiles(g, clip);
		g.popAntialiasing();
	}
	
	public boolean isActive() {
		return map().player.isAlive() && map().core.isAlive();
	}

	@Override
	public boolean onMouseDown(float x, float y, Button button, int mods) {
		if(button==Button.left && hover!=null && map().isInside(hover.x, hover.y)) {
			if(!isActive())
				return true;
			DataTile tile = map.tiles[hover.x][hover.y];
			if(tile!=null && tile.interact(map(), hover))
				repaint();
			return true;
		}
		else
			return super.onMouseDown(x, y, button, mods);
	}
}
