package com.sarahhand.fractals.model;

import java.awt.geom.Point2D.Double;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sarahhand.fractals.juliaset.JuliaConfig;
import com.sarahhand.fractals.mandelbrotset.MandelbrotConfig;

/**
 * Marker interface for a fractal's configuration information.
 */

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = MandelbrotConfig.class, name = "MandelbrotSet"),
    @JsonSubTypes.Type(value = JuliaConfig.class, name = "JuliaSet")
})
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
	 * Sets the colorSchemes
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
	
	public void setConstants(Map<String, Float> constants);
	
	/**
	 * @return the constants used by this fractal
	 */
	public Map<String,Float> getConstants();
}