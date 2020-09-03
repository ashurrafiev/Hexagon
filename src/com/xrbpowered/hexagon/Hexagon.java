package com.xrbpowered.hexagon;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class Hexagon {

	public final float w, a, h;
	public final float xspan, yspan, w2, r;
	
	public Hexagon(float w, float a, float h) {
		this.w = w;
		this.a = a;
		this.h = h;
		this.xspan = w+a;
		this.yspan = 2f*h;
		this.w2 = w/2f;
		this.r = calcR();
	}
	
	public Hexagon(float xspan, float yspan) {
		this.w = xspan*2f/3f;
		this.a = xspan/3f;
		this.h = yspan/2f;
		this.xspan = xspan;
		this.yspan = yspan;
		this.w2 = a;
		this.r = calcR();
	}
	
	public Hexagon(float r) {
		this.w = r;
		this.a = r/2f;
		this.h = r*(float)Math.cos(Math.PI/6.0);
		this.xspan = w+a;
		this.yspan = 2f*h;
		this.w2 = a;
		this.r = r;
	}

	private float calcR() {
		return Math.max((float)Math.sqrt(a*a+h*h), w/2f+a);
	}
	
	public Path2D createPath() {
		Path2D.Float p = new Path2D.Float();
		p.moveTo(-w2-a, 0);
		p.lineTo(-w2, -h);
		p.lineTo(w2, -h);
		p.lineTo(w2+a, 0);
		p.lineTo(w2, h);
		p.lineTo(-w2, h);
		p.closePath();
		return p;
	}
	
	public Shape createCircle(float s) {
		float r = this.r*s;
		return new Ellipse2D.Float(-r, -r, r*2f, r*2f);
	}

	public Rectangle2D createRect() {
		return new Rectangle2D.Float(-w/2-a/2, -h, w+a, h*2);
	}
	
	public void worldPos(Point pos, float x, float y) {
		pos.x = (int) Math.floor(x/(float)(w+a));
		pos.y = (int) Math.floor((y/(float)h+pos.x)/2f);
	}
	
	public float calcx(int wx) {
		return wx*xspan;
	}

	public float calcy(int wx, int wy) {
		return (-wx+2*wy)*h;
	}

	public boolean insideClip(float x, float y, Rectangle clip) {
		return x+w2+a>=clip.x && x-w2-a<=clip.x+clip.width && y+h>=clip.y && y-h<=clip.y+clip.height;
	}

	public static int dist(int wx1, int wy1, int wx2, int wy2) {
		int dx = wx2-wx1;
		int dy = wy2-wy1;
		if(dx<0 && dy>0 || dx>0 && dy<0)
			return Math.abs(dx)+Math.abs(dy);
		else
			return Math.max(Math.abs(dx), Math.abs(dy));
	}

}
