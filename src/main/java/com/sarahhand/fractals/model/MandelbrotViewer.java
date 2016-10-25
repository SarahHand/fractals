package com.sarahhand.fractals.model;

import java.awt.Dimension;
import java.awt.Image;

public interface MandelbrotViewer{

	public Image getView(Dimension dimensions);

	public void setConfig(MandelbrotConfig config);

	public MandelbrotConfig getConfig();
}
