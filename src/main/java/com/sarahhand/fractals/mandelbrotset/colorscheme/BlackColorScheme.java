package com.sarahhand.fractals.mandelbrotset.colorscheme;

import java.awt.Color;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sarahhand.fractals.model.ColorScheme;
import com.sarahhand.fractals.model.FractalConfig;
import com.sarahhand.fractals.model.PointData;


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
