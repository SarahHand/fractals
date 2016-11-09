package com.sarahhand.fractals.mapper;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sarahhand.fractals.model.ColorPalette;

public class ColorPaletteMapper{
	
	private static final ColorPaletteMapper defaultMapper = new ColorPaletteMapper();
	
	public static ColorPaletteMapper getDefaultMapper(){
		return defaultMapper;
	}
	
	private ColorPaletteMapper(){}
	
	public ColorPalette map(List<Color> colors, List<Integer> positions){
		
		if(errorCases(colors, positions)){
			return null;
		}
		
		Map<Integer, Color> map = new HashMap<>();
		
		Color[] palette = new Color[ColorPalette.COLOR_PALETTE_LENGTH];
		
		for(int n = 0; n < colors.size(); n++){
			map.put(positions.get(n), colors.get(n));
		}
		
		int beginInterval = 0;
		
		while(beginInterval < ColorPalette.COLOR_PALETTE_LENGTH){

			int endInterval = beginInterval + 1;
			
			while(!positions.contains(endInterval)){
				endInterval++;
			}
			
			Color[] gradient = gradiate(map.get(beginInterval), map.get(endInterval), endInterval-beginInterval);
			
			System.arraycopy(gradient, 0, palette, beginInterval, gradient.length);
			
			beginInterval = endInterval;
		}
		
		return new ColorPalette(palette);
	}
	
	private boolean errorCases(List<Color> colors, List<Integer> positions){
		
		if(colors == null){
			return true;
		}
		
		if(positions == null){
			return true;
		}
		
		if(colors.size() != positions.size()){
			return true;
		}
		
		for(int n = 0; n < colors.size(); n++){
			
			int pos = positions.get(n);
			
			if(pos < 0 || pos > ColorPalette.COLOR_PALETTE_LENGTH){
				return true;
			}
		}
		
		return false;
	}
	
	private Color[] gradiate(Color col1, Color col2, int length){
		
		Color[] gradient = new Color[length];
		
		for(int n = 0; n < gradient.length; n++){
			
			gradient[n] = interpolate(col1, col2, (double)n/length);
		}
		
		return gradient;
	}
	
	private Color interpolate(Color c1, Color c2, double amount){
		
		int r1, r2, g1, g2, b1, b2;
		
		r1 = c1.getRed();
		r2 = c2.getRed();
		g1 = c1.getGreen();
		g2 = c2.getGreen();
		b1 = c1.getBlue();
		b2 = c2.getBlue();
		
		double r = r1*(1 - amount) + r2*amount;
		double g = g1*(1 - amount) + g2*amount;
		double b = b1*(1 - amount) + b2*amount;
		
		return new Color((int)r, (int)g, (int)b);
	}
}
