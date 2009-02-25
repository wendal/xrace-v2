package com.sa.xrace.client.scene;

import com.sa.xrace.client.math.Plane3D;
import com.sa.xrace.client.math.Point3f;

/**
 * @author sliao
 * @version $Id$
 */
/**
 *  This class is used to optimizing the render by cutting down the models
 *  which are not in the frustum.
 */
public final class Frustum 
{	
//	private final static int LEFT = 1;
//	private final static int RIGHT = -1;
//	private final static int BOTTOM = 2;
//	private final static int TOP = -2;
//	private final static int NEAR = 3;
//	private final static int FAR = -3;


	private static Plane3D[] mPlanes;
//	private static final FloatBuffer   mModelViewMatrix = FloatBuffer.allocate(16);		// keep the model view matrix
//	private static final FloatBuffer   mProjectionMatrix = FloatBuffer.allocate(16);		// keep the projection matrix
//	private static final FloatBuffer   mCombinationMatrix = FloatBuffer.allocate(16);		// keep the combination of the two matrix
	
	static
	{
		mPlanes = new Plane3D[6];	 //6 planes in the srustum
		for (int index=0; index<6; index++)
		{
			mPlanes[index] = new Plane3D();
		}
	}
	
//	public static boolean updateFrustum(GL10 gl)
//	{
//
//		((GL11)gl).glGetFloatv(GL11.GL_PROJECTION, mProjectionMatrix);
//		((GL11)gl).glGetFloatv(GL11.GL_MODELVIEW, mModelViewMatrix);
//
//		gl.glMatrixMode(GL11.GL_MODELVIEW);
//		gl.glPushMatrix();
//		gl.glLoadMatrixf(mProjectionMatrix);
//		gl.glMultMatrixf(mModelViewMatrix);
//		((GL11)gl).glGetFloatv(GL11.GL_MODELVIEW, mCombinationMatrix);
//		gl.glPopMatrix();
//		
//		extractPlanes(mPlanes[0],mCombinationMatrix,LEFT);
//		extractPlanes(mPlanes[1],mCombinationMatrix,RIGHT);
//		extractPlanes(mPlanes[2],mCombinationMatrix,BOTTOM);
//		extractPlanes(mPlanes[3],mCombinationMatrix,TOP);
//		extractPlanes(mPlanes[4],mCombinationMatrix,NEAR);
//		extractPlanes(mPlanes[5],mCombinationMatrix,FAR);
//		
//		
//		return true;
//	}
//	
//	public static void extractPlanes(Plane3D plane, FloatBuffer inputMatrix,int planeFlag)
//	{
//		int scale =(planeFlag <0)? -1:1;
//		int row = Math.abs(planeFlag)-1;
//		
//		plane.a = inputMatrix.get(3) + scale*inputMatrix.get(row);
//		plane.b = inputMatrix.get(7) + scale*inputMatrix.get(row+4);
//		plane.c = inputMatrix.get(11) + scale*inputMatrix.get(row+8);
//		plane.d = inputMatrix.get(15) + scale*inputMatrix.get(row+12);
//		
//		float mod = (float)Math.sqrt(plane.a*plane.a + plane.b*plane.b + plane.c*plane.c);
//		
//		plane.a /= mod;
//		plane.b /= mod;
//		plane.c /= mod;
//		plane.d /= mod;
//	}
	
	public static boolean checkSphere(Point3f point, float radius)
	{
//		for (int index=0; index<6; index++)
//		{
//			if(mPlanes[index].getDistance(point) <= -radius)
//			{
//				return false;
//			}
//		}
		return true;
	}	
}
