package com.sarahhand.fractals.viewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sarahhand.fractals.model.FractalConfig;
import com.sarahhand.fractals.model.MandelbrotConfig;
import com.sarahhand.fractals.model.MandelbrotPointData;
import com.sarahhand.fractals.model.colorscheme.ColorScheme;
import com.sarahhand.fractals.model.colorscheme.mandelbrotset.BlackColorScheme;
import com.sarahhand.fractals.model.colorscheme.mandelbrotset.EscapeTimeColorScheme;

/**
 * Implements <code>FractalViewer</code> and draws the Mandelbrot Set.<p>
 * 
 * For more information about the mathematics of the<br>
 * Mandelbrot Set, see the
 * <a href="https://en.wikipedia.org/wiki/Mandelbrot_set">Wikipedia page</a>.
 * 
 * @author J9465812
 * @see FractalViewer
 */

class MandelbrotViewerImpl implements FractalViewer{
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private static final double LOG2 = Math.log(2);
	private static final int MIN_SIZE = 10;
	public static final int MAX_Z = 1000;
	
//	public static final int RECT_BUFFER = 2000;
	
	private MandelbrotConfig config;
	
	@Override
	public Image getView(Dimension dimensions){
		
		long time = System.currentTimeMillis();
		
		BufferedImage image = new BufferedImage(dimensions.width, dimensions.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		
		renderRect(g, new Rectangle(dimensions), dimensions, MIN_SIZE);
		
		log.debug("Time to generate Mandelbrot image: {}ms.", System.currentTimeMillis()-time);
		
		return image;
	}
	
	@Override
	public Image getViewPanning(Dimension dimensions, FractalConfig oldConfig, Image oldImage){
		
		if (!(oldConfig instanceof MandelbrotConfig)){
			throw new IllegalArgumentException("oldConfig must be instance of MandelbrotConfig.");
		}
		
		long time = System.currentTimeMillis();
		
		BufferedImage image = new BufferedImage(dimensions.width, dimensions.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		
		int x = (int)((oldConfig.getCenter().x-config.getCenter().x)*config.getZoom());
		int y = (int)((config.getCenter().y-oldConfig.getCenter().y)*config.getZoom());
		
		g.drawImage(oldImage, x, y, null);
		
		Rectangle predrawnPoints = new Rectangle(x, y, oldImage.getWidth(null), oldImage.getHeight(null));
		
		Rectangle frame = new Rectangle(dimensions);
		
		for(int m = 0; m < dimensions.width; m++){
			for(int n = 0; n < dimensions.width; n++){
				if(!predrawnPoints.contains(m, n)){
					renderPoint(new Point(m, n), g, dimensions);
				}
			}
		}
		
//		Another method I tried to optimize panning,
//		kept in case I find a way to make it better.
		
//		Rectangle top = new Rectangle(predrawnPoints.x, predrawnPoints.y - RECT_BUFFER, predrawnPoints.width, RECT_BUFFER);
//		Rectangle bottom = new Rectangle(predrawnPoints.x, predrawnPoints.y + predrawnPoints.height, predrawnPoints.width, RECT_BUFFER);//works
//		Rectangle left = new Rectangle(predrawnPoints.x - RECT_BUFFER, predrawnPoints.y - RECT_BUFFER, RECT_BUFFER, RECT_BUFFER*3);
//		Rectangle right = new Rectangle(predrawnPoints.x + predrawnPoints.width, predrawnPoints.y + RECT_BUFFER, RECT_BUFFER, RECT_BUFFER*3);
//		
//		Rectangle topIntersect = frame.intersection(top);
//		Rectangle bottomIntersect = frame.intersection(bottom);
//		Rectangle leftIntersect = frame.intersection(left);
//		Rectangle rightIntersect = frame.intersection(right);
//		
//		if(!topIntersect.isEmpty()){
//			renderRect(g, topIntersect, dimensions, 10);
//		}
//		
//		if(!bottomIntersect.isEmpty()){
//			renderRect(g, bottomIntersect, dimensions, 10);
//		}
//		
//		if(!leftIntersect.isEmpty()){
//			renderRect(g, leftIntersect, dimensions, 10);
//		}
//		
//		if(!rightIntersect.isEmpty()){
//			renderRect(g, rightIntersect, dimensions, 10);
//		}
		
		log.debug("Time to generate Mandelbrot image: {}ms.", System.currentTimeMillis()-time);
		
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
	public void setConfig(FractalConfig config){
		if (config instanceof MandelbrotConfig) {
			this.config = (MandelbrotConfig)config;
		} else {
			throw new IllegalArgumentException("MandlebrotViewer can only be updated with MandelbrotConfig.");
		}
	}

	@Override
	public FractalConfig getConfig(){
		return config;
	}

	MandelbrotViewerImpl(FractalConfig config){
		if (config instanceof MandelbrotConfig) {
			this.config = (MandelbrotConfig)config;
		} else {
			throw new IllegalArgumentException("MandlebrotViewer can only be created with MandelbrotConfig.");
		}
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
				
				return config.getColorScheme().getColor(new MandelbrotPointData(p, n, new Double(z.x, z.y)), config);
			}
		}
		
		return config.getColorScheme().getColor(new MandelbrotPointData(p, -1, new Double(z.x, z.y)), config);
	}
	
	private static class ComplexNumber{
		
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

	@Override
	public List<ColorScheme> getSupportedColorSchemes(){
		return Arrays.asList(new BlackColorScheme(), new EscapeTimeColorScheme());
	}
}
