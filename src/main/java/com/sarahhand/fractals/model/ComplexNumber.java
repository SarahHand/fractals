package com.sarahhand.fractals.model;

/**
 * Represents a complex number.
 * @author J9465812
 */
public class ComplexNumber{
	
	/**
	 * The real part.
	 */
	public final double x;
	/**
	 * The imaginary part.
	 */
	public final double y;
	
	/**
	 * The complex number representation of 1.
	 */
	public static final ComplexNumber ONE = new ComplexNumber(1, 0);
	/**
	 * The complex number representation of 2.
	 */
	public static final ComplexNumber TWO = new ComplexNumber(2, 0);
	
	/**
	 * The constructor.
	 * @param x
	 * @param y
	 */
	public ComplexNumber(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the sum of this and c.
	 * @param c
	 * @return
	 */
	public ComplexNumber add(ComplexNumber c){
		
		ComplexNumber addition = new ComplexNumber(this.x + c.x, this.y + c.y);
		return addition;
	}
	
	/**
	 * Returns the product of this and c.
	 * @param c
	 * @return
	 */
	public ComplexNumber multiply(ComplexNumber c){
		
		ComplexNumber multiplication = new ComplexNumber(this.x*c.x - this.y*c.y, this.x*c.y + this.y*c.x);
		return multiplication;
	}
}