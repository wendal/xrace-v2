package com.sa.xrace.client;

//import org.openintents.OpenIntents;
//import org.openintents.hardware.SensorManagerSimulator;
//import org.openintents.provider.Hardware;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

/**
 * @author jlin
 * @version $Id: SingleGame.java,v 1.3 2008-12-04 07:41:32 cpan Exp $
 */

public class SingleGame extends Activity implements SensorListener{
    /** Called when the activity is first created. */
	Sprite sprite_car,sprite_back,sprite_car2;
	Bitmap plane_img,dst_img,back_img,road_img;
	MyView view;
	private boolean isDown,isUp,isRight,isLeft;
	float speed_p=5.0f;
//	private SensorManager mSensorManager;
	public float lastDirection,nowDirection;
	public boolean isBegin = true;
	public int plane_w,plane_h ,dst_w_h,directionType ;
	float planeX=220,planeY=100;
	public float spead = 5,spead_X,spead_Y,car_speed=4.0f;
	public boolean sign;
	public float degree = 0;
	public double radian;
	float car_speedX, car_speedY;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = new MyView(this);
        loadImage(); 
        sprite_car=  new Sprite(makeDst(degree),dst_w_h,dst_w_h,planeX,planeY); 
        sprite_car2=  new Sprite(makeDst(degree),dst_w_h,dst_w_h,130,100); 
        sprite_back = new Sprite(back_img,back_img.getWidth(),back_img.getHeight(),0,0);
        
        
//    	OpenIntents.requiresOpenIntents(this);
//		Hardware.mContentResolver = getContentResolver();
//		mSensorManager = (SensorManager) new SensorManagerSimulator(
//				(SensorManager) getSystemService(SENSOR_SERVICE));
//		SensorManagerSimulator.connectSimulator();
		
		setContentView(view);
    }
    public void loadImage(){
    	plane_img = BitmapFactory.decodeResource(getResources(), R.drawable.car);
    	plane_w = plane_img.getWidth();
    	plane_h = plane_img.getHeight();
    	dst_w_h = (int)(Math.hypot(plane_w, plane_h));   // dst_w_h = 69
    	
    	back_img = BitmapFactory.decodeResource(getResources(), R.drawable.road3);
    	road_img = BitmapFactory.decodeResource(getResources(), R.drawable.roadback);
    }
    
    public Bitmap makeDst(float temdegree) {
        Bitmap bm = Bitmap.createBitmap(dst_w_h, dst_w_h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        c.save();
        c.rotate(temdegree,dst_w_h/2,dst_w_h/2);
        c.drawBitmap(plane_img, (dst_w_h-plane_w)/2, (dst_w_h-plane_h)/2, p);
        c.restore();
        return bm;
    }
    
    public void onSensorChanged(int sensor, float[] values) {
		
		switch (sensor) {
			case 0:{
				break;
			}
			case SensorManager.SENSOR_ORIENTATION:
			{
				if(values[1]!=0){
					degree = (float) (values[1] / 20) + degree;
					dst_img = makeDst(degree);
					sprite_car.setImage(dst_img);
//					if(sprite_car.collideWith_Sprite(sprite_back, 0)||sprite_car.collideWith_Sprite(sprite_back, 1)
//					||sprite_car.collideWith_Sprite(sprite_back, 2)||sprite_car.collideWith_Sprite(sprite_back, 3)){
//						
//						degree = temdegree;
//						dst_img = makeDst(degree);
//						sprite_car.setImage(dst_img);
//					}
				}
				
				car_speed = values[2]/20;
				break;
			}

		}
    }
    public void onAccuracyChanged(int arg0, int arg1) {
		
	}
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode){
			case KeyEvent.KEYCODE_DPAD_UP:
			case KeyEvent.KEYCODE_W:{
				isUp = true;
				directionType = 0;
				break;	
			}
			
			case KeyEvent.KEYCODE_DPAD_DOWN:
			case KeyEvent.KEYCODE_S:{
				isDown = true;
				directionType = 1;
				break;
			}
			
			case KeyEvent.KEYCODE_DPAD_LEFT:
			case KeyEvent.KEYCODE_A:{
				isLeft = true;
				directionType = 2;
				break;	
			}
			case KeyEvent.KEYCODE_DPAD_RIGHT:
			case KeyEvent.KEYCODE_D:{
				isRight = true;
				directionType = 3;
				break;	
			}
			case KeyEvent.KEYCODE_M:{
//				dst_rgb = new int[60*60];
//				dst_img.getPixels(dst_rgb, 0, 60, 0, 0, 60, 60);
//				for(int i=0;i<dst_rgb.length;i++){
//					Log.v("rgb__"+i,"== "+dst_rgb[i]);
//				}
				break;	
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch(keyCode){
			case KeyEvent.KEYCODE_DPAD_DOWN:
			case KeyEvent.KEYCODE_S:{
				isDown = false;
				break;
			}
			case KeyEvent.KEYCODE_DPAD_UP:
			case KeyEvent.KEYCODE_W:{
				isUp = false;
				break;	
			}
			case KeyEvent.KEYCODE_DPAD_LEFT:
			case KeyEvent.KEYCODE_A:{
				isLeft = false;
				break;	
			}
			case KeyEvent.KEYCODE_DPAD_RIGHT:
			case KeyEvent.KEYCODE_D:{
				isRight = false;
				break;	
			}
			
			case KeyEvent.KEYCODE_T:{
				break;
			}
		}
		return super.onKeyUp(keyCode, event);
	}
	
    class MyView extends View{

    	Paint paint;
    	
		public MyView(Context context) {
			super(context);
			paint = new Paint();
		}
		public void onDraw(Canvas canvas){
			canvas.drawColor(Color.WHITE);
			logicmove(canvas);
			canvas.drawBitmap(road_img, sprite_back.x, sprite_back.y,paint);
 			sprite_back.paint(canvas);
 			sprite_car.paint(canvas);
 			sprite_car2.paint(canvas);
			invalidate();
		}
		 public void logicmove(Canvas canvas){
			 
			 radian = Math.toRadians(degree);
	         car_speedX = (float)(car_speed*Math.sin(radian));
	         car_speedY = (float)(car_speed*Math.cos(radian));
	
	         
	         //****************************************************************************
	         
	        if(car_speedX>0){
	        	if(!sprite_car.collideWith_Sprite(sprite_back, 3)&& !sprite_car.collidesWith(sprite_car2, true)){   // 右
	        		sprite_back.move(-car_speedX,0);
	        		sprite_car2.move(-car_speedX,0);
	        	}
	        }
	        if(car_speedX<0){
	        	if(!sprite_car.collideWith_Sprite(sprite_back, 2)&& !sprite_car.collidesWith(sprite_car2, true)){   //左
	        		sprite_back.move(-car_speedX, 0);
	        		sprite_car2.move(-car_speedX,0);
	        	}
	        }
	        if(car_speedY>0){
	        	if(!sprite_car.collideWith_Sprite(sprite_back, 0)&& !sprite_car.collidesWith(sprite_car2, true)){  // 上
	        		sprite_back.move(0, car_speedY);
	        		sprite_car2.move(0, car_speedY);
	        	}
	        }
	        if(car_speedY<0){
	        	if(!sprite_car.collideWith_Sprite(sprite_back, 1)&& !sprite_car.collidesWith(sprite_car2, true)){  // 下
	        		sprite_back.move(0, car_speedY);
	        		sprite_car2.move(0, car_speedY);
	        	}
	        }
	        
	         
// 			if(isUp){
// 				if(!sprite_car.collideWith_Sprite(sprite_back, 0)){  // 上
//// 					sprite_plane.move(0, -speed_p);
// 					sprite_back.move(0, speed_p);
// 				}
// 			}
// 			if(isDown){
// 				if(!sprite_car.collideWith_Sprite(sprite_back, 1)){   // 下
//// 					sprite_plane.move(0, speed_p);
// 					sprite_back.move(0, -speed_p);
// 				}
// 				
// 			}
// 			if(isLeft){
// 				if(!sprite_car.collideWith_Sprite(sprite_back, 2)){   // 左
//// 					sprite_plane.move(-speed_p, 0);
// 					sprite_back.move(speed_p,0 );
// 				}
// 				
// 			}
// 			if(isRight){
// 				if(!sprite_car.collideWith_Sprite(sprite_back, 3)){    // 友
//// 					sprite_plane.move(speed_p, 0);
// 					sprite_back.move(-speed_p, 0);
// 				}
// 			}
			 
			 

 	    }
    }
    
    protected void onResume() {
		super.onResume();
//		mSensorManager.registerListener(this, SensorManager.SENSOR_ORIENTATION ,
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
}