package com.sarahhand.fractals.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D.Double;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.sarahhand.fractals.model.ColorPalette;
import com.sarahhand.fractals.model.MandelbrotConfig;
import com.sarahhand.fractals.model.MandelbrotViewer;
import com.sarahhand.fractals.model.MandelbrotViewerFactory;

/** This class will display the Mandelbrot set and allow you to do a variety of other things.
 * 
 * @author mhand
 *
 */
public class FractalsUI implements MouseListener {
	
	private JFrame frame;
	private Dimension frameDimension;
	MandelbrotViewer viewer;
	private MandelbrotConfig mandelConfig;
	private ColorPalette palette;
	private ImageIcon image;
	private JLabel imageLabel;
	
	public FractalsUI() {
		frame = new JFrame("Fractals Drawer");
		frame.setLayout(new FlowLayout());
		frameDimension = new Dimension(800, 700);
		frame.setSize(frameDimension);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		viewer = MandelbrotViewerFactory.createViewer();
		mandelConfig = MandelbrotConfig.DEAFAULT_CONFIG;
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
		Double center = new Double(mouseX, mouseY);
		mandelConfig.setCenter(center);
		if(buttonType == 1) {
			mandelConfig.setZoom(mandelConfig.getZoom() * 10);
		} else if(buttonType == 3) {
			mandelConfig.setZoom(mandelConfig.getZoom() / 10);
		}
		viewer.getView(frameDimension);
	}

	public void mouseEntered(MouseEvent me) {}

	public void mouseExited(MouseEvent me) {}

	public void mouseReleased(MouseEvent me) {}

	public void mousePressed(MouseEvent me) {}

	public static void main(String args[]) {
		FractalsUI ui = new FractalsUI();
	}
}