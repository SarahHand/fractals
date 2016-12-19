package com.sarahhand.fractals.event;

import com.sarahhand.fractals.juliaset.JuliaEventHandler;
import com.sarahhand.fractals.mandelbrotset.MandelbrotEventHandler;
import com.sarahhand.fractals.model.FractalType;
import com.sarahhand.fractals.view.FractalsUI;

/**
 * Class that creates FractalEventHandlers.
 * @author J9465812
 *
 */
public class FractalEventHandlerFactory{
	
	/**
	 * Returns a new FractalEventHandler.
	 * @param ui The FractalsUI that the handler should render to.
	 * @param type The fractal that should be represented by the handler.
	 * @return
	 */
	public FractalEventHandler createEventHandler(FractalsUI ui, FractalType type){
		switch (type) {
			case MANDELBROT_SET: 
				return new MandelbrotEventHandler(ui);
			case JULIA_SET: 
				return new JuliaEventHandler(ui);
			default: 
				return new MandelbrotEventHandler(ui);
		}
	}
}
