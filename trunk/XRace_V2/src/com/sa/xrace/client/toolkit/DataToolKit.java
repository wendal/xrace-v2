package com.sa.xrace.client.toolkit;

/**
 * 用来保存常量
 * @author zcchen
 *
 */
public final class DataToolKit {

    /**
     * 
     */
	public static final int GAME_ROOM 	= 1;
	public static final int GAME_RUNNING = 3;
	
	/**
	 * From Camera
	 */
    public static final float PRECISION = 1.0f;
    public static final float NORMAL_DISTANCE = 450.0f;		//distance between the car and camera when car's stop
    public static final float NEAR_DISTANCE = 400.0f;		//near distance between the car and camera when car's moving forward
    public static final float FAR_DISTANCE = 530.0f;		//far distance between the car and camera when car's moving backward
    public static final float DISTANCE_PER_FRAME = 10.0f;	//distance changed in one frame
    public static final float DIRECTION_PER_FRAME = 0.015f;
    public static final float CAMERA_EYE_Y = 130.0f;	//eye's y 
    public static final float CAMERA_CENTER_Y = 90.0f;	//center's y 
    public static final int MAX_OFFSET = 20;
    public static final int MIN_OFFSET = -20;
    
    /**
     * From InforPoolClient
     */
    public static final byte LOGIN = 101;
    public static final byte LOGOUT = 100;
    public static final byte START = 111;
    public static final byte NORMAL = 10;
    public static final byte ACCIDENT = 20;
    public static final byte IDLE = 110;
    public static final byte DROPOUT = 30;
    public static final byte CARTYPE = 14;
    public static final byte LOGINFAILURE =102;
    
    /**
     * From com.sa.xrace.client.model.Model
     */
    public static final int CAR = 1000;
    public static final int MAP = 1001;
    public static final int COLLISION = 1002;
    public static final int ROOM = 1003;
	
	

}
