package com.sarahhand.fractals.model;

import java.awt.geom.Point2D.Double;

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
	 * Returns the color scheme
	 * @return
	 */
	public ColorScheme getColorScheme();
	
	/**
	 * Sets the zoom.
	 * @param zoom 
	 * @return
	 */
	public void setZoom(double zoom);
	
	/**
	 * Sets the max dwell.
	 * @param maxDwell 
	 * @return
	 */
	public void setMaxDwell(int maxDwell);

	/**
	 * Sets the colorScheme.s
	 * @param colorScheme 
	 * @return
	 */
	public void setColorScheme(ColorScheme colorScheme);
	
	/**
	 * Sets the center.
	 * @param center 
	 * @return
	 */
	public void setCenter(Double center);
}