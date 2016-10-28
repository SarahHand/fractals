package com.sarahhand.fractals.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;

class MandelbrotViewerImpl implements MandelbrotViewer{
	
	private static final double log2 = Math.log(2);
	
	private MandelbrotConfig config;

	@Override
	public void setConfig(MandelbrotConfig config){
		this.config = config;
	}

	@Override
	public MandelbrotConfig getConfig(){
		return config;
	}
	
	@Override
	public Image getView(Dimension dimensions){
		
		BufferedImage image = new BufferedImage(dimensions.width, dimensions.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		
		for(int x = 0; x < dimensions.width; x++){
			for(int y = 0; y < dimensions.height; y++){
				
				Double point = transform(dimensions.width/2 - x, dimensions.height/2 - y, config);
				Color pointCol = getPointCol(point);
				
				g.setColor(pointCol);
				g.fillRect(x, y, 1, 1);
			}
		}
		
		return image;
	}
	
	private Double transform(int x, int y, MandelbrotConfig config){
		
		double transformedX = config.getCenter().x - (double)x/config.getZoom();
		double transformedY = config.getCenter().y + (double)y/config.getZoom();
		
		return new Double(transformedX, transformedY);
	}

	MandelbrotViewerImpl(MandelbrotConfig config){
		this.config = config;
	}
	
	private Color getPointCol(Double p){
		
		ComplexNumber c = new ComplexNumber(p.x, p.y);
		ComplexNumber z = new ComplexNumber(0, 0);
		
		for(int n = 0; n < config.getMaxDwell(); n++){
			
			z = z.multiply(z).add(c);
			
			if(z.x*z.x+z.y*z.y > 1000){//Reminder: test with 2
				
				double log_zn = Math.log(z.x*z.x+z.y*z.y)/2;
				double nu = Math.log(log_zn/log2)/log2;
				
				double interpolateValue = (double)n + 1.0 - nu;
				
				interpolateValue *= 10;
				
				Color col1 = config.getPalette().getColor((int)(((Math.floor(interpolateValue))%200) + 200) % 200);
				Color col2 = config.getPalette().getColor((int)(((Math.floor(interpolateValue + 1))%200) + 200) % 200);
				
				return interpolate(col1, col2, interpolateValue - Math.floor(interpolateValue));
			}
		}
		
		return Color.BLACK;
	}
	
	private Color interpolate(Color c1, Color c2, double amount){
		
		int r1, r2, g1, g2, b1, b2;
		
		r1 = c1.getRed();
		r2 = c2.getRed();
		g1 = c1.getGreen();
		g2 = c2.getGreen();
		b1 = c1.getBlue();
		b2 = c2.getBlue();
		
		double r = r1*(1 - amount) + r2*amount;
		double g = g1*(1 - amount) + g2*amount;
		double b = b1*(1 - amount) + b2*amount;
		
		return new Color((int)r, (int)g, (int)b);
	}
	
	private class ComplexNumber{
		
		double x;
		double y;
		
		public ComplexNumber(double x, double y){
			this.x = x;
			this.y = y;
		}
		
		public ComplexNumber add(ComplexNumber c){
			
			ComplexNumber addition = new ComplexNumber(this.x + c.x, this.y + c.y);
			return addition;
		}
		
		public ComplexNumber multiply(ComplexNumber c){
			
			ComplexNumber multiplication = new ComplexNumber(this.x*c.x - this.y*c.y, this.x*c.y + this.y*c.x);
			return multiplication;
		}
	}
}
