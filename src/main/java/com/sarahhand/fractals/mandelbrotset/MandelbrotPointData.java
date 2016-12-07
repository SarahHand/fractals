package com.sarahhand.fractals.mandelbrotset;

import java.awt.geom.Point2D.Double;
import java.util.List;

import com.sarahhand.fractals.model.ComplexNumber;
import com.sarahhand.fractals.model.PointData;

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
	
	/**
	 * The constructor.
	 * @param location
	 * @param escapeTime
	 * @param allZValues
	 */
	public MandelbrotPointData(Double location, int escapeTime, List<ComplexNumber> allZValues){
		this.location = location;
		this.escapeTime = escapeTime;
		this.allZValues = allZValues;
	}
	
	/**
	 * The location of the point.
	 * @return
	 */
	public Double getLocation(){
		return location;
	}
	
	/**
	 * The number of iterations through z -> z^2 + c needed for this point to exceed MAX_Z.
	 * @return
	 */
	public int getEscapeTime(){
		return escapeTime;
	}
	
	/**
	 * The list of values z iterated through before escaping.
	 * @return
	 */
	public List<ComplexNumber> getAllZValues(){
		return allZValues;
	}
}
