package com.sa.xrace.client.pool;

import javax.microedition.khronos.opengles.GL10;

import com.sa.xrace.client.collision.AABBbox;
import com.sa.xrace.client.math.Point3f;
import com.sa.xrace.client.model.Model;
import com.sa.xrace.client.model.t3DObject;


/**
 * @author twei
 * @version $Id: CarInforClient.java,v 1.19 2008-12-12 04:34:21 sliao Exp $
 */
public class CarInforClient {

	/**
	 * nName 					Car name
	 * nCarID 					Car ID
	 * nCarType 				Car type,include type,color
	 * nStatus					Current status of this car normal,accident,idle,login,collision,logout
	 * nDirection 				Current Direction
	 * nSpeed 					Current Speed
	 * nAcceleration 			Current Acceleration
	 * nXPosition 				Current X Position
	 * nYPosition 				Current Y Position
	 * nAccidenceSpeed			Collision speed
	 * nAccidenceAcceleration	Collision direction
	 * nAccidenceDirection		Collision Acceleration
	 * timeDelay				time delay of this car in network transmit
	 */
	public final static float VOLVO_MAX = 100.0f;
	public final static float BENZ_MAX = 2.0f;
	public final static float MAX_ANTI_SPEED = -10.0f;
	public final static float[] MAX_SPEED = {VOLVO_MAX,BENZ_MAX};
	private byte nCarID;
	private String nName;// Car name
	private int nCarType;// Car type,include type,color
	private byte nStatus;// Current Status
	private float nDirection;// Current Direction\
	private float nChangeDirection;//Current Direction Change...
	private float nSpeed;// Current Speed
	private float nAcceleration;// Current Acceleration
	private float nXPosition;// Current X Position
	private float nYPosition;// Current Y Position
	
	//car's model by sliao---------------------------------------------
	private Model mModel;			//for model render
	private Point3f[] mWheelPos = new Point3f[4];	
	private t3DObject mWheel;
	private AABBbox mOriginalBox;	//for collision detection
	private AABBbox mTransformedBox;	//for collision detection
	//car's model by sliao---------------------------------------------
	
//	public static int nAccidenceLastTime;
	private float nAccidenceSpeed;
	private float nAccidenceAcceleration;
	private float nAccidenceDirection;
	private float nAccidenceChangeDirection;

	private short timeDelay;//time delay of this car in network transmit
	
	private int carListName;
	
	
	public CarInforClient()
	{	
		nName = new String("");
		nCarID = 0;
		nCarType=0;//Car type,include type,color 
		nStatus=0;//Current Type
		nDirection=0;//Current Direction
		nChangeDirection=0;
		nSpeed=0;//Current Speed
		nAcceleration=0;//Current Acceleration
		nXPosition=200;//Current X Position
		nYPosition=200;//Current Y Position
		
 		//public static int nAccidenceLastTime;
		nAccidenceSpeed = 0.0f;
		nAccidenceAcceleration=0;
		nAccidenceDirection=0;
		
		timeDelay = 0;
		
		mOriginalBox = new AABBbox();
		mTransformedBox = new AABBbox();
	}

	public boolean draw(GL10 gl)
	{
		if(mModel == null || mWheel == null)
		{
			return false;
		}
		float rotateAngle;
		if (nChangeDirection > 0.2)
		{
			rotateAngle = 0.2f;
		}
		else if (nChangeDirection < -0.2)
		{
			rotateAngle = -0.2f;
		}
		else
		{
			rotateAngle = nChangeDirection;
		}
		mModel.getObject("shadow").draw(gl);
		gl.glPushMatrix();
		if(nSpeed > 0.0f)
		{
			gl.glRotatef((float) Math.toDegrees(rotateAngle*0.5), 0, 0, 1);
		}
		else if (nSpeed < 0.0f)
		{
			gl.glRotatef((float) Math.toDegrees(-rotateAngle*0.5), 0, 0, 1);
		}
		mModel.getObject("body").draw(gl);
		mModel.getObject("basis").draw(gl);
		gl.glPopMatrix();

		for (int i=0; i<2; i++)
		{
			gl.glPushMatrix();
			gl.glTranslatef(mWheelPos[i].x, mWheelPos[i].y, mWheelPos[i].z);
			if(nSpeed > 0.0f)
			{
				gl.glRotatef((float) Math.toDegrees(rotateAngle*2.5), 0, 1, 0);
				gl.glRotatef((float) Math.toDegrees(rotateAngle*0.5), 0, 0, 1);
			}
			else if (nSpeed < 0.0f)
			{
				gl.glRotatef((float) Math.toDegrees(-rotateAngle*2.5), 0, 1, 0);
				gl.glRotatef((float) Math.toDegrees(-rotateAngle*0.5), 0, 0, 1);
			}
			
//			gl.glRotatef(nSpeed, 1, 0, 0);
			mWheel.draw(gl);
			gl.glPopMatrix();
		}
		for (int i=2; i<4; i++)
		{
			gl.glPushMatrix();
			gl.glTranslatef(mWheelPos[i].x, mWheelPos[i].y, mWheelPos[i].z);
			if(nSpeed > 0.0f)
			{
				gl.glRotatef((float) Math.toDegrees(rotateAngle*0.5), 0, 0, 1);
			}
			else if (nSpeed < 0.0f)
			{
				gl.glRotatef((float) Math.toDegrees(-rotateAngle*0.5), 0, 0, 1);
			}
//			gl.glRotatef(nSpeed, 1, 0, 0);
			mWheel.draw(gl);
			gl.glPopMatrix();
		}
		
	    return true;
	}
	
	private boolean generateWheel()
	{
		if (mModel == null) 
		{
			return false;
		}
		mWheel = (t3DObject) mModel.getObject("rlwheel").clone();
		mWheel.resetVertice(new Point3f(0.0f, 0.0f, 0.0f));
		mWheel.createVertexBuffer();
		mWheelPos[0] = mModel.getObject("flwheel").getCenter();
		mWheelPos[1] = mModel.getObject("frwheel").getCenter();
		mWheelPos[2] = mModel.getObject("rlwheel").getCenter();
		mWheelPos[3] = mModel.getObject("rrwheel").getCenter();
		
		return true;
	}
	
	public void setModel(Model model)
	{
		this.mModel = model;
		generateWheel();
	}
	
	public Model getModel()
	{
		return this.mModel;
	}

	public byte getNCarID() {
		return nCarID;
	}


	public void setNCarID(byte carID) {
		nCarID = carID;
	}


	public String getNName() {
		return nName;
	}


	public void setNName(String name) {
		nName = name;
	}


	public int getNCarType() {
		return nCarType;
	}


	public void setNCarType(int carType) {
		nCarType = carType;
	}


	public byte getNStatus() {
		return nStatus;
	}


	public void setNStatus(byte status) {
		nStatus = status;
	}


	public float getNDirection() {
		return nDirection;
	}


	public void setNDirection(float direction) {
		nDirection = direction;
	}


	public float getNChangeDirection() {
		return nChangeDirection;
	}


	public void setNChangeDirection(float changeDirection) {
		nChangeDirection = changeDirection;
	}


	public float getNSpeed() {
		return nSpeed;
	}


	public void setNSpeed(float speed) {
		nSpeed = speed;
	}


	public float getNAcceleration() {
		return nAcceleration;
	}


	public void setNAcceleration(float acceleration) {
		nAcceleration = acceleration;
	}


	public float getNXPosition() {
		return nXPosition;
	}


	public void setNXPosition(float position) {
		nXPosition = position;
	}


	public float getNYPosition() {
		return nYPosition;
	}


	public void setNYPosition(float position) {
		nYPosition = position;
	}


	public float getNAccidenceSpeed() {
		return nAccidenceSpeed;
	}


	public void setNAccidenceSpeed(float accidenceSpeed) {
		nAccidenceSpeed = accidenceSpeed;
	}


	public float getNAccidenceAcceleration() {
		return nAccidenceAcceleration;
	}


	public void setNAccidenceAcceleration(float accidenceAcceleration) {
		nAccidenceAcceleration = accidenceAcceleration;
	}


	public float getNAccidenceDirection() {
		return nAccidenceDirection;
	}


	public void setNAccidenceDirection(float accidenceDirection) {
		nAccidenceDirection = accidenceDirection;
	}


	public short getTimeDelay() {
		return timeDelay;
	}


	public void setTimeDelay(short timeDelay) {
		this.timeDelay = timeDelay;
	}


	public static float getVOLVO_MAX() {
		return VOLVO_MAX;
	}


	public static float getBENZ_MAX() {
		return BENZ_MAX;
	}


	public static float getMAX_ANTI_SPEED() {
		return MAX_ANTI_SPEED;
	}


	public static float[] getMAX_SPEED() {
		return MAX_SPEED;
	}

	public float getNAccidenceChangeDirection() {
		return nAccidenceChangeDirection;
	}

	public void setNAccidenceChangeDirection(float accidenceChangeDirection) {
		nAccidenceChangeDirection = accidenceChangeDirection;
	}
	

	public int getCarListName() {
		return carListName;
	}

	public void setCarListName(int carListName) {
		this.carListName = carListName;
	}

//{{sliao
	public boolean generateAABBbox()
	{
		if (mModel != null)
		{
			mOriginalBox = new AABBbox(mModel);
			mTransformedBox = new AABBbox();
			return true;
		}
		return false;
	}
	
    public int getSpeedKeyState()
    {
    	return mSpeedKeyState;
    }
    
    public int getDirectionKeyState()
    {
    	return mDirectionKeyState;
    }
    
    public void setSpeedKeyState(int state)
    {
    	mSpeedKeyState = state;
    }
    
    public void setDirectionKeyState(int state)
    {
    	mDirectionKeyState = state;
    }
    
    public int getSpeedSensorState()
    {
    	return mSpeedSensorState;
    }
    
    public int getDirectionSensorState()
    {
    	return mDirectionSensorState;
    }
    
    public void setSpeedSensorState(int state)
    {
    	mSpeedSensorState = state;
    }
    
    public void setDirectionSensorState(int state)
    {
    	mDirectionSensorState = state;
    }
	public AABBbox getMOriginalBox() {
		return mOriginalBox;
	}

	public void setMOriginalBox(AABBbox originalBox) {
		mOriginalBox = originalBox;
	}

	public AABBbox getMTranformedBox() {
		return mTransformedBox;
	}

	public void setMTranformedBox(AABBbox tranformedBox) {
		mTransformedBox = tranformedBox;
	}
    
    
    public static final int NO_KEY_EVENT = 2001;
    public static final int NO_SENSOR_EVENT = 2002;
    
    public static final int SPEED_UP_KEYBOARD = 3001;
    public static final int SPEED_DOWN_KEYBOARD = 3002;
    public static final int DIRECTION_LEFT_KEYBOARD = 3003;
    public static final int DIRECTION_RIGHT_KEYBOARD = 3004;
    
    public static final int SPEED_UP_SENSOR = 4001;
    public static final int SPEED_DOWN_SENSOR = 4002;
    public static final int DIRECTION_LEFT_SENSOR = 4003;
    public static final int DIRECTION_RIGHT_SENSOR = 4004;

    public static final float SPEED_PER_FRAME = 0.3f;
    public static final float TOP_ACCELERATION = 2.0f;
    public static final float ACCELERATION_PER_FRAME = 0.1f;
    public static final float TOP_SPEED = 40.0f;
    public static final float TOP_ANTI_SPEED = 10.0f;
    public static final float CHANGE_DIRECTION = 0.01f;
    public static final float TOP_CHANGE_DIRECTION = 0.3f;	//
    public static final float PRECISION = 2.0f;
    
    public static final float CAR_CENTER_Y = 0.0f;			//the Y coordinate of car's center
	public static final int LOOK_BACK = 61;
	public static final int LOOK_FRONT = 64;

    private int mSpeedKeyState = NO_KEY_EVENT;
    private int mDirectionKeyState = NO_KEY_EVENT;
    private int mSpeedSensorState = NO_SENSOR_EVENT;
    private int mDirectionSensorState = NO_SENSOR_EVENT;
    private int mLookDirction = LOOK_FRONT;
    
    public void updateSpeedBySensor(float acceleration, float timeElapsed)
    {
    	if (mSpeedSensorState != NO_SENSOR_EVENT)
		{						
			if (mSpeedSensorState == SPEED_DOWN_SENSOR && nSpeed > PRECISION)
			{
				nSpeed += 3 * timeElapsed * acceleration;
			}
			else
			{
				nSpeed += timeElapsed * acceleration;
			}
			if (nSpeed < -TOP_ANTI_SPEED)
			{
				nSpeed = -TOP_ANTI_SPEED;				
			}
			else if (nSpeed > TOP_SPEED)
			{
				nSpeed = TOP_SPEED;
			}										
		}  	
    }
    public void updateDirectionBySensor(float changeDirection, float timeElapsed)
    {
    	if (mDirectionSensorState != NO_SENSOR_EVENT  && mDirectionKeyState == NO_KEY_EVENT)
		{			
			if(nSpeed > PRECISION || nSpeed < -PRECISION)
			{				
				nChangeDirection = timeElapsed * changeDirection;
				if (nChangeDirection > TOP_CHANGE_DIRECTION)
				{	
					nChangeDirection = TOP_CHANGE_DIRECTION;
				}
				else if (nChangeDirection < -TOP_CHANGE_DIRECTION)
				{
					nChangeDirection = -TOP_CHANGE_DIRECTION;
				}	
				
				nDirection += nChangeDirection;
			}
		}  	
    }
    
    public void updateSpeedByKeyboard(int timeElapsed)
    {
    	if (timeElapsed == 0)
    	{
    		timeElapsed = 1;
    	}
		//{{acceleration-----------------------
		if (mSpeedKeyState == SPEED_UP_KEYBOARD)
		{
			if (nSpeed < PRECISION)
			{
				nAcceleration = timeElapsed * TOP_ACCELERATION;
			}
			else
			{
				nAcceleration = timeElapsed * TOP_ACCELERATION / nSpeed;
			}
		}
		else if (mSpeedKeyState == SPEED_DOWN_KEYBOARD)
		{			
			if (nSpeed > -PRECISION)
			{					
				nAcceleration = -timeElapsed *TOP_ACCELERATION; //slow down faster if the speed is greater than zero
			}
			else
			{
				nAcceleration = timeElapsed * TOP_ACCELERATION / nSpeed;
			}
		}
		//}}acceleration-----------------------
		
		//{{speed-----------------------
		if (mSpeedKeyState != NO_KEY_EVENT)
		{
			nSpeed += timeElapsed * nAcceleration;	//can not put this out of this if-else statement
			if (nSpeed > TOP_SPEED)
			{
				nSpeed = TOP_SPEED;			
			}
			else if (nSpeed < -TOP_ANTI_SPEED)
			{
				nSpeed = -TOP_ANTI_SPEED;
			}
		}
		//}}speed-----------------------
    }
    
    public void updateDirectionByKeyboard(int timeElapsed)
    {
    	if (timeElapsed == 0)
    	{
    		timeElapsed = 1;
    	}
    	//{{changeDirection-----------------------
		if (mDirectionKeyState == DIRECTION_LEFT_KEYBOARD)
		{
			if (nSpeed > PRECISION)
			{
				nChangeDirection+= timeElapsed * CHANGE_DIRECTION;
			}
			else if (nSpeed < -PRECISION)
			{				
				nChangeDirection-= timeElapsed * CHANGE_DIRECTION;
			}
			else
			{
				//prevent the car from keeping rotating even if the speed is zero
				nChangeDirection = 0.0f;	
			}
		}
		else if (mDirectionKeyState == DIRECTION_RIGHT_KEYBOARD)
		{
			if (nSpeed > PRECISION)
			{
				nChangeDirection-= timeElapsed * CHANGE_DIRECTION;
			}
			else if (nSpeed < -PRECISION)
			{
				nChangeDirection+= timeElapsed * CHANGE_DIRECTION;
			}
			else
			{				
				nChangeDirection = 0.0f;	//prevent the car from keeping rotating even if the speed is zero
			}
		}
    	//}}changeDirection-----------------------
		

    	//{{direction-----------------------
		if (mDirectionKeyState != NO_KEY_EVENT)
		{
			if (nChangeDirection > TOP_CHANGE_DIRECTION)
			{
				nChangeDirection = TOP_CHANGE_DIRECTION;
			}
			else if (nChangeDirection < -TOP_CHANGE_DIRECTION)
			{
				nChangeDirection = -TOP_CHANGE_DIRECTION;
			}			
			nDirection += nChangeDirection;	//can not get this out of this if statement					
			if(nDirection < 0 )
			{
				nDirection = (float) (nDirection + 2*Math.PI);
			}
			else if(nDirection > 2*Math.PI )
			{
				nDirection  = (float) (nDirection - 2*Math.PI);
			}	
		}
		//}}direction-----------------------
    }
    
    public void weakOff(int timeElapsed)
    {
    	if (timeElapsed == 0)
    	{
    		timeElapsed = 1;
    	}
    	if (mSpeedKeyState == NO_KEY_EVENT )//slow down if no event from the sensor or keyboard
		{
			nAcceleration = 0.0f;	//the sensor's value would not evaluate to nAcceleration
			if (mSpeedSensorState == NO_SENSOR_EVENT)
			{
				if (nSpeed >PRECISION)
				{	
					nSpeed -= timeElapsed *SPEED_PER_FRAME;
				}
				else if (nSpeed < -PRECISION)
				{
					nSpeed += timeElapsed *SPEED_PER_FRAME;
				}
				else
				{
					nSpeed = 0;
				}	
			}
		}

		if (mDirectionKeyState == NO_KEY_EVENT && mDirectionSensorState == NO_SENSOR_EVENT)//stop rotating if no event from the sensor or keyboard
		{						
//			nChangeDirection = 0.0f;	//prevent the car from keeping rotating even if the speed is zero
			if (nChangeDirection > 0.03f)
			{
				nChangeDirection -= 0.03f;
			}
			else if (nChangeDirection < -0.03f)
			{
				nChangeDirection += 0.03f;
			}
			else
			{
				nChangeDirection = 0.0f;
			}
		}	
    }

	/**
	 * @return the mLookDirction
	 */
	public int getLookDirction() {
		return mLookDirction;
	}

	/**
	 * @param lookDirction the mLookDirction to set
	 */
	public void setLookDirction(int lookDirction) {
		mLookDirction = lookDirction;
	}
	
	private float mWheelRollDirection = 0.0f;
	public void transform(GL10 gl)
	{
        gl.glTranslatef(-100.0f, CAR_CENTER_Y+30f, 180.0f);
        if(nSpeed > 0.1f)
    	{
        	if (mDirectionKeyState != NO_KEY_EVENT || mDirectionSensorState != NO_SENSOR_EVENT)
        	{
        		gl.glRotatef((float) Math.toDegrees(nChangeDirection*3), 0, 1, 0);
            	gl.glRotatef((float) Math.toDegrees((mWheelRollDirection++)+nSpeed), 1, 0, 0); 
        	}
        	else
        	{
        		mWheelRollDirection = 0.0f;
        	}
    	}
        else
        {
        	gl.glRotatef((float) Math.toDegrees(-nChangeDirection*2), 0, 1, 0); 
        }
	}
}
