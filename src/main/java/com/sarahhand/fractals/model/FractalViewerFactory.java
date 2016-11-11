package com.sarahhand.fractals.model;

/**
 * Factory for <code>FractalViewer</code>.
 * 
 * @author J9465812
 * @see FractalViewer
 * @see MandelbrotViewerImpl
 */

public class FractalViewerFactory{
	
	/**
	 * Creates a FractalViewer for the specified FractalType.
	 * @param type
	 * @return
	 */
	public FractalViewer createViewer(FractalType type){
		switch (type) {
			case MANDELBROT_SET : 
				return new MandelbrotViewerImpl(MandelbrotConfig.DEAFAULT_CONFIG);
			default : 
				return new MandelbrotViewerImpl(MandelbrotConfig.DEAFAULT_CONFIG);
		}
		
	}
}
