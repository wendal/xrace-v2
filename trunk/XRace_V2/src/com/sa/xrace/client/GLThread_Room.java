package com.sa.xrace.client;

import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGL11;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.SurfaceHolder;

import com.sa.xrace.client.manager.PostManagerClient;
import com.sa.xrace.client.model.Model;
import com.sa.xrace.client.model.ModelInforPool;
import com.sa.xrace.client.pool.GIPool;
import com.sa.xrace.client.pool.InforPoolClient;
import com.sa.xrace.client.pool.RoomPicPool;
import com.sa.xrace.client.pool.WRbarPool;
import com.sa.xrace.client.scene.GLWorld;

public final class GLThread_Room extends Thread {
	
	public static final int GAME_ROOM 	= 1;
	public static final int GAME_RUNNING = 3;  	   
	private static int mPhase = GAME_ROOM;
//	private boolean isInitGameFinished=false;
	private float cameraStep =1f; 
	private float cameraLimit=0f;
	
	private int mWidth;
	private int mHeight;
	private IntBuffer texturesB;
	boolean mDone;
	private RoomPicPool picPool;
	private GIPool giPool;
	private SurfaceHolder mHolder;
	private GameView callingView;
	private WRbarPool barPool;
	private ModelInforPool mModelContainer;
	private InforPoolClient inPool;
	private GLWorld mWorld;
	private GameActivity mActivity;
	public IntBuffer tempIB;
	public  Bitmap carMyB_img;
	public  boolean isModelGenerate = false;
	boolean done = false;
	public PostManagerClient mPostManager;
	public static boolean addBar =true;
	public static boolean loginFailure = false;
	public static byte bindex=0;
	
	public static int mTextureLoad = 1;
	private static int tem2[] = {0,512,512,-512};
	public static EGL10 egl;
	public static EGLDisplay dpy ;
	public static EGLSurface surface;
	public static GL10 gl;
	public static int progress ,startPro = 22;
	
	public static long nowTime = 0;
	public static long lastTime = 0;
	public static long timeElapsed = 0;
	public static long timeadd = 0;
	
	GLThread_Room(GameActivity activity,SurfaceHolder mHolder,GameView inputView,RoomPicPool picPool,
			WRbarPool barPool,GIPool giPool,ModelInforPool mModelContainer,GLWorld mWorld,InforPoolClient inPool,PostManagerClient mPostManager) {
		super();
		addBar =true;
		loginFailure = false;
		progress = 22;
		texturesB= IntBuffer.allocate(11);
		mDone =false;
		mWidth = 0;
		mHeight = 0;
		this.mHolder=mHolder;
		callingView = inputView;
		this.picPool = picPool;
		this.giPool = giPool;
		this.barPool = barPool;
		this.mModelContainer=mModelContainer;
		this.inPool = inPool;
		this.mWorld=mWorld;
		this.mActivity = activity;
		this.mPostManager = mPostManager;
		RoomPicPool.setLoading(mActivity.getImageReadyfor(R.drawable.laod));
		carMyB_img = mActivity.getBitmap(R.drawable.mysite);
	}
	
	public static int getPhase()
	{
		return mPhase;
	}
	
	public static void setPhase(int input)
	{
		mPhase =input;
	}

	public void run() {
		Log.i(getClass().getSimpleName(), "in run()");
		
		egl = (EGL10) EGLContext.getEGL();
		dpy = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
		int[] version = new int[2];
		egl.eglInitialize(dpy, version);
		int[] configSpec = { EGL10.EGL_DEPTH_SIZE, 16, EGL10.EGL_NONE };
		EGLConfig[] configs = new EGLConfig[1];
		int[] num_config = new int[1];
		egl.eglChooseConfig(dpy, configSpec, configs, 1, num_config);
		EGLConfig config = configs[0];
		EGLContext context = egl.eglCreateContext(dpy, config,EGL10.EGL_NO_CONTEXT, null);
		surface = egl.eglCreateWindowSurface(dpy, config,mHolder, null);
		egl.eglMakeCurrent(dpy, surface, surface, context);
		gl = (GL10) context.getGL();
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			    
	    initForGame(gl);
		getLoginTextureReady(gl);
		barPool.initWRbarPool(gl,picPool,texturesB);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		
		
		
		while (!mDone) {
			// Update the asynchronous state (window size, key events)
			int w, h;
			synchronized (this) {
				w = mWidth;
				h = mHeight;
			}
			
			
			if(!isModelGenerate){
				Loading(gl);
			}
			
			gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
			drawFrame(gl, w, h);
			
			egl.eglSwapBuffers(dpy, surface);
			
			if (egl.eglGetError() == EGL11.EGL_CONTEXT_LOST) {
				// we lost the gpu, quit immediately
				Context c = callingView.getContext();
				if (c instanceof Activity) {
					((Activity) c).finish();
				}
			}
			
		}

		egl.eglMakeCurrent(dpy, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE,
				EGL10.EGL_NO_CONTEXT);
		egl.eglDestroyContext(dpy, context);
		egl.eglDestroySurface(dpy, surface);
		egl.eglTerminate(dpy);
	}

	private void drawFrame(GL10 gl, int w, int h) {
		nowTime = System.currentTimeMillis();
		if(lastTime ==0)
		{
			lastTime =  nowTime;
		}
		timeElapsed = nowTime - lastTime;
		lastTime = nowTime;
		timeElapsed = InforPoolClient.timeFixing(timeElapsed);
		
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		if(loginFailure){
			giPool.drawLoginFailure(gl);
			mActivity.finish();
			
		}
		if(!addBar)
		{
			Log.e("----------------addBar-----------------","addBar");
			barPool.addBar_Login(bindex);			
			GameActivity.isLogin = true;
			addBar=true;
		}
		 switch (mPhase)
		 {
		 	case GAME_ROOM:
		 		
		 		drawGarage(gl,timeElapsed);
				barPool.drawOut();
				
				if(GameActivity.carOn)
				{
					drawCarSelection(gl);
				}
		 		break;
		 	case GAME_RUNNING:
		 		
		 		mWorld.draw(gl,timeElapsed);
		 		giPool.drawStarPoints(gl);
		 		giPool.drawDiameter(gl);
		 		giPool.drawMiniMap(gl);
		 		
		 		giPool.drawTimeCount(gl,mWorld);

		 		break;
		 }
			if(GameActivity.isStart){
				timeadd += timeElapsed;
				if(timeadd >=30){
					mPostManager.sendNormalPostToServer();
					timeadd=0;
				}
			}
	}
	
	

	/**
	 * 应该是控制进度条的
	 * @param gl
	 * @param numTime
	 * @param imgIndex
	 */
	
	
	public final static void makeLoading(GL10 gl,int numTime,int imgIndex) {
		for(int i=progress;i<numTime;i+=5,progress+=5){
			Bitmap bm = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);
			bm.eraseColor(0);
			Canvas c = new Canvas(bm);
			Paint p = new Paint();
			
			p.setAntiAlias(true);
			p.setTextSize(13);  
			p.setTextAlign(Paint.Align.CENTER);
			p.setStrokeWidth(9.0f);
			c.drawColor(Color.BLACK);
			switch(imgIndex){
				case 0:{
					c.drawBitmap(RoomPicPool.load0,0,192,p);
					break;
				}
				case 1:{
					c.drawBitmap(RoomPicPool.load1,0,192,p);
					break;
				}
				case 2:{
					c.drawBitmap(RoomPicPool.load2,0,192,p);
					break;
				}
				case 3:{
					c.drawBitmap(RoomPicPool.load3,0,192,p);
					break;
				}
				
			}
			
			p.setColor(Color.BLUE);
			c.drawLine(startPro, 488, progress, 488, p);
			p.setColor(Color.WHITE);
			c.drawText(""+(int)((progress-startPro)*1.0/438*100)+" %",240,492,p);
			
			gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureLoad);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bm, 0);
	
			// Reclaim storage used by bitmap and canvas.
			bm.recycle();
			bm = null;
			c = null;
			
			gl.glBindTexture(GL10.GL_TEXTURE_2D,mTextureLoad);
			((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,GL11Ext.GL_TEXTURE_CROP_RECT_OES, tem2, 0);
			((GL11Ext) gl).glDrawTexiOES(0,0, 0, 512, 512); 
			egl.eglSwapBuffers(dpy, surface);
		}
	}

	public void onWindowResize(int w, int h) {
		synchronized (this) {
			mWidth = w;
			mHeight = h;
		}
	}

	public void requestExitAndWait() {
		// don't call this from GLThread thread or it a guaranteed
		// deadlock!
		mDone = true;
		try {
			join();
		} catch (InterruptedException ex) {
		}
	}

	
//	public void destroy() {
//			// TODO Auto-generated method stub
//			super.destroy();
//	}
	
	private void Loading(GL10 gl){
		
		long start = System.currentTimeMillis();
		long first = start;
		Log.e("KKK@@@@@@@@@@@ "+(System.currentTimeMillis() -start),"begin Loading");
		makeLoading(gl,60,0);
		start = System.currentTimeMillis();
		
		picPool.generateEveryThing();            // 23s
		Log.e("KKK@@@@@@@@@@@ "+(System.currentTimeMillis() -start),"generateEveryThing");
		start = System.currentTimeMillis();
		
		giPool.makeAllInterface(gl);            // >1s
		makeLoading(gl,182,2);
		Log.e("KKK@@@@@@@@@@@ "+(System.currentTimeMillis() -start),"makeAllInterface");
		start = System.currentTimeMillis();
		
		mActivity.LoadMapFromXML("scene.xml");  //4.7s
		makeLoading(gl,242,2);
		Log.e("KKK@@@@@@@@@@@ "+(System.currentTimeMillis() -start),"LoadMapFromXML");
		start = System.currentTimeMillis();
		
//		long start = System.currentTimeMillis();
		mModelContainer.generate(gl);           // 49s
		Log.e("KKK@@@@@@@@@@@ "+(System.currentTimeMillis() -start),"mModelContainer.generate(gl);");
		start = System.currentTimeMillis();
		
		getCommonTextureReady(gl);                    // >1s
		makeLoading(gl,442,3);
		
		mModelContainer.setType(Model.CAR);
		inPool.getOneCarInformation(inPool.getMyCarIndex()).setModel(mModelContainer.getCurrentModel());
		if (!GameActivity.isLogin) {
			Log.e("----------------isLogin-----------------","isLogin");
			Log.v("sendLoginPostToServer", "sendLoginPostToServer");
			mPostManager.sendLoginPostToServer();
		}		
		makeLoading(gl,460,3);
		isModelGenerate = true;
		Log.e("Done Loading-->Time used: "+(System.currentTimeMillis() - first),"It shall be show Srceen now");
	}
	
	
	
	
	
	private void getLoginTextureReady(GL10 gl){
		gl.glGenTextures(11,texturesB);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(10));		
		gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, 256, 256, 0,
				GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE,
				RoomPicPool.getLoading());	
	}
	
	private void getCommonTextureReady(GL10 gl)
	{
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(4));
		Log.e("in getCommonTextureReady", "Step A getCommonTextureReady");
		gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, 256, 256, 0,
				GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE,
				RoomPicPool.getDown_PicB());
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(5));		
		gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, 256, 256, 0,
				GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE,
				RoomPicPool.getMapchoice_PicB());
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(6));		
		gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, 256, 256, 0,
				GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE,
				RoomPicPool.getMapview1_PicB());
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(7));		
		gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, 256, 256, 0,
				GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE,
				RoomPicPool.getMapview2_PicB());
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(8));		
		gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, 256, 256, 0,
				GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE,
				RoomPicPool.getCarchoice_PicB());
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(9));		
		gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, 256, 256, 0,
				GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE,
				RoomPicPool.getCarview1_PicB());	
		

		Log.e("in getCommonTextureReady", "Step B getCommonTextureReady");
		
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
				GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);				
		gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
				GL10.GL_MODULATE);
		Log.e("in getCommonTextureReady", "Finish getCommonTextureReady");
	}
	
	private void initForGame(GL10 gl)
	{
		   // Define the view frustrum
		   gl.glViewport(0, 0, callingView.getWidth(), callingView.getHeight());
		   gl.glMatrixMode(GL10.GL_PROJECTION);
		   gl.glLoadIdentity();
		   float ratio = (float) callingView.getWidth() / callingView.getHeight();
		   GLU.gluPerspective(gl, 45.0f, ratio, 1, 50000f); 
		   gl.glFrontFace(GL10.GL_CW);
		   gl.glShadeModel(GL10.GL_FLAT);
		   gl.glClearDepthf(1.0f);
		   gl.glEnable(GL10.GL_DEPTH_TEST);
		   gl.glDepthFunc(GL10.GL_LEQUAL);
		   gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		   gl.glEnable(GL10.GL_CULL_FACE);		
		   gl.glEnable(GL10.GL_TEXTURE_2D);
	       gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	       gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
	       gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);	       
	       
	       gl.glEnable(GL10.GL_SMOOTH_LINE_WIDTH_RANGE);
		   gl.glEnable(GL10.GL_SMOOTH_POINT_SIZE_RANGE);
		   gl.glEnable(GL10.GL_SMOOTH);
	      
	       gl.glEnable(GL10.GL_BLEND);
	       gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	       gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
	}
	
	private void drawCarSelection(GL10 gl)
	{
		gl.glPushMatrix(); 
//		mModelContainer.setTypeAndUpdate(Model.CAR, GameActivity.carBack);
		mModelContainer.setMAngle(38f);
		mModelContainer.setPosition(0, -1.7f, -8.4f);
		mModelContainer.setScale(0.02f, 0.02f, 0.02f);
		mModelContainer.draw(gl);
		gl.glPopMatrix();
	}
	private void drawGarage(GL10 gl,long timeElapsed)
	{
		if(timeElapsed >=30)
		{
			gl.glPushMatrix();
//			mModelContainer.setType(Model.ROOM);
			if(Math.abs(cameraLimit)<22)
			{
				cameraLimit+=cameraStep;
			}else
			{
				cameraStep =-cameraStep;
				cameraLimit+=cameraStep;
			}
			mModelContainer.setMAngle(38f+cameraLimit);
			mModelContainer.setPosition(0, -2.5f, -7.4f);
			mModelContainer.setScale(0.04f, 0.04f, 0.04f);
			mModelContainer.drawByID(gl,4);
			gl.glPopMatrix();
		}
	}
}
