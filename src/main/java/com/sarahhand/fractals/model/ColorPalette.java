package com.sarahhand.fractals.model;

import java.awt.Color;

public class ColorPalette{

	static final ColorPalette DEFAULT_PALETTE;

	static{

		Color[] palette = new Color[200];

		for (int n = 0; n < 200; n++) {
			Color color = Color.getHSBColor((float) n / 200, 1f, 1f);
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
