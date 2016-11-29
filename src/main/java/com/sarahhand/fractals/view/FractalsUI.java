package com.sarahhand.fractals.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sarahhand.fractals.colorpalette.ChangeColorPalette;
import com.sarahhand.fractals.json.ConfigSaverLoader;
import com.sarahhand.fractals.model.FractalConfig;
import com.sarahhand.fractals.model.FractalType;
import com.sarahhand.fractals.model.MandelbrotConfig;
import com.sarahhand.fractals.model.colorscheme.ColorScheme;
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

	private JPanel buttonPanel;
	private JButton saveFractalConfig;
	private JButton loadFractalConfig;
	private JButton saveImage;
	private JButton createColorPalette;

	private JComboBox<ColorScheme> colorSchemeComboBox;

	final int FRAME_WIDTH = 800;
	final int FRAME_HEIGHT = 600;

	/** This method sets up the UI for the Mandelbrot Set Viewer.
	 */
	public FractalsUI() {
		frame = new JFrame("Fractal Viewer");
		frame.setLayout(new BorderLayout());
		frameDimension = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setSize(frameDimension);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		viewerFactory = new FractalViewerFactory();
		viewer = viewerFactory.createViewer(FractalType.MANDELBROT_SET);
		fractalConfig = viewer.getConfig();
		image = new ImageIcon(viewer.getView(frameDimension));
		imageLabel = new JLabel(image);

		buttonPanel = new JPanel();
		saveFractalConfig = new JButton("Save");
		loadFractalConfig = new JButton("Load");
		saveImage = new JButton("Save Screenshot");
		createColorPalette = new JButton("Create New Color Palette");
		buttonPanel.add(saveFractalConfig);
		buttonPanel.add(loadFractalConfig);
		buttonPanel.add(saveImage);
		buttonPanel.add(createColorPalette);

		colorSchemeComboBox = new JComboBox<>();
		colorSchemeComboBox.setRenderer(new ColorSchemeListCellRenderer());
		loadColorSchemes(fractalConfig.getColorScheme());
		buttonPanel.add(colorSchemeComboBox);

		saveFractalConfig.addActionListener(new SaveConfigActionListener());
		loadFractalConfig.addActionListener(new LoadConfigActionListener());
		saveImage.addActionListener(new SaveImageActionListener());
		createColorPalette.addActionListener(new CreateColorPaletteListener());
		colorSchemeComboBox.addActionListener(new ColorSchemeComboBoxListener());

		FractalsMouseListener mouseListener = new FractalsMouseListener();
		frame.addMouseListener(mouseListener);
		frame.addMouseMotionListener(mouseListener);

		frame.add(buttonPanel, BorderLayout.NORTH);
		frame.add(imageLabel, BorderLayout.SOUTH);

		frame.pack();
		frame.setVisible(true);
	}

	/** This method is used to add all of the ColorSchemes to colorSchemeComboBox.
	 * It also sets the selected ColorScheme to the one in the FractalConfig.
	 * 
	 * @param selectedColorScheme
	 */
	private void loadColorSchemes(ColorScheme selectedColorScheme) {
		DefaultComboBoxModel<ColorScheme> model = (DefaultComboBoxModel<ColorScheme>)this.colorSchemeComboBox.getModel();
		for (ColorScheme scheme : viewer.getSupportedColorSchemes()) {
			model.addElement(scheme);
		}
		model.setSelectedItem(selectedColorScheme);
	}

	/** This class is used to add ColorSchemes to colorSchemeComboBox,
	 * but it only shows the name of the ColorScheme.
	 * 
	 * @author M00031
	 *
	 */
	private class ColorSchemeListCellRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = -8726267565122387060L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

			if (value instanceof ColorScheme) {
				value = ((ColorScheme)value).getName();
			}

			return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		}

	}

	/** This class is the MouseListener for the frame. It is used for zooming in and out. It also handles panning.
	 * 
	 * @author M00031
	 *
	 */
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
				if(imageLabel.getBounds().contains(mouseX, mouseY)) {
					if(buttonType == LEFT_CLICK) {
						fractalConfig = createNewConfig(frameDimension, mouseX, mouseY, ZOOM_IN_FACTOR,
								MAX_DWELL_INCREASE);
					} else if(buttonType == RIGHT_CLICK) {
						fractalConfig = createNewConfig(frameDimension, mouseX, mouseY, ZOOM_OUT_FACTOR,
								MAX_DWELL_DECREASE);
					}
				}
				viewer.setConfig(fractalConfig);
				image.setImage(viewer.getView(frameDimension));
				frame.repaint();
			} finally {
				frame.setCursor(Cursor.getDefaultCursor());
			}
		}

		/* These methods are not used for zooming in and out.
		 */
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

		private ConfigSaverLoader saverLoader = ConfigSaverLoader.getDefaultConfigSaverLoader();
		private JFileChooser fileChooser = new JFileChooser();

		public void actionPerformed(ActionEvent ae) {
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

		private ConfigSaverLoader saverLoader = ConfigSaverLoader.getDefaultConfigSaverLoader();
		private JFileChooser fileChooser = new JFileChooser();

		public void actionPerformed(ActionEvent ae) {
			if(fileChooser.showDialog(null, "Load") == JFileChooser.APPROVE_OPTION) {
				fractalConfig = (MandelbrotConfig)saverLoader.load(fractalConfig.getClass(), fileChooser.getSelectedFile().getAbsolutePath());
				viewer.setConfig(fractalConfig);
				image.setImage(viewer.getView(frameDimension));
				frame.repaint();
			}
		}
	}

	/** This class is the ActionListener for the saveImage button.
	 * 
	 * @author M00031
	 *
	 */
	private class SaveImageActionListener implements ActionListener {

		private List<String> extensions = Arrays.asList("png", "jpg", "jpeg", "bmp", "gif");
		private JFileChooser fileChooser = new JFileChooser();

		public void actionPerformed(ActionEvent ae) {
			if(fileChooser.showDialog(null, "Save") == JFileChooser.APPROVE_OPTION) {
				String fileName = fileChooser.getName(fileChooser.getSelectedFile());
				String[] fileNameParts = fileName.split("\\.");
				if(fileNameParts != null &&
						fileNameParts.length >= 2 &&
						extensions.contains(fileNameParts[fileNameParts.length - 1])) {
					try {
						ImageIO.write((BufferedImage)viewer.getView(frameDimension),
								fileNameParts[fileNameParts.length - 1],
								fileChooser.getSelectedFile());
					} catch(IOException ioe) {
						log.error("IOException thrown while saving a screenshot.");
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Your file name must have one of the extensions: png, jpg, jpeg, gif, or bmp.");
				}
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
	
	/** This class is the ActionListener for the colorSchemeComboBox JComboBox.
	 * It is used to detect when a different item is selected in the drop-down.
	 * 
	 * @author M00031
	 *
	 */
	private class ColorSchemeComboBoxListener implements ActionListener {
		
		public void actionPerformed(ActionEvent ae) {
			fractalConfig = new MandelbrotConfig((ColorScheme)colorSchemeComboBox.getSelectedItem(), (MandelbrotConfig)fractalConfig);
			viewer.setConfig(fractalConfig);
			image.setImage(viewer.getView(frameDimension));
			frame.repaint();
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