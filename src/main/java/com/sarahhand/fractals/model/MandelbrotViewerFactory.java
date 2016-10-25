package com.sarahhand.fractals.model;

import java.awt.Dimension;
import java.awt.Image;

public class MandelbrotViewerFactory{
	
	public static MandelbrotViewer createViewer(){
		return new MandelbrotViewerImpl(MandelbrotConfig.DEAFAULT_CONFIG);
	}
}
