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

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.sa.xrace.client.manager.PostManagerClient;
import com.sa.xrace.client.model.ModelInforPool;
import com.sa.xrace.client.pool.GIPool;
import com.sa.xrace.client.pool.InforPoolClient;
import com.sa.xrace.client.pool.RoomPicPool;
import com.sa.xrace.client.pool.WRbarPool;
import com.sa.xrace.client.scene.GLWorld;
import com.sa.xrace.client.toolkit.ObjectPool;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{

	private SurfaceHolder mHolder;
//	private GameActivity Activity;
	private GLThread_Room drawThread;
	private RoomPicPool picPool;
	private InforPoolClient inPool;
	private WRbarPool barPool;
	private GIPool giPool;
	private ModelInforPool mModelInforPool;
	private GLWorld mWorld;
	private PostManagerClient mPostManager;
	
	
	public GameView(Context context,RoomPicPool picPool,InforPoolClient inPool,
			WRbarPool barPool,GIPool giPool,ModelInforPool mModelInforPool, GLWorld mWorld,PostManagerClient mPostManager)
	{
		super(context);
//		Activity = inputActivity;
		this.picPool = picPool;
		this.inPool = inPool;
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_HARDWARE);
		mHolder.setFormat(PixelFormat.RGBA_8888);
		this.barPool = barPool;
		this.giPool = giPool;
		this.mWorld =mWorld;
		this.mModelInforPool = mModelInforPool;
		this.mPostManager = mPostManager;
	}
	
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// 
		drawThread.onWindowResize(arg2, arg3);
	}

	public void surfaceCreated(SurfaceHolder arg0) {
		// 
		drawThread = new GLThread_Room(ObjectPool.activity,mHolder,this,picPool,barPool,giPool,mModelInforPool,mWorld,inPool,mPostManager);
		drawThread.start();	
		
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {
		// 
		drawThread.requestExitAndWait();
		drawThread = null;
	}
	
}
