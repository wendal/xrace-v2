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
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.opengl.GLUtils;
import android.util.Log;

import com.sa.xrace.client.R;
import com.sa.xrace.client.toolkit.MethodsPool;
import com.sa.xrace.client.toolkit.ObjectPool;
import com.sa.xrace.client.toolkit.StateValuePool;

public class WRbarPool {

    private Bar[] barList;

    private GL10 gl;

    private IntBuffer texturesB, carList_FaceVB, map_FaceVB;
    private FloatBuffer carList_FaceCB, map_FaceCB;
    private ShortBuffer carList_FaceIB, map_FaceIB;
    private FloatBuffer carList_FaceUTB, map_FaceUTB;
    private ByteBuffer tempBuffer;

    private byte index;
    private InforPoolClient inPool;
    private final static int MAXCARN = 4;
    private final static int NUMCOUNT = 30;

    private static final int NEWCAR_FLAG = 10;
    private static final int IDLE_FLAG = 1;
    private static int[] listCar;
    private String temPlayerName;

    private int temindex;
    private int tempLName;

//    private int mapchange = 5;
//    private int MAPCOUNT = 3;
//    private int carchange = 8;
//    private int CARCOUNT = 2;
    private Bitmap carB_img, carMyB_img;

    private int mTextureIDS[];
    private final int tem[] = { 0, 256, 256, -256 };
    private int panelX, panelY;

    // /////////////////////////////////////Car_List/////////////////////////////////////

    private final int carList_position[] = {
            -MethodsPool.toFixed(2.62f),
            MethodsPool.toFixed(1.5f),
            -MethodsPool.toFixed(12), // up_left
            -MethodsPool.toFixed(2.62f), -MethodsPool.toFixed(1.5f),
            -MethodsPool.toFixed(12), MethodsPool.toFixed(2.62f),
            -MethodsPool.toFixed(1.5f), -MethodsPool.toFixed(12),

            -MethodsPool.toFixed(2.62f), MethodsPool.toFixed(1.5f),
            -MethodsPool.toFixed(12), MethodsPool.toFixed(2.62f),
            -MethodsPool.toFixed(1.5f), -MethodsPool.toFixed(12),
            MethodsPool.toFixed(2.62f), MethodsPool.toFixed(1.5f),
            -MethodsPool.toFixed(12), };

    private final float carList_color[] = { 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,

            1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, };
    private final short carList_index[] = { 5, 4, 3, 2, 1, 0 };

    private final float carList_UT[] = { 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0 };

    // //////////////////////////////////MAP///////////////////////////
    private final int map_position[] = { -MethodsPool.toFixed(7),
            MethodsPool.toFixed(4), -MethodsPool.toFixed(11),
            -MethodsPool.toFixed(7), -MethodsPool.toFixed(1.8f),
            -MethodsPool.toFixed(11), MethodsPool.toFixed(7),
            -MethodsPool.toFixed(1.8f), -MethodsPool.toFixed(11),

            -MethodsPool.toFixed(7), MethodsPool.toFixed(4),
            -MethodsPool.toFixed(11), MethodsPool.toFixed(7),
            -MethodsPool.toFixed(1.8f), -MethodsPool.toFixed(11),
            MethodsPool.toFixed(7), MethodsPool.toFixed(4),
            -MethodsPool.toFixed(11), };
    private final float map_color[] = { 1.0f, 1.0f, 1.0f, 0.6f, 1.0f, 1.0f,
            1.0f, 0.6f, 1.0f, 1.0f, 1.0f, 0.6f,

            1.0f, 1.0f, 1.0f, 0.6f, 1.0f, 1.0f, 1.0f, 0.6f, 1.0f, 1.0f, 1.0f,
            0.6f,

    };
    private final short map_index[] = { 5, 4, 3, 2, 1, 0 };
    private final float map_UT[] = { 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0 };


    /**
     * 应该只有一个新对象,考虑用单例模式
     * 
     */
    public WRbarPool() {
        this.inPool = ObjectPool.inPoolClient;
        barList = new Bar[4];
        for (int i = 0; i < barList.length; i++) {
            barList[i] = new Bar();
        }

        listCar = new int[MAXCARN];
        for (int i = 0; i < MAXCARN; i++) {
            listCar[i] = i * 100;
        }

        carB_img = MethodsPool.getBitmap(R.drawable.upleft_pic);
        carMyB_img = MethodsPool.getBitmap(R.drawable.mysite);

        preparecarList_Face();
        prepareMap_Face();
    }

    public void initWRbarPool(IntBuffer texturesB) {
        this.gl = ObjectPool.gl;
        ;
        this.texturesB = texturesB;

        mTextureIDS = new int[4];
        gl.glGenTextures(4, mTextureIDS, 0);

    }

    public static void setListCar(int input) {
        listCar[input / 100] = input;
    }

    public static int getListCar(int index) {
        return listCar[index];
    }

    public void preparecarList_Face() {
        tempBuffer = ByteBuffer.allocateDirect(carList_position.length * 4);
        tempBuffer.order(ByteOrder.nativeOrder());
        carList_FaceVB = tempBuffer.asIntBuffer();
        for (int i = 0; i < carList_position.length; i++) {
            carList_FaceVB.put(carList_position[i]);
        }
        carList_FaceVB.position(0);

        tempBuffer = ByteBuffer.allocateDirect(carList_color.length * 4);
        tempBuffer.order(ByteOrder.nativeOrder());
        carList_FaceCB = tempBuffer.asFloatBuffer();
        for (int i = 0; i < carList_color.length; i++) {
            carList_FaceCB.put(carList_color[i]);
        }
        carList_FaceCB.position(0);

        tempBuffer = ByteBuffer.allocateDirect(carList_index.length * 2);
        tempBuffer.order(ByteOrder.nativeOrder());
        carList_FaceIB = tempBuffer.asShortBuffer();
        for (int i = 0; i < carList_index.length; i++) {
            carList_FaceIB.put(carList_index[i]);
        }
        carList_FaceIB.position(0);

        tempBuffer = ByteBuffer.allocateDirect(carList_UT.length * 4);
        tempBuffer.order(ByteOrder.nativeOrder());
        carList_FaceUTB = tempBuffer.asFloatBuffer();
        for (int i = 0; i < carList_UT.length; i++) {
            carList_FaceUTB.put(carList_UT[i]);
        }
        carList_FaceUTB.position(0);

    }

    public void prepareMap_Face() {
        tempBuffer = ByteBuffer.allocateDirect(map_position.length * 4);
        tempBuffer.order(ByteOrder.nativeOrder());
        map_FaceVB = tempBuffer.asIntBuffer();
        for (int i = 0; i < map_position.length; i++) {
            map_FaceVB.put(map_position[i]);
        }
        map_FaceVB.position(0);

        tempBuffer = ByteBuffer.allocateDirect(map_color.length * 4);
        tempBuffer.order(ByteOrder.nativeOrder());
        map_FaceCB = tempBuffer.asFloatBuffer();
        for (int i = 0; i < map_color.length; i++) {
            map_FaceCB.put(map_color[i]);
        }
        map_FaceCB.position(0);

        tempBuffer = ByteBuffer.allocateDirect(map_index.length * 2);
        tempBuffer.order(ByteOrder.nativeOrder());
        map_FaceIB = tempBuffer.asShortBuffer();
        for (int i = 0; i < map_index.length; i++) {
            map_FaceIB.put(map_index[i]);
        }
        map_FaceIB.position(0);
        tempBuffer = ByteBuffer.allocateDirect(map_UT.length * 4);
        tempBuffer.order(ByteOrder.nativeOrder());
        map_FaceUTB = tempBuffer.asFloatBuffer();
        for (int i = 0; i < map_UT.length; i++) {
            map_FaceUTB.put(map_UT[i]);
        }
        map_FaceUTB.position(0);
    }

    public void addBar_Login(byte id) {
        if (!InforPoolClient.Logined) {

            for (int i = 0; i <= id; i++) {
                temPlayerName = inPool.getOneCarInformation(i).getNName();
                temindex = inPool.getOneCarInformation(i).getCarListName();
                if (i != id) {
                    makeDst(temindex / 100, temPlayerName, "Car", false);
                } else {
                    makeDst(temindex / 100, temPlayerName, "Car", true);
                }

                setBarLogin(temindex / 100);
            }
        } else {
            for (int i = 0; i < inPool.getNCarNumber(); i++) {
                temindex = inPool.getOneCarInformation(i).getCarListName();
                if (temindex % 100 >= 10) {
                    temPlayerName = inPool.getOneCarInformation(i).getNName();
                    makeDst(temindex / 100, temPlayerName, "Car", false);
                    setBarLogin(temindex / 100);
                    break;
                }
            }
        }
        InforPoolClient.Logined = true;
        loginReplyProcess();
    }

    public void makeDst(int index, String name, String car, boolean isMyself) {
        Bitmap bm = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
        bm.eraseColor(0);
        Canvas c = new Canvas(bm);
        Paint p = new Paint();
        p.setColor(0xFF000000);
        p.setAntiAlias(true);
        p.setTextSize(20);
        p.setTextAlign(Paint.Align.CENTER);

        if (isMyself) {
            c.drawBitmap(carMyB_img, 0, 0, p);
        } else {
            c.drawBitmap(carB_img, 0, 0, p);
        }
        c.drawText(name, 78, 40, p);
        c.drawText(car, 78, 78, p);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureIDS[index]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bm, 0);
        // Reclaim storage used by bitmap and canvas.
        bm.recycle();
        bm = null;
        c = null;

    }

    public void loginReplyProcess() {
        for (int i = 0; i < inPool.getNCarNumber(); i++) {
            tempLName = inPool.getOneCarInformation(i).getCarListName();
            if (tempLName % 100 >= NEWCAR_FLAG) {
                inPool.getOneCarInformation(i).setCarListName(
                        (tempLName / 100) * 100 + IDLE_FLAG);
            }
        }
    }

    public void setBarLogin(int index) {
        barList[index].status = Bar.LOGIN;
        barList[index].timeCount = NUMCOUNT;
        switch (index) {
        case 0:
            barList[index].basicX = 33;
            barList[index].basicY = 28;
            barList[index].stepX = 0;
            barList[index].stepY = 134f / 50;
            break;
        case 1:
            barList[index].basicX = 263;
            barList[index].basicY = 28;
            barList[index].stepX = 0;
            barList[index].stepY = 134f / 50;
            break;
        case 2:
            barList[index].basicX = 33;
            barList[index].basicY = -75;
            barList[index].stepX = -184f / 50;
            barList[index].stepY = 0;
            break;
        case 3:
            barList[index].basicX = 263;
            barList[index].basicY = -75;
            barList[index].stepX = 184f / 50;
            barList[index].stepY = 0;
            break;
        }
    }

    public void deleteBar_Logout(int input) {
        temindex = input / 100;
        barList[temindex].status = Bar.LOGOUT;
        barList[temindex].timeCount = 20;

        listCar[temindex] = temindex * 100;
    }

    public void deleteBar_Dropout(int input) {
        temindex = input / 100;
        barList[temindex].status = Bar.LOGOUT;
        barList[temindex].timeCount = 20;

        listCar[temindex] = temindex * 100;
    }

    public void noBody(int index) {
        barList[index].status = Bar.NA;
    }

    private void drawLogin(int index) {

        if (barList[index].timeCount >= 0) {
            gl.glEnable(GL10.GL_TEXTURE_2D);
            panelX = (int) (barList[index].basicX + barList[index].timeCount
                    * barList[index].stepX);
            panelY = (int) (barList[index].basicY + barList[index].timeCount
                    * barList[index].stepY);

            gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureIDS[index]);
            ((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
                    GL11Ext.GL_TEXTURE_CROP_RECT_OES, tem, 0);
            ((GL11Ext) gl).glDrawTexiOES(panelX, panelY, 0, 256, 256);

            barList[index].timeCount--;
            if (barList[index].timeCount < 0) {
                barList[index].status = Bar.IDLE;
            }
        }
    }

    private void drawIDLE(int index) {
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureIDS[index]);
        ((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
                GL11Ext.GL_TEXTURE_CROP_RECT_OES, tem, 0);
        ((GL11Ext) gl).glDrawTexiOES((int) (barList[index].basicX),
                (int) (barList[index].basicY), 0, 256, 256);

    }

    private void drawLogout(int index) {
        if (barList[index].timeCount <= 50) {
            gl.glPushMatrix();
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl.glEnable(GL10.GL_TEXTURE_2D);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(index));

            gl.glVertexPointer(3, GL10.GL_FIXED, 0, carList_FaceVB);
            gl.glColorPointer(4, GL10.GL_FLOAT, 0, carList_FaceCB);
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, carList_FaceUTB);
            gl.glTranslatef(barList[index].basicX + barList[index].timeCount
                    * barList[index].stepX, barList[index].basicY
                    + barList[index].timeCount * barList[index].stepY, 0);
            gl.glDrawElements(GL10.GL_TRIANGLES, carList_index.length,
                    GL10.GL_UNSIGNED_SHORT, carList_FaceIB);

            gl.glPopMatrix();
            barList[index].timeCount++;
            if (barList[index].timeCount > 50) {
                barList[index].status = Bar.NA;
            }
        }
    }

    public void drawOut() {
        if (gl != null) {

//            if (DataToolKit.mapOn) {
//                gl.glPushMatrix();
//                gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//                gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
//                gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
//                gl.glEnable(GL10.GL_TEXTURE_2D);
//                if (StateValuePool.mapNext && mapchange < 5 + MAPCOUNT - 1) {
//                    mapchange = mapchange + 1;
//
//                }
//                if (StateValuePool.mapBack && mapchange > 5) {
//                    mapchange = mapchange - 1;
//
//                }
//                gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(mapchange));
//                StateValuePool.mapBack = false;
//                StateValuePool.mapNext = false;
//                map_FaceVB.position(0);
//                map_FaceCB.position(0);
//                map_FaceIB.position(0);
//                map_FaceUTB.position(0);
//                gl.glVertexPointer(3, GL10.GL_FIXED, 0, map_FaceVB);
//                gl.glColorPointer(4, GL10.GL_FLOAT, 0, map_FaceCB);
//                gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, map_FaceUTB);
//                gl.glDrawElements(GL10.GL_TRIANGLES, map_index.length,
//                        GL10.GL_UNSIGNED_SHORT, map_FaceIB);
//                gl.glPopMatrix();
//            } else
                if (StateValuePool.carOn) {
                // gl.glPushMatrix();
                // gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
                // gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
                // gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
                // gl.glEnable(GL10.GL_TEXTURE_2D);
//                if ( carchange < 8 + CARCOUNT - 1) {
//                    carchange = carchange + 1;

//                }
//                if ( carchange > 8) {
//                    carchange = carchange - 1;

//                }
            } else {
                for (index = 0; index < MAXCARN; index++) {
                    switch (barList[index].status) {
                    case Bar.NA:
                        // do nothing
                        break;
                    case Bar.IDLE:
                        drawIDLE(index);
                        break;
                    case Bar.LOGIN:
                        drawLogin(index);
                        break;
                    case Bar.LOGOUT:
                        drawLogout(index);
                        break;
                    }

                }
            }
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

        }
    }
    private class Bar {
    	static final int LOGIN = 101;
    	static final int LOGOUT = 100;
    	static final int IDLE = 110;
    	static final int NA = 120;
    	int status;
    	int timeCount;
    	byte carID;
    	float stepX, stepY;
    	int basicX, basicY;

    	public Bar() {
    		status = NA;
    	}
}
    @Override
    protected void finalize() throws Throwable {
        Log.e("Object finalize",this.getClass().getName());
        super.finalize();
    }
}

