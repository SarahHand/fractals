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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sarahhand.fractals.colorpalette.ChangeColorPalette;
import com.sarahhand.fractals.json.ConfigSaverLoader;
import com.sarahhand.fractals.model.FractalConfig;
import com.sarahhand.fractals.model.FractalType;
import com.sarahhand.fractals.model.MandelbrotConfig;
import com.sarahhand.fractals.viewer.FractalViewer;
import com.sarahhand.fractals.viewer.FractalViewerFactory;

/** This class will display the Mandelbrot set and allow you to do a variety of other things.
 * 
 * @author M00031
 *
 */
public class FractalsUI {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	JFrame frame;
	Dimension frameDimension;
	FractalViewerFactory viewerFactory;
	FractalViewer viewer;
	FractalConfig fractalConfig;
	ImageIcon image;
	private JLabel imageLabel;

	private JButton saveFractalConfig;
	private JButton loadFractalConfig;
	private JButton createColorPalette;

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

		viewerFactory = new FractalViewerFactory();
		viewer = viewerFactory.createViewer(FractalType.MANDELBROT_SET);
		fractalConfig = viewer.getConfig();
		image = new ImageIcon(viewer.getView(frameDimension));
		imageLabel = new JLabel(image);

		saveFractalConfig = new JButton("Save");
		loadFractalConfig = new JButton("Load");
		createColorPalette = new JButton("Create New Color Palette");

		saveFractalConfig.addActionListener(new SaveConfigActionListener());
		loadFractalConfig.addActionListener(new LoadConfigActionListener());
		createColorPalette.addActionListener(new CreateColorPaletteListener());

		frame.add(imageLabel);
		frame.add(saveFractalConfig);
		frame.add(loadFractalConfig);
		
		FractalsMouseListener mouseListener = new FractalsMouseListener();
		frame.addMouseListener(mouseListener);
		frame.addMouseMotionListener(mouseListener);
		
		frame.add(createColorPalette);

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
		
		private Point draggingPos;
		
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
			double centerX = fractalConfig.getCenter().x + mouseX / fractalConfig.getZoom();
			double centerY = fractalConfig.getCenter().y + mouseY / fractalConfig.getZoom();
			Double newCenter = new Double(centerX, centerY);
			double newZoom = fractalConfig.getZoom() * zoomFactor;
			int newMaxDwell = fractalConfig.getMaxDwell() + maxDwellOffset;
			return new MandelbrotConfig(newCenter, newZoom, newMaxDwell, (MandelbrotConfig)fractalConfig);
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
					fractalConfig = createNewConfig(frameDimension, mouseX, mouseY, ZOOM_IN_FACTOR,
							MAX_DWELL_INCREASE);
				} else if(buttonType == RIGHT_CLICK) {
					fractalConfig = createNewConfig(frameDimension, mouseX, mouseY, ZOOM_OUT_FACTOR,
							MAX_DWELL_DECREASE);
				}
				viewer.setConfig(fractalConfig);
				image.setImage(viewer.getView(frameDimension));
				frame.repaint();
			} finally {
				frame.setCursor(Cursor.getDefaultCursor());
			}
		}

		public void mouseEntered(MouseEvent me) {}

		public void mouseExited(MouseEvent me) {}
		
		public void mouseReleased(MouseEvent me) {
			image.setImage(viewer.getView(frameDimension));
			log.debug("Mouse Released");
			frame.repaint();
		}

		public void mousePressed(MouseEvent me) {
			
			log.debug("Start Position: " + me.getX() + " " + me.getY());
			draggingPos = me.getPoint();
		}

		@Override
		public void mouseDragged(MouseEvent me){
			
			FractalConfig oldConfig = fractalConfig;
			Image oldImage = image.getImage();
			
			int xChange = draggingPos.x - me.getX();
			int yChange = me.getY() - draggingPos.y;
			
			double centerX = fractalConfig.getCenter().x + xChange / fractalConfig.getZoom();
			double centerY = fractalConfig.getCenter().y + yChange / fractalConfig.getZoom();
			Double newCenter = new Double(centerX, centerY);
			fractalConfig =  new MandelbrotConfig(newCenter, fractalConfig.getZoom(), fractalConfig.getMaxDwell(), (MandelbrotConfig)fractalConfig);
			
			viewer.setConfig(fractalConfig);
			image.setImage(viewer.getViewPanning(frameDimension, oldConfig, oldImage));
			frame.repaint();
			
			log.debug("Zoom: " + fractalConfig.getZoom());
			
			log.debug("Change: " + xChange + " " + yChange);
			
			log.debug("Max Dwell: " + fractalConfig.getMaxDwell());
			
			draggingPos = me.getPoint();
		}
		
		@Override
		public void mouseMoved(MouseEvent me){}
	}

	/** This class is the ActionListener for the saveFractalConfig button.
	 * 
	 * @author M00031
	 *
	 */
	private class SaveConfigActionListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			ConfigSaverLoader saverLoader = ConfigSaverLoader.getDefaultConfigSaverLoader();
			JFileChooser fileChooser = new JFileChooser();
			if(fileChooser.showDialog(null, "Save") == JFileChooser.APPROVE_OPTION) {
				saverLoader.save(fractalConfig, fileChooser.getSelectedFile().getAbsolutePath());
			}
		}
	}

	/** This class is the ActionListener for the loadFractalConfig button.
	 * 
	 * @author M00031
	 *
	 */
	private class LoadConfigActionListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			ConfigSaverLoader saverLoader = ConfigSaverLoader.getDefaultConfigSaverLoader();
			JFileChooser fileChooser = new JFileChooser();
			if(fileChooser.showDialog(null, "Load") == JFileChooser.APPROVE_OPTION) {
				fractalConfig = (MandelbrotConfig)saverLoader.load(fractalConfig.getClass(), fileChooser.getSelectedFile().getAbsolutePath());
				viewer.setConfig(fractalConfig);
				image.setImage(viewer.getView(frameDimension));
				frame.repaint();
			}
		}
	}

	/** This class is the ActionListener for the createColorPalette button.
	 * 
	 * @author M00031
	 *
	 */
	private class CreateColorPaletteListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			ChangeColorPalette changeCP = new ChangeColorPalette();
			Thread changePaletteThread = new Thread(new ChangePaletteRunnable(changeCP));
			changePaletteThread.start();			
		}
	}

	/** This lets the ChangeColorPalette frame run on a different Thread.
	 * 
	 * @author M00031
	 *
	 */
	private class ChangePaletteRunnable implements Runnable {

		private ChangeColorPalette changeCP;

		public ChangePaletteRunnable(ChangeColorPalette changeCP) {
			this.changeCP = changeCP;
		}

		public void run() {
			while (changeCP.getCreatedColorPalette() == null) {}
			fractalConfig = new MandelbrotConfig(fractalConfig.getCenter(),
					fractalConfig.getZoom(), fractalConfig.getMaxDwell(),
					changeCP.getCreatedColorPalette(), fractalConfig.getColorScheme());
			viewer.setConfig(fractalConfig);
			image.setImage(viewer.getView(frameDimension));
			frame.repaint();
		}
	}

	public static void main(String args[]) {
		new FractalsUI();
	}
}