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
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.util.Log;
import android.view.SurfaceHolder;

import com.sa.xrace.client.model.ModelInforPool;
import com.sa.xrace.client.network.NetWorkManager;
import com.sa.xrace.client.pool.GIPool;
import com.sa.xrace.client.pool.InforPoolClient;
import com.sa.xrace.client.scene.GLWorld;
import com.sa.xrace.client.toolkit.DataToolKit;
import com.sa.xrace.client.toolkit.MethodsPool;
import com.sa.xrace.client.toolkit.ObjectPool;
import com.sa.xrace.client.toolkit.StateValuePool;

public final class GLThread_Room extends Thread {

    public static int mPhase = DataToolKit.GAME_ROOM;
    private int cameraStep = 1;
    private int cameraLimit = 0;

    private IntBuffer texturesB;
    private boolean mDone;
    private GIPool giPool;
    private SurfaceHolder mHolder;
    private GameView callingView;
    private ModelInforPool mModelContainer;
    private GLWorld mWorld;
    public static boolean addBar = true;
    public static boolean loginFailure = false;
    public static byte bindex = 0;

    private static long nowTime = 0;
    private static long lastTime = 0;
    private static long timeElapsed = 0;
    private static int timeadd = 0;

    GLThread_Room(SurfaceHolder mHolder, GameView inputView) {
        super();
        addBar = true;
        loginFailure = false;
        texturesB = IntBuffer.allocate(11);
        mDone = false;
        this.mHolder = mHolder;
        callingView = inputView;
        this.giPool = ObjectPool.giPool;
        this.mModelContainer = ObjectPool.mModelInforPool;
        this.mWorld = ObjectPool.mWorld;
    }

    private boolean needGenerateCollisionMap = true;

    public void run() {
        Log.i(getClass().getSimpleName(), "in run()");

        EGL10 egl;
        EGLDisplay dpy;
        EGLSurface surface;
        
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
        
        GL10 gl = (GL10) context.getGL();//获取GL
        
        ObjectPool.gl = gl;//加入对象池,关键一步!!

        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        
        MethodsPool.LoadMapFromXML("scene.xml");
        

        initForGame();
        getLoginTextureReady();
        ObjectPool.barPool.initWRbarPool(texturesB);
        ObjectPool.gl.glMatrixMode(GL10.GL_MODELVIEW);

        /* 这是耗时的一步 */
        Loading();
        
        while (!mDone) {

//            gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
            drawFrame();// 这是主要耗时的地方,循环绘图

            egl.eglSwapBuffers(dpy, surface);
        }

        // Log.e("Come Here?","after while (!mDone)");
        // 好像从来为运行到这里

        egl.eglMakeCurrent(dpy, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE,
                EGL10.EGL_NO_CONTEXT);
        egl.eglDestroyContext(dpy, context);
        egl.eglDestroySurface(dpy, surface);
        egl.eglTerminate(dpy);
    }

    private boolean needCreateRoad = true;
    private void drawFrame() {
        GL10 gl = ObjectPool.gl;
        nowTime = System.currentTimeMillis();
        if (lastTime == 0) {
            lastTime = nowTime;
        }
        timeElapsed = InforPoolClient.timeFixing(nowTime - lastTime);
        lastTime = nowTime;
//        timeElapsed = InforPoolClient.timeFixing(timeElapsed);

        // 耗时 2 ~ 3ms
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        

        switch (mPhase) {
        case DataToolKit.GAME_ROOM:
            if (loginFailure) {
                giPool.drawLoginFailure(gl);
                ObjectPool.activity.finish();

            }
            if (!addBar) {
                Log.e("----------------addBar-----------------", "addBar");
                ObjectPool.barPool.addBar_Login(bindex);
                StateValuePool.isLogin = true;
                addBar = true;
            }
            if (timeElapsed >= 30) {
                drawGarage(gl);
                if (ObjectPool.barPool != null) {
                    ObjectPool.barPool.drawOut();
                }
                if (StateValuePool.carOn) {
                    drawCarSelection(gl);
                }
            }else{
                //期待能执行到这里
                Log.e("GLThread_Room", "---------------------->Time less than 30ms!!!");
            }
            break;
        case DataToolKit.GAME_RUNNING:
            
            if(needCreateRoad){
                ObjectPool.mModelInforPool.removeGarageModel();
                ObjectPool.mModelInforPool.roadModel.generate();
                needCreateRoad = false;
            }

            mWorld.draw(gl, timeElapsed);
            giPool.drawStarPoints(gl);
            giPool.drawDiameter(gl);
            giPool.drawMiniMap(gl);

            if (StateValuePool.needTimeCount) {
                giPool.drawTimeCount(gl, mWorld);
            } else if (needGenerateCollisionMap) {
                ObjectPool.mWorld.generateCollisionMap();
                needGenerateCollisionMap = false;
            }

            break;
        }
        
        if (StateValuePool.isStart) {
            timeadd += (int)timeElapsed;
//            Log.v("Time Add", ""+timeadd);
//            Log.v("Time Elapsed", ""+timeElapsed);
            if (timeadd >= 30) {
                NetWorkManager.mPostManager.sendNormalPostToServer();
                
                timeadd = 0;
            }
        }
    }

    private void Loading() {
        GL10 gl = ObjectPool.gl;
        giPool.makeAllInterface(gl);
        mModelContainer.generate(gl);
        getCommonTextureReady(gl);

//        ObjectPool.myCar.setModel(mModelContainer.getCurrentCarModel());
        if (!StateValuePool.isLogin) {
            Log.e("----------------isLogin-----------------", "isLogin");
            Log.v("sendLoginPostToServer", "sendLoginPostToServer");
            NetWorkManager.mPostManager.sendLoginPostToServer();
        }
    }

    private void getLoginTextureReady() {
//        GL10 gl = ObjectPool.gl;
//        gl.glGenTextures(11, texturesB);
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(10));
//        gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, 256, 256, 0,
//                GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, DataUnti
//                        .getByteBuffer_ByFileName(DataUnti
//                                .getNameByID(R.drawable.laod)));
    }

    private void getCommonTextureReady(GL10 gl) {
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(4));
//        gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, 256, 256, 0,
//                GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, DataUnti
//                        .getByteBuffer_ByID(R.drawable.down_pic));
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(5));
//        gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, 256, 256, 0,
//                GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, DataUnti
//                        .getByteBuffer_ByFileName(DataUnti
//                                .getNameByID(R.drawable.mapchoice_pic)));
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(6));
//        gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, 256, 256, 0,
//                GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, DataUnti
//                        .getByteBuffer_ByID(R.drawable.mapview1));
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(7));
//        gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, 256, 256, 0,
//                GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, DataUnti
//                        .getByteBuffer_ByID(R.drawable.mapview2));
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(8));
//        gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, 256, 256, 0,
//                GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, DataUnti
//                        .getByteBuffer_ByID(R.drawable.carchoice));
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(9));
//        gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, 256, 256, 0,
//                GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, DataUnti
//                        .getByteBuffer_ByID(R.drawable.carchoice));

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

    }

    private void initForGame() {
        GL10 gl = ObjectPool.gl;
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
        mModelContainer.setMAngle(38);
        mModelContainer.setPosition(0, -1.7f, -8.4f);
        mModelContainer.setScale(0.02f, 0.02f, 0.02f);
        mModelContainer.draw();
        gl.glPopMatrix();
    }

    private void drawGarage(GL10 gl) {
//        if (timeElapsed >= 30) {
            gl.glPushMatrix();
            if (Math.abs(cameraLimit) < 22) {
                cameraLimit += cameraStep;
            } else {
                cameraStep = -cameraStep;
                cameraLimit += cameraStep;
            }
            
            mModelContainer.setMAngle(38 + cameraLimit);
            mModelContainer.drawGarageNow();

            gl.glPopMatrix();
        }
//    }
}
