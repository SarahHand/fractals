package com.sarahhand.fractals.juliaset;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D.Double;

import com.sarahhand.fractals.event.FractalEventHandler;
import com.sarahhand.fractals.model.ColorPalette;
import com.sarahhand.fractals.model.ColorScheme;
import com.sarahhand.fractals.model.FractalConfig;
import com.sarahhand.fractals.model.FractalType;
import com.sarahhand.fractals.view.FractalsUI;
import com.sarahhand.fractals.viewer.FractalViewer;
import com.sarahhand.fractals.viewer.FractalViewerFactory;

/**
 * The FractalEventHandler that represents the Julia Set.
 * @author J9465812
 *
 */
public class JuliaEventHandler implements FractalEventHandler{
	
	private final double ZOOM_IN_FACTOR = 5;
	private final double ZOOM_OUT_FACTOR = 1 / ZOOM_IN_FACTOR;
	private final int MAX_DWELL_INCREASE = 200;
	private final int MAX_DWELL_DECREASE = -MAX_DWELL_INCREASE;
	private final int LEFT_CLICK = 1;
	private final int RIGHT_CLICK = 3;
	
	private FractalsUI ui;
	private JuliaViewer viewer;
	
	/**
	 * The constructor.
	 * @param ui
	 */
	public JuliaEventHandler(FractalsUI ui){
		this.viewer = (JuliaViewer) new FractalViewerFactory().createViewer(FractalType.JULIA_SET);
		this.ui = ui;
	}
	@Override
	public void mouseClicked(MouseEvent me){
		
		try {
			ui.getImageLabel().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			int buttonType = me.getButton();
			int mouseX = me.getX();
			int mouseY = me.getY();
			if(buttonType == LEFT_CLICK) {
				zoom(ui.getFrameDimension(), mouseX, mouseY, ZOOM_IN_FACTOR, MAX_DWELL_INCREASE);
			} else if(buttonType == RIGHT_CLICK) {
				zoom(ui.getFrameDimension(), mouseX, mouseY, ZOOM_OUT_FACTOR, MAX_DWELL_DECREASE);
			}
			ui.getImage().setImage(viewer.getView(ui.getFrameDimension()));
			ui.getFrame().repaint();
		} finally {
			ui.getImageLabel().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		}
	}
	
	private Point draggingPos;
	
	@Override
	public void mouseDragged(MouseEvent me){
		
		FractalConfig oldConfig = viewer.getConfig();
		Image oldImage = ui.getImage().getImage();

		int xChange = draggingPos.x - me.getX();
		int yChange = me.getY() - draggingPos.y;

		double centerX = oldConfig.getCenter().x + xChange / oldConfig.getZoom();
		double centerY = oldConfig.getCenter().y + yChange / oldConfig.getZoom();
		Double newCenter = new Double(centerX, centerY);
		
		viewer.setConfig(new JuliaConfig(newCenter, oldConfig.getZoom(), oldConfig.getMaxDwell(), ((JuliaConfig)oldConfig).getPalette(), oldConfig.getColorScheme()));
		
		ui.getImage().setImage(viewer.getViewPanning(ui.getFrameDimension(), oldConfig, oldImage));
		ui.getFrame().repaint();
		
		draggingPos = me.getPoint();
	}

	public void mousePressed(MouseEvent me){
		draggingPos = me.getPoint();
	}
	
	@Override
	public FractalViewer getFractalViewer(){
		return viewer;
	}

	public void mouseReleased(MouseEvent me){
		ui.getImage().setImage(viewer.getView(ui.getFrameDimension()));
		ui.getFrame().repaint();
	}
	
	public void mouseExited(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseMoved(MouseEvent e){}
	public void mouseWheelMoved(MouseWheelEvent e){}
	public void keyTyped(KeyEvent e){}
	public void keyPressed(KeyEvent e){}
	public void keyReleased(KeyEvent e){}

	@Override
	public void setColorScheme(ColorScheme scheme){
		viewer.getConfig().setColorScheme(scheme);
	}

	@Override
	public ColorScheme getColorScheme(){
		return viewer.getConfig().getColorScheme();
	}
	
	@Override
	public void setColorPalette(ColorPalette palette){
		((JuliaConfig) viewer.getConfig()).setPalette(palette);
	}
	
	@Override
	public ColorPalette getColorPalette(){
		return ((JuliaConfig) viewer.getConfig()).getPalette();
	}
	
	@Override
	public boolean supportsColorPalette(){
		return true;
	} 
	
	private void zoom(Dimension frameDimension, int xPos, int yPos,
			double zoomFactor, int maxDwellOffset) {
		JuliaConfig config = (JuliaConfig) viewer.getConfig();
		double mouseX = xPos - frameDimension.getWidth() / 2;
		double mouseY = frameDimension.getHeight() / 2 - yPos;
		double centerX = config.getCenter().x + mouseX / config.getZoom();
		double centerY = config.getCenter().y + mouseY / config.getZoom();
		Double newCenter = new Double(centerX, centerY);
		double newZoom = config.getZoom() * zoomFactor;
		int newMaxDwell = config.getMaxDwell() + maxDwellOffset;
		
		JuliaConfig newConfig = new JuliaConfig(newCenter, newZoom, newMaxDwell, config.getPalette(), config.getColorScheme());
		
		viewer.setConfig(newConfig);
	}
}
