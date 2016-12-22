package com.sarahhand.fractals.juliaset;

import java.awt.geom.Point2D.Double;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sarahhand.fractals.juliaset.colorscheme.DefaultJuliaColorScheme;
import com.sarahhand.fractals.model.ColorPalette;
import com.sarahhand.fractals.model.ColorScheme;
import com.sarahhand.fractals.model.FractalConfig;

/**
 * Class that stores information about a particular image of the Julia Set.
 * 
 * @author J9465812
 */
public class JuliaConfig implements FractalConfig{

	private static final double CENTER_X_DEFAULT = 0;
	private static final double CENTER_Y_DEFAULT = 0;
	private static final int ZOOM_DEFAULT = 200;
	private static final int MAX_DWELL_DEFAULT = 500;
	private static final ColorScheme COLOR_SCHEME_DEFAULT = new DefaultJuliaColorScheme();
	private static final Map<String,Float> DEFAULT_CONSTANTS = initDefaultConstants();
	
	private static Map<String,Float> initDefaultConstants(){
		
		Map<String,Float> constants = new HashMap<>();
		constants.put(JuliaViewer.C_REAL_NAME, -1.235f);
		constants.put(JuliaViewer.C_IMAGINARY_NAME, 0.1f);
		
		return constants;
	}
	
	/**
	 * The default JuliaConfig.
	 */
	public static final JuliaConfig DEAFAULT_CONFIG = new JuliaConfig();

	private double x = CENTER_X_DEFAULT;
	private double y = CENTER_Y_DEFAULT;
	private double zoom = ZOOM_DEFAULT;
	private int maxDwell = MAX_DWELL_DEFAULT;
	private ColorPalette palette = ColorPalette.DEFAULT_PALETTE;
	private ColorScheme colorScheme = COLOR_SCHEME_DEFAULT;
	
	/**
	 * Valid constant names:<ul>
	 * <b><li>c imaginary</li>
	 * <li>c real</li>
	 * </ul></b>
	 */
	private Map<String,Float> constants = DEFAULT_CONSTANTS;
	
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
	
	/**
	 * Default constructor creates config with default values.
	 */
	private JuliaConfig() {}
	
	/** Creates a JuliaConfig with the specified values.
	 * 
	 * @param center
	 * @param zoom
	 * @param maxDwell
	 * @param palette
	 * @param colorScheme
	 * @param constants must contain keys: c real and c imaginary
	 */
	public JuliaConfig(Double center, double zoom, int maxDwell, ColorPalette palette, ColorScheme colorScheme, Map<String,Float> constants) {
		this.x = center.x;
		this.y = center.y;
		this.zoom = zoom;
		this.maxDwell = maxDwell;
		this.palette = palette;
		this.colorScheme = colorScheme;
		this.constants = constants;
	}
	
	/**
	 * Creates a JuliaConfig with the specified values, separating out the center x/y coordinates 
	 * into different constructor args.
	 * @param x
	 * @param y
	 * @param zoom
	 * @param maxDwell
	 * @param palette
	 * @param constants must contain keys: c real and c imaginary
	 */
	@JsonCreator
	public JuliaConfig(@JsonProperty("x") double x, @JsonProperty("y") double y, 
			@JsonProperty("zoom") double zoom, 
			@JsonProperty("maxDwell") int maxDwell, 
			@JsonProperty("palette") ColorPalette palette,
			@JsonProperty("constants") Map<String,Float> constants) {
		this.x = x;
		this.y = y;
		this.zoom = zoom;
		this.maxDwell = maxDwell;
		this.palette = palette;
		this.constants = constants;
	}

	@Override
	public Map<String, Float> getConstants(){
		return constants;
	}
}
