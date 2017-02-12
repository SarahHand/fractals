package com.sarahhand.fractals.juliaconstants;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sarahhand.fractals.event.FractalEventHandler;

/** This class opens a window that lets the user change the constants
 * of the fractal the user is viewing.
 * 
 * @author M00031
 *
 */
public class ChangeJuliaConstants {

	private Map<String, Float> constants;

	private JFrame frame;

	private JPanel constantPanelsPanel;

	private JButton done;

	private volatile boolean finished = false;

	public Map<String, Float> getJuliaConstants() {
		return constants;
	}

	public boolean isFinished() {
		return finished;
	}

	private List<ConstantsPanel> constantPanels;

	public ChangeJuliaConstants(FractalEventHandler events) {
		frame = new JFrame("Julia Constants");
		frame.setLayout(new BorderLayout());
		frame.setSize(500, 500);

		constants = events.getFractalViewer().getConfig().getConstants();

		constantPanelsPanel = new JPanel();

		constantPanels = new ArrayList<ConstantsPanel>();

		Iterator<String> iterator = constants.keySet().iterator();
		while(iterator.hasNext()) {
			ConstantsPanel panel = new ConstantsPanel(iterator.next());
			constantPanels.add(panel);
			constantPanelsPanel.add(panel);
		}

		done = new JButton("Done");

		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				for(ConstantsPanel panel:constantPanels) {
					constants.put(panel.getConstantName(), panel.getConstantValue());
					finished = true;
					frame.setVisible(false);
				}
			}
		});

		frame.add(constantPanelsPanel);
		frame.add(done, BorderLayout.NORTH);

		frame.pack();
		frame.setVisible(true);
	}
}