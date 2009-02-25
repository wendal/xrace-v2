///:~
/**
 * XRace V2
 * <p/>Use for ... ...
 * <p/>ID: $Id$
 * Last Commit:  $Author$
 * @version $Revision: $
 * 
 */
package com.sa.xrace.client.math;


/**
 * @author sliao
 * @version $Id$
 */
/**
 *  This class is used to store a point in a 3D space
 */
public class Point2f extends android.graphics.PointF implements Cloneable 
{
//	public float x = 0.f;
//	public float y = 0.f;

//	public Point2f() {
//
//	}
//	public Point2f(float x, float y) {
//		this.x = x;
//		this.y = y;
//	}
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
