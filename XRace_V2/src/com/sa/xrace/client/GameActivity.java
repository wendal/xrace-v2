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

import android.app.Activity;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.HandlerThread;
import android.view.KeyEvent;
import android.view.Window;

import com.sa.xrace.client.math.Point3f;
import com.sa.xrace.client.model.ModelInforPool;
import com.sa.xrace.client.network.NetWorkManager;
import com.sa.xrace.client.pool.CarInforClient;
import com.sa.xrace.client.pool.GIPool;
import com.sa.xrace.client.pool.InforPoolClient;
import com.sa.xrace.client.pool.WRbarPool;
import com.sa.xrace.client.scene.Camera;
import com.sa.xrace.client.scene.GLWorld;
import com.sa.xrace.client.toolkit.DataToolKit;
import com.sa.xrace.client.toolkit.NetworkToolKit;
import com.sa.xrace.client.toolkit.ObjectPool;
import com.sa.xrace.client.toolkit.StateValuePool;

public class GameActivity extends Activity implements SensorListener {
    private Camera mCamera;
    private ModelInforPool mModelInforPool;

    private InforPoolClient inPool;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        ObjectPool.mHandler.post(new initThread(this));
    }

    private static class initThread extends HandlerThread {
        private static boolean isInited = false;
        public initThread(GameActivity activity) {
            super("init GameActivity");
            ObjectPool.activity = activity;
        }

        public void run() {
            if (isInited) {
                ;
            } else {
                // 将部分共用对象写入对象池
                if (ObjectPool.resources == null) {
                    ObjectPool.resources = ObjectPool.activity.getResources();
                }
                if (ObjectPool.assetManager == null) {
                    ObjectPool.assetManager = ObjectPool.activity.getAssets();
                }

                ObjectPool.mHandler.post(new NetWorkManager());

                ObjectPool.activity.mModelInforPool = new ModelInforPool();
                // 将mModelInforPool加入对象池
                ObjectPool.mModelInforPool = ObjectPool.activity.mModelInforPool;
                // mModelImport = new ModelImport();
                ObjectPool.activity.mCamera = new Camera();
                ObjectPool.camera = ObjectPool.activity.mCamera;

                ObjectPool.activity.inPool = new InforPoolClient();
                // 将inPoolClient加入对象池
                ObjectPool.inPoolClient = ObjectPool.activity.inPool;
                
                ObjectPool.myCar = ObjectPool.inPoolClient.getOneCarInformation(ObjectPool.inPoolClient.getMyCarIndex());
                ObjectPool.myCar.setNName(NetworkToolKit.NAME);
                // 将WRbarPool加入对象池
                ObjectPool.barPool = new WRbarPool();
                // 将GIPool加入对象池
                ObjectPool.giPool = new GIPool();
                // 把GLWorld加入对象池
                ObjectPool.mWorld = new GLWorld();

                GameView drawView = new GameView(ObjectPool.activity
                        .getApplication());
                ObjectPool.activity.setContentView(drawView);

                isInited = true;
            }
        }
    }

    public boolean onKeyDown(int arg0, KeyEvent arg1) {

        if (GLThread_Room.mPhase == DataToolKit.GAME_ROOM) {
            onRoomWaiting(arg0, arg1);
        } else if (GLThread_Room.mPhase == DataToolKit.GAME_RUNNING) {
            onGameRunning(arg0, arg1);
        }
        return super.onKeyDown(arg0, arg1);
    }

    private void onRoomWaiting(int arg0, KeyEvent arg1) {
        switch (arg0) {
        case KeyEvent.KEYCODE_DPAD_LEFT:
            StateValuePool.carOn = true;
                // ///////////////////////////////////////
                mModelInforPool.nextCarModel();
                // ///////////////////////////////////////

                StateValuePool.carBack = true;
                StateValuePool.carNext = false;
            
            break;
        case KeyEvent.KEYCODE_DPAD_RIGHT:
            StateValuePool.carOn = true;
                // ///////////////////////////////////////
                mModelInforPool.nextCarModel();
                // ///////////////////////////////////////
                StateValuePool.carNext = true;
                StateValuePool.carBack = false;
            
            break;
        case KeyEvent.KEYCODE_DPAD_DOWN:
            StateValuePool.carOn = false;
            break;
        case KeyEvent.KEYCODE_DPAD_UP:
            StateValuePool.carOn = true;
            break;
        case KeyEvent.KEYCODE_ENTER:
            ObjectPool.myCar.setModel(mModelInforPool.getCurrentCarModel());
            ObjectPool.myCar.generateAABBbox();
            NetWorkManager.mPostManager.sendCarTypePostToServer();
            NetWorkManager.mPostManager.sendStartPostToServer();
            break;
        }
    }

    public void initGameRunning() {
        ObjectPool.barPool = null;//释放
        GLThread_Room.mPhase = DataToolKit.GAME_RUNNING;
        CarInforClient myCar = ObjectPool.myCar;
        Point3f center = new Point3f(myCar.getNXPosition(),
                DataToolKit.CAMERA_CENTER_Y, myCar.getNYPosition());
        mCamera.initCamera(center, new Point3f(0.0f, 1.0f, 0.0f), myCar
                .getNDirection(), DataToolKit.FAR_DISTANCE);
        mCamera.setEye(mCamera.getEye().x, DataToolKit.CAMERA_EYE_Y, mCamera
                .getEye().z);
    }

    private void onGameRunning(int arg0, KeyEvent arg1) {

        CarInforClient car = ObjectPool.myCar;
        switch (arg0) {
        case KeyEvent.KEYCODE_SPACE:
            if (car.getNSpeed() > 1.0f) {
                car.setNSpeed(car.getNSpeed() - 2.0f);
            } else if (car.getNSpeed() < -1.0f) {
                car.setNSpeed(car.getNSpeed() + 2.0f);
            } else {
                car.setNSpeed(0);
            }
            break;
        case KeyEvent.KEYCODE_N:
            mCamera.setDistance(mCamera.getDistance() + 10.0f);
            break;
        case KeyEvent.KEYCODE_M:
            mCamera.setDistance(mCamera.getDistance() - 10.0f);
            break;
        case KeyEvent.KEYCODE_U:
            mCamera.setEye(mCamera.getEye().x, mCamera.getEye().y + 10.0f,
                    mCamera.getEye().z);
            break;
        case KeyEvent.KEYCODE_D:
            mCamera.setEye(mCamera.getEye().x, mCamera.getEye().y - 10.0f,
                    mCamera.getEye().z);
            break;
        case KeyEvent.KEYCODE_B:
            car.setLookDirction(CarInforClient.LOOK_BACK);
            break;
        case KeyEvent.KEYCODE_T:
            // 打开Debug功能
            StateValuePool.isDebug = true;
            break;

        // {{for testing
        case KeyEvent.KEYCODE_DPAD_UP:
            if (StateValuePool.isBeginWait) {
                car.setSpeedKeyState(CarInforClient.SPEED_UP_KEYBOARD);
            }
            break;
        case KeyEvent.KEYCODE_DPAD_DOWN:
            if (StateValuePool.isBeginWait) {
                car.setSpeedKeyState(CarInforClient.SPEED_DOWN_KEYBOARD);
            }
            break;
        case KeyEvent.KEYCODE_DPAD_LEFT:
            if (StateValuePool.isBeginWait) {
                car
                        .setDirectionKeyState(CarInforClient.DIRECTION_LEFT_KEYBOARD);
            }
            break;
        case KeyEvent.KEYCODE_DPAD_RIGHT:
            if (StateValuePool.isBeginWait) {
                car
                        .setDirectionKeyState(CarInforClient.DIRECTION_RIGHT_KEYBOARD);
            }
            break;
        }
    }

    public boolean onKeyUp(int arg0, KeyEvent arg1) {
        if (GLThread_Room.mPhase == DataToolKit.GAME_RUNNING) {
            CarInforClient car = ObjectPool.myCar;
            switch (arg0) {
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
                // 关闭Debug功能
                StateValuePool.isDebug = false;
                break;
            }
        }
        return super.onKeyUp(arg0, arg1);
    }


    /**
     * The method of the interface SensorListener
     * 
     * @see android.hardware.SensorListener#onSensorChanged(int, float[])
     */
    public void onSensorChanged(int sensor, float[] values) {
        // nowTime = System.currentTimeMillis();
        // if(lastTime ==0){
        // lastTime = nowTime;
        // }
        // timeElapsed = nowTime - lastTime;
        // lastTime = nowTime;
        // if(isStart){
        // timeadd += timeElapsed;
        // if(timeadd >=30){
        // mPostManager.sendNormalPostToServer();
        // timeadd=0;
        // }
        // }
        switch (sensor) {
        case SensorManager.SENSOR_ORIENTATION: {
            // {{sliao
            CarInforClient car = inPool.getOneCarInformation(inPool
                    .getMyCarIndex());

            InforPoolClient.mSensor[0] = values[2];// acceleration
            if (values[2] > 0.01f) {
                car.setSpeedSensorState(CarInforClient.SPEED_UP_SENSOR);
            } else if (values[2] < -0.01f) {
                car.setSpeedSensorState(CarInforClient.SPEED_DOWN_SENSOR);
            } else {
                car.setSpeedSensorState(CarInforClient.NO_SENSOR_EVENT);
            }
            if (car.getNSpeed() > CarInforClient.PRECISION) // when the car's
                                                            // speed is bigger
                                                            // than zero
            {
                InforPoolClient.mSensor[1] = -values[1];// changeDirection
            } else if (car.getNSpeed() < -CarInforClient.PRECISION) // when the
                                                                    // car's
                                                                    // speed is
                                                                    // smaller
                                                                    // than zero
            {
                InforPoolClient.mSensor[1] = values[1];// changeDirection
            }

            if (-values[1] > 0.01f) {
                car
                        .setDirectionSensorState(CarInforClient.DIRECTION_LEFT_SENSOR);
            } else if (-values[1] < -0.01f) {
                car
                        .setDirectionSensorState(CarInforClient.DIRECTION_RIGHT_SENSOR);
            } else {
                car.setDirectionSensorState(CarInforClient.NO_SENSOR_EVENT);
            }
            // }}sliao
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
}
