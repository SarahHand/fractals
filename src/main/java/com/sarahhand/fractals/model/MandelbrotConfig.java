package com.sarahhand.fractals.model;

import java.awt.geom.Point2D.Double;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sarahhand.fractals.model.colorscheme.ColorScheme;
import com.sarahhand.fractals.model.colorscheme.mandelbrotset.EscapeTimeColorScheme;
import com.sarahhand.fractals.model.colorscheme.mandelbrotset.ExternalDistanceEstimateColorScheme;

/**
 * Class that stores information about a particular image of the Mandelbrot Set.
 * 
 * @author J9465812
 */
public class MandelbrotConfig implements FractalConfig{

	private static double CENTER_X_DEFAULT = -0.5;
	private static double CENTER_Y_DEFAULT = 0;
	private static int ZOOM_DEFAULT = 200;
	private static int MAX_DWELL_DEFAULT = 500;
	private static ColorScheme COLOR_SCHEME_DEFAULT = new EscapeTimeColorScheme();
	
	public static final MandelbrotConfig DEAFAULT_CONFIG = new MandelbrotConfig();

	private double x = CENTER_X_DEFAULT;
	private double y = CENTER_Y_DEFAULT;
	private double zoom = ZOOM_DEFAULT;
	private int maxDwell = MAX_DWELL_DEFAULT;
	private ColorPalette palette = ColorPalette.DEFAULT_PALETTE;
	private ColorScheme colorScheme = COLOR_SCHEME_DEFAULT;
	
	public double getX(){
		return x;
	}

	public double getY(){
		return y;
	}
	
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
	
	@Override
	public ColorPalette getPalette() {
		return palette;
	}
	
	@JsonIgnore
	public ColorScheme getColorScheme(){
		return colorScheme;
	}
	
	/**
	 * Default constructor creates config with default values.
	 */
	private MandelbrotConfig() {}
	
	/** Creates a MandelbrotConfig with the specified values.
	 * 
	 * @param center
	 * @param zoom
	 * @param maxDwell
	 * @param palette
	 * @param colorScheme
	 */
	public MandelbrotConfig(Double center, double zoom, int maxDwell, ColorPalette palette, ColorScheme colorScheme) {
		this.x = center.x;
		this.y = center.y;
		this.zoom = zoom;
		this.maxDwell = maxDwell;
		this.palette = palette;
		this.colorScheme = colorScheme;
	}
	
	/**
	 * Creates a MandelbrotConfig with the specified values, separating out the center x/y coordinates 
	 * into different constructor args.
	 * @param x
	 * @param y
	 * @param zoom
	 * @param maxDwell
	 * @param palette
	 */
	@JsonCreator
	public MandelbrotConfig(@JsonProperty("x") double x, @JsonProperty("y") double y, 
			@JsonProperty("zoom") double zoom, 
			@JsonProperty("maxDwell") int maxDwell, 
			@JsonProperty("palette") ColorPalette palette) {
		this.x = x;
		this.y = y;
		this.zoom = zoom;
		this.maxDwell = maxDwell;
		this.palette = palette;
	}

	/**
	 * Creates a new MandelbrotConfig using an existing config with a new zoom, center and maxDwell. It
	 * uses the ColorPalette and the ColorScheme from the existing config.
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
		this.colorScheme = old.colorScheme;
	}

	/** This method creates a new MandelbrotConfig that is the same as the old config except that the ColorScheme is changed to colorScheme.
	 * 
	 * @param colorScheme
	 * @param old
	 */
	public MandelbrotConfig(ColorScheme colorScheme, MandelbrotConfig old) {
		this.x = old.getCenter().x;
		this.y = old.getCenter().y;
		this.zoom = old.zoom;
		this.maxDwell = old.maxDwell;
		this.palette = old.palette;
		this.colorScheme = colorScheme;
	}
}
