package com.sarahhand.fractals.juliaset;

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

import com.sarahhand.fractals.juliaset.colorscheme.DefaultJuliaColorScheme;
import com.sarahhand.fractals.model.ColorScheme;
import com.sarahhand.fractals.model.ComplexNumber;
import com.sarahhand.fractals.model.FractalConfig;
import com.sarahhand.fractals.viewer.FractalViewer;

/**
 * Implements <code>FractalViewer</code> and draws the Julia Set.<p>
 * 
 * For more information about the mathematics of the<br>
 * Julia Set, see the
 * <a href="https://en.wikipedia.org/wiki/Julia_set">Wikipedia page</a>.
 * 
 * @author J9465812
 * @see FractalViewer
 */

public class JuliaViewer implements FractalViewer{
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private static final int MIN_SIZE = 10;
	
	public static final String C_REAL_NAME = "c real";
	public static final String C_IMAGINARY_NAME = "c imaginary";
	
	/**
	 * The square of the maximum value z can reach before it is assumed to diverge.
	 */
	public static final int MAX_Z = 1000000;
	
//	public static final int RECT_BUFFER = 2000;
	
	private JuliaConfig config;
	
	@Override
	public Image getView(Dimension dimensions){
		
		long time = System.currentTimeMillis();
		
		BufferedImage image = new BufferedImage(dimensions.width, dimensions.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		
		renderRect(g, new Rectangle(dimensions), dimensions, MIN_SIZE);
		
		log.info("Time to generate Julia image: {}ms.", System.currentTimeMillis()-time);
		
		return image;
	}
	
	@Override
	public Image getViewPanning(Dimension dimensions, FractalConfig oldConfig, Image oldImage){
		
		if (!(oldConfig instanceof JuliaConfig)){
			throw new IllegalArgumentException("oldConfig must be instance of JuliaConfig.");
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
		
		log.info("Time to generate Julia image: {}ms.", System.currentTimeMillis()-time);
		
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
	
	private Double transform(int x, int y, JuliaConfig config){
		
		double transformedX = config.getCenter().x - (double)x/config.getZoom();
		double transformedY = config.getCenter().y + (double)y/config.getZoom();
		
		return new Double(transformedX, transformedY);
	}

	@Override
	public void setConfig(FractalConfig config){
		if (config instanceof JuliaConfig) {
			this.config = (JuliaConfig)config;
		} else {
			throw new IllegalArgumentException("MandlebrotViewer can only be updated with JuliaConfig.");
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
	public JuliaViewer(FractalConfig config){
		if (config instanceof JuliaConfig) {
			this.config = (JuliaConfig)config;
		} else {
			throw new IllegalArgumentException("MandlebrotViewer can only be created with JuliaConfig.");
		}
	}
	
	private Color getPointCol(Double p){
		
		ComplexNumber c = new ComplexNumber(config.getConstants().get(C_REAL_NAME), config.getConstants().get(C_IMAGINARY_NAME));
		
		//ComplexNumber c = new ComplexNumber(0.37259789000000026, 0.0909054899999998);
		ComplexNumber z = new ComplexNumber(p.x, p.y);
		
		List<ComplexNumber> zValues = new ArrayList<>((int)(config.getMaxDwell()));
		
		for(int n = 0; n < config.getMaxDwell(); n++){
			
			zValues.add(z);
			
			z = z.multiply(z).add(c);
			
			if(z.x*z.x+z.y*z.y > MAX_Z){
				
				return config.getColorScheme().getColor(new JuliaPointData(p, n, zValues), config);
			}
		}
		
		//System.out.println(zValues.size());
		
		return Color.black;
	}
	
	@Override
	public List<ColorScheme> getSupportedColorSchemes(){
		return Arrays.asList(new DefaultJuliaColorScheme());
	}
}
