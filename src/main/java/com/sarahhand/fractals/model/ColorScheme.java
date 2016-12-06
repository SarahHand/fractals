package com.sarahhand.fractals.model;

import java.awt.Color;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sarahhand.fractals.mandelbrotset.colorscheme.BlackColorScheme;
import com.sarahhand.fractals.mandelbrotset.colorscheme.EscapeTimeColorScheme;
import com.sarahhand.fractals.mandelbrotset.colorscheme.ExternalDistanceEstimateColorScheme;

/**
 * Interface that represents ways of coloring fractals.
 * @author J9465812
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = EscapeTimeColorScheme.class, name = "EscapeTime"),
    @JsonSubTypes.Type(value = ExternalDistanceEstimateColorScheme.class, name = "ExternalDistance") ,
    @JsonSubTypes.Type(value = BlackColorScheme.class, name = "Black") 
})
public interface ColorScheme{
	
	/**
	 * Gets the name that should be shown to the user.
	 * @return
	 */
	public String getName();
	
	/**
	 * Uses <code>data</code> and <code>config</code> to create a color.
	 * @param data
	 * @param config
	 * @return
	 */
	public Color getColor(PointData data, FractalConfig config);
}
