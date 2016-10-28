package com.sarahhand.fractals.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.sarahhand.fractals.model.MandelbrotViewer;
import com.sarahhand.fractals.model.MandelbrotViewerFactory;

public class FractalsUI implements MouseListener {
	
	JFrame frame;
	Dimension frameDimension;
	MandelbrotViewerFactory viewerFactory = new MandelbrotViewerFactory();
	MandelbrotViewer viewer;
	ImageIcon image;
	JLabel imageLabel;
	
	public FractalsUI() {
		frame = new JFrame("Fractal Viewer");
		
		frame.setLayout(new FlowLayout());
		frameDimension = new Dimension(800, 700);
		frame.setSize(frameDimension);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		viewer = viewerFactory.createViewer();
		image = new ImageIcon(viewer.getView(frameDimension));
		imageLabel = new JLabel(image);
		frame.add(imageLabel);
		frame.addMouseListener(this);
		frame.setVisible(true);
	}

	//TODO This one, not the others.
	public void mouseClicked(MouseEvent me) {
		int buttonType = me.getButton();
		int mouseX = me.getX();
		int mouseY = me.getY();
		if(buttonType == 1) {
			
		} else if(buttonType == 3) {
			
		}
	}

	public void mouseEntered(MouseEvent me) {}

	public void mouseExited(MouseEvent me) {}

	public void mouseReleased(MouseEvent me) {}

	public void mousePressed(MouseEvent me) {}

	public static void main(String args[]) {
		FractalsUI ui = new FractalsUI();
	}
}