package com.sarahhand.fractals.viewer;

import com.sarahhand.fractals.mandelbrotset.MandelbrotConfig;
import com.sarahhand.fractals.mandelbrotset.MandelbrotViewer;
import com.sarahhand.fractals.model.FractalType;

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
				return new MandelbrotViewer(MandelbrotConfig.DEAFAULT_CONFIG);
			default : 
				return new MandelbrotViewer(MandelbrotConfig.DEAFAULT_CONFIG);
		}
	}
}
