package com.sarahhand.fractals.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;

class MandelbrotViewerImpl implements MandelbrotViewer{
	
	private static final double LOG2 = Math.log(2);
	private static final int MIN_SIZE = 10;
	public static final int MAX_Z = 1000;
	
	private MandelbrotConfig config;
	
	@Override
	public Image getView(Dimension dimensions){
		
		long time = System.currentTimeMillis();
		
		BufferedImage image = new BufferedImage(dimensions.width, dimensions.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		
		renderRect(g, new Rectangle(dimensions), dimensions, MIN_SIZE);
		
		System.out.println("Time: " + ((System.currentTimeMillis()-time)/1000) + "(secs)");
		return image;
	}
	
	private void renderRect(Graphics2D g2d, Rectangle rect, Dimension dimensions, int minSize){
		
		if(rect.width < minSize || rect.height < minSize){
			
			for(int x = rect.x; x < rect.x + rect.width; x++){
				for(int y = rect.y; y < rect.y + rect.width; y++){
					renderPoint(new Point(x, y), g2d, dimensions);
				}
			}
			
			return;
		}
		
		boolean filledRect = true;
		
		for(int x = rect.x; x < rect.x+rect.width; x++){
			
			filledRect = filledRect && renderPoint(new Point(x, rect.y), g2d, dimensions);
			filledRect = filledRect && renderPoint(new Point(x, rect.y + rect.height), g2d, dimensions);
		}
		
		for(int y = rect.y; y < rect.y+rect.width; y++){
			
			filledRect = filledRect && renderPoint(new Point(rect.x, y), g2d, dimensions);
			filledRect = filledRect && renderPoint(new Point(rect.x + rect.width, y), g2d, dimensions);
		}
		
		if(filledRect == true){
			
			g2d.setColor(Color.BLACK);
			
			g2d.fill(rect);
		}else{
			
			int centerX = rect.x + rect.width/2;
			int centerY = rect.y + rect.height/2;
			
			renderRect(g2d, newRect(rect.x, rect.y, centerX, centerY), dimensions, minSize);
			renderRect(g2d, newRect(centerX, rect.y, rect.x+rect.width, centerY), dimensions, minSize);
			renderRect(g2d, newRect(rect.x, centerY, centerX, rect.y+rect.height), dimensions, minSize);
			renderRect(g2d, newRect(centerX, centerY, rect.x+rect.width, rect.y+rect.height), dimensions, minSize);
		}
	}
	
	private Rectangle newRect(int x1, int y1, int x2, int y2){
		return new Rectangle(x1, y1, x2-x1, y2-y1);
	}
	
	private boolean renderPoint(Point screenCoords, Graphics2D g2d, Dimension dimensions){
		
		Double point = transform(dimensions.width/2 - screenCoords.x, dimensions.height/2 - screenCoords.y, config);
		Color pointCol = getPointCol(point);
		
		g2d.setColor(pointCol);
		g2d.fillRect(screenCoords.x, screenCoords.y, 1, 1);
		
		return pointCol.getRGB() == Color.black.getRGB();
	}
	
	private Double transform(int x, int y, MandelbrotConfig config){
		
		double transformedX = config.getCenter().x - (double)x/config.getZoom();
		double transformedY = config.getCenter().y + (double)y/config.getZoom();
		
		return new Double(transformedX, transformedY);
	}

	@Override
	public void setConfig(MandelbrotConfig config){
		this.config = config;
	}

	@Override
	public MandelbrotConfig getConfig(){
		return config;
	}

	MandelbrotViewerImpl(MandelbrotConfig config){
		this.config = config;
	}
	
	private Color getPointCol(Double p){
		
		double q = (p.x - 0.25)*(p.x - 0.25) + p.y*p.y;
		
		if(q*(q+(p.x - 0.25)) < 0.25*p.y*p.y){
			return Color.black;
		}
		
		if((p.x+1)*(p.x+1)+p.y*p.y < 0.0625){
			return Color.black;
		}
		
		ComplexNumber c = new ComplexNumber(p.x, p.y);
		ComplexNumber z = new ComplexNumber(0, 0);
		
		for(int n = 0; n < config.getMaxDwell(); n++){
			
			z = z.multiply(z).add(c);
			
			if(z.x*z.x+z.y*z.y > MAX_Z){
				
				double log_zn = Math.log(z.x*z.x+z.y*z.y)/2;
				double nu = Math.log(log_zn/LOG2)/LOG2;
				
				double interpolateValue = (double)n + 1.0 - nu;
				
				interpolateValue *= 10;
				
				Color col1 = config.getPalette().getColor(((int)(Math.floor(interpolateValue)) %
						ColorPalette.COLOR_PALETTE_LENGTH + ColorPalette.COLOR_PALETTE_LENGTH) %
						ColorPalette.COLOR_PALETTE_LENGTH);
				
				Color col2 = config.getPalette().getColor(((int)(Math.floor(interpolateValue+1)) %
						ColorPalette.COLOR_PALETTE_LENGTH + ColorPalette.COLOR_PALETTE_LENGTH) %
						ColorPalette.COLOR_PALETTE_LENGTH);
				
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
