package com.sarahhand.fractals.model;

import java.awt.Color;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class that represents/defines a color palette.
 * 
 * @author J9465812
 */

public class ColorPalette{
	
	/**
	 * the default number of colors contained in a color palette.
	 */
	public static final int COLOR_PALETTE_LENGTH = 200;

	/**
	 * the default rainbow color palette
	 */
	public static final ColorPalette DEFAULT_PALETTE;
	static{

		Color[] palette = new Color[COLOR_PALETTE_LENGTH];

		for (int n = 0; n < COLOR_PALETTE_LENGTH; n++) {
			Color color = Color.getHSBColor((float) n / COLOR_PALETTE_LENGTH, 1f, 1f);
			palette[n] = color;
		}

		DEFAULT_PALETTE = new ColorPalette(palette);
	}

	@JsonIgnore
	private Color[] palette;
	
	/**
	 * Creates a color palette from the specified array of colors.
	 * @param palette
	 */
	public ColorPalette(Color[] palette){
		this.palette = palette;
	}
	
	/**
	 * Creates a color palette from the specified list of RGB color values.
	 * Primarily used by JSON deserialization.
	 * @param colorValues
	 */
	@JsonCreator
	public ColorPalette(@JsonProperty("colorValues") int[] colorValues){
		this.palette = new Color[colorValues.length];
		for (int idx = 0; idx < colorValues.length; idx++) {
			this.palette[idx] = new Color(colorValues[idx]);
		}
	}

	/**
	 * Returns the specified color from the palette based on the specified index.
	 * If the requested index is beyond the limits of the color palette, then the
	 * last color in the palette is returned.
	 * @param idx
	 * @return
	 */
	public Color getColor(int idx){
		if (idx < palette.length) {
			return palette[idx];
		}
		return palette[palette.length-1];
	}
	
	/**
	 * Converts the color palette's color array into an array
	 * of RGB (int) values. Primarily used for JSON serialization.
	 * @return
	 */
	public int[] getColorValues() {
		int[] rgbValues = new int[palette.length];
		for (int idx = 0; idx < palette.length; idx++) {
			if (palette[idx] != null) {
				rgbValues[idx] = palette[idx].getRGB();
			}
		}
		
		return rgbValues;
	}
}
