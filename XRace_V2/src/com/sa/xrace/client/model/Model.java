package com.sa.xrace.client.model;

import javax.microedition.khronos.opengles.GL10;

import com.sa.xrace.client.math.Point3f;

public final class Model
{	
	public static final int CAR = 1000;
	public static final int MAP = 1001;
	public static final int COLLISION = 1002;
	public static final int ROOM = 1003;
	
	private int mModelID;			//model's ID
	private int mType;				//model's type
	private t3DModel mModel;		//keeping model's data
	private Point3f mScale;			//scale model on the x, y, z axis
	private float mRadius;			//for optimization	
	
	public Model(int modelID, int type, t3DModel model, Point3f scale, float radius)
	{
		this.mModel 	= model;
		this.mModelID	= modelID;
		this.mType		= type;
		this.mScale		= scale;
		this.mRadius    = radius;
	}
	public void generate(GL10 gl)
	{
		mModel.generate(gl);
	}
	
	public void scale(GL10 gl)
	{
		gl.glScalef(mScale.x, mScale.y, mScale.z);	//Scale the object on x, y and z axis
	}
	
	public void draw(GL10 gl)
	{
		mModel.draw(gl);
	}
	
	public t3DObject getObject(String objectName)
	{
		return mModel.getObject(objectName);
	}
	
	
	
	//{{member variables management---------------------------------------------------
	public int getID()
	{
		return this.mModelID;
		
	}
	public int getType()
	{
		return this.mType;
		
	}
	public Point3f getScale()
	{
		return mScale;
	}
	public t3DModel getModel()
	{
		return mModel;
	}
		
	public float getRadius()
	{
		return this.mRadius;
	}
	
	public void setID(int modelID)
	{
		this.mModelID = modelID;
		
	}	
	public void setType(int type)
	{
		this.mType = type;
		
	}	
	public void setRadius(float radius)
	{
		this.mRadius = radius;
	}
	//}}-----------------------------------------------------------
}