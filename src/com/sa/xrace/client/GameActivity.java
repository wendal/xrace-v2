///:~
/**
 * XRace V2
 * <p/>Use for ... ...
 * <p/>ID: $Id$
 * Last Commit:  $Author$
 * @version $Revision: $
 * 
 */
package com.sa.xrace.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;

import com.sa.xrace.client.listener.ServerListenerImp;
import com.sa.xrace.client.manager.PostManagerClientImp;
import com.sa.xrace.client.math.Point3f;
import com.sa.xrace.client.model.ModelInforPool;
import com.sa.xrace.client.pool.CarInforClient;
import com.sa.xrace.client.pool.GIPool;
import com.sa.xrace.client.pool.InforPoolClient;
import com.sa.xrace.client.pool.WRbarPool;
import com.sa.xrace.client.scene.Camera;
import com.sa.xrace.client.scene.GLWorld;
import com.sa.xrace.client.toolkit.DataToolKit;
import com.sa.xrace.client.toolkit.NetworkToolKit;
import com.sa.xrace.client.toolkit.ObjectPool;

public class GameActivity extends Activity implements SensorListener {
//	private final static String TAG = "----- GameActivity -----";
	
//	private GameView drawView;
	private Camera mCamera;

//	private ModelImport mModelImport;
	private ModelInforPool mModelInforPool;

	private InforPoolClient inPool;
	// private InforPoolClient mPool;
//	private RoomPicPool rpPool;
//	private WRbarPool barPool;
//	private GIPool giPool;

	private GLWorld mWorld;

//	public SensorManager mSensorManager ;
//	private ServerListenerImp mServerListener;
//	private PostManagerClient mPostManager;

//	private RelativeLayout layout;
//	private Intent incomeIntent;

//	private Socket mSocket;
//	private String IP = "";
//	private String NAME = "";
//	private final static int PORT = 4444;

	

//	private  static final float DISTANCE = 600.0f;	//distance between the car and camera when car's stop
//	private  static final float CAMERA_EYE_Y = 110.0f;	//distance between the car and camera when car's stop
	
//	private  int modelID = 0;
	public static boolean isLogin = false;
	public static boolean isCarType = false;
	private static boolean isPostStart = false;
	public static boolean isStart = false;
//	private IntBuffer tempIB;

//	private long timeadd = 0;
//	private static long timeElapsed = 0;
//	private static long nowTime = 0;
//	private static long lastTime = 0;

	public static boolean mapOn = false;
	public static boolean mapNext = false;
	public static boolean mapBack = false;
	public static boolean carOn = false;
	public static boolean carNext = false;
	public static boolean carBack = false;
//	private static boolean raceOn = false;
	
	public static boolean testPosition = false;
	
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		if(this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		Log.e("-----------getRequestedOrientation",""+getRequestedOrientation());
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//将部分共用对象写入对象池
		if(ObjectPool.activity == null){
			ObjectPool.activity = this;
		}
		if(ObjectPool.resources == null){
			ObjectPool.resources = getResources();
		}
		if(ObjectPool.assetManager == null){
		    ObjectPool.assetManager = getAssets();
		}
		
		try {
		    //将Socket加入对象池
			ObjectPool.mSocket = new Socket(InetAddress.getByName(NetworkToolKit.SERVERIP), NetworkToolKit.SERVERPORT);
		} catch (UnknownHostException e) {
			Log.v("-- UnknownHostException --","-- UnknownHostException --");
			e.printStackTrace();
			//是否应该退出呢?
		} catch (IOException e) {
			Log.v("-- IOException --","-- IOException --");
			e.printStackTrace();
			//是否应该退出呢?
		}
		//		Log.i("GameActivity", "After connect socket");
		
		// Connect to the OpentIntents for simulator of the sensor
//		OpenIntents.requiresOpenIntents(this);
//		Hardware.mContentResolver = getContentResolver();
//		mSensorManager = (SensorManager) getSystemService("sensor");
//		Log.e(getClass().getName(), ""+mSensorManager);
//		SensorManagerSimulator.connectSimulator();

//		DataUnti.init(this);

//		incomeIntent = getIntent();
//		String temp = Uri.decode(incomeIntent.getData().toString());
//		StringTokenizer st = new StringTokenizer(temp, "@&");
//		NAME = NetworkToolKit.NAME;
//		IP = NetworkToolKit.SERVERIP;

		mModelInforPool = new ModelInforPool(new Point3f(0, 0, -3.0f));
		//将mModelInforPool加入对象池
		ObjectPool.mModelInforPool = mModelInforPool;
//		mModelImport = new ModelImport();
		mCamera = new Camera();
		
		inPool = new InforPoolClient();
		//将inPoolClient加入对象池
		ObjectPool.inPoolClient = inPool;
		//将WRbarPool加入对象池
		ObjectPool.barPool = new WRbarPool();
//		RoomPicPool rpPool = new RoomPicPool(this);
		//将RoomPicPool加入对象池
//		ObjectPool.rpPool = new RoomPicPool();
		ObjectPool.giPool = new GIPool(mCamera);

//		Socket mSocket = null;
		

		//把PostManagerClientImp加入对象池
		ObjectPool.mPostManager  = new PostManagerClientImp();
//		mServerListener = 
		    new ServerListenerImp();
		mWorld = new GLWorld( mCamera);
		//把GLWorld加入对象池
		ObjectPool.mWorld = mWorld;
		inPool.getOneCarInformation(inPool.getMyCarIndex()).setNName(NetworkToolKit.NAME);

		mModelInforPool.setType(DataToolKit.CAR);
		inPool.getOneCarInformation(inPool.getMyCarIndex()).setModel(mModelInforPool.getCurrentModel());
	
		GameView drawView = new GameView(getApplication());
		setContentView(drawView);
		System.gc();
//		System.out.println(Runtime.getRuntime().freeMemory());
		Log.i("GameActivity", "Finish onCreate(), Free Mem: " + Runtime.getRuntime().freeMemory());
	}

//	public boolean onTouchEvent(MotionEvent event) {	
//			switch(event.getAction()){
//				case MotionEvent.ACTION_DOWN:{
//					//the map choice button
//						if(mapOn == false &&
//						   event.getX()>30f &&
//						   event.getX()<110f &&
//						   event.getY()>270f &&
//						   event.getY()<290f)
//								mapOn = true;
//					//the race button
//						if(raceOn == false &&
//						   event.getX()>365f &&
//						   event.getX()<445f &&
//						   event.getY()>270f &&
//						   event.getY()<290f)
//							raceOn = true;
//
//					//the right_down car choice button	
//						else if(mapOn == false &&
//								carOn == false &&
//								event.getX()>370f &&
//								  event.getX()<420f &&
//								  event.getY()>195f &&
//								  event.getY()<215f)
//						{
//							mModelInforPool.setType(Model.CAR);
//							carOn = true;
//						}
//					//the right_up car choice button
//						else if(mapOn == false &&
//								carOn == false &&
//								event.getX()>370f &&
//								  event.getX()<420f &&
//								  event.getY()>95f &&
//								  event.getY()<115f)
//						{
//							mModelInforPool.setType(Model.CAR);
//							carOn = true;
//						}
//					//the left_down car choice button
//						else if(mapOn == false &&
//								carOn == false &&
//								event.getX()>170f &&
//								  event.getX()<220f &&
//								  event.getY()>195f &&
//								  event.getY()<215f)
//						{
//							mModelInforPool.setType(Model.CAR);
//							carOn = true;
//						}
//					//the left_up car choice button
//						else if(mapOn == false &&
//								carOn == false &&
//								event.getX()>170f &&
//								  event.getX()<220f &&
//								  event.getY()>95f &&
//								  event.getY()<115f)
//						{
//							mModelInforPool.setType(Model.CAR);
//							carOn = true;
//						}
//					//hen car quit button
//						else if( carOn == true &&
//								event.getX()>330f &&
//								event.getX()<380f &&
//								event.getY()>195f &&
//								event.getY()<225f)
//						{
////							inPool.getOneCarInformation(inPool.getMyCarIndex()).setModel(mModelInforPool.getCurrentModel());
//							mPostManager.sendCarTypePostToServer(); 
//							carOn = false;
//						}
//						
//					//the map quit button
//						else if( 
//								mapOn == true &&
//								event.getX()>330f &&
//								event.getX()<380f &&
//								event.getY()>195f &&
//								event.getY()<225f)
//								mapOn = false;
//						
//					//the next map button
//						else if( mapOn == true &&
//								event.getX()>240f &&
//								event.getX()<265f &&
//								event.getY()>210f &&
//								event.getY()<225f)
//								mapNext = true;
//						
//					//the previous map button
//						else if( mapOn == true &&
//								event.getX()>170f &&
//								event.getX()<195f &&
//								event.getY()>210f &&
//								event.getY()<225f)
//								mapBack = true;
//						
//						//the next car button
//						else if( carOn == true &&
//								event.getX()>240f &&
//								event.getX()<265f &&
//								event.getY()>210f &&
//								event.getY()<225f)
//						{
//							mModelInforPool.updateCurrentModel(false);
//							inPool.getOneCarInformation(inPool.getMyCarIndex()).setModel(mModelInforPool.getCurrentModel());
//							carNext = true;
//							carBack = false;
//						}
//					//the previous car button
//						else if( carOn == true &&
//								event.getX()>170f &&
//								event.getX()<195f &&
//								event.getY()>210f &&
//								event.getY()<225f)
//						{
//							mModelInforPool.updateCurrentModel(true);
//							inPool.getOneCarInformation(inPool.getMyCarIndex()).setModel(mModelInforPool.getCurrentModel());
//							carBack = true;
//							carNext = false;
//						}
//					}
//				}		
//		return super.onTouchEvent(event);
//	}
	

//	public Bitmap getBitmap(int resID){
//		Bitmap tem = BitmapFactory.decodeResource(getResources(),resID);
//		return tem;
//	}

	public boolean onKeyDown(int arg0, KeyEvent arg1) 
	{
	
		if (GLThread_Room.mPhase == DataToolKit.GAME_ROOM)
		{
			onRoomWaiting(arg0, arg1);
		}
		else if (GLThread_Room.mPhase == DataToolKit.GAME_RUNNING)
		{
			onGameRunning(arg0, arg1);
		}
		return super.onKeyDown(arg0, arg1);
	}

	private void onRoomWaiting(int arg0, KeyEvent arg1)
	{
		switch (arg0)
		{
			case KeyEvent.KEYCODE_DPAD_LEFT: 
				if(carOn == true)
				{
				/////////////////////////////////////////
				mModelInforPool.updateCurrentModel(true);
				/////////////////////////////////////////
				
				carBack = true;
				carNext = false;
				}
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				if(carOn == true)
				{
				/////////////////////////////////////////
				mModelInforPool.updateCurrentModel(false);
				/////////////////////////////////////////
				carNext = true;
				carBack = false;
				}
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				//mModelInforPool.setType(TYPE_CAR);
				inPool.getOneCarInformation(inPool.getMyCarIndex()).setModel(mModelInforPool.getCurrentModel());
				
				inPool.getOneCarInformation(inPool.getMyCarIndex()).generateAABBbox();
				ObjectPool.mPostManager.sendCarTypePostToServer();
				carOn = false;
				break;				
			case KeyEvent.KEYCODE_DPAD_UP:
				
				mModelInforPool.setType(DataToolKit.CAR);
				
				carOn = true;
				break;
			case KeyEvent.KEYCODE_ENTER:	
				mModelInforPool.setTypeAndUpdate(DataToolKit.CAR, GameActivity.carBack);
				
				if(inPool.getOneCarInformation(inPool.getMyCarIndex()).getModel() == null)
				{
//					mModelInforPool.setType(Model.CAR);
					inPool.getOneCarInformation(inPool.getMyCarIndex()).setModel(mModelInforPool.getCurrentModel());
//					inPool.getOneCarInformation(inPool.getMyCarIndex()).generateAABBbox();
				}else
				{		
				inPool.getOneCarInformation(inPool.getMyCarIndex()).setModel(mModelInforPool.getCurrentModel());
//				inPool.getOneCarInformation(inPool.getMyCarIndex()).generateAABBbox();
				}
//				mPostManager.sendCarTypePostToServer();
				if(!isPostStart)
				{
				    ObjectPool.mPostManager.sendStartPostToServer();
					isPostStart = true;
				}				
//				GLThread_Room.setPhase(GLThread_Room.GAME_RUNNING);	
//				CarInforClient myCar = inPool.getOneCarInformation(inPool.getMyCarIndex());
//				Point3f center = new Point3f(myCar.getNXPosition(), Camera.CAMERA_CENTER_Y, myCar.getNYPosition());
//				mCamera.initCamera(center, new Point3f(0.0f, 1.0f, 0.0f), myCar.getNDirection(), Camera.FAR_DISTANCE);
//				mCamera.setEye(mCamera.getEye().x, Camera.CAMERA_EYE_Y, mCamera.getEye().z);	
				break;				
		}
	}
	
	public void initGameRunning(){
		GLThread_Room.mPhase = DataToolKit.GAME_RUNNING;	
		CarInforClient myCar = inPool.getOneCarInformation(inPool.getMyCarIndex());
		Point3f center = new Point3f(myCar.getNXPosition(), DataToolKit.CAMERA_CENTER_Y, myCar.getNYPosition());
		mCamera.initCamera(center, new Point3f(0.0f, 1.0f, 0.0f), myCar.getNDirection(), DataToolKit.FAR_DISTANCE);
		mCamera.setEye(mCamera.getEye().x, DataToolKit.CAMERA_EYE_Y, mCamera.getEye().z);	
	}
	   
    private void onGameRunning(int arg0, KeyEvent arg1)
    {
    	CarInforClient car =  inPool.getOneCarInformation(inPool.getMyCarIndex());
    	switch (arg0)
		{
			case KeyEvent.KEYCODE_SPACE:
				if (car.getNSpeed() > 1.0f)
				{
					car.setNSpeed(car.getNSpeed()-2.0f);
				}
				else if (car.getNSpeed() < -1.0f)
				{
					car.setNSpeed(car.getNSpeed()+2.0f);
				}
				else
				{
					car.setNSpeed(0);
				}
				break;
			case KeyEvent.KEYCODE_N:
				mCamera.setDistance(mCamera.getDistance()+10.0f);
				break;
			case KeyEvent.KEYCODE_M:
				mCamera.setDistance(mCamera.getDistance()-10.0f);
				break;
			case KeyEvent.KEYCODE_U:
				mCamera.setEye(mCamera.getEye().x, mCamera.getEye().y + 10.0f, mCamera.getEye().z);
				break;
			case KeyEvent.KEYCODE_D:
				mCamera.setEye(mCamera.getEye().x, mCamera.getEye().y - 10.0f, mCamera.getEye().z);
				break;
			case KeyEvent.KEYCODE_B:
				car.setLookDirction(CarInforClient.LOOK_BACK);
				break;
			case KeyEvent.KEYCODE_T:
				testPosition = true;
				break;
				
//{{for testing
			case KeyEvent.KEYCODE_DPAD_UP:
				if(mWorld.isBeginWait){
					car.setSpeedKeyState(CarInforClient.SPEED_UP_KEYBOARD);
				}
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				if(mWorld.isBeginWait){
					car.setSpeedKeyState(CarInforClient.SPEED_DOWN_KEYBOARD);
				}
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				if(mWorld.isBeginWait){
					car.setDirectionKeyState(CarInforClient.DIRECTION_LEFT_KEYBOARD);
				}
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				if(mWorld.isBeginWait){
					car.setDirectionKeyState(CarInforClient.DIRECTION_RIGHT_KEYBOARD);
				}
				break;

//}}for testing
		}
    }

    public boolean onKeyUp(int arg0, KeyEvent arg1) 
    {
    	if (GLThread_Room.mPhase == DataToolKit.GAME_RUNNING)
		{
    		CarInforClient car =  inPool.getOneCarInformation(inPool.getMyCarIndex());
    		switch (arg0)
    		{
				case KeyEvent.KEYCODE_DPAD_LEFT:
					car.setDirectionKeyState(CarInforClient.NO_KEY_EVENT);
					break;
				case KeyEvent.KEYCODE_DPAD_RIGHT:
					car.setDirectionKeyState(CarInforClient.NO_KEY_EVENT);
					break;
				case KeyEvent.KEYCODE_DPAD_UP:
					car.setSpeedKeyState(CarInforClient.NO_KEY_EVENT);
					break;
				case KeyEvent.KEYCODE_DPAD_DOWN:
					car.setSpeedKeyState(CarInforClient.NO_KEY_EVENT);
					break;
				case KeyEvent.KEYCODE_B:
					car.setLookDirction(CarInforClient.LOOK_FRONT);
					break;
				case KeyEvent.KEYCODE_T:
					testPosition = false;
					break;
    		}
		}		
    	return super.onKeyUp(arg0, arg1);
    }
//}}for testing
    
	/**
	 * Called when the activity will start interacting with the user.
	 * </p>
	 * Register the Sensor Manager and enable the MainActivity.
	 */
	protected void onResume() {
		super.onResume();
//		mSensorManager.registerListener(this, SensorManager.SENSOR_ORIENTATION,
//				SensorManager.SENSOR_DELAY_GAME);
	}

	/**
	 * Called when the activity is no longer visible to the user. Remove the
	 * Listener of the SensorManager
	 */
	protected void onStop() {
//		mSensorManager.unregisterListener(this);
		super.onStop();
	}
	protected void onDestroy(){
		finish();
		super.onDestroy();
	}
	/**
	 * The method of the interface SensorListener
	 * 
	 * @see android.hardware.SensorListener#onSensorChanged(int, float[])
	 */
	public void onSensorChanged(int sensor, float[] values) {
//		nowTime = System.currentTimeMillis();
//		if(lastTime ==0){
//			lastTime =  nowTime;
//		}
//		timeElapsed = nowTime - lastTime;
//		lastTime = nowTime;
//		if(isStart){
//			timeadd += timeElapsed;
//			if(timeadd >=30){
//				mPostManager.sendNormalPostToServer();
//				timeadd=0;
//			}
//		}
		switch (sensor) 
		{
			case SensorManager.SENSOR_ORIENTATION: 
			{			
				//{{sliao
				CarInforClient car =  inPool.getOneCarInformation(inPool.getMyCarIndex());
				
				InforPoolClient.mSensor[0] = values[2];// acceleration			
				if (values[2] > 0.01f)
				{
					car.setSpeedSensorState(CarInforClient.SPEED_UP_SENSOR);
				}
				else if (values[2] < -0.01f)
				{
					car.setSpeedSensorState(CarInforClient.SPEED_DOWN_SENSOR);
				}
				else
				{
					car.setSpeedSensorState(CarInforClient.NO_SENSOR_EVENT);
				}
				if (car.getNSpeed() > CarInforClient.PRECISION)	//when the car's speed is bigger than zero
				{
					InforPoolClient.mSensor[1] = -values[1];// changeDirection
				}
				else if (car.getNSpeed() < -CarInforClient.PRECISION)	//when the car's speed is smaller than zero
				{
					InforPoolClient.mSensor[1] = values[1];// changeDirection
				}
				
				if ( -values[1] > 0.01f)
				{
					car.setDirectionSensorState(CarInforClient.DIRECTION_LEFT_SENSOR);
				}
				else if (-values[1] < -0.01f)
				{
					car.setDirectionSensorState(CarInforClient.DIRECTION_RIGHT_SENSOR);
				}
				else
				{
					car.setDirectionSensorState(CarInforClient.NO_SENSOR_EVENT);
				}
				//}}sliao
				break;
			}
		}
	}

	/**
	 * The method of the interface SensorListener
	 * 
	 * @see android.hardware.SensorListener#onSensorChanged(int, float[])
	 */

	public void onAccuracyChanged(int arg0, int arg1) {

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#getRequestedOrientation()
	 */
	@Override
	public int getRequestedOrientation() {
		// 
		return super.getRequestedOrientation();
	}

}
