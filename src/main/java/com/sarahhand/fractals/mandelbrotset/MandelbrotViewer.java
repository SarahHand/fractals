package com.sarahhand.fractals.mandelbrotset;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sarahhand.fractals.mandelbrotset.colorscheme.EscapeTimeColorScheme;
import com.sarahhand.fractals.mandelbrotset.colorscheme.ExternalDistanceEstimateColorScheme;
import com.sarahhand.fractals.model.ColorScheme;
import com.sarahhand.fractals.model.ComplexNumber;
import com.sarahhand.fractals.model.FractalConfig;
import com.sarahhand.fractals.viewer.FractalViewer;

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

public class MandelbrotViewer implements FractalViewer{
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private static final int MIN_SIZE = 10;
	
	/**
	 * The square of the maximum value z can reach before it is assumed to diverge.
	 */
	public static final int MAX_Z = 1000000;
	
	private MandelbrotConfig config;
	
	@Override
	public Image getView(Dimension dimensions){
		
		long time = System.currentTimeMillis();
		
		BufferedImage image = new BufferedImage(dimensions.width, dimensions.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		
		renderRect(g, new Rectangle(dimensions), dimensions, MIN_SIZE);
		
		log.info("Time to generate Mandelbrot image: {}ms.", System.currentTimeMillis()-time);
		
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
		
		for(int m = 0; m < dimensions.width; m++){
			for(int n = 0; n < dimensions.width; n++){
				if(!predrawnPoints.contains(m, n)){
					renderPoint(new Point(m, n), g, dimensions);
				}
			}
		}
		
		log.info("Time to generate Mandelbrot image: {}ms.", System.currentTimeMillis()-time);
		
		return image;
	}
	
	private void renderRect(Graphics2D g2d, Rectangle rect, Dimension dimensions, int minSize){
		
		//if rect is small...
		if(rect.width < minSize || rect.height < minSize){
			
			//fill it in
			for(int x = rect.x; x < rect.x + rect.width; x++){
				for(int y = rect.y; y < rect.y + rect.width; y++){
					renderPoint(new Point(x, y), g2d, dimensions);
				}
			}
			
			return;
		}
		
		//check boundary points
		boolean filledRect = true;
		
		for(int x = rect.x; x < rect.x+rect.width; x++){
			
			filledRect = filledRect && renderPoint(new Point(x, rect.y), g2d, dimensions);
			filledRect = filledRect && renderPoint(new Point(x, rect.y + rect.height), g2d, dimensions);
		}
		
		for(int y = rect.y; y < rect.y+rect.width; y++){
			
			filledRect = filledRect && renderPoint(new Point(rect.x, y), g2d, dimensions);
			filledRect = filledRect && renderPoint(new Point(rect.x + rect.width, y), g2d, dimensions);
		}
		
		//if they are all black ...
		if(filledRect == true){
			
			//fill it in with black
			g2d.setColor(Color.BLACK);
			
			g2d.fill(rect);
		}else{
			
			//otherwise, divide into four rectangles and render them
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

	/**
	 * The constructor.
	 * @param config
	 */
	public MandelbrotViewer(FractalConfig config){
		if (config instanceof MandelbrotConfig) {
			this.config = (MandelbrotConfig)config;
		} else {
			throw new IllegalArgumentException("MandlebrotViewer can only be created with MandelbrotConfig.");
		}
	}
	
	private Color getPointCol(Double p){
		
		double q = (p.x - 0.25)*(p.x - 0.25) + p.y*p.y;
		
		//point is black if it is in cardioid...
		if(q*(q+(p.x - 0.25)) < 0.25*p.y*p.y){
			return Color.black;
		}
		
		//or the main bulb
		if((p.x+1)*(p.x+1)+p.y*p.y < 0.0625){
			return Color.black;
		}
		
		//iterate equations...
		ComplexNumber c = new ComplexNumber(p.x, p.y);
		ComplexNumber z = new ComplexNumber(0, 0);
		
		List<ComplexNumber> zValues = new ArrayList<>((int)(config.getMaxDwell()));
		
		for(int n = 0; n < config.getMaxDwell(); n++){
			
			zValues.add(z);
			
			z = z.multiply(z).add(c);
			
			//until it diverges...
			if(z.x*z.x+z.y*z.y > MAX_Z){
				
				return config.getColorScheme().getColor(new MandelbrotPointData(p, n, zValues), config);
			}
		}
		
		//or it doesn't
		return Color.black;
	}
	
	@Override
	public List<ColorScheme> getSupportedColorSchemes(){
		return Arrays.asList(new EscapeTimeColorScheme(), new ExternalDistanceEstimateColorScheme());
	}
}
