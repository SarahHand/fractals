package com.sarahhand.fractals.model;

import java.awt.Color;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class that stores a color palette.
 * 
 * @author J9465812
 *
 */

public class ColorPalette{
	
	/**
	 * the default rainbow color palette
	 */
	public static final ColorPalette DEFAULT_PALETTE;
	
	/**
	 * 
	 */
	public static final int COLOR_PALETTE_LENGTH = 200;

	static{

		Color[] palette = new Color[COLOR_PALETTE_LENGTH];

		for (int n = 0; n < COLOR_PALETTE_LENGTH; n++) {
			Color color = Color.getHSBColor((float) n / COLOR_PALETTE_LENGTH, 1f, 1f);
			palette[n] = color;
		}

		DEFAULT_PALETTE = new ColorPalette(palette);
	}

	public Color[] getPalette(){
		return palette;
	}

	private Color[] palette;
	
	@JsonCreator
	public ColorPalette(@JsonProperty("palette") Color[] palette){
		this.palette = palette;
	}

	public Color getColor(int n){
		return palette[n];
	}
}
