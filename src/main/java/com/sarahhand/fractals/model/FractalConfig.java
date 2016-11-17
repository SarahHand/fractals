package com.sarahhand.fractals.model;

import java.awt.geom.Point2D.Double;

import com.sarahhand.fractals.model.colorscheme.ColorScheme;

/**
 * Marker interface for a fractal's configuration information.
 */
public interface FractalConfig{

	/**
	 * The current center point of the fractal.
	 * @return
	 */
	public Double getCenter();
	
	/**
	 * The current zoom of the fractal.
	 * @return
	 */
	public double getZoom();
	
	/**
	 * The current maxDwell for the fractal - a number used to control
	 * when the fractal equation assumes non-divergence.
	 * @return
	 */
	public int getMaxDwell();
	
	/**
	 * The current color palette of the fractal.
	 * @return
	 */
	public ColorPalette getPalette();
	
	public ColorScheme getColorScheme();
}