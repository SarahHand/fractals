package com.sarahhand.fractals.model;

import java.awt.Color;

public class ColorPalette{

	static final ColorPalette DEFAULT_PALETTE;
	public static final int COLOR_PALETTE_LENGTH = 200;

	static{

		Color[] palette = new Color[COLOR_PALETTE_LENGTH];

		for (int n = 0; n < COLOR_PALETTE_LENGTH; n++) {
			Color color = Color.getHSBColor((float) n / COLOR_PALETTE_LENGTH, 1f, 1f);
			palette[n] = color;
		}

		DEFAULT_PALETTE = new ColorPalette(palette);
	}

	private Color[] palette;

	public ColorPalette(Color[] palette){
		this.palette = palette;
	}

	public Color getColor(int n){
		return palette[n];
	}
}
