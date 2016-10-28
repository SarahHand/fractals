package com.sarahhand.fractals.view;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D.Double;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.sarahhand.fractals.model.ColorPalette;
import com.sarahhand.fractals.model.MandelbrotConfig;
import com.sarahhand.fractals.model.MandelbrotViewer;
import com.sarahhand.fractals.model.MandelbrotViewerFactory;

/** This class will display the Mandelbrot set and allow you to do a variety of other things.
 * 
 * @author mhand
 *
 */
public class FractalsUI implements MouseListener {

	private JFrame frame;
	private Dimension frameDimension;
	MandelbrotViewerFactory viewerFactory;
	MandelbrotViewer viewer;
	private MandelbrotConfig mandelConfig;
	private ColorPalette palette;
	private ImageIcon image;
	private JLabel imageLabel;

	private final int FRAME_WIDTH = 800;
	private final int FRAME_HEIGHT = 700;
	private final double ZOOM_IN_FACTOR = 5;
	private final double ZOOM_OUT_FACTOR = 1 / ZOOM_IN_FACTOR;
	private final int MAX_DWELL_INCREASE = 200;
	private final int MAX_DWELL_DECREASE = -MAX_DWELL_INCREASE;
	private final int LEFT_CLICK = 1;
	private final int RIGHT_CLICK = 3;

	public FractalsUI() {
		frame = new JFrame("Fractal Viewer");
		frame.setLayout(new FlowLayout());
		frameDimension = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
		viewerFactory = new MandelbrotViewerFactory();
		frame.setSize(frameDimension);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		viewer = viewerFactory.createViewer();
		mandelConfig = viewer.getConfig();
		image = new ImageIcon(viewer.getView(frameDimension));
		imageLabel = new JLabel(image);
		frame.add(imageLabel);
		frame.addMouseListener(this);
		frame.setVisible(true);
	}

	/** Creates a new MandelbrotConfig using the existing one. The new config will have a new center and zoom.
	 * 
	 * @param frameDimension
	 * @param xPos
	 * @param yPos
	 * @param zoomFactor
	 * @param maxDwellOffset
	 * @return
	 */
	private MandelbrotConfig createNewConfig(Dimension frameDimension, int xPos, int yPos,
			double zoomFactor, int maxDwellOffset) {
		double mouseX = xPos - frameDimension.getWidth() / 2;
		double mouseY = frameDimension.getHeight() / 2 - yPos;
		double centerX = mandelConfig.getCenter().x + mouseX / mandelConfig.getZoom();
		double centerY = mandelConfig.getCenter().y + mouseY / mandelConfig.getZoom();
		Double newCenter = new Double(centerX, centerY);
		double newZoom = mandelConfig.getZoom() * zoomFactor;
		int newMaxDwell = mandelConfig.getMaxDwell() + maxDwellOffset;
		return new MandelbrotConfig(newCenter, newZoom, newMaxDwell, mandelConfig);
	}

	public void mouseClicked(MouseEvent me) {
		try {
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			int buttonType = me.getButton();
			int mouseX = me.getX();
			int mouseY = me.getY();
			if(buttonType == LEFT_CLICK) {
				mandelConfig = createNewConfig(frameDimension, mouseX, mouseY, ZOOM_IN_FACTOR, MAX_DWELL_INCREASE);
			} else if(buttonType == RIGHT_CLICK) {
				mandelConfig = createNewConfig(frameDimension, mouseX, mouseY, ZOOM_OUT_FACTOR, MAX_DWELL_DECREASE);
			}
			viewer.setConfig(mandelConfig);
			image.setImage(viewer.getView(frameDimension));
			// repaint refreshes the frame
			frame.repaint();
		} finally {
			frame.setCursor(Cursor.getDefaultCursor());
		}
	}

	public void mouseEntered(MouseEvent me) {}

	public void mouseExited(MouseEvent me) {}

	public void mouseReleased(MouseEvent me) {}

	public void mousePressed(MouseEvent me) {}

	public static void main(String args[]) {
		FractalsUI ui = new FractalsUI();
	}
}