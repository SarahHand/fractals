package com.sarahhand.fractals.model;

/**
 * Factory for <code>MandelbrotViewer</code>.
 * 
 * @author J9465812
 * @see MandelbrotViewer
 * @see MandelbrotViewerImpl
 */

public class MandelbrotViewerFactory{
	
	public MandelbrotViewer createViewer(){
		return new MandelbrotViewerImpl(MandelbrotConfig.DEAFAULT_CONFIG);
	}
}
