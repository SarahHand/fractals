package com.sarahhand.fractals.mapper;

import static org.junit.Assert.assertThat;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.sarahhand.fractals.model.ColorPalette;

import static org.hamcrest.Matchers.*;

public class ColorPaletteMapperTest{
	
	@Test
	public void mapTest(){
		
		//Arrange
		List<Color> colors = Arrays.asList(Color.WHITE, Color.BLUE, Color.RED, Color.WHITE);
		List<Integer> positions = Arrays.asList(0, 50, 150, 200);
		
		//Act
		ColorPalette palette = ColorPaletteMapper.getDefaultMapper().map(colors, positions);
		
		//Assert
		assertThat(palette, notNullValue());
		assertThat(palette.getColor(0), equalTo(Color.WHITE));
		assertThat(palette.getColor(50), equalTo(Color.BLUE));
		assertThat(palette.getColor(150), equalTo(Color.RED));
	}
}
