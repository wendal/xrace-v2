package com.sa.xrace.client.math;


/**
 * @author sliao
 * @version $Id: Point2f.java,v 1.2 2008-12-11 10:03:42 sliao Exp $
 */
/**
 *  This class is used to store a point in a 3D space
 */
public class Point2f implements Cloneable 
{
	public float x = 0.f;
	public float y = 0.f;

	public Point2f() {

	}
	public Point2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public Object clone()
	{
		Object o = null;
		try {
			o = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}
	
}
