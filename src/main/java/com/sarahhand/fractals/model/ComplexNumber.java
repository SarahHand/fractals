package com.sarahhand.fractals.model;

public class ComplexNumber{
	
	public final double x;
	public final double y;
	
	public static final ComplexNumber ONE = new ComplexNumber(1, 0);
	public static final ComplexNumber TWO = new ComplexNumber(2, 0);
	
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