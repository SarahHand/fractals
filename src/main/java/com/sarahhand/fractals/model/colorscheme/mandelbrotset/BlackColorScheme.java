package com.sarahhand.fractals.model.colorscheme.mandelbrotset;

import java.awt.Color;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sarahhand.fractals.model.FractalConfig;
import com.sarahhand.fractals.model.PointData;
import com.sarahhand.fractals.model.colorscheme.ColorScheme;

@JsonTypeName("Black")
public class BlackColorScheme implements ColorScheme {

	@Override
	public String getName(){
		return "Black";
	}

	@Override
	public Color getColor(PointData data, FractalConfig config){
		return Color.BLACK;
	}
}
