package com.sarahhand.fractals.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/** This class brings up a frame with a JScrollPane that contains instructions in it. It is used by FractalsUI.
 * 
 * @author M00031
 *
 */
public class Help {

	private JFrame frame;

	private List<JScrollPane> panes = new ArrayList<>();
	private List<JPanel> panels = new ArrayList<>();
	private List<JLabel> headers = new ArrayList<>();
	private List<JLabel> labels = new ArrayList<>();

	private JScrollPane currentPane;
	private int current = 0;

	private JPanel buttonsPanel;
	private JButton next;
	private JButton previous;

	public Help() {
		frame = new JFrame("Help");
		frame.setLayout(new BorderLayout());
		frame.setSize(1000, 1000);

		headers.add(new JLabel("Definitions", SwingConstants.CENTER));
		labels.add(new JLabel("<html><ul><li>Color Palette - What colors the fractal has in it"
				+ "<li>Color Scheme - How the colors show up in the fractal"
				+ "<li>Constants - The equations of some fractals change depending on the constants"));

		headers.add(new JLabel("Moving Around The Fractal", SwingConstants.CENTER));
		labels.add(new JLabel("<html>Left Mouse Button - Zoom in<br>"
				+ "Right Mouse Button - Zoom out<br>"
				+ "Drag Mouse - Pan around the fractal"));

		headers.add(new JLabel("Color Palette Navigation", SwingConstants.CENTER));
		labels.add(new JLabel("<html>After clicking on Change Color Palette, a window shows up. This window has an Add<br>Color and Change End Colors "
				+ "option. Clicking Add Color will add a button, to change<br>the color, a second button, to remove the color, and a slider. The slider "
				+ "says where<br>the color is located in the Color Palette. The Change End Colors will do what it implies.<br>You can also save and load "
				+ "Color Palettes. Click Done when you are finished."));

		headers.add(new JLabel("Saving, Loading, And Photography", SwingConstants.CENTER));
		labels.add(new JLabel("<html>Under the file tab, there is a save option. This option lets you pick the location in which<br>to place the JSON "
				+ "file containing the save data. The load option lets you load any<br>JSON file of any fractal and continue where you left off. "
				+ "The save screenshot option<br>allows you to save and image in any of the following formats."
				+ "<ul><li>jpg<li>jpeg<li>png<li>gif<li>bmp"));

		for(int count = 0; count < headers.size(); count++) {
			panels.add(new JPanel());
			panels.get(count).add(labels.get(count));
			headers.get(count).setFont(new Font("", Font.BOLD + Font.ITALIC, 15));
			panes.add(new JScrollPane(panels.get(count)));
			panes.get(count).setColumnHeaderView(headers.get(count));
		}

		currentPane = panes.get(current);

		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BorderLayout());
		next = new JButton("Next");
		previous = new JButton("Previous");
		buttonsPanel.add(previous, BorderLayout.WEST);
		buttonsPanel.add(next, BorderLayout.EAST);

		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					frame.remove(currentPane);
					frame.repaint();
					current++;
					currentPane = panes.get(current);
					frame.add(currentPane, BorderLayout.CENTER);
					frame.repaint();
					frame.validate();
				} catch(Exception e) {
					current--;
					frame.add(currentPane);
					frame.repaint();
					frame.validate();
				}
			}
		});

		previous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					frame.remove(currentPane);
					frame.repaint();
					current--;
					currentPane = panes.get(current);
					frame.add(currentPane, BorderLayout.CENTER);
					frame.repaint();
					frame.validate();
				} catch(Exception e) {
					current++;
					frame.add(currentPane);
					frame.repaint();
					frame.validate();
				}
			}
		});

		frame.add(currentPane, BorderLayout.CENTER);
		frame.add(buttonsPanel, BorderLayout.SOUTH);

		frame.pack();
		frame.setVisible(true);
	}
}