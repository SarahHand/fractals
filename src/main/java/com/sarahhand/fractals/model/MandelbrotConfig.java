package com.sarahhand.fractals.model;

import java.awt.geom.Point2D.Double;

/**
 * Class that stores information about a particular image of the Mandelbrot Set.
 * 
 * @author J9465812
 *
 */

public class MandelbrotConfig{

	public static final MandelbrotConfig DEAFAULT_CONFIG = new MandelbrotConfig(new Double(-0.5, 0), 200, ColorPalette.DEFAULT_PALETTE, 500);

	private Double center;
	private double zoom;
	private ColorPalette palette;
	private int maxDwell;

	public Double getCenter() {
		return center;
	}

	public double getZoom() {
		return zoom;
	}

	public ColorPalette getPalette() {
		return palette;
	}

	public int getMaxDwell() {
		return maxDwell;
	}

	public MandelbrotConfig(Double center, double zoom, ColorPalette palette, int maxDwell) {
		this.center = center;
		this.zoom = zoom;
		this.palette = palette;
		this.maxDwell = maxDwell;
	}
	
	/** Creates a new MandelbrotConfig using an existing config, a new zoom, and a new center.
	 * 
	 * @param center
	 * @param zoom
	 * @param maxdwell
	 * @param old
	 */
	public MandelbrotConfig(Double center, double zoom, int maxDwell, MandelbrotConfig old) {
		this.center = center;
		this.zoom = zoom;
		this.maxDwell = maxDwell;
		this.palette = old.palette;
	}
}
