package com.sarahhand.fractals.mapper;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sarahhand.fractals.model.ColorPalette;

/**
 * Converts several colors into a ColorPalette
 * 
 * @see {@link #map(List, List) ColorpaletteMapper.map}
 * @see ColorPalette
 * @see Color
 * 
 * @author J9465812
 */

public class ColorPaletteMapper{
	
	private static final ColorPaletteMapper defaultMapper = new ColorPaletteMapper();
	
	public static ColorPaletteMapper getDefaultMapper(){
		return defaultMapper;
	}
	
	private ColorPaletteMapper(){}
	
	/**
	 * Converts several colors into a complete color palette.
	 * 
	 * This method takes a list of colors along with a list of integers 
	 * that specify where in the palette the colors should occur. It
	 * then fills in the gaps by blending adjacent colors to create a
	 * smooth gradient.
	 * <p>
	 * This method will throw an <code>IllegalArgumentException</code> if
	 * these requirements are not met:<ul>
	 * <li>neither list can be null</li>
	 * <li>the lists must have equal size</li>
	 * <li>the positions list must contain both 0 and <code>ColorPalette.COLOR_PALETTE_LENGTH</code></li>
	 * <li>all items in the positions list must lie between 0 and <code>ColorPalette.COLOR_PALETTE_LENGTH</code> inclusive</li>
	 * </ul>
	 * 
	 * @param colors - the list of colors
	 * @param positions - the list of positions
	 * @return the <code>ColorPalette</code> created
	 * 
	 * @throws IllegalArgumentException if any of the requirements above are not met
	 */
	
	public ColorPalette map(List<Color> colors, List<Integer> positions){
		
		errorCases(colors, positions);
		
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
	
	private void errorCases(List<Color> colors, List<Integer> positions){
		
		if(colors == null){
			throw new IllegalArgumentException("Colors null.");
		}
		
		if(positions == null){
			throw new IllegalArgumentException("Positions null.");
		}
		
		if(colors.size() != positions.size()){
			throw new IllegalArgumentException("Lists of unequal sizes.");
		}
		
		if(!positions.contains(0) || !positions.contains(ColorPalette.COLOR_PALETTE_LENGTH)){
			throw new IllegalArgumentException("Required positions not found: 0 and " + ColorPalette.COLOR_PALETTE_LENGTH);
		}
		
		for(int n = 0; n < colors.size(); n++){
			
			int pos = positions.get(n);
			
			if(pos < 0 || pos > ColorPalette.COLOR_PALETTE_LENGTH){
				throw new IllegalArgumentException("Positions contains an integer outside of [0, " + ColorPalette.COLOR_PALETTE_LENGTH + "].");
			}
		}
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
