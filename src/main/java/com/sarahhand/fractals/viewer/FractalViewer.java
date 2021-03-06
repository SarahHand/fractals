package com.sarahhand.fractals.viewer;

import java.awt.Dimension;
import java.awt.Image;
import java.util.List;

import com.sarahhand.fractals.model.ColorScheme;
import com.sarahhand.fractals.model.FractalConfig;

/**
 * Interface that draws a fractal.
 * 
 * @author J9465812
 * 
 * @see
 * MandelbrotViewer
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
	 * Generates an image of the fractal based on its current fractal config
	 * and the specified dimensions, while taking advantage of pixels previously
	 * rendered in oldImage.
	 * @param dimensions
	 * @param oldConfig the config used to generate oldImage
	 * @param oldImage the previous image
	 * @return 
	 */
	public Image getViewPanning(Dimension dimensions, FractalConfig oldConfig, Image oldImage);

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
	
	/**
	 * Returns a list of color schemes supported by this viewer.
	 * @return 
	 */
	public List<ColorScheme> getSupportedColorSchemes();
}
