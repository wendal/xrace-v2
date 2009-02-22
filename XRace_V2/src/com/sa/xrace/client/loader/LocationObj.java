package com.sa.xrace.client.loader;

import com.sa.xrace.client.math.Point3f;

public class LocationObj {
	private int size ;
	private Point[] point;
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public Point[] getPointArray() {
		if(this.point != null){
			return point;
		}else{
			return null;
		}
	}
	public Point3f getPoint(int index)
	{
		return new Point3f(point[index].getX(), point[index].getY(), point[index].getZ());
	}

	public float getAngle(int index)
	{
		return point[index].getAngle();
	}
	
	public void setPoint(Point[] point) {
		this.point = point;
	}
	
}
