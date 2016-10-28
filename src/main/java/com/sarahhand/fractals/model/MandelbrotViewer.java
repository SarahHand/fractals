package com.sarahhand.fractals.model;

import java.awt.Dimension;
import java.awt.Image;

/**
 * Interface that draws the Mandelbrot Set.
 * 
 * @author J9465812
 * 
 * @see
 * MandelbrotViewerImpl
 */

public interface MandelbrotViewer{

	public Image getView(Dimension dimensions);

	public void setConfig(MandelbrotConfig config);

	public MandelbrotConfig getConfig();
}
