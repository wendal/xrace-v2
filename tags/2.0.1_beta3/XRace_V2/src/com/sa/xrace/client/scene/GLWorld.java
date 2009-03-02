package com.sa.xrace.client.scene;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.sa.xrace.client.collision.AABBbox;
import com.sa.xrace.client.collision.CollisionHandler;
import com.sa.xrace.client.collision.CollisionMap;
import com.sa.xrace.client.math.Matrix4f;
import com.sa.xrace.client.math.Point3f;
import com.sa.xrace.client.model.t3DModel;
import com.sa.xrace.client.pool.CarInforClient;
import com.sa.xrace.client.pool.InforPoolClient;
import com.sa.xrace.client.toolkit.DataToolKit;
import com.sa.xrace.client.toolkit.ObjectPool;
import com.sa.xrace.client.toolkit.StateValuePool;

/**
 * @author sliao
 * @version $Id$
 */
/**
 * This class is used to store and render those objects that would appear in the
 * World
 */
public class GLWorld {
    private ArrayList<AppearableObject> mObjectVector = new ArrayList<AppearableObject>();
    private InforPoolClient mInforPoolClient;
    private CollisionMap collisionMap;
    // private Frustum mFrustum;
    private Camera mCamera;
    private float mAngle;
    private Matrix4f mTranslateMatrix;
    private Matrix4f mScaleMatrix;
    private Matrix4f mRotateMatrix;
    private Matrix4f mCombineMatrix;

    private CarInforClient myCar;
    private CollisionHandler collisionHandler;

    private boolean collisionFlag;

    // private Report report;
    // private int collisionStyle;
    // private Line2f tempLine;
    // private Point3f tempCollisionPoint;
    // private PostManagerClient mPostManager;

    public GLWorld() {
        mInforPoolClient = ObjectPool.inPoolClient;
        mCamera = ObjectPool.camera;
        // mFrustum = new Frustum();
        mTranslateMatrix = new Matrix4f();
        mRotateMatrix = new Matrix4f();
        mScaleMatrix = new Matrix4f();
        mCombineMatrix = new Matrix4f();
        collisionMap = new CollisionMap();
        // mPostManager = postmanager;

        collisionHandler = new CollisionHandler();

    }

    public void generateCollisionMap() {
    	for (int index = 0; index < mObjectVector.size(); index++) {
			AppearableObject appearableObject = mObjectVector.get(index);
            if (appearableObject.mVerts != null) {
                collisionMap.generateWallCollisionMap(appearableObject);
            }
        }
        collisionMap.generateWallLines();
        collisionMap.prepare();
    }

//    private static long lastrun = 0;

    public void draw(GL10 gl, long timeElapsed) {
//        Log.e("Since Last run ", "" + (System.currentTimeMillis() - lastrun));
//        lastrun = System.currentTimeMillis();
    	final int time = (int)timeElapsed;
        myCar = ObjectPool.myCar;
        if (StateValuePool.isBeginWait) {
            myCar.updateSpeedByKeyboard(time / 80);
            myCar.updateDirectionByKeyboard((int) time / 80);
            synchronized (mInforPoolClient) {
                mInforPoolClient.prepareAllCarInformation(
                        time / 10,
                        InforPoolClient.mSensor[1] / 10,
                        InforPoolClient.mSensor[0] / 100);
            }
        }
        myCar.weakOff(time / 80);

        float myDirection = myCar.getNDirection();
        float speed = myCar.getNSpeed();
        float changeDirection = myCar.getNChangeDirection();
        int speedKeyState = myCar.getSpeedKeyState();
        int directionKeyState = myCar.getDirectionKeyState();
        int speedSensorState = myCar.getSpeedSensorState();
        int directionSensorState = myCar.getDirectionSensorState();
        int lookDiection = myCar.getLookDirction();
        Point3f myCenter = new Point3f(myCar.getNXPosition(),
                CarInforClient.CAR_CENTER_Y, myCar.getNYPosition());
        Point3f cameraCenter = new Point3f(myCar.getNXPosition(),
                DataToolKit.CAMERA_CENTER_Y, myCar.getNYPosition());

        mCamera.updateCamera(cameraCenter, myDirection, changeDirection, speed,
                speedKeyState, directionKeyState, speedSensorState,
                directionSensorState, lookDiection);

        // collisionMap.drawWall(gl, mCamera);

//        for (AppearableObject appearableObject : mObjectVector) {
//        Log.e("GLWorld", "Start new time(before for)");
		for (int index = 0; index < mObjectVector.size(); index++) {
			AppearableObject appearableObject = mObjectVector.get(index);
            if (appearableObject.mVerts != null) {
                continue;
            }

            gl.glMatrixMode(GL10.GL_MODELVIEW); // set the matrix which would be
                                                // changed
            gl.glLoadIdentity();
            // make all the transform matrix to be identity matrix
            mCamera.setCamera(gl);
            appearableObject.translate(gl);
            appearableObject.rotate(gl);
            appearableObject.scale(gl);
            // mFrustum.updateFrustum(gl);
            // if (Frustum.checkSphere(object.getPosition(),
            // object.getModel().getRadius()) == true)
            // {
            appearableObject.draw();
//            Log.e("GLThread_Room", "in for");
            // }
        }

        if (mInforPoolClient != null) {
            // if there are other players, not only myself
            if (mInforPoolClient.getNCarNumber() != 1) {
                // we need update the others' car first, we need their data be
                // updated to this frame first
                for (int index = 0; index < mInforPoolClient.getNCarNumber(); index++) {
                    // do some optimization according to the car's position
                    if (index != mInforPoolClient.getMyCarIndex()) {
                    	
                    	Log.e("GLWorld", "NCarNumber: " + mInforPoolClient.getNCarNumber());
                    	
                        CarInforClient car = mInforPoolClient
                                .getOneCarInformation(index);
                        Point3f center = new Point3f(car.getNXPosition(),
                                CarInforClient.CAR_CENTER_Y, car
                                        .getNYPosition());
                        float direction = car.getNDirection();
                        t3DModel model = car.getModel();
                        gl.glMatrixMode(GL10.GL_MODELVIEW); // set the matrix
                                                            // which would be
                                                            // changed
                        gl.glLoadIdentity(); // make all the transform matrix to
                                             // be identity matrix
                        mCamera.setCamera(gl);
                        gl.glTranslatef(center.x, center.y, center.z);
                        gl
                                .glRotatef((float) Math.toDegrees(direction),
                                        0, 1, 0);
                        model.scale();
                        mTranslateMatrix.getTranlateMatrix(center);
                        mRotateMatrix.getRotateMatrixY(myDirection);
                        mCombineMatrix = mTranslateMatrix
                                .multiply(mRotateMatrix);

                        // mCombineMatrix =
                        // mScaleMatrix.multiply(mRotateMatrix.multiply(mTranslateMatrix));
                        // ////////////////////////
                        updateCarAABB(index, mCombineMatrix);
                        // ////////////////////////

                        // mFrustum.updateFrustum(gl);
                        // if (Frustum.checkSphere(center, model.getRadius()) ==
                        // true)
                        // {
                        model.draw();
                        // }
                    }
                }
            }
            // then my car
            t3DModel model = myCar.getModel();
            gl.glMatrixMode(GL10.GL_MODELVIEW); // set the matrix which would be
                                                // changed
            gl.glLoadIdentity(); // make all the transform matrix to be identity
                                 // matrix
            mCamera.setCamera(gl);
            gl.glTranslatef(myCenter.x, myCenter.y, myCenter.z);
            gl.glRotatef((float) Math.toDegrees(myDirection), 0, 1, 0);
            model.scale();
            mTranslateMatrix.getTranlateMatrix(myCenter);
            mRotateMatrix.getRotateMatrixY(myDirection);
            mCombineMatrix = mTranslateMatrix.multiply(mRotateMatrix);
            mScaleMatrix.getScaleMatrix(model.mScale_x , model.mScale_y , model.mScale_z );

            mCombineMatrix = mTranslateMatrix.multiply(mRotateMatrix
                    .multiply(mScaleMatrix));

            // mCombineMatrix =
            // mScaleMatrix.multiply(mRotateMatrix.multiply(mTranslateMatrix));

            // ////////////////////////
            updateCarAABB(mInforPoolClient.getMyCarIndex(), mCombineMatrix);
            collisionProcess(mInforPoolClient.getMyCarIndex());

            // ////////////////////////
            // mFrustum.updateFrustum(gl);
            // if (Frustum.checkSphere(myCenter, model.getRadius()) == true)
            {
                myCar.draw(gl);
            }

        }

        // mInforPoolClient.getOneCarInformation(mInforPoolClient.getMyCarIndex()).getMTranformedBox().rectangle.prepare();
        // mInforPoolClient.getOneCarInformation(mInforPoolClient.getMyCarIndex()).getMTranformedBox().rectangle.drawCarLine(gl,mCamera);
    }

    public void rotate(GL10 gl) {
        gl.glRotatef(mAngle, 0, 1, 0);
        mAngle += 1.2f;
    }

    public void addObject(AppearableObject appearableObject) {
        mObjectVector.add(appearableObject);
    }

    public void updateCarAABB(int carIndex, Matrix4f matrix) {
        mInforPoolClient.getOneCarInformation(carIndex).getMTranformedBox()
                .transformBox(
                        mInforPoolClient.getOneCarInformation(carIndex)
                                .getMOriginalBox(), matrix);
    }

    public void collisionProcess(int carIndex) {
        // check whether I collide with another car
        if (mInforPoolClient.getNCarNumber() != 1) {
            for (int i = 0; i < mInforPoolClient.getNCarNumber(); i++) {
                if (i != carIndex) {
                    collisionFlag = mInforPoolClient.getOneCarInformation(
                            carIndex).getMTranformedBox()
                            .checkCollisionWithCar(
                                    mInforPoolClient.getOneCarInformation(i)
                                            .getMTranformedBox());
                }
                if (collisionFlag != false) {
                    // do something
                    carCollisionHandle(mInforPoolClient
                            .getOneCarInformation(carIndex), mInforPoolClient
                            .getOneCarInformation(i));
                    collisionFlag = false;
                }
            }
        }

        // check whether I collide with wall
        collisionFlag = mInforPoolClient.getOneCarInformation(carIndex)
                .getMTranformedBox().checkCollisionWithScene(collisionMap);
        if (collisionFlag != false) {
            // do something
            wallCollisionHandle(mInforPoolClient.getOneCarInformation(carIndex));
            collisionFlag = false;
        }

        // check whether i collide with the finishing line
        collisionFlag = mInforPoolClient.getOneCarInformation(carIndex)
                .getMTranformedBox().checkCollisionWithFinishLine(collisionMap);
        if (collisionFlag != false) {
            // do something
            finishLineHandle(mInforPoolClient.getOneCarInformation(carIndex)
                    .getMTranformedBox());
            collisionFlag = false;
        }
    }

    public void finishLineHandle(AABBbox box) {
        // report =
        box.getReport();
        // collisionStyle =
        box.getCollisionStyle();

    }

    public void finishLineHandle(CarInforClient oneCarInfor) {
        collisionHandler.finishLineCollisonHandle(oneCarInfor);
    }

    private void wallCollisionHandle(CarInforClient oneCarInfor) {
        // oneCarInfor.getMTranformedBox().wallCollisionHandle(oneCarInfor);
        collisionHandler
                .wallCollisionHandle(oneCarInfor, collisionMap, mCamera);
    }

    private void carCollisionHandle(CarInforClient myCarInfor,
            CarInforClient targetCarInfor) {
        // oneCarInfor.getMTranformedBox().carCollisionHandle(oneCarInfor);
        collisionHandler.carCollisionHandle(myCarInfor, targetCarInfor
                .getNXPosition(), targetCarInfor.getNYPosition(), mCamera);

    }
}
