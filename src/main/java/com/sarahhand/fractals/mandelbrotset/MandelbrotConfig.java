package com.sarahhand.fractals.mandelbrotset;

import java.awt.geom.Point2D.Double;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sarahhand.fractals.mandelbrotset.colorscheme.EscapeTimeColorScheme;
import com.sarahhand.fractals.model.ColorPalette;
import com.sarahhand.fractals.model.ColorScheme;
import com.sarahhand.fractals.model.FractalConfig;

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
	
	/**
	 * The default MandelbrotConfig.
	 */
	public static final MandelbrotConfig DEAFAULT_CONFIG = new MandelbrotConfig();

	private double x = CENTER_X_DEFAULT;
	private double y = CENTER_Y_DEFAULT;
	private double zoom = ZOOM_DEFAULT;
	private int maxDwell = MAX_DWELL_DEFAULT;
	private ColorPalette palette = ColorPalette.DEFAULT_PALETTE;
	private ColorScheme colorScheme = COLOR_SCHEME_DEFAULT;
	
	/**
	 * The current x position of the center.
	 * @return
	 */
	public double getX(){
		return x;
	}
	
	/**
	 * The current y position of the center.
	 * @return
	 */
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
	
	/**
	 * The current color palette.
	 * @return
	 */
	public ColorPalette getPalette() {
		return palette;
	}

	public ColorScheme getColorScheme(){
		return colorScheme;
	}
	
	public void setZoom(double zoom){
		this.zoom = zoom;
	}

	public void setMaxDwell(int maxDwell){
		this.maxDwell = maxDwell;
	}
	
	/**
	 * Sets the current color palette.
	 * @param palette
	 */
	public void setPalette(ColorPalette palette){
		this.palette = palette;
	}

	public void setColorScheme(ColorScheme colorScheme){
		this.colorScheme = colorScheme;
	}
	
	public void setCenter(Double center) {
		x = center.x;
		y = center.y;
	}
	
	public void setConstants(Map<String, Float> constants) {}
	
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

	@Override
	public Map<String, Float> getConstants(){
		return new HashMap<>();
	}
}
