///:~
/**
 * XRace V2
 * <p/>Use for ... ...
 * <p/>ID: $Id: $
 * Last Commit:  $Author: $
 * @version $Revision: $
 * 
 */
package com.sa.xrace.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.RelativeLayout;

//import com.sa.xrace.client.listener.ServerListener;
import com.sa.xrace.client.listener.ServerListenerImp;
import com.sa.xrace.client.loader.LocationObj;
import com.sa.xrace.client.loader.ModelObj;
import com.sa.xrace.client.loader.SenceObj;
import com.sa.xrace.client.loader.SenceParser2;
import com.sa.xrace.client.manager.PostManagerClient;
import com.sa.xrace.client.manager.PostManagerClientImp;
import com.sa.xrace.client.math.Point3f;
import com.sa.xrace.client.model.Model;
import com.sa.xrace.client.model.ModelImport;
import com.sa.xrace.client.model.ModelInforPool;
import com.sa.xrace.client.model.t3DModel;
import com.sa.xrace.client.pool.CarInforClient;
import com.sa.xrace.client.pool.GIPool;
import com.sa.xrace.client.pool.InforPoolClient;
import com.sa.xrace.client.pool.RoomPicPool;
import com.sa.xrace.client.pool.WRbarPool;
import com.sa.xrace.client.scene.Camera;
import com.sa.xrace.client.scene.GLWorld;
import com.sa.xrace.client.scene.Object;

public class GameActivity extends Activity implements SensorListener {
	public final static String TAG = "----- GameActivity -----";
	
	private GameView drawView;
	private Camera mCamera;

	public ModelImport mModelImport;
	public ModelInforPool mModelInforPool;

	private InforPoolClient inPool;
	// private InforPoolClient mPool;
	private RoomPicPool rpPool;
	private WRbarPool barPool;
	private GIPool giPool;

	public GLWorld mWorld;

//	public SensorManager mSensorManager ;
	public ServerListenerImp mServerListener;
	public PostManagerClient mPostManager;

	RelativeLayout layout;
	Intent incomeIntent;

	public Socket mSocket;
	public  String IP = "";
	public  String NAME = "";
	public final static int PORT = 4444;

	

    public static final float DISTANCE = 600.0f;	//distance between the car and camera when car's stop
    public static final float CAMERA_EYE_Y = 110.0f;	//distance between the car and camera when car's stop
	
	public int modelID = 0;
	public static boolean isLogin = false;
	public static boolean isCarType = false;
	private static boolean isPostStart = false;
	public static boolean isStart = false;
	public IntBuffer tempIB;

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
	public static boolean raceOn = false;
	
	public static boolean testPosition = false;
	
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		if(this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		Log.e("-----------getRequestedOrientation",""+getRequestedOrientation());
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Connect to the OpentIntents for simulator of the sensor
//		OpenIntents.requiresOpenIntents(this);
//		Hardware.mContentResolver = getContentResolver();
//		mSensorManager = (SensorManager) getSystemService("sensor");
//		Log.e(getClass().getName(), ""+mSensorManager);
//		SensorManagerSimulator.connectSimulator();

		

		incomeIntent = getIntent();
		String temp = Uri.decode(incomeIntent.getData().toString());
		StringTokenizer st = new StringTokenizer(temp, "@&");
		NAME = st.nextToken();
		IP = st.nextToken();

		mModelInforPool = new ModelInforPool(new Point3f(0, 0, -3.0f));
		mModelImport = new ModelImport();
		mCamera = new Camera();
		inPool = new InforPoolClient(mModelInforPool);
		barPool = new WRbarPool(null, null, null, inPool, this);
		rpPool = new RoomPicPool(this);
		giPool = new GIPool(rpPool,inPool,mCamera);

		try {
			mSocket = new Socket(InetAddress.getByName(IP), PORT);
		} catch (UnknownHostException e) {
			Log.v("-- UnknownHostException --","-- UnknownHostException --");
			e.printStackTrace();
		} catch (IOException e) {
			Log.v("-- IOException --","-- IOException --");
			e.printStackTrace();
		}
		Log.i("GameActivity", "After connect socket");
		mPostManager = new PostManagerClientImp(mSocket, inPool);
		mServerListener = new ServerListenerImp(mSocket, inPool, barPool,this);
		mWorld = new GLWorld(inPool, mCamera, mPostManager);
		inPool.getOneCarInformation(inPool.getMyCarIndex()).setNName(NAME);

		mModelInforPool.setType(Model.CAR);
		inPool.getOneCarInformation(inPool.getMyCarIndex()).setModel(mModelInforPool.getCurrentModel());
	
		drawView = new GameView(getApplication(), this, rpPool, inPool, barPool,giPool, mModelInforPool, mWorld,mPostManager);
		setContentView(drawView);
		System.gc();
		System.out.println(Runtime.getRuntime().freeMemory());
		Log.i("GameActivity", "Finish onCreate()");
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
	

	public Bitmap getBitmap(int resID){
		Bitmap tem = BitmapFactory.decodeResource(getResources(),resID);
		return tem;
	}

	public ByteBuffer getImageReadyfor(int resname) {
		ByteBuffer tempBuffer;
		Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), resname);
		int pic_width = mBitmap.getWidth();
		int pic_height = mBitmap.getHeight();
		// Log.e("pic_width",""+pic_width+" "+pic_height);
		tempBuffer = ByteBuffer.allocateDirect(pic_width * pic_height * 4);
		tempBuffer.order(ByteOrder.nativeOrder());
		tempIB = tempBuffer.asIntBuffer();

		for (int y = 0; y < pic_width; y++) {
			for (int x = 0; x < pic_height; x++) {
				tempIB.put(mBitmap.getPixel(x, y));
			}
		}

		for (int i = 0; i < pic_width * pic_height * 4; i += 4) {
			byte temp = tempBuffer.get(i);
			tempBuffer.put(i, tempBuffer.get(i + 2));
			tempBuffer.put(i + 2, temp);

		}
		tempBuffer.position(0);
		return tempBuffer;
	}

	public void LoadMapFromXML(String filename) {
		long start = System.currentTimeMillis();
		InputStream fis;
		DataInputStream dis;
		t3DModel t3Dmodel;
		Model model;
		Object object;

		SenceParser2 senceParser;
		SenceObj sence;

		try {
			//处理XML,并统计时间
			long xml_start = System.currentTimeMillis();
			fis = getAssets().open(filename);
//			Document document = new SAXReader()
//					.read(new InputStreamReader(fis));
			senceParser = new SenceParser2(fis);
			sence = senceParser.getScene();
			Log.i("Time in XML parse", ""+(System.currentTimeMillis() - xml_start));
			
			ArrayList<ModelObj> modelList = sence.getLModelList();
			ModelObj modelObj = null;
			LocationObj locationObj = null;

			for (int i = 0; i < modelList.size(); i++) {
				//处理图片,并统计时间,原本的时间为 17700ms
				long image_start = System.currentTimeMillis();
				modelObj = (ModelObj) modelList.get(i);
				fis = getAssets().open(modelObj.getFilename());
				dis = new DataInputStream(fis);
				t3Dmodel = new t3DModel(this);
				mModelImport.import3DS(t3Dmodel, dis);
				model = new Model(Integer.parseInt(modelObj.getID()), Integer
						.parseInt(modelObj.getType()), t3Dmodel, modelObj
						.getScale(), modelObj.getRadius());
				mModelInforPool.addModel(model);
				locationObj = modelObj.getLocation();
				for (int index = 0; index < locationObj.getSize(); index++) {
					object = new Object(model, locationObj.getPoint(index),
							locationObj.getAngle(index));
					if (Integer.parseInt(modelObj.getType()) == Model.COLLISION)
					{
						object.updateTransformMatrix();
					}
					mWorld.addObject(object);
				}
			Log.i("Time in Image parse", modelObj.getFilename()+" "+(System.currentTimeMillis() - image_start));
			}
			//
			long gcm_time = System.currentTimeMillis();
			mWorld.generateCollisionMap();
			Log.i("Time in generateCollisionMap", ""+(System.currentTimeMillis() - gcm_time));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// 
			e.printStackTrace();
		} catch (SAXException e) {
			// 
			e.printStackTrace();
		} finally {
			sence = null;
			senceParser = null;

		}
		Log.i("Load Image Time", ""+(System.currentTimeMillis() - start));
	}

	public boolean onKeyDown(int arg0, KeyEvent arg1) 
	{
	
		if (GLThread_Room.getPhase() == GLThread_Room.GAME_ROOM)
		{
			onRoomWaiting(arg0, arg1);
		}
		else if (GLThread_Room.getPhase() == GLThread_Room.GAME_RUNNING)
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
				mPostManager.sendCarTypePostToServer();
				carOn = false;
				break;				
			case KeyEvent.KEYCODE_DPAD_UP:
				
				mModelInforPool.setType(Model.CAR);
				
				carOn = true;
				break;
			case KeyEvent.KEYCODE_ENTER:	
				mModelInforPool.setTypeAndUpdate(Model.CAR, GameActivity.carBack);
				
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
					mPostManager.sendStartPostToServer();
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
		GLThread_Room.setPhase(GLThread_Room.GAME_RUNNING);	
		CarInforClient myCar = inPool.getOneCarInformation(inPool.getMyCarIndex());
		Point3f center = new Point3f(myCar.getNXPosition(), Camera.CAMERA_CENTER_Y, myCar.getNYPosition());
		mCamera.initCamera(center, new Point3f(0.0f, 1.0f, 0.0f), myCar.getNDirection(), Camera.FAR_DISTANCE);
		mCamera.setEye(mCamera.getEye().x, Camera.CAMERA_EYE_Y, mCamera.getEye().z);	
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
    	if (GLThread_Room.getPhase() == GLThread_Room.GAME_RUNNING)
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
