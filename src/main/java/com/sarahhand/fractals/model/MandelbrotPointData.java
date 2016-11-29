package com.sarahhand.fractals.model;

import java.awt.geom.Point2D.Double;
import java.util.List;

/**
 * Represents the data needed for a color scheme to color a point in the Mandelbrot set
 * @author J9465812
 */
public class MandelbrotPointData implements PointData{
	
	private Double location;
	private int escapeTime;
	
	/**
	 * @see MandelbrotViewerImpl
	 */
	private List<ComplexNumber> allZValues;
	
	public MandelbrotPointData(Double location, int escapeTime, List<ComplexNumber> allZValues){
		this.location = location;
		this.escapeTime = escapeTime;
		this.allZValues = allZValues;
	}
	
	public Double getLocation(){
		return location;
	}
	public int getEscapeTime(){
		return escapeTime;
	}
	public List<ComplexNumber> getAllZValues(){
		return allZValues;
	}
}
