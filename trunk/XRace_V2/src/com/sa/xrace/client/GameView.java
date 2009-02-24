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

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{

	private SurfaceHolder mHolder;
//	private GameActivity Activity;
	private GLThread_Room drawThread;
//	private RoomPicPool picPool;
//	private InforPoolClient inPool;
//	private WRbarPool barPool;
//	private GIPool giPool;
//	private ModelInforPool mModelInforPool;
//	private GLWorld mWorld;
//	private PostManagerClient mPostManager;
	
	
	public GameView(Context context)
	{
		super(context);
//		Activity = inputActivity;
//		this.picPool = picPool;
//		this.inPool = ObjectPool.inPoolClient;
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_HARDWARE);
		mHolder.setFormat(PixelFormat.RGBA_8888);
//		this.barPool = barPool;
//		this.giPool = giPool;
//		this.mWorld =mWorld;
//		this.mModelInforPool = mModelInforPool;
//		this.mPostManager = ObjectPool.mPostManager;
	}
	
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// 
		drawThread.onWindowResize(arg2, arg3);
	}

	public void surfaceCreated(SurfaceHolder arg0) {
		// 
		drawThread = new GLThread_Room(mHolder,this);
		drawThread.start();	
		
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {
		// 
		drawThread.requestExitAndWait();
		drawThread = null;
	}
	
}
