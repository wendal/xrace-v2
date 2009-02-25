package com.sa.xrace.client.scene;


import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;

import com.sa.xrace.client.math.Point3f;
import com.sa.xrace.client.pool.CarInforClient;
import com.sa.xrace.client.toolkit.DataToolKit;


/**
 * @author sliao
 * @version $Id$
 */
/**
 *  This class is used to init and control the Camera
 */
public class Camera
{

    private int mOffset = 0;
	
	public Camera()
	{
		this.mEye		= new Point3f(0.0f, 0.0f, 0.0f);
		this.mCenter	= new Point3f(0.0f, 0.0f, 1.0f);
		this.mUp		= new Point3f(0.0f, 1.0f, 0.0f);
		this.mDistance  = 5.0f;
		this.mDirection = 0.0f;
		this.mChangeDirection = 0.0f;
	}
	
	public void initCamera(Point3f eye, Point3f center, Point3f up)
	{
		mEye 		= eye;
		mCenter 	= center;
		mUp			= up;
		mDistance  = (float) Math.sqrt((mEye.x - mCenter.x) * (mEye.x - mCenter.x)+
										(mEye.y - mCenter.y) * (mEye.y - mCenter.y)+
										(mEye.z - mCenter.z) * (mEye.z - mCenter.z));
		mDirection = 0.0f;
		mChangeDirection = 0.0f;
	}
	public void initCamera(Point3f center, Point3f up, float angle, float distance)
	{
		double x = (distance * Math.sin(angle));
		double z = (distance * Math.cos(angle));
		mEye.x = (float) (mCenter.x - x);
		mEye.z = (float) (mCenter.z - z);
		mEye.y = center.y;
		
		mCenter 	= center;
		mUp			= up;
		mDistance  	= distance;
		mDirection = 0.0f;
		mChangeDirection = 0.0f;
	}
	
	public void setCamera(GL10 gl)
	{
		GLU.gluLookAt(gl, mEye.x, mEye.y, mEye.z, mCenter.x, mCenter.y, mCenter.z, mUp.x, mUp.y, mUp.z);
	}
	
	//update the camera according to the center that camera will be looking at and the direction
	public void updateCamera(Point3f center, float angle)
	{
		mCenter = center;
		mDirection = (float) (angle%(2*Math.PI));
		double x =  (mDistance * Math.sin(mDirection));
		double z =  (mDistance * Math.cos(mDirection));
		mEye.x = (float) (mCenter.x - x);
		mEye.z = (float) (mCenter.z - z);	
	}
	
	//update the camera according to the car's information given by the parameters
	public void updateCamera(Point3f myCenter, float myDirection, float changeDirection, float speed, 
							int speedKeyState, int directionKeyState, int speedSensorState, int directionSensorState,int lookDirection)
	{		
//		 mDirection = myDirection;
		//{{update distance between camera and car-----------------------
		if (speedKeyState == CarInforClient.SPEED_UP_KEYBOARD || speedSensorState == CarInforClient.SPEED_UP_SENSOR)
		{
			if (mDistance < DataToolKit.FAR_DISTANCE)
			{
				mDistance += DataToolKit.DISTANCE_PER_FRAME;
			}
		}
		else if (speedKeyState == CarInforClient.SPEED_DOWN_KEYBOARD  || speedSensorState == CarInforClient.SPEED_DOWN_SENSOR)
		{
			if (mDistance > DataToolKit.NEAR_DISTANCE)	//nearest distance
			{
				mDistance -= DataToolKit.DISTANCE_PER_FRAME;
			}
		}		
		else if (speedKeyState == CarInforClient.NO_KEY_EVENT && speedSensorState == CarInforClient.NO_SENSOR_EVENT)
		{
			if (mDistance > DataToolKit.NORMAL_DISTANCE)
			{
				mDistance -= DataToolKit.DISTANCE_PER_FRAME;
			}
			else if (mDistance < DataToolKit.NORMAL_DISTANCE)
			{
				mDistance += DataToolKit.DISTANCE_PER_FRAME;
			}
			else
			{
				mDistance = DataToolKit.NORMAL_DISTANCE;
			}
		}
		//}}update distance between camera and car-----------------------
		
		//{{update direction of camera-----------------------
		if (directionKeyState == CarInforClient.DIRECTION_LEFT_KEYBOARD || 
				(directionSensorState == CarInforClient.DIRECTION_LEFT_SENSOR && directionKeyState != CarInforClient.DIRECTION_RIGHT_KEYBOARD))
		{
			if (speed > DataToolKit.PRECISION)
			{
				if (mOffset < DataToolKit.MAX_OFFSET && changeDirection > DataToolKit.DIRECTION_PER_FRAME)
				{
					mChangeDirection = changeDirection-DataToolKit.DIRECTION_PER_FRAME;
					mOffset++;
				}
				else
				{
					mChangeDirection = changeDirection;
				}
			}	
			else if (speed < -DataToolKit.PRECISION)
			{
				if (mOffset > DataToolKit.MIN_OFFSET)
				{
					mChangeDirection = changeDirection+DataToolKit.DIRECTION_PER_FRAME;
					mOffset--;
				}
				else
				{
					mChangeDirection = changeDirection;
				}
			}
			else
			{
				//prevent the camera from keeping rotating even if the speed is zero
				mChangeDirection = 0;
			}
			
			
		}
		else if (directionKeyState == CarInforClient.DIRECTION_RIGHT_KEYBOARD ||
				(directionSensorState == CarInforClient.DIRECTION_RIGHT_SENSOR && directionKeyState != CarInforClient.DIRECTION_LEFT_KEYBOARD))
		{
			if (speed > DataToolKit.PRECISION)
			{
				if (mOffset > DataToolKit.MIN_OFFSET && changeDirection<-DataToolKit.DIRECTION_PER_FRAME)
				{
					mChangeDirection = changeDirection+DataToolKit.DIRECTION_PER_FRAME;
					mOffset--;
				}
				else
				{
					mChangeDirection = changeDirection;
				}
			}
			else if (speed < -DataToolKit.PRECISION)
			{
				if (mOffset < DataToolKit.MAX_OFFSET)
				{
					mChangeDirection = changeDirection-DataToolKit.DIRECTION_PER_FRAME;
					mOffset++;
				}
				else
				{
					mChangeDirection = changeDirection;
				}
			}
			else
			{
				//prevent the car from keeping rotating even if the speed is zero
				mChangeDirection = 0;	
			}
			
			
		}
		else if (directionKeyState == CarInforClient.NO_KEY_EVENT && directionSensorState == CarInforClient.NO_SENSOR_EVENT
				|| speed < 0.2f && speed > -0.2f)
		{
			
			mChangeDirection = 0;	
			if (mOffset > 0)
			{
				updateCamera(myCenter, mDirection - DataToolKit.DIRECTION_PER_FRAME*(mOffset%2-2));
				mLookDirection = (float) (mDirection - DataToolKit.DIRECTION_PER_FRAME*(mOffset%2-2) + Math.PI);
				mOffset+=(mOffset%2-2);
			}
			else if (mOffset < 0)
			{
				updateCamera(myCenter, mDirection - DataToolKit.DIRECTION_PER_FRAME*(mOffset%2+2));
				mLookDirection = (float) (mDirection - DataToolKit.DIRECTION_PER_FRAME*(mOffset%2+2) + Math.PI );
				mOffset+=(mOffset%2+2);
			}
			else
			{
				updateCamera(myCenter, myDirection);
				mLookDirection = (float) (myDirection + Math.PI);
			}
		}
		
		
		if(lookDirection ==  CarInforClient.LOOK_BACK){
			mLookDirection = (float) (myDirection + Math.PI - mChangeDirection);
			updateCamera(myCenter ,mLookDirection);
		}
		else if( lookDirection ==  CarInforClient.LOOK_FRONT)
		{
			mLookDirection = (float) (mDirection + Math.PI - mChangeDirection);
			updateCamera(myCenter, mDirection + mChangeDirection);
			
//			if(directionKeyState == CarInforClient.NO_KEY_EVENT && directionSensorState == CarInforClient.NO_SENSOR_EVENT){
//				updateCamera(myCenter, mDirection + mChangeDirection);//write by sliao
//			}else{
//				updateCamera(myCenter, mLookDirection);//write by sliao
//			}
		}
	}
	
	//{{member variables management---------------------------------------------------	
	public float getDistance()
	{
		return mDistance;
	}	
	public Point3f getEye()
	{
		return mEye;
	}	
	public Point3f getCenter()
	{
		return mCenter;
	}
	public float getDirection()
	{
		return mDirection;
	}
	public float getChangeDirection()
	{
		return mChangeDirection;
	}	
	public void setDistance(float distance)
	{
		mDistance = distance;
	}
	public void setDirection(float direction)
	{
		mDirection = direction;
	}
	public void setChangeDirection(float changeDirection)
	{
		mChangeDirection = changeDirection;
	}	
	public void setEye(float x, float y, float z)
	{
		mEye.x = x;
		mEye.y = y;
		mEye.z = z;
	}	
	public void setCenter(float x, float y, float z)
	{
		mCenter.x = x;
		mCenter.y = y;
		mCenter.z = z;
	}
	//}}member variables management--------------------------------------------------------
	
	private Point3f mEye;		//Eye's Eye
	private Point3f mCenter;	//the point the camera is looking at 
	private Point3f mUp;		//up direction of camera
	private float mDistance;	//distance between the center and eye
	
	private float mDirection;
	private float mChangeDirection;
	private float mLookDirection;

	/**
	 * @return the mOffset
	 */
	public int getOffset() {
		return mOffset;
	}

	/**
	 * @param offset the mOffset to set
	 */
	public void setOffset(int offset) {
		mOffset = offset;
	}
}
