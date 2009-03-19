package com.sa.xrace.client.scene;

import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

import com.sa.xrace.client.loader.Point;
import com.sa.xrace.client.math.Matrix4f;
import com.sa.xrace.client.math.Point3f;
import com.sa.xrace.client.model.t3DModel;
import com.sa.xrace.client.model.t3DObject;
import com.sa.xrace.client.toolkit.ObjectPool;

/**
 * @author sliao
 * @version $Id: Object.java 43 2009-02-26 14:15:40Z wendal1985 $
 */
/**
 * This class represents the Object appeared in the world
 */
public final class AppearableObject {
    private t3DModel mModel; // which model should be used for render
    private Point3f mPosition; // where the model should be render
    private float mAngle; // the angle model should rotate along Y axis

    private Matrix4f mTransformMatrix; // transform matrix
    public Point3f[][] mVerts; // point array of each t3DObject after
                               // transformed

    public AppearableObject(t3DModel model, Point point) {
        this.mModel = model;
        this.mPosition = new Point3f(point.x, point.y, point.z);
        this.mAngle = point.angle;
//        this.mTransformMatrix = null;
//        this.mVerts = null;
    }

//    public void translate() {
//        ObjectPool.gl.glTranslatef(mPosition.x, mPosition.y, mPosition.z);
//    }
//
//    public void rotate() {
//    	ObjectPool.gl.glRotatef(mAngle, 0, 1, 0);
//    }
//
//    public void scale() {
//        mModel.scale();
//    }

    public void updateTransformMatrix() {
        Matrix4f translateMatrix = new Matrix4f();
        Matrix4f rotateMatrix = new Matrix4f();
        Matrix4f scaleMatrix = new Matrix4f();
        translateMatrix.getTranlateMatrix(mPosition);
        rotateMatrix.getRotateMatrixY((float) Math.toRadians(mAngle));
        scaleMatrix.getScaleMatrix(mModel.mScale_x , mModel.mScale_y , mModel.mScale_z);
        mTransformMatrix = translateMatrix.multiply(rotateMatrix
                .multiply(scaleMatrix));

        mVerts = new Point3f[mModel._3Dobjects.size()][];

        int num = 0;
        Iterator<t3DObject> t3DObjectIterator = mModel._3Dobjects
                .iterator();
        while (t3DObjectIterator.hasNext()) {
            t3DObject object = t3DObjectIterator.next();
            mVerts[num] = new Point3f[object.numOfVerts];
            for (int index = 0; index < object.numOfVerts; index++) {
                mVerts[num][index] = new Point3f();
                mTransformMatrix.transformPoint(object.Verts[index],
                        mVerts[num][index]);
            }
            num++;
        }
    }

    public void draw() {
    	GL10 gl = ObjectPool.gl;
    	gl.glTranslatef(mPosition.x, mPosition.y, mPosition.z);
    	gl.glRotatef(mAngle, 0, 1, 0);
    	mModel.scale();
        mModel.draw();
    }
}
