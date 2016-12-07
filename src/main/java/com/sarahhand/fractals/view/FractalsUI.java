package com.sarahhand.fractals.view;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sarahhand.fractals.colorpalette.ChangeColorPalette;
import com.sarahhand.fractals.event.FractalEventHandler;
import com.sarahhand.fractals.event.FractalEventHandlerFactory;
import com.sarahhand.fractals.json.ConfigSaverLoader;
import com.sarahhand.fractals.model.ColorScheme;
import com.sarahhand.fractals.model.FractalConfig;
import com.sarahhand.fractals.model.FractalType;

/** This class will display the Mandelbrot set and allow you to do a variety of other things.
 * 
 * @author M00031
 *
 */
public class FractalsUI {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private JFrame frame;
	private Dimension frameDimension;
	private FractalEventHandlerFactory eventHandlerFactory;
	private FractalEventHandler events;
	
	private ImageIcon image;
	private JLabel imageLabel;

	private JMenuBar menuBar;

	private JMenu fileMenu;
	private JMenuItem saveMenuItem;
	private JMenuItem loadMenuItem;
	private JMenuItem saveImageMenuItem;
	private JMenuItem exitMenuItem;

	private JMenu optionsMenu;
	private JMenuItem createColorPaletteMenuItem;

	private JMenu selectColorSchemeMenu;

	private final int FRAME_WIDTH = 800;
	private final int FRAME_HEIGHT = 600;

	/** This method sets up the UI for the Mandelbrot Set Viewer.
	 */
	public FractalsUI() {
		frame = new JFrame("Fractal Viewer");
		frame.setLayout(new BorderLayout());
		frameDimension = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setSize(frameDimension);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		eventHandlerFactory = new FractalEventHandlerFactory();
		events = eventHandlerFactory.createEventHandler(this, FractalType.MANDELBROT_SET);
		image = new ImageIcon(events.getFractalViewer().getView(frameDimension));
		imageLabel = new JLabel(image);

		menuBar = new JMenuBar();

		fileMenu = new JMenu("File");
		saveMenuItem = new JMenuItem("Save");
		loadMenuItem = new JMenuItem("Load");
		saveImageMenuItem = new JMenuItem("Save Screenshot");
		exitMenuItem = new JMenuItem("Exit");
		fileMenu.add(saveMenuItem);
		fileMenu.add(loadMenuItem);
		fileMenu.add(saveImageMenuItem);
		fileMenu.add(exitMenuItem);

		optionsMenu = new JMenu("Options");
		createColorPaletteMenuItem = new JMenuItem("Create Color Palette");
		selectColorSchemeMenu = new JMenu("Select Color Scheme");
		setSelectedColorScheme();
		
		optionsMenu.add(createColorPaletteMenuItem);
		optionsMenu.add(selectColorSchemeMenu);

		menuBar.add(fileMenu);
		menuBar.add(optionsMenu);

		saveMenuItem.addActionListener(new SaveMenuItemActionListener());
		loadMenuItem.addActionListener(new LoadMenuItemActionListener());
		saveImageMenuItem.addActionListener(new SaveImageMenuItemActionListener());
		exitMenuItem.addActionListener(new ExitMenuItemActionListener());
		createColorPaletteMenuItem.addActionListener(new CreateColorPaletteMenuItemActionListener());

		imageLabel.addMouseListener(events);
		imageLabel.addMouseMotionListener(events);

		frame.add(menuBar, BorderLayout.NORTH);
		frame.add(imageLabel, BorderLayout.SOUTH);
		
		imageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Returns the current frame
	 * @return
	 */
	public JFrame getFrame(){
		return frame;
	}
	
	/**
	 * Returns the current frame dimensions
	 * @return
	 */
	public Dimension getFrameDimension(){
		return frameDimension;
	}
	
	/**
	 * Returns the current image
	 * @return
	 */
	public ImageIcon getImage(){
		return image;
	}
	
	/**
	 * Returns the current image label
	 * @return
	 */
	public JLabel getImageLabel(){
		return imageLabel;
	}

	private void setSelectedColorScheme() {
		selectColorSchemeMenu.removeAll();
		ButtonGroup bg = new ButtonGroup();
		for(ColorScheme scheme : events.getFractalViewer().getSupportedColorSchemes()) {
			JRadioButtonMenuItem newRadioButton = new JRadioButtonMenuItem(scheme.getName());
			newRadioButton.addActionListener(new ColorSchemeMenuItemsListener(scheme.getName()));
			bg.add(newRadioButton);
			selectColorSchemeMenu.add(newRadioButton);
			if(events.getFractalViewer().getConfig().getColorScheme().getName().equals(scheme.getName())) {
				newRadioButton.setSelected(true);
			}
		}
	}



	/** This class is the ActionListener for the saveMenuItem JMenuItem.
	 * 
	 * @author M00031
	 *
	 */
	private class SaveMenuItemActionListener implements ActionListener {

		private ConfigSaverLoader saverLoader = ConfigSaverLoader.getDefaultConfigSaverLoader();
		private JFileChooser fileChooser = new JFileChooser();

		public void actionPerformed(ActionEvent ae) {
			if(fileChooser.showDialog(null, "Save") == JFileChooser.APPROVE_OPTION) {
				saverLoader.save(events.getFractalViewer().getConfig(), fileChooser.getSelectedFile().getAbsolutePath());
			}
		}
	}

	/** This class is the ActionListener for the loadMenuItem JMenuItem.
	 * 
	 * @author M00031
	 *
	 */
	private class LoadMenuItemActionListener implements ActionListener {

		private ConfigSaverLoader saverLoader = ConfigSaverLoader.getDefaultConfigSaverLoader();
		private JFileChooser fileChooser = new JFileChooser();

		public void actionPerformed(ActionEvent ae) {
			if(fileChooser.showDialog(null, "Load") == JFileChooser.APPROVE_OPTION) {
				events.getFractalViewer().setConfig((FractalConfig)saverLoader.load(events.getFractalViewer().getConfig().getClass(), fileChooser.getSelectedFile().getAbsolutePath()));
				image.setImage(events.getFractalViewer().getView(frameDimension));
				frame.repaint();
			}
			setSelectedColorScheme();
		}
	}

	/** This class is the ActionListener for the saveImageMenuItem JMenuItem.
	 * 
	 * @author M00031
	 *
	 */
	private class SaveImageMenuItemActionListener implements ActionListener {

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
						ImageIO.write((BufferedImage)events.getFractalViewer().getView(frameDimension),
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

	/** This class is the ActionListener for the exitMenuItem JMenuItem.
	 * 
	 * @author M00031
	 *
	 */
	private class ExitMenuItemActionListener implements ActionListener {

		public void actionPerformed(ActionEvent ae) {
			System.exit(0);
		}
	}

	/** This class is the ActionListener for the createColorPaletteMenuItem JMenuItem.
	 * 
	 * @author M00031
	 *
	 */
	private class CreateColorPaletteMenuItemActionListener implements ActionListener {

		public void actionPerformed(ActionEvent ae) {
			if(events.supportsColorPalette()){
				ChangeColorPalette changeCP = new ChangeColorPalette();
				Thread changePaletteThread = new Thread(new ChangePaletteRunnable(changeCP));
				changePaletteThread.start();
			}
		}
	}

	/** This class serves as the ActionListeners for all of the JMenuItems that have to do with changing the ColorScheme.
	 * 
	 * @author M00031
	 *
	 */
	private class ColorSchemeMenuItemsListener implements ActionListener {

		ColorScheme colorScheme;

		public ColorSchemeMenuItemsListener(String colorSchemeName) {
			List<ColorScheme> supportedColorSchemes = events.getFractalViewer().getSupportedColorSchemes();
			for(int count = 0; count < supportedColorSchemes.size(); count++) {
				if(supportedColorSchemes.get(count).getName().equals(colorSchemeName)) {
					colorScheme = events.getFractalViewer().getSupportedColorSchemes().get(count);
				}
			}
		}

		public void actionPerformed(ActionEvent ae) {
			events.setColorScheme(colorScheme);
			image.setImage(events.getFractalViewer().getView(frameDimension));
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
			events.setColorPalette(changeCP.getCreatedColorPalette());
			image.setImage(events.getFractalViewer().getView(frameDimension));
			frame.repaint();
		}
	}
	
	/**
	 * The main method.
	 * @param args
	 */
	public static void main(String args[]) {
		new FractalsUI();
	}
}