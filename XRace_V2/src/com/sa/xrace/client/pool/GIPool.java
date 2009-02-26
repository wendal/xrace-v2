///:~
/**
 * XRace V2
 * <p/>Use for ... ...
 * <p/>ID: $Id$
 * Last Commit:  $Author$
 * @version $Revision: $
 * 
 */
package com.sa.xrace.client.pool;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.opengl.GLUtils;
import android.util.Log;

import com.sa.xrace.client.R;
import com.sa.xrace.client.scene.Camera;
import com.sa.xrace.client.scene.GLWorld;
import com.sa.xrace.client.toolkit.MethodsPool;
import com.sa.xrace.client.toolkit.ObjectPool;
import com.sa.xrace.client.toolkit.StateValuePool;
import com.wendal.java.xrace.toolkit.bmpconvert.ObjectNumber;

public final class GIPool {

    // private RoomPicPool picPool;
    private InforPoolClient inPool;
    private int[] mTextureIDS;
    private int tem[] = { 0, 74, 74, -74 };
    private int size[] = { 0, 72, 72, -72 };
    private int point[] = { 0, 5, 5, -5 };
    private int tem2[] = { 0, 74, 74, -74 };
    private int tem3[] = { 0, 64, 64, -64 };
    private int time[] = { 0, 256, 256, -256 };

    private float[] points_position = new float[12 * 3];
    private float[] points_color = new float[12 * 4];

    private FloatBuffer fb, fb2, cb, cb2;

    private ByteBuffer bb;

    private static final float MINANGLE = -62.0f;
    private static final float MAXANGLE = 60.0f;

    private float angle, perSpeed, temp;
    // private int carNumber;
    // private static final float RADIUS = 38.0f;

    // CENTERX = 370+50 CENTERY = 15+36-8
    // private static final float CENTERX = 420.0f , CENTERY = 43.0f ;
    // private float triangleX,triangleY;
    private Camera camera;
    private Random r;
    private long nowTime, lasttime;
    private int timeCount = 3;

    public GIPool() {
        // this.picPool = ObjectPool.rpPool;
        this.inPool = ObjectPool.inPoolClient;
        mTextureIDS = new int[7];
        this.camera = ObjectPool.camera;
        r = new Random();
        perSpeed = (MAXANGLE - MINANGLE) / CarInforClient.TOP_SPEED;
        ObjectNumber.regNew(this);
    }

    public void makeAllInterface(GL10 gl) {
        gl.glGenTextures(6, mTextureIDS, 0);
        makeDiameter(gl);
        makeMiniMap(gl);
        makeCarPoints(gl);
        initStarPoints();
    }

    public void drawDiameter(GL10 gl) {
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureIDS[0]);
        ((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
                GL11Ext.GL_TEXTURE_CROP_RECT_OES, tem, 0);
        ((GL11Ext) gl).glDrawTexiOES(370, 15, 0, 74, 74);
        drawTriangle(gl);
        drawSpeedText(gl);
    }

    private void makeDiameter(GL10 gl) {
        Bitmap bm = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
        bm.eraseColor(0);
        Canvas c = new Canvas(bm);
//        Paint p = new Paint();
        c.drawBitmap(MethodsPool.getBitmap(R.drawable.speedometer), 0, 0, p_Triangle_Diameter);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureIDS[0]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bm, 0);

        // Reclaim storage used by bitmap and canvas.
        bm.recycle();
        bm = null;
        c = null;
    }

    // private static Bitmap _bm_128 = Bitmap.createBitmap(128, 128,
    // Bitmap.Config.ARGB_8888);
    // static{
    //			
    // }
    
    private static final Paint p_Triangle_Diameter = new Paint(); 
    public void drawTriangle(GL10 gl) {
        Bitmap bm = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
        // Bitmap bm = _bm_128.copy(null, true);
        bm.eraseColor(0);
        Canvas c = new Canvas(bm);
//        Paint p = new Paint();

        c.save();

        temp = Math.abs(ObjectPool.myCar
                .getNSpeed());
        angle = perSpeed * temp + MINANGLE;
        c.rotate(angle, 50, 36);
        c.drawBitmap(MethodsPool.getBitmap(R.drawable.triangle), 13, 28, p_Triangle_Diameter);

        c.restore();

        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureIDS[1]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bm, 0);

//        bm.recycle();
//        bm = null;
//        c = null;

        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureIDS[1]);
        ((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
                GL11Ext.GL_TEXTURE_CROP_RECT_OES, tem2, 0);
        ((GL11Ext) gl).glDrawTexiOES(370, 15, 0, 74, 74);
    }

    // private static final Bitmap _bm_64 = Bitmap.createBitmap(64, 64,
    // Bitmap.Config.ARGB_8888);
    // static{
    // _bm_64.eraseColor(0);
    // }
    private static Paint p_SpeedText = new Paint();
    static {
        p_SpeedText.setColor(Color.WHITE);
        p_SpeedText.setTextAlign(Paint.Align.CENTER);
        p_SpeedText.setTextSize(25.0f);
        p_SpeedText
                .setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
    }

    public void drawSpeedText(GL10 gl) {
        Bitmap bm = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888);
        bm.eraseColor(0);
        Canvas c = new Canvas(bm);
        // Paint p = new Paint();
        // p.setColor(Color.WHITE);
        // p.setTextAlign(Paint.Align.CENTER);
        // p.setTextSize(25.0f);
        // p.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        //		
        int ppeedText_int = (int) Math.abs(ObjectPool.myCar.getNSpeed()) * 5;

        c.drawText("" + ppeedText_int, 32, 32, p_SpeedText);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureIDS[2]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bm, 0);

        // Reclaim storage used by bitmap and canvas.
        // bm.recycle();
        // bm = null;
        // c = null;

        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureIDS[2]);
        ((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
                GL11Ext.GL_TEXTURE_CROP_RECT_OES, tem3, 0);
        ((GL11Ext) gl).glDrawTexiOES(400, 15, 0, 64, 64);
    }

    public void makeMiniMap(GL10 gl) {

        // Bitmap bitmap = Bitmap.createBitmap(128, 128
        // ,Bitmap.Config.ARGB_8888);
        // bitmap.eraseColor(0);
        // Canvas c = new Canvas(bitmap);
        // Paint paint = new Paint();
        // c.drawBitmap(MethodsPool.getBitmap(R.drawable.minimap), 0, 0, paint);
        Bitmap bitmap = MethodsPool.getBitmap(R.drawable.minimap);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureIDS[3]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        // bitmap.recycle();
        // bitmap = null;
        // c = null;
    }

    public void drawMiniMap(GL10 gl) {

        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureIDS[3]);
        ((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
                GL11Ext.GL_TEXTURE_CROP_RECT_OES, size, 0);
        ((GL11Ext) gl).glDrawTexiOES(15, 15, 0, 72, 72);
        int num = inPool.getNCarNumber();
        for (int i = 0; i < num; i++) {
            float carX = 0.0f, carY = 0.0f;
            float x = 0.0f, y = 0.0f;
            float carI_X = inPool.getOneCarInformation(i).getNXPosition();
            x = (float) (87 - (carI_X * 72 / 23500 + 53.62));
            float carI_Y = inPool.getOneCarInformation(i).getNYPosition();
            y = (float) (carI_Y * 72 / 23500 + 68.62);
            carX = (int) carI_X;
            carY = (int) carI_Y;
            if (StateValuePool.isDebug == true) {
                Log.e("carX Position", "" + carX);
                Log.e("carY Position", "" + carY);
                Log.e("-------------------------------------",
                        "---------------------------");
                Log.e("X Position", "" + x);
                Log.e("Y Position", "" + y);

            }
            drawCarPoints(gl, x, y);
        }
    }

    public void makeCarPoints(GL10 gl) {
        Bitmap bitmap = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(0);
        Canvas c = new Canvas(bitmap);
        Paint paint = new Paint();
        c
                .drawBitmap(MethodsPool.getBitmap(R.drawable.carpointpic), 0,
                        0, paint);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureIDS[4]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();
        bitmap = null;
        c = null;
    }

    public void drawCarPoints(GL10 gl, float x, float y) {
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureIDS[4]);
        ((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
                GL11Ext.GL_TEXTURE_CROP_RECT_OES, point, 0);
        ((GL11Ext) gl).glDrawTexfOES(x, y, 0, 5, 5);
    }

    public void initStarPoints() {
        for (int i = 0; i < points_position.length; i += 3) {
            points_position[i] = -19000 + r.nextInt(28000);
            points_position[i + 1] = 1900 + r.nextInt(300);
            points_position[i + 2] = -19000 + r.nextInt(28000);
        }

        bb = ByteBuffer.allocateDirect(points_position.length * 4);
        bb.order(ByteOrder.nativeOrder());
        fb2 = bb.asFloatBuffer();
        for (int i = 0; i < points_position.length; i++) {
            fb2.put(points_position[i]);
        }
        fb2.position(0);

        for (int i = 0; i < points_color.length; i += 4) {
            points_color[i] = 1.0f;
            points_color[i + 1] = 1.0f;
            points_color[i + 2] = 0.0f;
            points_color[i + 3] = 1.0f;
        }

        bb = ByteBuffer.allocateDirect(points_color.length * 4);
        bb.order(ByteOrder.nativeOrder());
        cb2 = bb.asFloatBuffer();
        for (int i = 0; i < points_color.length; i++) {
            cb2.put(points_color[i]);
        }
        cb2.position(0);

    }

    public void drawStarPoints(GL10 gl) {
        gl.glDisable(GL10.GL_TEXTURE_2D);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        camera.setCamera(gl);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glPointSize(2.0f);
        fb2.position(0);
        cb2.position(0);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, fb2);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, cb2);
        gl.glDrawArrays(GL10.GL_POINTS, 0, points_position.length / 3);

        gl.glPopMatrix();

        gl.glEnable(GL10.GL_BLEND);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }

    public void drawTimeCount(GL10 gl, GLWorld mWorld) {
        if (StateValuePool.isStart && !mWorld.isBeginWait) { // mWorld.isBeginWait
            nowTime = System.currentTimeMillis();

            if (lasttime == 0) {
                lasttime = nowTime;
                makeDst("" + timeCount, gl);
            }
            if (nowTime - lasttime >= 1000) {
                lasttime = nowTime;
                timeCount--;
                if (timeCount >= 1) {
                    makeDst("" + timeCount, gl);
                } else if (timeCount == 0) {
                    makeDst("GO", gl);
                } else {
                    mWorld.isBeginWait = true;
                    StateValuePool.needTimeCount = false;
                }

            }
            gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureIDS[5]);
            ((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
                    GL11Ext.GL_TEXTURE_CROP_RECT_OES, time, 0);
            ((GL11Ext) gl).glDrawTexiOES(112, 0, 0, 256, 256);
        }

    }

    private void makeDst(String time, GL10 gl) {
        Bitmap bm = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
        bm.eraseColor(0);
        Canvas c = new Canvas(bm);
        Paint p = new Paint();
        p.setColor(0xFFffffff);
        p.setAntiAlias(true);
        p.setTextSize(110);
        p.setStrokeWidth(30);
        p.setTextAlign(Paint.Align.CENTER);
        c.drawText(time, 128, 128, p);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureIDS[5]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bm, 0);

        // Reclaim storage used by bitmap and canvas.
        bm.recycle();
        bm = null;
        c = null;
    }

    public void drawLoginFailure(GL10 gl) {
        int size[] = { 0, 128, 128, -128 };
        makeLoginFailure("Login Failure!!", gl);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureIDS[6]);
        ((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
                GL11Ext.GL_TEXTURE_CROP_RECT_OES, size, 0);
        // left down ,withe hight
        ((GL11Ext) gl).glDrawTexiOES(0, 0, 0, 128, 128);

    }

    public void makeLoginFailure(String time, GL10 gl) {
        Bitmap bm = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
        bm.eraseColor(0);
        Canvas c = new Canvas(bm);
        Paint p = new Paint();
        p.setColor(0xFFffffff);
        p.setAntiAlias(true);
        p.setTextSize(110);
        p.setStrokeWidth(30);
        p.setTextAlign(Paint.Align.CENTER);
        c.drawText(time, 128, 128, p);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureIDS[6]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bm, 0);
        bm.recycle();
        bm = null;
        c = null;
    }

}
