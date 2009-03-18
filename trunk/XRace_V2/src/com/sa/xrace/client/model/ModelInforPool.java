///:~
/**
 * XRace V2
 * <p/>Use for ... ...
 * <p/>ID: $Id$
 * Last Commit:  $Author$
 * @version $Revision: $
 * 
 */

/* 
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sa.xrace.client.model;

import javax.microedition.khronos.opengles.GL10;

import android.util.SparseArray;

import com.sa.xrace.client.math.Point3f;
import com.sa.xrace.client.toolkit.DataToolKit;
import com.sa.xrace.client.toolkit.ObjectPool;

/**
 * @author sliao
 * @version $Id$
 */
/**
 * This class is used to store and render the t3DModel objects.
 */
public final class ModelInforPool {
    /**
     * 只有一个实例,考虑用单例模式,或者转为工具类
     * 
     * @param position
     */
    public ModelInforPool() {
        mPosition = new Point3f(0, 0, -3.0f);
        mScale = new Point3f(1.0f, 1.0f, 1.0f);
        // ObjectNumber.regNew(this);
    }
    public void addModel(t3DModel mod) {
        spAy.put(mod.mModelID, mod);
        switch(mod.mType){
        case DataToolKit.CAR :
        	if(carModelPool[0] == null){
        		carModelPool[0] = mod;
        		mCurrentModel = mod;
        	}else {
				carModelPool[1] = mod;
			}
        	break;
        case DataToolKit.ROOM :
        	garageModel = mod;
        	break;
        
        case DataToolKit.MAP :
        	roadModel = mod;
        	break;
//        case DataToolKit.COLLISION :
//        	road_line_model = mod;
        }
//        Log.e("Model Add ID", ""+mCurrentModel);
    }
       

    /**
     * This function would generate all the buffer for render according those
     * data has been imported into the model objects.
     */
    public final void generate(GL10 gl) {
        for (int index = 0; index < 5; index++) {
        	t3DModel model = spAy.get(index);
            if (model.mType != DataToolKit.COLLISION && model.mType != DataToolKit.MAP) {
                model.generate();
            }
        }
    }

    /**
     * This function would render the current model
     */
    public void draw() {
        GL10 gl = ObjectPool.gl;
        if (mCurrentModel != null) {
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();
            // GLU.gluLookAt(gl, 0, 0, 0, 0, 0.1f, 0, 0, 1, 0);

            gl.glTranslatef(mPosition.x, mPosition.y, mPosition.z);
            gl.glScalef(mScale.x, mScale.y, mScale.z);
            gl.glRotatef(mAngle, 0, 1, 0);

            if (mCurrentModel.mType == DataToolKit.CAR) {
                mPresentationAngle += 1.2f;
                if (mPresentationAngle >= 360f) {
                    mPresentationAngle = 0f;
                }
                gl.glRotatef(mPresentationAngle, 0, 1, 0);
            }

            mCurrentModel.scale();
            mCurrentModel.draw();
        }
    }

    public void drawGarageNow() {

        setPosition(0, -2.5f, -7.4f);
        setScale(0.04f, 0.04f, 0.04f);
        
        GL10 gl = ObjectPool.gl;
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glTranslatef(mPosition.x, mPosition.y, mPosition.z);
        gl.glScalef(mScale.x, mScale.y, mScale.z);
        gl.glRotatef(mAngle, 0, 1, 0);

        t3DModel model = garageModel;
        
        model.scale();
        model.draw();

        
    }

    public void setScale(float x, float y, float z) {
        mScale.x = x;
        mScale.y = y;
        mScale.z = z;
    }

    public void setPosition(float x, float y, float z) {
        mPosition.x = x;
        mPosition.y = y;
        mPosition.z = z;
    }

//    public void setType(int type) {
//        this.mCurrentType = type;
//        mCurrentModel = carModelPool[0];
//        mAngle = 0;
//    }

    public void nextCarModel() {
//        this.mCurrentType = DataToolKit.CAR;
        if(carModelPool[0] == mCurrentModel){
        	mCurrentModel = carModelPool[1];
            }else{
            	mCurrentModel = carModelPool[0];
        }
        mAngle = 0;
//        nextCarModel(previousOrNext);
    }

//    public int getType() {
//        return mCurrentType;
//    }

    public t3DModel getCurrentCarModel() {
        return mCurrentModel;
    }

//    /**
//     * @param currentModel
//     *            the mCurrentModel to set
//     */
//    public void setMCurrentModel(t3DModel currentModel) {
//        mCurrentModel = currentModel;
//    }

    public t3DModel getModel(int modelID) {
        return spAy.get(modelID);
    }

//    public boolean nextCarModel() {
////    	t3DModel previous = null;
////        mCurrentModel = model2;
//    	
//        mAngle = 0;
//                                
//        return false;
//    }

//    public int getMAngle() {
//        return mAngle;
//    }

    public void setMAngle(int angle) {
        mAngle = angle;
    }

//    private int mCurrentType = -1;
    private t3DModel mCurrentModel = null;
    private int mAngle = 0;
    private float mPresentationAngle = 0.0f;
    private Point3f mScale;
    private Point3f mPosition;
    // private Vector<Model> mModelVector = new Vector<Model>();
    // private HashMap<Integer, Model> mModelMap = new HashMap<Integer,
    // Model>();
    private SparseArray<t3DModel> spAy = new SparseArray<t3DModel>(5);
    
    private t3DModel [] carModelPool = new t3DModel[2];
    
    public t3DModel roadModel ;
//    
//    public t3DModel road_line_model;
//    
    private t3DModel garageModel;
   
    public void removeGarageModel(){
        spAy.remove(garageModel.mModelID);
        garageModel = null;
    }

//    public t3DModel currentCarMode;
}
