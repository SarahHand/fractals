package com.sarahhand.fractals.event;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

import com.sarahhand.fractals.model.ColorPalette;
import com.sarahhand.fractals.model.ColorScheme;
import com.sarahhand.fractals.viewer.FractalViewer;

/**
 * An interface that serves as a middleman between
 * the FractalViewer/FractalConfig and the FractalsUI.
 * @author J9465812
 */
public interface FractalEventHandler extends MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
	
	/**
	 * Returns the color palette.
	 * @return
	 */
	public default ColorPalette getColorPalette(){
		return null;
	}
	
	/**
	 * Sets the color palette.
	 * @param colorPalette 
	 * @return
	 */
	public default void setColorPalette(ColorPalette colorPalette){}
	
	/**
	 * Returns whether the fractal this FractalEventHandler represents supports color palettes.
	 * @return
	 */
	public default boolean supportsColorPalette(){
		return false;
	}
	
	/**
	 * Returns the fractal viewer.
	 * @return
	 */
	public FractalViewer getFractalViewer(); 
	
	/**
	 * Sets the color scheme.
	 * @param scheme 
	 * @return
	 */
	public void setColorScheme(ColorScheme scheme);
	
	/**
	 * Returns the colorScheme.
	 * @return
	 */
	public ColorScheme getColorScheme();
}
