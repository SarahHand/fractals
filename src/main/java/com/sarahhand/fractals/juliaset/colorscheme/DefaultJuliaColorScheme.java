package com.sarahhand.fractals.juliaset.colorscheme;

import java.awt.Color;

import com.sarahhand.fractals.juliaset.JuliaConfig;
import com.sarahhand.fractals.juliaset.JuliaPointData;
import com.sarahhand.fractals.model.ColorPalette;
import com.sarahhand.fractals.model.ColorScheme;
import com.sarahhand.fractals.model.ComplexNumber;
import com.sarahhand.fractals.model.FractalConfig;
import com.sarahhand.fractals.model.PointData;

public class DefaultJuliaColorScheme implements ColorScheme{
	
	private static final double LOG2 = Math.log(2);
	
	@Override
	public String getName(){
		return "Default";
	}

	@Override
	public Color getColor(PointData data, FractalConfig config){
		if(!(data instanceof JuliaPointData)){
			
			throw new IllegalArgumentException("data must be instance of JuliaPointData");
		}
		
		if(!(config instanceof JuliaConfig)){
			
			throw new IllegalArgumentException("config must be instance of JuliaConfig");
		}
		
		JuliaPointData castData = (JuliaPointData)data;
		JuliaConfig castConfig = (JuliaConfig)config;
		
		ComplexNumber z = castData.getAllZValues().get(castData.getAllZValues().size()-1);
		
		double log_zn = Math.log(z.x*z.x+z.y*z.y)/2;
		double nu = Math.log(log_zn/LOG2)/LOG2;
		
		double interpolateValue = (double)castData.getEscapeTime() + 1.0 - nu;
		
		interpolateValue = Math.log(Math.abs(interpolateValue))/Math.log(1.01);
		
		Color col1 = castConfig.getPalette().getColor(((int)(Math.floor(interpolateValue)) %
				ColorPalette.COLOR_PALETTE_LENGTH + ColorPalette.COLOR_PALETTE_LENGTH) %
				ColorPalette.COLOR_PALETTE_LENGTH);
		
		Color col2 = castConfig.getPalette().getColor(((int)(Math.floor(interpolateValue+1)) %
				ColorPalette.COLOR_PALETTE_LENGTH + ColorPalette.COLOR_PALETTE_LENGTH) %
				ColorPalette.COLOR_PALETTE_LENGTH);
		
		return interpolate(col1, col2, interpolateValue - Math.floor(interpolateValue));
	}
	
	/**
	 * Mixes c1 and c2 based on the value of amount.
	 * @param c1
	 * @param c2
	 * @param amount
	 * @return
	 */
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
