package com.sarahhand.fractals.mapper;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.sarahhand.fractals.model.ColorPalette;

public class ColorPaletteMapperTest{
	
	@Test
	public void normalTest(){
		
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
	
	@Test(expected=IllegalArgumentException.class)
	public void nullPositionsTest(){
		
		//Arrange
		List<Color> colors = Arrays.asList(Color.WHITE, Color.BLUE, Color.RED, Color.WHITE);
		List<Integer> positions = null;
		
		//Act
		ColorPalette palette = ColorPaletteMapper.getDefaultMapper().map(colors, positions);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void nullColorsTest(){
		
		//Arrange
		List<Color> colors = null;
		List<Integer> positions = Arrays.asList(0, 50, 200);
		
		//Act
		ColorPalette palette = ColorPaletteMapper.getDefaultMapper().map(colors, positions);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void sizeTest(){
		
		//Arrange
		List<Color> colors = Arrays.asList(Color.WHITE, Color.BLUE, Color.RED, Color.WHITE);
		List<Integer> positions = Arrays.asList(0, 50, 200);
		
		//Act
		ColorPalette palette = ColorPaletteMapper.getDefaultMapper().map(colors, positions);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void endpointTest(){
		
		//Arrange
		List<Color> colors = Arrays.asList(Color.WHITE, Color.BLUE, Color.RED, Color.WHITE);
		List<Integer> positions = Arrays.asList(20, 150, 50, 200);
		
		//Act
		ColorPalette palette = ColorPaletteMapper.getDefaultMapper().map(colors, positions);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void rangeTest(){
		
		//Arrange
		List<Color> colors = Arrays.asList(Color.WHITE, Color.BLUE, Color.RED, Color.WHITE);
		List<Integer> positions = Arrays.asList(0, 150, 250, 200);
		
		//Act
		ColorPalette palette = ColorPaletteMapper.getDefaultMapper().map(colors, positions);
	}
}
