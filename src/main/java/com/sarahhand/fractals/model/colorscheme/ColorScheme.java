package com.sarahhand.fractals.model.colorscheme;

import java.awt.Color;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sarahhand.fractals.model.FractalConfig;
import com.sarahhand.fractals.model.PointData;
import com.sarahhand.fractals.model.colorscheme.mandelbrotset.EscapeTimeColorScheme;
import com.sarahhand.fractals.model.colorscheme.mandelbrotset.ExternalDistanceEstimateColorScheme;

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
    @JsonSubTypes.Type(value = ExternalDistanceEstimateColorScheme.class, name = "ExternalDistance")
})
public interface ColorScheme{
	
	public String getName();
	
	public Color getColor(PointData data, FractalConfig config);
}
