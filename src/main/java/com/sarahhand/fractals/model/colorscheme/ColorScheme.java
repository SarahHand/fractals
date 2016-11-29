package com.sarahhand.fractals.model.colorscheme;

import java.awt.Color;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sarahhand.fractals.model.FractalConfig;
import com.sarahhand.fractals.model.PointData;
import com.sarahhand.fractals.model.colorscheme.mandelbrotset.EscapeTimeColorScheme;

// @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = EscapeTimeColorScheme.class, name = "EscapeTime") 
})
public interface ColorScheme{
	
	public String getName();
	
	public Color getColor(PointData data, FractalConfig config);
}
