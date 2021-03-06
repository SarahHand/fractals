package com.sarahhand.fractals.mandelbrotset.colorscheme;

import java.awt.Color;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sarahhand.fractals.mandelbrotset.MandelbrotConfig;
import com.sarahhand.fractals.mandelbrotset.MandelbrotPointData;
import com.sarahhand.fractals.model.ColorPalette;
import com.sarahhand.fractals.model.ColorScheme;
import com.sarahhand.fractals.model.ComplexNumber;
import com.sarahhand.fractals.model.FractalConfig;
import com.sarahhand.fractals.model.PointData;

/**
 * The default color scheme.
 * @author J9465812
 */
@JsonTypeName("EscapeTime")
public class EscapeTimeColorScheme implements ColorScheme{
	
	private static final double LOG2 = Math.log(2);

	@Override
	public String getName(){
		return "Escape Time";
	}

	@Override
	public Color getColor(PointData data, FractalConfig config){
		
		if(!(data instanceof MandelbrotPointData)){
			
			throw new IllegalArgumentException("data must be instance of MandelbrotPointData");
		}
		
		if(!(config instanceof MandelbrotConfig)){
			
			throw new IllegalArgumentException("config must be instance of MandelbrotConfig");
		}
		
		MandelbrotPointData castData = (MandelbrotPointData)data;
		MandelbrotConfig castConfig = (MandelbrotConfig)config;
		
		ComplexNumber z = castData.getAllZValues().get(castData.getEscapeTime());
		
		//nu = log2(log2(|z|))
		double log_zn = Math.log(z.x*z.x+z.y*z.y)/2;
		double nu = Math.log(log_zn/LOG2)/LOG2;
		
		double interpolateValue = (double)castData.getEscapeTime() + 1.0 - nu;
		
		//fudge factor to improve visualization
		interpolateValue = Math.log(Math.abs(interpolateValue))/Math.log(1.01);
		
		//To handle negative numbers with mod
		//((n mod m)+ m ) mod m)
		//n = interpolateValue
		//m = color palette length
		Color col1 = castConfig.getPalette().getColor(((int)(Math.floor(interpolateValue)) %
				ColorPalette.COLOR_PALETTE_LENGTH + ColorPalette.COLOR_PALETTE_LENGTH) %
				ColorPalette.COLOR_PALETTE_LENGTH);
		
		//n = interpolateValue + 1
		Color col2 = castConfig.getPalette().getColor(((int)(Math.floor(interpolateValue+1)) %
				ColorPalette.COLOR_PALETTE_LENGTH + ColorPalette.COLOR_PALETTE_LENGTH) %
				ColorPalette.COLOR_PALETTE_LENGTH);
		
		return interpolate(col1, col2, interpolateValue - Math.floor(interpolateValue));
	}
	
	
	/**
	 * Weighted mixture of c1 and c2 based on the value of amount.
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
