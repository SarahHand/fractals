package com.sarahhand.fractals.model.colorscheme.mandelbrotset;

import java.awt.Color;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sarahhand.fractals.model.ColorPalette;
import com.sarahhand.fractals.model.ComplexNumber;
import com.sarahhand.fractals.model.FractalConfig;
import com.sarahhand.fractals.model.MandelbrotConfig;
import com.sarahhand.fractals.model.MandelbrotPointData;
import com.sarahhand.fractals.model.PointData;
import com.sarahhand.fractals.model.colorscheme.ColorScheme;

@JsonTypeName("ExternalDistance")
public class ExternalDistanceEstimateColorScheme implements ColorScheme{

	@Override
	public String getName(){
		return "Exterior Distance";
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
		
		ComplexNumber pnc = castData.getAllZValues().get(castData.getEscapeTime());
		double absPnc = Math.sqrt(pnc.x*pnc.x+pnc.y*pnc.y);
		
		ComplexNumber partialC = ComplexNumber.ONE;
		
		for(int n = 0; n < castData.getEscapeTime(); n++){
			
			partialC = ComplexNumber.TWO.multiply(castData.getAllZValues().get(n).multiply(partialC)).add(ComplexNumber.ONE);
		}
		
		double absPartialC = Math.sqrt(partialC.x*partialC.x+partialC.y*partialC.y);
		
		double b = 2 * absPnc*Math.log(absPnc)/absPartialC;
		
		double interpolateValue = Math.log(b)/Math.log(1.05);
		
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
