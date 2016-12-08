package com.sarahhand.fractals.colorpalette;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import com.sarahhand.fractals.json.ColorPaletteSaverLoader;
import com.sarahhand.fractals.mapper.ColorPaletteMapper;
import com.sarahhand.fractals.model.ColorPalette;

/** This class changes the color palette to one of your choice using the class ColorPanel.
 * 
 * @author M00031
 *
 */
public class ChangeColorPalette {

	private JFrame frame;

	private JPanel colorPanelsPanel;
	private List<ColorPanel> colorPanels;

	private volatile ColorPalette newColorPalette;
	private ColorPalette temporaryColorPalette;

	private JMenuBar menuBar;

	private JMenu fileMenu;
	private JMenuItem revertMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem loadMenuItem;
	private JMenuItem doneMenuItem;

	private JMenu optionsMenu;
	private JMenuItem addColorMenuItem;
	private JMenuItem changeEndColorsMenuItem;

	private final int FRAME_WIDTH = 500;
	private final int FRAME_HEIGHT = 500;
	private final int COLOR_PALETTE_LENGTH = ColorPalette.COLOR_PALETTE_LENGTH;

	public ColorPalette getCreatedColorPalette() {
		return newColorPalette;
	}

	public int getColorPaletteLength() {
		return COLOR_PALETTE_LENGTH;
	}

	public ChangeColorPalette() {
		frame = new JFrame("Color Palette Creator");
		frame.setLayout(new BorderLayout());
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);

		colorPanelsPanel = new JPanel();

		colorPanels = new ArrayList<>();

		menuBar = new JMenuBar();

		fileMenu = new JMenu("File");
		revertMenuItem = new JMenuItem("Revert To Original");
		saveMenuItem = new JMenuItem("Save");
		loadMenuItem = new JMenuItem("Load(This will exit the ColorPalette Creator)");
		doneMenuItem = new JMenuItem("Done");
		fileMenu.add(revertMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.add(loadMenuItem);
		fileMenu.add(doneMenuItem);

		optionsMenu = new JMenu("Options");
		addColorMenuItem = new JMenuItem("Add Color");
		changeEndColorsMenuItem = new JMenuItem("Change End Colors");
		optionsMenu.add(addColorMenuItem);
		optionsMenu.add(changeEndColorsMenuItem);

		menuBar.add(fileMenu);
		menuBar.add(optionsMenu);

		saveMenuItem.addActionListener(new ActionListener() {

			private ColorPaletteSaverLoader saverLoader = ColorPaletteSaverLoader.getDefaultColorPaletteSaverLoader();
			private JFileChooser fileChooser = new JFileChooser();

			public void actionPerformed(ActionEvent ae) {
				createColorPalette();
				if(fileChooser.showDialog(null, "Save") == JFileChooser.APPROVE_OPTION) {
					saverLoader.save(newColorPalette, fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		loadMenuItem.addActionListener(new ActionListener() {

			private ColorPaletteSaverLoader saverLoader = ColorPaletteSaverLoader.getDefaultColorPaletteSaverLoader();
			private JFileChooser fileChooser = new JFileChooser();

			public void actionPerformed(ActionEvent ae) {
				if(fileChooser.showDialog(null, "Load") == JFileChooser.APPROVE_OPTION) {
					temporaryColorPalette = saverLoader.load(ColorPalette.class,
							fileChooser.getSelectedFile().getAbsolutePath());
					frame.setVisible(false);
					newColorPalette = temporaryColorPalette;
				}
			}
		});
		addColorMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				createColor();
			}
		});
		changeEndColorsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				changeEndColorsMenuItem.setBackground(JColorChooser.showDialog(null, "Color Chooser", Color.BLACK));
			}
		});
		doneMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				createColorPalette();
				newColorPalette = temporaryColorPalette;
				frame.setVisible(false);
			}
		});
		revertMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				newColorPalette = ColorPalette.DEFAULT_PALETTE;
				frame.setVisible(false);
			}
		});

		frame.add(menuBar, BorderLayout.NORTH);
		frame.add(colorPanelsPanel, BorderLayout.CENTER);

		frame.setVisible(true);
	}

	/** Validates and repaints the frame.
	 */
	public void repaint() {
		frame.validate();
		frame.repaint();
	}

	/** Removes a color from both the colorPanels list and the frame.
	 * 
	 * @param colorPanel
	 */
	public void removeColor(ColorPanel colorPanel) {
		colorPanels.remove(colorPanel);
		colorPanelsPanel.remove(colorPanel);
		repaint();
	}

	/** Adds a new ColorPanel to the colorPanels list and to the frame.
	 */
	private void createColor() {
		ColorPanel addition = new ColorPanel(this);
		colorPanels.add(addition);
		colorPanelsPanel.add(addition);
		repaint();
	}

	/** This will create a colorPalette and set newColorPalette equal to it.
	 */
	private void createColorPalette() {
		List<Color> colors = new ArrayList<>();
		List<Integer> positions = new ArrayList<>();
		colors.add(changeEndColorsMenuItem.getBackground());
		positions.add(0);
		for(int count = 0; count < colorPanels.size(); count++) {
			colors.add(colorPanels.get(count).getColor());
			positions.add(colorPanels.get(count).getPosition());
		}
		colors.add(changeEndColorsMenuItem.getBackground());
		positions.add(COLOR_PALETTE_LENGTH);

		temporaryColorPalette = ColorPaletteMapper.getDefaultMapper().map(colors, positions);
	}
}