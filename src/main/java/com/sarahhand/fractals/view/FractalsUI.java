package com.sarahhand.fractals.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import com.sarahhand.fractals.event.FractalEventHandler;
import com.sarahhand.fractals.event.FractalEventHandlerFactory;
import com.sarahhand.fractals.json.ConfigSaverLoader;
import com.sarahhand.fractals.mandelbrotset.MandelbrotConfig;
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

	private JPanel buttonPanel;
	private JButton saveFractalConfig;
	private JButton loadFractalConfig;
	private JButton saveImage;
	private JButton createColorPalette;

	private JComboBox<ColorScheme> colorSchemeComboBox;

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
		loadColorSchemes(MandelbrotConfig.DEAFAULT_CONFIG.getColorScheme());
		buttonPanel.add(colorSchemeComboBox);

		saveFractalConfig.addActionListener(new SaveConfigActionListener());
		loadFractalConfig.addActionListener(new LoadConfigActionListener());
		saveImage.addActionListener(new SaveImageActionListener());
		createColorPalette.addActionListener(new CreateColorPaletteListener());
		colorSchemeComboBox.addActionListener(new ColorSchemeComboBoxListener());
		imageLabel.addMouseListener(events);
		imageLabel.addMouseMotionListener(events);

		frame.add(buttonPanel, BorderLayout.NORTH);
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

	/** This method is used to add all of the ColorSchemes to colorSchemeComboBox.
	 * It also sets the selected ColorScheme to the one in the FractalConfig.
	 * 
	 * @param selectedColorScheme
	 */
	private void loadColorSchemes(ColorScheme selectedColorScheme) {
		DefaultComboBoxModel<ColorScheme> model = (DefaultComboBoxModel<ColorScheme>)this.colorSchemeComboBox.getModel();
		for (ColorScheme scheme : events.getFractalViewer().getSupportedColorSchemes()) {
			model.addElement(scheme);
		}
		setSelectedColorSchemes(selectedColorScheme);
	}

	/** This method updates the selected ColorScheme to the one in the FractalConfig.
	 * 
	 * @param selectedColorScheme
	 */
	private void setSelectedColorSchemes(ColorScheme selectedColorScheme) {
		DefaultComboBoxModel<ColorScheme> model = (DefaultComboBoxModel<ColorScheme>)this.colorSchemeComboBox.getModel();
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
				saverLoader.save(events.getFractalViewer().getConfig(), fileChooser.getSelectedFile().getAbsolutePath());
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
				events.getFractalViewer().setConfig((FractalConfig)saverLoader.load(events.getFractalViewer().getConfig().getClass(), fileChooser.getSelectedFile().getAbsolutePath()));
				image.setImage(events.getFractalViewer().getView(frameDimension));
				setSelectedColorSchemes(events.getFractalViewer().getConfig().getColorScheme());
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

	/** This class is the ActionListener for the createColorPalette button.
	 * 
	 * @author M00031
	 *
	 */
	private class CreateColorPaletteListener implements ActionListener {

		public void actionPerformed(ActionEvent ae) {
			if(events.supportsColorPalette()){
				ChangeColorPalette changeCP = new ChangeColorPalette();
				Thread changePaletteThread = new Thread(new ChangePaletteRunnable(changeCP));
				changePaletteThread.start();
			}
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
			events.setColorScheme((ColorScheme)colorSchemeComboBox.getSelectedItem());
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