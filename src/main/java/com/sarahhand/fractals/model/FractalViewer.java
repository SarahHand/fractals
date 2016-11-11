package com.sarahhand.fractals.model;

import java.awt.Dimension;
import java.awt.Image;

/**
 * Interface that draws a fractal.
 * 
 * @author J9465812
 * 
 * @see
 * MandelbrotViewerImpl
 */

public interface FractalViewer{

	/**
	 * Generates an image of the fractal based on its current fractal config
	 * and the specified dimensions.
	 * @param dimensions
	 * @return
	 */
	public Image getView(Dimension dimensions);

	/**
	 * Updates the fractal's current config to that specified.
	 * @param config
	 */
	public void setConfig(FractalConfig config);

	/**
	 * Returns the fractal's current config.
	 * @return
	 */
	public FractalConfig getConfig();
}
