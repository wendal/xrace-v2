package com.sa.xrace.client.math;

public class Plane3D {

	public float a, b, c, d;
	public Plane3D()
	{
		a = 0.0f;
		b = 0.0f;
		c = 0.0f;
		d = 0.0f;
	}
	
	public Plane3D(float[] modulus)
	{
		if (modulus != null)
		{
			a = modulus[0];
			b = modulus[1];
			c = modulus[2];
			d = modulus[3];
		}
	}
	public Plane3D(float a, float b, float c, float d)
	{
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
	
	public float getDistance(Point3f point)
	{
		if (point == null)
		{
			return 0.0f;
		}
		//System.out.println("Plane3D = " + a +", "+ b +", " + c +", " + d);
		return a * point.x + b * point.y + c * point.z + d;
	}
	
	public static void normalize(Plane3D plane)
	{
		double length;				
		length = Math.sqrt(plane.a * plane.a + plane.b * plane.b + plane.c * plane.c);	// get the length of the vector
		plane.a /= (float)length;				
		plane.b /= (float)length;				
		plane.c /= (float)length;				
		plane.d /= (float)length;	
	}
}
