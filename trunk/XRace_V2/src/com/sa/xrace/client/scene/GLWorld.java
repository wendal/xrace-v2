package com.sa.xrace.client.scene;

import java.util.Iterator;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.sa.xrace.client.collision.AABBbox;
import com.sa.xrace.client.collision.CollisionHandler;
import com.sa.xrace.client.collision.CollisionMap;
import com.sa.xrace.client.math.Matrix4f;
import com.sa.xrace.client.math.Point3f;
import com.sa.xrace.client.model.Model;
import com.sa.xrace.client.pool.CarInforClient;
import com.sa.xrace.client.pool.InforPoolClient;
import com.sa.xrace.client.toolkit.DataToolKit;
import com.sa.xrace.client.toolkit.ObjectPool;

/**
 * @author sliao
 * @version $Id$
 */
/**
 *  This class is used to store and render those objects that would
 *  appear in the World
 */
public class GLWorld
{		
	private Vector<Object> mObjectVector = new Vector<Object>();
	private InforPoolClient mInforPoolClient;
	private CollisionMap collisionMap;
	private Frustum mFrustum = new Frustum();
	private Camera mCamera;
    private float mAngle;
    private Matrix4f mTranslateMatrix;
    private Matrix4f mScaleMatrix;
    private Matrix4f mRotateMatrix;
    private Matrix4f mCombineMatrix;
    
    private CarInforClient myCar;
    private CollisionHandler collisionHandler;
    
//	public static long nowTime = 0;
//	public static long lastTime = 0;
//	public static long timeElapsed = 0;
//	public static long timeadd = 0;
	public  boolean isBeginWait = false;
	
	private boolean collisionFlag;
//	private Report report;
//	private int collisionStyle;
//	private Line2f tempLine;
//	private Point3f tempCollisionPoint;
//	private PostManagerClient mPostManager;
	
	public GLWorld( Camera camera)
	{
		mInforPoolClient = ObjectPool.inPoolClient;
		mCamera = camera;
		mFrustum = new Frustum();
		mTranslateMatrix = new Matrix4f();
		mRotateMatrix = new Matrix4f();
		mScaleMatrix = new Matrix4f();
		mCombineMatrix = new Matrix4f();
		collisionMap = new CollisionMap();
//		mPostManager = postmanager;
		
		collisionHandler = new CollisionHandler();
	}
	public void generateCollisionMap()
	{
		Iterator<Object> objectIterator = mObjectVector.iterator();
		while (objectIterator.hasNext())
		{
			Object object = objectIterator.next();
			if (object.mVerts != null)
			{
				collisionMap.generateWallCollisionMap(object);
			}
		}
		collisionMap.generateWallLines();
		collisionMap.prepare();
	}
	
	private static long lastrun = 0;
	public void draw(GL10 gl, long timeElapsed)
	{
		Log.e("Since Last run ", ""+(System.currentTimeMillis() - lastrun));
		lastrun = System.currentTimeMillis();
		
		myCar = mInforPoolClient.getOneCarInformation(mInforPoolClient.getMyCarIndex());	
		if(isBeginWait){
			myCar.updateSpeedByKeyboard((int)timeElapsed/80);	
			myCar.updateDirectionByKeyboard((int)timeElapsed/80);
			synchronized(mInforPoolClient)
			{mInforPoolClient.prepareAllCarInformation((int)timeElapsed/10, InforPoolClient.mSensor[1]/10, InforPoolClient.mSensor[0]/100);}
		}
		myCar.weakOff((int)timeElapsed/80);
	      
		float myDirection = myCar.getNDirection();
		float speed = myCar.getNSpeed();
		float changeDirection = myCar.getNChangeDirection();
		int speedKeyState = myCar.getSpeedKeyState();
		int directionKeyState = myCar.getDirectionKeyState();
		int speedSensorState = myCar.getSpeedSensorState();
		int directionSensorState = myCar.getDirectionSensorState();	
		int lookDiection = myCar.getLookDirction();
		Point3f myCenter = new Point3f(myCar.getNXPosition(), CarInforClient.CAR_CENTER_Y, myCar.getNYPosition());
		Point3f cameraCenter = new Point3f(myCar.getNXPosition(), DataToolKit.CAMERA_CENTER_Y, myCar.getNYPosition());

		mCamera.updateCamera(cameraCenter, myDirection, changeDirection, speed, 
							speedKeyState, directionKeyState, speedSensorState, directionSensorState,lookDiection);
		
	
//		collisionMap.drawWall(gl, mCamera);
		
		Iterator<Object> objectIterator = mObjectVector.iterator();
		while (objectIterator.hasNext())
		{
			Object object = objectIterator.next();
			if (object.mVerts != null)
			{
				continue;
			}
			
	        gl.glMatrixMode(GL10.GL_MODELVIEW);		//set the matrix which would be changed
			gl.glLoadIdentity();
	        //make all the transform matrix to be identity matrix
			mCamera.setCamera(gl);	
			object.translate(gl);
			object.rotate(gl);
			object.scale(gl);				
			//mFrustum.updateFrustum(gl);
			if (mFrustum.checkSphere(object.getPosition(), object.getModel().getRadius()) == true)
			{	
				object.draw(gl);

			}
		}		
		
		if (mInforPoolClient != null)
		{			
			// if there are other players, not only myself
			if(mInforPoolClient.getNCarNumber() != 1)
			{
				// we need update the others' car first, we need their data be updated to this frame first
				for (int index = 0; index < mInforPoolClient.getNCarNumber(); index++)
				{
					//do some optimization according to the car's position
					if(index != mInforPoolClient.getMyCarIndex())
					{
						CarInforClient car = mInforPoolClient.getOneCarInformation(index);
						Point3f center = new Point3f(car.getNXPosition(), CarInforClient.CAR_CENTER_Y, car.getNYPosition());
						float direction = car.getNDirection();
						Model model = car.getModel();
						gl.glMatrixMode(GL10.GL_MODELVIEW);			//set the matrix which would be changed
				        gl.glLoadIdentity();						//make all the transform matrix to be identity matrix
						mCamera.setCamera(gl);	
				        gl.glTranslatef(center.x, center.y, center.z);
				        gl.glRotatef((float) Math.toDegrees(direction), 0, 1, 0); 
						model.scale(gl);
						mTranslateMatrix.getTranlateMatrix(center);
						mRotateMatrix.getRotateMatrixY(myDirection);
						mCombineMatrix = mTranslateMatrix.multiply(mRotateMatrix);
						
	//					mCombineMatrix = mScaleMatrix.multiply(mRotateMatrix.multiply(mTranslateMatrix));
						//////////////////////////
						updateCarAABB(index,mCombineMatrix);
				        //////////////////////////
						
						
						//mFrustum.updateFrustum(gl);
						if (mFrustum.checkSphere(center, model.getRadius()) == true)
						{		      
							model.draw(gl);
						}	
					}
				}
			}
			// then my car
			Model model = myCar.getModel();	
			gl.glMatrixMode(GL10.GL_MODELVIEW);			//set the matrix which would be changed
	        gl.glLoadIdentity();						//make all the transform matrix to be identity matrix
			mCamera.setCamera(gl);	
	        gl.glTranslatef(myCenter.x, myCenter.y, myCenter.z);
	        gl.glRotatef((float) Math.toDegrees(myDirection), 0, 1, 0); 
	        model.scale(gl);
			mTranslateMatrix.getTranlateMatrix(myCenter);
			mRotateMatrix.getRotateMatrixY(myDirection);
			mCombineMatrix = mTranslateMatrix.multiply(mRotateMatrix);	
			mScaleMatrix.getScaleMatrix(model.getScale());
			
			mCombineMatrix = mTranslateMatrix.multiply(mRotateMatrix.multiply(mScaleMatrix));	

//			mCombineMatrix = mScaleMatrix.multiply(mRotateMatrix.multiply(mTranslateMatrix));
			
			//////////////////////////
			updateCarAABB(mInforPoolClient.getMyCarIndex(),mCombineMatrix);
			collisionProcess(mInforPoolClient.getMyCarIndex());
			
	        //////////////////////////
			//mFrustum.updateFrustum(gl);
			if (mFrustum.checkSphere(myCenter, model.getRadius()) == true)
			{		
 		        myCar.draw(gl);
			}

		}

//		mInforPoolClient.getOneCarInformation(mInforPoolClient.getMyCarIndex()).getMTranformedBox().rectangle.prepare();
//		mInforPoolClient.getOneCarInformation(mInforPoolClient.getMyCarIndex()).getMTranformedBox().rectangle.drawCarLine(gl,mCamera);
	}
	
	public void rotate(GL10 gl) 
	{
		gl.glRotatef(mAngle, 0, 1, 0);
		mAngle += 1.2f;
	}
	
	public void addObject(Object Object)
	{
		mObjectVector.add(Object);
	}
	
	public void updateCarAABB(int carIndex,Matrix4f matrix)
	{
		mInforPoolClient.getOneCarInformation(carIndex).getMTranformedBox().transformBox(
						mInforPoolClient.getOneCarInformation(carIndex).getMOriginalBox(),
						matrix);
	}
	
	public void collisionProcess(int carIndex)
	{
		// check whether I collide with another car
		if(mInforPoolClient.getNCarNumber() != 1)
		{
			for(int i =0;i<mInforPoolClient.getNCarNumber();i++)
			{
				if(i != carIndex)
				{
					collisionFlag =	mInforPoolClient.getOneCarInformation(carIndex).getMTranformedBox().
					checkCollisionWithCar(mInforPoolClient.getOneCarInformation(i).getMTranformedBox());
				}
				if(collisionFlag != false)
				{
					// do something 
					carCollisionHandle(mInforPoolClient.getOneCarInformation(carIndex),mInforPoolClient.getOneCarInformation(i));
					collisionFlag = false;
				}
			}
		}
		
		// check whether I collide with wall
		collisionFlag =	mInforPoolClient.getOneCarInformation(carIndex).getMTranformedBox().checkCollisionWithScene(collisionMap);
		if(collisionFlag != false)
		{
			// do something 
			wallCollisionHandle(mInforPoolClient.getOneCarInformation(carIndex));
			collisionFlag = false;
		}
		
		// check whether i collide with the finishing line
		collisionFlag =	mInforPoolClient.getOneCarInformation(carIndex).getMTranformedBox().checkCollisionWithFinishLine(collisionMap);
		if(collisionFlag != false)
		{
			// do something 
			finishLineHandle(mInforPoolClient.getOneCarInformation(carIndex).getMTranformedBox());
			collisionFlag = false;
		}
	}
	
	public void finishLineHandle(AABBbox box)
	{
//		report = 
			box.getReport();
//		collisionStyle = 
			box.getCollisionStyle();
		
	}
		
	public void finishLineHandle(CarInforClient oneCarInfor)
	{
		collisionHandler.finishLineCollisonHandle(oneCarInfor);
	}
	
	private void wallCollisionHandle(CarInforClient oneCarInfor)
	{
//		oneCarInfor.getMTranformedBox().wallCollisionHandle(oneCarInfor);
		collisionHandler.wallCollisionHandle(oneCarInfor,collisionMap,mCamera);
	}
	
	private void carCollisionHandle(CarInforClient myCarInfor,CarInforClient targetCarInfor)
	{
//		oneCarInfor.getMTranformedBox().carCollisionHandle(oneCarInfor);
		collisionHandler.carCollisionHandle(myCarInfor,targetCarInfor.getNXPosition(),targetCarInfor.getNYPosition(),mCamera);

	}
}



