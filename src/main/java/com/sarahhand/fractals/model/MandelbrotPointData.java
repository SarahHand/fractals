package com.sarahhand.fractals.model;

import java.awt.geom.Point2D.Double;

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
	private Double endZValue;
	
	public MandelbrotPointData(Double location, int escapeTime, Double z){
		this.location = location;
		this.escapeTime = escapeTime;
		this.endZValue = z;
	}
	
	public Double getLocation(){
		return location;
	}
	public int getEscapeTime(){
		return escapeTime;
	}
	public Double getEndZValue(){
		return endZValue;
	}
}
