package com.sa.xrace.client.math;

/**
 * @author sliao
 * @version $Id: Point3f.java,v 1.3 2008-12-11 10:03:42 sliao Exp $
 */
/**
 *  This class is used to store a point in a 3D space
 */
public class Point3f {

	public float x = 0.f;
	public float y = 0.f;
	public float z = 0.f;

	public Point3f() {

	}
	

	public Point3f(float inx, float iny, float inz) {
		this.x = inx;
		this.y = iny;
		this.z = inz;
	}
	

	public Point3f clone()
	{
		Point3f point = new Point3f();
		point.x = this.x;
		point.y = this.y;
		point.z = this.z;
		return point;
	}
	
	public void setValues(float inx, float iny, float inz ){
		this.x = inx;
		this.y = iny;
		this.z = inz;
	}
	
	/** 
	 * get a vector from two point
	 */	
	public Point3f getVector( Point3f pointStart)
	{
		Point3f vector = new Point3f();	

		vector.x = x - pointStart.x;			
		vector.y = y - pointStart.y;			
		vector.z = z - pointStart.z;			

		return vector;								
	}

	/** 
	 * get a vector from adding two vectors
	 */	
	public Point3f addVector(Point3f pointStart)
	{
		Point3f vector = new Point3f();							

		vector.x = x + pointStart.x;			
		vector.y = y + pointStart.y;			
		vector.z = z + pointStart.z;			

		return vector;								
	}

	/** 
	 * get a vector by scaling
	 */	
	public void scaleVector(float scaler)
	{
//		Point3f vector = new Point3f();							
				
		x *=  scaler;			
		y *=  scaler;			
		z *=  scaler;	

									
	}

	/** 
	 * get a vector by crossing two vectors
	 */	
	public static Point3f cross(Point3f vector1, Point3f vector2)
	{
		Point3f vector = new Point3f();			
													
		vector.x = ((vector1.y * vector2.z) - (vector1.z * vector2.y));													
		vector.y = ((vector1.z * vector2.x) - (vector1.x * vector2.z));													
		vector.z = ((vector1.x * vector2.y) - (vector1.y * vector2.x));

		return vector;								
	}
	/** 
	 * normalize a vector
	 */	
	public void normalize()
	{
		double length;							

		length = Math.sqrt(x * x + y * y + z * z);					// 获得矢量的长度

		x /= (float)length;				
		y /= (float)length;				
		z /= (float)length;											
	}

	public String toString()
	{
		StringBuffer buffer = new StringBuffer("Point = ");
		buffer.append(x);
		buffer.append(", ");
		buffer.append(y);
		buffer.append(", ");
		buffer.append(z);
		return buffer.toString();
	}
}




