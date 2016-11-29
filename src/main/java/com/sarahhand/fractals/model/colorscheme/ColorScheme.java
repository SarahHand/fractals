package com.sarahhand.fractals.model.colorscheme;

import java.awt.Color;

import com.sarahhand.fractals.model.FractalConfig;
import com.sarahhand.fractals.model.PointData;


/**
 * Interface that represents ways of coloring fractals.
 * @author J9465812
 */
public interface ColorScheme{
	
	public String getName();
	
	public Color getColor(PointData data, FractalConfig config);
	
	public boolean isReady(PointData data, FractalConfig config);
}
