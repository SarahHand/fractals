package com.sarahhand.fractals.view;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D.Double;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.sarahand.fractals.json.ConfigSaverLoader;
import com.sarahhand.fractals.model.MandelbrotConfig;
import com.sarahhand.fractals.model.MandelbrotViewer;
import com.sarahhand.fractals.model.MandelbrotViewerFactory;

/** This class will display the Mandelbrot set and allow you to do a variety of other things.
 * 
 * @author M00031
 *
 */
public class FractalsUI {

	JFrame frame;
	Dimension frameDimension;
	MandelbrotViewerFactory viewerFactory;
	MandelbrotViewer viewer;
	MandelbrotConfig mandelConfig;
	ImageIcon image;
	private JLabel imageLabel;

	private JButton saveFractalConfig;
	private JButton loadFractalConfig;

	final int FRAME_WIDTH = 800;
	final int FRAME_HEIGHT = 700;

	/** This method sets up the UI for the Mandelbrot Set Viewer.
	 */
	public FractalsUI() {
		frame = new JFrame("Fractal Viewer");
		frame.setLayout(new FlowLayout());
		frameDimension = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setSize(frameDimension);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		viewerFactory = new MandelbrotViewerFactory();
		viewer = viewerFactory.createViewer();
		mandelConfig = viewer.getConfig();
		image = new ImageIcon(viewer.getView(frameDimension));
		imageLabel = new JLabel(image);

		saveFractalConfig = new JButton("Save");
		loadFractalConfig = new JButton("Load");
		
		saveFractalConfig.addActionListener(new SaveConfigActionListener());
		loadFractalConfig.addActionListener(new LoadConfigActionListener());

		frame.add(imageLabel);
		frame.add(saveFractalConfig);
		frame.add(loadFractalConfig);

		frame.addMouseListener(new FractalsMouseListener());

		frame.pack();
		frame.setVisible(true);
	}
	
	private class FractalsMouseListener implements MouseListener, MouseMotionListener {

		private final double ZOOM_IN_FACTOR = 5;
		private final double ZOOM_OUT_FACTOR = 1 / ZOOM_IN_FACTOR;
		private final int MAX_DWELL_INCREASE = 200;
		private final int MAX_DWELL_DECREASE = -MAX_DWELL_INCREASE;
		private final int LEFT_CLICK = 1;
		private final int RIGHT_CLICK = 3;
		
		/** Creates a new MandelbrotConfig using the existing one.
		 * The new configuration will have a new center and zoom.
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
		
		/** Zooms in and out depending on whether the left button or the right button respectively are clicked.
		 */
		public void mouseClicked(MouseEvent me) {
			try {
				frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				int buttonType = me.getButton();
				int mouseX = me.getX();
				int mouseY = me.getY();
				if(buttonType == LEFT_CLICK) {
					mandelConfig = createNewConfig(frameDimension, mouseX, mouseY, ZOOM_IN_FACTOR,
							MAX_DWELL_INCREASE);
				} else if(buttonType == RIGHT_CLICK) {
					mandelConfig = createNewConfig(frameDimension, mouseX, mouseY, ZOOM_OUT_FACTOR,
							MAX_DWELL_DECREASE);
				}
				viewer.setConfig(mandelConfig);
				image.setImage(viewer.getView(frameDimension));
				frame.repaint();
			} finally {
				frame.setCursor(Cursor.getDefaultCursor());
			}
		}

		public void mouseEntered(MouseEvent me) {}

		public void mouseExited(MouseEvent me) {}
		
		private boolean isDragging = false;
		private Point draggingStartPos;
		
		public void mouseReleased(MouseEvent me) {
			isDragging = true;
			draggingStartPos = me.getPoint();
		}

		public void mousePressed(MouseEvent me) {
			isDragging = false;
		}

		@Override
		public void mouseDragged(MouseEvent me){
			
			MandelbrotConfig oldConfig = mandelConfig;
			Image oldImage = image.getImage();
			
			int xChange = draggingStartPos.x - me.getX();
			int yChange = me.getY() - draggingStartPos.y;
			mandelConfig = createNewConfig(frameDimension, xChange, yChange, ZOOM_IN_FACTOR, MAX_DWELL_INCREASE);
			
			viewer.setConfig(mandelConfig);
			//image.setImage(viewer.getViewPanning(frameDimension, oldConfig, oldImage));
			frame.repaint();
		}
		
		@Override
		public void mouseMoved(MouseEvent me){}
	}
	
	private class SaveConfigActionListener implements ActionListener {
		
		public void actionPerformed(ActionEvent ae) {
			ConfigSaverLoader saverLoader = ConfigSaverLoader.getDefaultConfigSaverLoader();
			JFileChooser fileChooser = new JFileChooser();
			if(fileChooser.showDialog(null, "Save") == JFileChooser.APPROVE_OPTION) {
				saverLoader.save(mandelConfig, fileChooser.getSelectedFile().getAbsolutePath());
			}
		}
	}
	
	private class LoadConfigActionListener implements ActionListener {
		
		public void actionPerformed(ActionEvent ae) {
			ConfigSaverLoader saverLoader = ConfigSaverLoader.getDefaultConfigSaverLoader();
			JFileChooser fileChooser = new JFileChooser();
			if(fileChooser.showDialog(null, "Load") == JFileChooser.APPROVE_OPTION) {
				mandelConfig = (MandelbrotConfig)saverLoader.load(mandelConfig, fileChooser.getSelectedFile().getAbsolutePath());
				viewer.setConfig(mandelConfig);
				image.setImage(viewer.getView(frameDimension));
				frame.repaint();
			}
		}
	}
	
	@SuppressWarnings("unused")
	public static void main(String args[]) {
		FractalsUI ui = new FractalsUI();
	}
}