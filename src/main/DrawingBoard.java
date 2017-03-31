package main;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import objects.*;

public class DrawingBoard extends JPanel {

	private MouseAdapter mouseAdapter; 
	private List<GObject> gObjects;
	private GObject target;

	private int gridSize = 10;

	public DrawingBoard() {
		gObjects = new ArrayList<GObject>();
		mouseAdapter = new MAdapter();
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		setPreferredSize(new Dimension(800, 600));
	}

	public void addGObject(GObject gObject) {
		gObjects.add( gObject );
		repaint();
	}

	public void groupAll() {
		CompositeGObject objects = new CompositeGObject();
		boolean existedCompositeObject = false;
		for( GObject g : gObjects ) {
			if( g instanceof CompositeGObject ) {
				existedCompositeObject = true;
			} 
		}
		
		for( int i = 0 ; i < gObjects.size() ; i++ ) {
			if( existedCompositeObject == true && gObjects.get(i) instanceof CompositeGObject ) {
				objects = (CompositeGObject) gObjects.get(i);
			} else {
				gObjects.get(i).deselected();
				objects.add(gObjects.get(i));
			}
		}

		objects.recalculateRegion();
		clear();
		gObjects.add( objects );
		repaint();
	}

	public void deleteSelected() {
		gObjects.remove( target );
		repaint();
	}

	public void clear() {
		gObjects.clear();
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		paintBackground(g);
		paintGrids(g);
		paintObjects(g);
	}

	private void paintBackground(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	private void paintGrids(Graphics g) {
		g.setColor(Color.lightGray);
		int gridCountX = getWidth() / gridSize;
		int gridCountY = getHeight() / gridSize;
		for (int i = 0; i < gridCountX; i++) {
			g.drawLine(gridSize * i, 0, gridSize * i, getHeight());
		}
		for (int i = 0; i < gridCountY; i++) {
			g.drawLine(0, gridSize * i, getWidth(), gridSize * i);
		}
	}

	private void paintObjects(Graphics g) {
		for (GObject go : gObjects) {
			go.paint(g);
		}
	}

	class MAdapter extends MouseAdapter {

		int x;
		int y;

		private void deselectAll() {
			for( GObject g : gObjects ) {
				g.deselected();
			}
			repaint();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			x = e.getX();
			y = e.getY();
			deselectAll();
			for( GObject g : gObjects ) {
				if( g.pointerHit(x, y)) {
					g.selected();
					target = g;
					break;
				} 
			}
			repaint();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			x = e.getX();
			y = e.getY();

			target.move(x, y);

			repaint();
		}
	}

}