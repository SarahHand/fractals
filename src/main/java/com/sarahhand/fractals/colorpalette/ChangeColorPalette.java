package com.sarahhand.fractals.colorpalette;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

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

	private JPanel topPanel;
	private JButton changeEndColors;
	private JButton resetColorPalette;

	private JPanel bottomPanel;
	private JButton addColor;
	private JButton doneButton;

	private final int FRAME_WIDTH = 500;
	private final int FRAME_HEIGHT = 500;
	private final int COLOR_PALETTE_LENGTH = 200;

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
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		colorPanelsPanel = new JPanel();

		colorPanels = new ArrayList<>();

		topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		changeEndColors = new JButton("Change End Colors");
		resetColorPalette = new JButton("Reset");
		topPanel.add(changeEndColors, BorderLayout.SOUTH);
		topPanel.add(resetColorPalette, BorderLayout.NORTH);

		bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		addColor = new JButton("Add Color");
		doneButton = new JButton("Done");
		bottomPanel.add(addColor, BorderLayout.NORTH);
		bottomPanel.add(doneButton, BorderLayout.SOUTH);

		addColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				createColor();
			}
		});
		changeEndColors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				changeEndColors.setBackground(JColorChooser.showDialog(null, "Color Chooser", Color.BLACK));
			}
		});
		doneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				newColorPalette = createColorPalette();
				frame.setVisible(false);
			}
		});
		resetColorPalette.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				newColorPalette = ColorPalette.DEFAULT_PALETTE;
				frame.setVisible(false);
			}
		});

		frame.add(bottomPanel, BorderLayout.SOUTH);
		frame.add(topPanel, BorderLayout.NORTH);
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

	/** This will create a colorPalette using the method XXXXX() from the class XXXXX.
	 */
	private ColorPalette createColorPalette() {
		List<Color> colors = new ArrayList<>();
		List<Integer> positions = new ArrayList<>();
		colors.add(changeEndColors.getBackground());
		positions.add(0);
		for(int count = 0; count < colorPanels.size(); count++) {
			colors.add(colorPanels.get(count).getColor());
			positions.add(colorPanels.get(count).getPosition());
		}
		colors.add(changeEndColors.getBackground());
		positions.add(COLOR_PALETTE_LENGTH);

		frame.setVisible(false);

		return ColorPaletteMapper.getDefaultMapper().map(colors, positions);
	}
}