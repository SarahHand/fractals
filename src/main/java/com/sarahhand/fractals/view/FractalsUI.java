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
import com.sarahhand.fractals.model.FractalConfig;
import com.sarahhand.fractals.model.FractalType;
import com.sarahhand.fractals.model.FractalViewer;
import com.sarahhand.fractals.model.FractalViewerFactory;
import com.sarahhand.fractals.model.MandelbrotConfig;

/** This class will display the Mandelbrot set and allow you to do a variety of other things.
 * 
 * @author M00031
 *
 */
public class FractalsUI implements MouseListener {

	private JFrame frame;
	private Dimension frameDimension;
	FractalViewerFactory viewerFactory;
	FractalViewer viewer;
	private FractalConfig fractalConfig;
	private ColorPalette palette;
	private ImageIcon image;
	private JLabel imageLabel;

	private final int FRAME_WIDTH = 800;
	private final int FRAME_HEIGHT = 700;
	private final double ZOOM_IN_FACTOR = 5;
	private final double ZOOM_OUT_FACTOR = 1 / ZOOM_IN_FACTOR;
	private final double MAX_DWELL_INCREASE = 1.5;
	private final double MAX_DWELL_DECREASE = 1/MAX_DWELL_INCREASE;
	private final int LEFT_CLICK = 1;
	private final int RIGHT_CLICK = 3;

	public FractalsUI() {
		frame = new JFrame("Fractal Viewer");
		
		frame.setLayout(new FlowLayout());
		frameDimension = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
		viewerFactory = new FractalViewerFactory();
		frame.setSize(frameDimension);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		viewer = viewerFactory.createViewer(FractalType.MANDELBROT_SET);
		fractalConfig = viewer.getConfig();
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
			double zoomFactor, double maxDwellOffset) {
		double mouseX = xPos - frameDimension.getWidth() / 2;
		double mouseY = frameDimension.getHeight() / 2 - yPos;
		double centerX = fractalConfig.getCenter().x + mouseX / fractalConfig.getZoom();
		double centerY = fractalConfig.getCenter().y + mouseY / fractalConfig.getZoom();
		Double newCenter = new Double(centerX, centerY);
		double newZoom = fractalConfig.getZoom() * zoomFactor;
		int newMaxDwell = (int)(fractalConfig.getMaxDwell() * maxDwellOffset);
		return new MandelbrotConfig(newCenter, newZoom, newMaxDwell, (MandelbrotConfig)fractalConfig);
	}

	public void mouseClicked(MouseEvent me) {
		try {
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			int buttonType = me.getButton();
			int mouseX = me.getX();
			int mouseY = me.getY();
			if(buttonType == LEFT_CLICK) {
				fractalConfig = createNewConfig(frameDimension, mouseX, mouseY, ZOOM_IN_FACTOR, MAX_DWELL_INCREASE);
			} else if(buttonType == RIGHT_CLICK) {
				fractalConfig = createNewConfig(frameDimension, mouseX, mouseY, ZOOM_OUT_FACTOR, MAX_DWELL_DECREASE);
			}
			viewer.setConfig(fractalConfig);
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
		@SuppressWarnings("unused")
		FractalsUI ui = new FractalsUI();
	}
}