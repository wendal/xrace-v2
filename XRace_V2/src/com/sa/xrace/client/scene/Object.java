package com.sa.xrace.client.scene;

import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

import com.sa.xrace.client.math.Matrix4f;
import com.sa.xrace.client.math.Point3f;
import com.sa.xrace.client.model.Model;
import com.sa.xrace.client.model.t3DObject;


/**
 * @author sliao
 * @version $Id: Object.java,v 1.5 2008-12-05 07:10:46 jlin Exp $
 */
/**
 *  This class represents the Object appeared in the world
 */
public class Object 
{
	private Model mModel;					//which model should be used for render
	private Point3f mPosition;				//where the model should be render
	private float mAngle;					//the angle model should rotate along Y axis
	
	public Matrix4f mTransformMatrix;		//transform matrix
	public Point3f[][] mVerts;				//point array of each t3DObject after transformed
	
	public Object(Model model, Point3f position, float angle)
	{
		this.mModel 	= model;
		this.mPosition 	= position;
		this.mAngle 	= angle;
		this.mTransformMatrix = null;
		this.mVerts     = null;
	}
	public void translate(GL10 gl)
	{
        gl.glTranslatef(mPosition.x, mPosition.y, mPosition.z);	
	}
	
	public void rotate(GL10 gl)
	{	
        gl.glRotatef(mAngle, 0, 1, 0);
	}
	public void scale(GL10 gl)
	{
		mModel.scale(gl);
	}
	
	public void updateTransformMatrix()
	{
	    Matrix4f translateMatrix = new Matrix4f();
	    Matrix4f rotateMatrix = new Matrix4f();
	    Matrix4f scaleMatrix = new Matrix4f();
	    translateMatrix.getTranlateMatrix(mPosition);
	    rotateMatrix.getRotateMatrixY((float)Math.toRadians(mAngle));
	    scaleMatrix.getScaleMatrix(mModel.getScale());
	    mTransformMatrix = translateMatrix.multiply(rotateMatrix.multiply(scaleMatrix));
	    //mTransformMatrix = scaleMatrix.multiply(rotateMatrix.multiply(translateMatrix));
	    
	    mVerts = new Point3f[mModel.getModel().numOfObjects][];

	    int num = 0;
	    Iterator<t3DObject> t3DObjectIterator = mModel.getModel().Objects.iterator();
		while (t3DObjectIterator.hasNext())
		{
			t3DObject object = t3DObjectIterator.next();
			mVerts[num] = new Point3f[object.numOfVerts];
			for(int index=0; index<object.numOfVerts; index++)
			{
				mVerts[num][index] = new Point3f();
				mTransformMatrix.transformPoint(object.Verts[index], mVerts[num][index]);
			}
			num++;
		}
	}
	
	public void transform(GL10 gl)
	{
        gl.glTranslatef(mPosition.x, mPosition.y, mPosition.z);	
        gl.glRotatef(mAngle, 0, 1, 0);
		mModel.scale(gl);
	}
	
	public void draw(GL10 gl) 
	{
		mModel.draw(gl);
	}
	
	
	
	//{{member variables management---------------------------------------------------
	public Point3f getPosition()
	{
		return this.mPosition;
	}

	public Model getModel()
	{
		return this.mModel;
	}
	
	public float getAngle()
	{
		return this.mAngle;
	}

	public Point3f getScale()
	{
		return mModel.getScale();
	}
	
	public void setAngle(float angle)
	{
		this.mAngle = angle;
	}

	public void setPosition(Point3f pos)
	{
		this.mPosition = pos;
	}
	//}}-----------------------------------------------------------
}
