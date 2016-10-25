package com.sarahhand.fractals.model;

import java.awt.geom.Point2D.Double;

public class MandelbrotConfig{

	public static final MandelbrotConfig DEAFAULT_CONFIG = new MandelbrotConfig(new Double(-0.5, 0), 200, ColorPalette.DEFAULT_PALETTE, 500);

	private Double center;
	private int zoom;
	private ColorPalette palette;
	private int maxDwell;

	public Double getCenter(){
		return center;
	}

	public void setCenter(Double center){
		this.center = center;
	}

	public int getZoom(){
		return zoom;
	}

	public void setZoom(int zoom){
		this.zoom = zoom;
	}

	public ColorPalette getPalette(){
		return palette;
	}

	public void setPalette(ColorPalette palette){
		this.palette = palette;
	}

	public int getMaxDwell(){
		return maxDwell;
	}

	public void setMaxDwell(int maxDwell){
		this.maxDwell = maxDwell;
	}

	public MandelbrotConfig(Double center, int zoom, ColorPalette palette, int maxDwell){
		super();
		this.center = center;
		this.zoom = zoom;
		this.palette = palette;
		this.maxDwell = maxDwell;
	}
}
