package com.sarahhand.fractals.model.colorscheme;

import java.awt.Color;

import com.sarahhand.fractals.model.FractalConfig;
import com.sarahhand.fractals.model.PointData;

public interface ColorScheme{
	
	public String getName();
	
	public Color getColor(PointData data, FractalConfig config);
}
