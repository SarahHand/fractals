package com.sarahhand.fractals.colorpalette;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/** Extension from JPanel that adds two JButtons and a JSlider to itself.
 * 
 * @author M00031
 *
 */
public class ColorPanel extends JPanel {

	private static final long serialVersionUID = -3900605965559036583L;

	JButton changeColor;
	JButton removeColor;
	JSlider colorPosition;

	Color color = Color.WHITE;
	int position = 100;

	public Color getColor() {
		return color;
	}

	public int getPosition() {
		return position;
	}

	public ColorPanel(ChangeColorPalette colorPalette) {
		super();

		changeColor = new JButton("Change Color");
		removeColor = new JButton("Remove");
		colorPosition = new JSlider(0, 200);

		position = colorPosition.getValue();

		changeColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				color = JColorChooser.showDialog(null, "Color Chooser", Color.BLACK);
				changeColor.setBackground(color);
			}
		});
		removeColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				remove(colorPalette);
			}
		});
		colorPosition.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				position = colorPosition.getValue();
			}
		});

		add(changeColor);
		add(removeColor);
		add(colorPosition);
	}

	/** Used to call removeColor() from ChangeColorPalette because "this" needs to be used and it refers
	 * to ActionListener on line 39.
	 */
	private void remove(ChangeColorPalette colorPalette) {
		colorPalette.removeColor(this);
	}
}