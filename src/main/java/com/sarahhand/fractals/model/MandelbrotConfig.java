package com.sarahhand.fractals.model;

import java.awt.geom.Point2D.Double;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class that stores information about a particular image of the Mandelbrot Set.
 * 
 * @author J9465812
 *
 */

public class MandelbrotConfig implements Config{

	public static final MandelbrotConfig DEAFAULT_CONFIG = new MandelbrotConfig(new Double(-0.5, 0), 200, ColorPalette.DEFAULT_PALETTE, 500);
	
	
	private double x;
	private double y;
	private double zoom;
	private ColorPalette palette = ColorPalette.DEFAULT_PALETTE;
	
	private int maxDwell;
	
	@JsonIgnore
	public Double getCenter() {
		return new Double(x, y);
	}

	public double getZoom() {
		return zoom;
	}

	public int getMaxDwell() {
		return maxDwell;
	}
	
	public ColorPalette getPalette() {
		return palette;
	}
	
	public double getX(){
		return x;
	}

	public double getY(){
		return y;
	}
	
	public MandelbrotConfig(Double center, double zoom, ColorPalette palette, int maxDwell) {
		this.x = center.x;
		this.y = center.y;
		this.zoom = zoom;
		this.palette = palette;
		this.maxDwell = maxDwell;
	}
	
	@JsonCreator
	public MandelbrotConfig(@JsonProperty("x") double x, @JsonProperty("y") double y, @JsonProperty("zoom") double zoom, @JsonProperty("maxDwell") int maxDwell, @JsonProperty("palette") ColorPalette palette) {
		this.x = x;
		this.y = y;
		this.zoom = zoom;
		this.palette = palette;
		this.maxDwell = maxDwell;
	}

	/** Creates a new MandelbrotConfig using an existing config, a new zoom, a new center, and a new maxDwell.
	 * 
	 * @param center
	 * @param zoom
	 * @param maxdwell
	 * @param old
	 */
	public MandelbrotConfig(Double center, double zoom, int maxDwell, MandelbrotConfig old) {
		this.x = center.x;
		this.y = center.y;
		this.zoom = zoom;
		this.maxDwell = maxDwell;
		this.palette = old.palette;
	}
}
