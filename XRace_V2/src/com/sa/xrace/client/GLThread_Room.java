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

import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGL11;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLU;
import android.util.Log;
import android.view.SurfaceHolder;

import com.sa.xrace.client.model.ModelInforPool;
import com.sa.xrace.client.pool.GIPool;
import com.sa.xrace.client.pool.InforPoolClient;
import com.sa.xrace.client.scene.GLWorld;
import com.sa.xrace.client.toolkit.DataToolKit;
import com.sa.xrace.client.toolkit.MethodsPool;
import com.sa.xrace.client.toolkit.ObjectPool;
import com.wendal.java.xrace.toolkit.bmpconvert.DataUnti;

public final class GLThread_Room extends Thread {

    public static int mPhase = DataToolKit.GAME_ROOM;
    // private boolean isInitGameFinished=false;
    private float cameraStep = 1f;
    private float cameraLimit = 0f;

    private int mWidth;
    private int mHeight;
    private IntBuffer texturesB;
    private boolean mDone;
    // private RoomPicPool picPool;
    private GIPool giPool;
    private SurfaceHolder mHolder;
    private GameView callingView;
    // private WRbarPool barPool;
    private ModelInforPool mModelContainer;
    private InforPoolClient inPool;
    private GLWorld mWorld;
    // private GameActivity mActivity;
    // private IntBuffer tempIB;
    // private Bitmap carMyB_img;
    private boolean isModelGenerate = false;
    // private boolean done = false;
    // public PostManagerClient mPostManager;
    public static boolean addBar = true;
    public static boolean loginFailure = false;
    public static byte bindex = 0;

//    private final static int mTextureLoad = 1;
//    private static int tem2[] = { 0, 512, 512, -512 };
    private static EGL10 egl;
    private static EGLDisplay dpy;
    private static EGLSurface surface;
    // public static GL10 gl;
    public static int progress, startPro = 22;

    private static long nowTime = 0;
    private static long lastTime = 0;
    private static long timeElapsed = 0;
    private static long timeadd = 0;

    GLThread_Room(SurfaceHolder mHolder, GameView inputView) {
        super();
        addBar = true;
        loginFailure = false;
        progress = 22;
        texturesB = IntBuffer.allocate(11);
        mDone = false;
        mWidth = 0;
        mHeight = 0;
        this.mHolder = mHolder;
        callingView = inputView;
        // this.picPool = ObjectPool.rpPool;
        this.giPool = ObjectPool.giPool;
        // this.barPool = ObjectPool.barPool;
        this.mModelContainer = ObjectPool.mModelInforPool;
        this.inPool = ObjectPool.inPoolClient;
        this.mWorld = ObjectPool.mWorld;
        // this.mActivity = ObjectPool.activity;
        // this.mPostManager = ObjectPool.mPostManager;
        // RoomPicPool.loading =
        // DataUnti.getByteBuffer_ByFileName(DataUnti.getNameByID(R.drawable.laod));
        // carMyB_img = MethodsPool.getBitmap(R.drawable.mysite);
    }

    // public static int getPhase()
    // {
    // return mPhase;
    // }

    // public static void setPhase(int input)
    // {
    // mPhase =input;
    // }

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
        EGLContext context = egl.eglCreateContext(dpy, config,
                EGL10.EGL_NO_CONTEXT, null);
        surface = egl.eglCreateWindowSurface(dpy, config, mHolder, null);
        egl.eglMakeCurrent(dpy, surface, surface, context);
        ObjectPool.gl = (GL10) context.getGL();
        ObjectPool.gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                GL10.GL_FASTEST);

        initForGame();
        getLoginTextureReady();
        ObjectPool.barPool.initWRbarPool(texturesB);
        ObjectPool.gl.glMatrixMode(GL10.GL_MODELVIEW);

        while (!mDone) {
            // Update the asynchronous state (window size, key events)
            int w, h;
            synchronized (this) {
                w = mWidth;
                h = mHeight;
            }

            /* 这是耗时的一步 */
            if (!isModelGenerate) {
                Loading();
            }

            ObjectPool.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
            drawFrame(w, h);

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

    private void drawFrame(int w, int h) {
        GL10 gl = ObjectPool.gl;
        nowTime = System.currentTimeMillis();
        if (lastTime == 0) {
            lastTime = nowTime;
        }
        timeElapsed = nowTime - lastTime;
        lastTime = nowTime;
        timeElapsed = InforPoolClient.timeFixing(timeElapsed);

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        if (loginFailure) {
            giPool.drawLoginFailure(gl);
            ObjectPool.activity.finish();

        }
        if (!addBar) {
            Log.e("----------------addBar-----------------", "addBar");
            ObjectPool.barPool.addBar_Login(bindex);
            GameActivity.isLogin = true;
            addBar = true;
        }
        switch (mPhase) {
        case DataToolKit.GAME_ROOM:

            drawGarage(gl, timeElapsed);
            ObjectPool.barPool.drawOut();

            if (GameActivity.carOn) {
                drawCarSelection(gl);
            }
            break;
        case DataToolKit.GAME_RUNNING:

            ObjectPool.barPool = null;
            mWorld.draw(gl, timeElapsed);
            giPool.drawStarPoints(gl);
            giPool.drawDiameter(gl);
            giPool.drawMiniMap(gl);

            giPool.drawTimeCount(gl, mWorld);

            break;
        }
        if (GameActivity.isStart) {
            timeadd += timeElapsed;
            if (timeadd >= 30) {
                ObjectPool.mPostManager.sendNormalPostToServer();
                timeadd = 0;
            }
        }
    }

    /**
     * 应该是控制进度条的
     * 
     * @param gl
     * @param numTime
     * @param imgIndex
     */

    public final static void makeLoading(int numTime, int imgIndex) {
//        GL10 gl = ObjectPool.gl;
//        
//        android.widget.ProgressBar pBar = new android.widget.ProgressBar(ObjectPool.activity,null);
//        pBar.setProgress((int) ((progress - startPro) / 4.38));
        
        
        
        // boolean isDone = false;

//        Bitmap bm = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);
//        bm.eraseColor(0);
//        CloneableCanvas c = new CloneableCanvas(bm);
//        Paint p = new Paint();
//
//        p.setAntiAlias(true);
//        p.setTextSize(13);
//        p.setTextAlign(Paint.Align.CENTER);
//        p.setStrokeWidth(9.0f);
//        c.drawColor(Color.BLACK);
//        // if(isDone == false){
//        switch (imgIndex) {
//        case 0: {
//            c.drawBitmap(MethodsPool.getBitmap(R.drawable.load0), 0, 192, p);
//            break;
//        }
//        case 1: {
//            c.drawBitmap(MethodsPool.getBitmap(R.drawable.load1), 0, 192, p);
//            break;
//        }
//        case 2: {
//            c.drawBitmap(MethodsPool.getBitmap(R.drawable.load2), 0, 192, p);
//            break;
//        }
//        case 3: {
//            c.drawBitmap(MethodsPool.getBitmap(R.drawable.load3), 0, 192, p);
//            break;
//        }
//
//        }
//        // isDone = true;
//        // }
//        p.setColor(Color.BLUE);
//        c.drawLine(startPro, 488, progress, 488, p);

//        bm.clone();
        
        for (int i = progress; i < numTime; i += 5, progress += 5) {
//            try {
//                Canvas c2 = c.clone();
//
//                p.setColor(Color.WHITE);
            
            //(int) ((progress - startPro) / 4.38)
            
//                c2.drawText("" + (int) ((progress - startPro) / 4.38) + " %",
//                        240, 492, p);

//                gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureLoad);
//                GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bm, 0);
//
//                // Reclaim storage used by bitmap and canvas.
//                // bm.recycle();
//                // bm = null;
//                // c2 = null;
//
//                gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureLoad);
//                ((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
//                        GL11Ext.GL_TEXTURE_CROP_RECT_OES, tem2, 0);
//                ((GL11Ext) gl).glDrawTexiOES(0, 0, 0, 512, 512);
//                egl.eglSwapBuffers(dpy, surface);
//            } catch (CloneNotSupportedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
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

    // public void destroy() {
    // // TODO Auto-generated method stub
    // super.destroy();
    // }

    private void Loading() {
        GL10 gl = ObjectPool.gl;
        long start = System.currentTimeMillis();
        long first = start;
        Log.e("-->Begin " + (System.currentTimeMillis() - start),
                "GLThread_Room Loading");
        makeLoading(60, 0);

        // start = System.currentTimeMillis();
        // // picPool.generateEveryThing(); // 23s
        // Log.e("-->>Time use :"+(System.currentTimeMillis()
        // -start),"generateEveryThing");

        start = System.currentTimeMillis();
        giPool.makeAllInterface(gl); // >1s
        makeLoading(182, 2);
        Log.e("-->>Time use :" + (System.currentTimeMillis() - start),
                "makeAllInterface");

        start = System.currentTimeMillis();
        MethodsPool.LoadMapFromXML("scene.xml"); // 4.7s
        makeLoading(242, 2);
        Log.e("-->>Time use :" + (System.currentTimeMillis() - start),
                "LoadMapFromXML");

        start = System.currentTimeMillis();
        // long start = System.currentTimeMillis();
        mModelContainer.generate(gl); // 49s
        Log.e("-->>Time use :" + (System.currentTimeMillis() - start),
                "mModelContainer.generate(gl);");
        start = System.currentTimeMillis();

        getCommonTextureReady(gl); // >1s
        makeLoading(442, 3);

        mModelContainer.setType(DataToolKit.CAR);
        inPool.getOneCarInformation(inPool.getMyCarIndex()).setModel(
                mModelContainer.getCurrentModel());
        if (!GameActivity.isLogin) {
            Log.e("----------------isLogin-----------------", "isLogin");
            Log.v("sendLoginPostToServer", "sendLoginPostToServer");
            ObjectPool.mPostManager.sendLoginPostToServer();
        }
        makeLoading(460, 3);
        isModelGenerate = true;
        Log.e("Done Loading-->Time used: "
                + (System.currentTimeMillis() - first),
                "It shall be show Srceen now");
    }

    private void getLoginTextureReady() {
        GL10 gl = ObjectPool.gl;
        gl.glGenTextures(11, texturesB);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(10));
        gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, 256, 256, 0,
                GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, DataUnti
                        .getByteBuffer_ByFileName(DataUnti
                                .getNameByID(R.drawable.laod)));
    }

    private void getCommonTextureReady(GL10 gl) {
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(4));
        // Log.e("in getCommonTextureReady", "Step A getCommonTextureReady");
        gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, 256, 256, 0,
                GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, DataUnti
                        .getByteBuffer_ByID(R.drawable.down_pic));
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(5));
        gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, 256, 256, 0,
                GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, DataUnti
                        .getByteBuffer_ByFileName(DataUnti
                                .getNameByID(R.drawable.mapchoice_pic)));
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(6));
        gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, 256, 256, 0,
                GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, DataUnti
                        .getByteBuffer_ByID(R.drawable.mapview1));
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(7));
        gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, 256, 256, 0,
                GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, DataUnti
                        .getByteBuffer_ByID(R.drawable.mapview2));
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(8));
        gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, 256, 256, 0,
                GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, DataUnti
                        .getByteBuffer_ByID(R.drawable.carchoice));
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(9));
        gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, 256, 256, 0,
                GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, DataUnti
                        .getByteBuffer_ByID(R.drawable.carchoice));

        // Log.e("in getCommonTextureReady", "Step B getCommonTextureReady");

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
        // Log.e("in getCommonTextureReady", "Finish getCommonTextureReady");
    }

    private void initForGame() {
        GL10 gl = ObjectPool.gl;
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

    private void drawCarSelection(GL10 gl) {
        gl.glPushMatrix();
        // mModelContainer.setTypeAndUpdate(Model.CAR, GameActivity.carBack);
        mModelContainer.setMAngle(38f);
        mModelContainer.setPosition(0, -1.7f, -8.4f);
        mModelContainer.setScale(0.02f, 0.02f, 0.02f);
        mModelContainer.draw(gl);
        gl.glPopMatrix();
    }

    private void drawGarage(GL10 gl, long timeElapsed) {
        if (timeElapsed >= 30) {
            gl.glPushMatrix();
            // mModelContainer.setType(Model.ROOM);
            if (Math.abs(cameraLimit) < 22) {
                cameraLimit += cameraStep;
            } else {
                cameraStep = -cameraStep;
                cameraLimit += cameraStep;
            }
            mModelContainer.setMAngle(38f + cameraLimit);
            mModelContainer.setPosition(0, -2.5f, -7.4f);
            mModelContainer.setScale(0.04f, 0.04f, 0.04f);
            mModelContainer.drawByID(gl, 4);
            gl.glPopMatrix();
        }
    }
}
