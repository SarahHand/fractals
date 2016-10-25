package com.sarahhand.fractals.model;

import java.awt.geom.Point2D.Double;

import com.sarahhand.fractals.view.FractalsUI;

public class MandelbrotViewerImplTest{
	
	public void readerTest(){
		
		//Arrange
		MandelbrotConfig config = new MandelbrotConfig(new Double(-0.5, 0), 100, ColorPalette.DEFAULT_PALETTE, 100);
		MandelbrotViewer viewer = new MandelbrotViewerImpl(config);
		FractalsUI ui = new FractalsUI();
		
		//Act
		ui.
		
		//Assert
		
	}
}
