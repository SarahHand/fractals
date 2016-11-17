package com.sarahhand.fractals.model;

import java.awt.geom.Point2D.Double;

/**
 * Represents the data needed for a color scheme to color a point in the Mandelbrot set
 * @author J9465812
 */
public class MandelbrotPointData implements PointData{
	
	private Double location;
	private int escapeTime;
	private Double z;
	
	public MandelbrotPointData(Double location, int escapeTime, Double z){
		this.location = location;
		this.escapeTime = escapeTime;
		this.z = z;
	}
	
	public Double getLocation(){
		return location;
	}
	public void setLocation(Double location){
		this.location = location;
	}
	public int getEscapeTime(){
		return escapeTime;
	}
	public void setEscapeTime(int escapeTime){
		this.escapeTime = escapeTime;
	}
	public Double getZ(){
		return z;
	}
	public void setZ(Double z){
		this.z = z;
	}
}
