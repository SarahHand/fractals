package com.sarahhand.fractals.model;


public class MandelbrotViewerFactory{
	
	public MandelbrotViewer createViewer(){
		return new MandelbrotViewerImpl(MandelbrotConfig.DEAFAULT_CONFIG);
	}
}
