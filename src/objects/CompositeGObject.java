package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class CompositeGObject extends GObject {

	private List<GObject> gObjects;

	public CompositeGObject() {
		super(0, 0, 0, 0);
		gObjects = new ArrayList<GObject>();
	}

	public void add(GObject gObject) {
		gObjects.add( gObject );
	}

	public void remove(GObject gObject) {
		gObjects.remove( gObject );
	}

	@Override
	public void move(int dX, int dY) {
		int x = super.x;
		int y = super.y;
		super.move(dX, dY);
		for( GObject g : gObjects ) {
			g.move( dX, dY );
		}
	}
	
	public void recalculateRegion() {
		int minx = 400;
		int miny = 400;
		int maxx = 0;
		int maxy = 0;
		int heightmax = 0;
		int widthmax = 0;
		for( GObject g : gObjects ) {
			if( minx > g.x ) minx = g.x;
			if( miny > g.y ) miny = g.y;
			if( maxx < g.x ) {
				maxx = g.x;
				heightmax = g.height;
			}
			if( maxy < g.y ) {
				maxy = g.y;
				widthmax = g.width;
			}
		}
		
		super.x = minx;
		super.y = miny;
		super.height = maxy - miny + heightmax;
		super.width = maxx - minx + widthmax;
	}

	@Override 
	public void paintObject(Graphics g) {
		for( GObject a : gObjects ) 
			a.paintObject(g);
	}

	@Override
	public void paintLabel(Graphics g) {
		g.drawString("Grouped", x, y+height+10);
	}
	
}
