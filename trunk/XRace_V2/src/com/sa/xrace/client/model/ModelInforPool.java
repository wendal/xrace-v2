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
    public ModelInforPool(Point3f position) {
        mPosition = position;
        mScale = new Point3f(1.0f, 1.0f, 1.0f);
        // ObjectNumber.regNew(this);
    }

    // public void addModel(Model model)
    // {
    // // mModelVector.add(model);
    // mModelMap.put(model.getID(), model);
    // }

    public void addModel(t3DModel mod) {
//    	t3DModel mod = new t3DModel(modelID, type,  scale, radius);
        // mModelVector.add(mod);
        spAy.put(mod.mModelID, mod);
//        return mod;
    }

    /**
     * This function would generate all the buffer for render according those
     * data has been imported into the model objects.
     */
    public final void generate(GL10 gl) {
        // Iterator<Model> ModelIterator = mModelVector.iterator();

        // Collection<Model> collection = mModelVector.values();
        // Iterator<Model> modelIterator = spAy.values().iterator();
        //
        // // int size = mModelVector.size(); // for loading
        // // int interval = 180/mModelMap.size();
        //
        // while (modelIterator.hasNext()) {
        // // int temp = GLThread_Room.progress;
        // Model model = modelIterator.next();
        for (int index = 0; index < 5; index++) {
        	t3DModel model = spAy.get(index);
            if (model.mType != DataToolKit.COLLISION) {
                model.generate();
            }
            // 进度条
            // GLThread_Room.makeLoading(GLThread_Room.progress+interval,3);
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

    public void drawByID(GL10 gl, int modelID) {
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glTranslatef(mPosition.x, mPosition.y, mPosition.z);
        gl.glScalef(mScale.x, mScale.y, mScale.z);
        gl.glRotatef(mAngle, 0, 1, 0);

        t3DModel model = spAy.get(modelID);
        
        model.scale();

//        // ////////////////////////////////////////////
//        //当modelID = 0 或者 modelID = 1时,为Car
//        if (modelID == 0 || modelID == 1)// /////////buhao
//        {
//            mPresentationAngle += 1.2f;
//            if (mPresentationAngle >= 360f) {
//                mPresentationAngle = 0f;
//            }
//            gl.glRotatef(mPresentationAngle, 0, 1, 0);
//            Log.e("drawByID", "Here ?");
//        }
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

    public void setType(int type) {
        this.mCurrentType = type;
        // Iterator<Model> ModelIterator = mModelVector.iterator();
        // Collection<Model> collection = mModelMap.values();
        // Iterator<Model> modelIterator = collection.iterator();
        // while (modelIterator.hasNext()) {
        // Model model = modelIterator.next();
        for (int index = 0; index < 5; index++) {
        	t3DModel model = spAy.get(index);

//            if (model == null) {
//                Log.e("In ModelInforPool", "" + System.currentTimeMillis() + " Index: " + index);
//            }

//            try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
            
            if (model.mType == mCurrentType) {
                mCurrentModel = model;
                mAngle = 0.0f;
            }
        }
    }

    public void setTypeAndUpdate(int type, boolean previousOrNext) {
        this.mCurrentType = type;
        // Collection<Model> collection = mModelMap.values();
        // Iterator<Model> modelIterator = collection.iterator();
        // while (modelIterator.hasNext()) {
        // Model model = modelIterator.next();
        for (int index = 0; index < 5; index++) {
        	t3DModel model = spAy.get(index);
            if (model.mType == mCurrentType) {
                mCurrentModel = model;
                mAngle = 0.0f;
            }
        }
        updateCurrentModel(previousOrNext);
    }

    public int getType() {
        return mCurrentType;
    }

    public t3DModel getCurrentModel() {
        return mCurrentModel;
    }

    /**
     * @param currentModel
     *            the mCurrentModel to set
     */
    public void setMCurrentModel(t3DModel currentModel) {
        mCurrentModel = currentModel;
    }

    public t3DModel getModel(int modelID) {
        // Iterator<Model> modelIterator = mModelVector.iterator();
        // while (modelIterator.hasNext())
        // {
        // Model model = modelIterator.next();
        // if (model.getID() == modelID)
        // {
        // return model;
        // }
        // }
        return spAy.get(modelID);
    }

    public boolean updateCurrentModel(boolean previousOrNext) {
        // Iterator<Model> modelIterator = mModelVector.iterator();
        // Collection<Model> collection = mModelMap.values();
        // Iterator<Model> modelIterator = collection.iterator();
        //		
    	t3DModel previous = null;
        // while (modelIterator.hasNext())
        for (int index = 0; index < 5; index++) {
        	t3DModel model = spAy.get(index);
            if (model.mType == mCurrentType) {
                if (model == mCurrentModel) {
                    if (previousOrNext == true) {
                        if (previous == null) {
                            break;
                        } else {
                            mCurrentModel = previous;
                            mAngle = 0.0f;
                            return true;
                        }
                    } else {
                        for (int index2 = 0; index2 < 5; index2++) {
                        	t3DModel model2 = spAy.get(index2);
                            if (model.mType == mCurrentType) {
                                mCurrentModel = model2;
                                mAngle = 0.0f;
                                return true;
                            }
                        }
                    }
                }
                previous = model;
            }
        }
        return false;
    }

    public float getMAngle() {
        return mAngle;
    }

    public void setMAngle(float angle) {
        mAngle = angle;
    }

    private int mCurrentType = -1;
    private t3DModel mCurrentModel = null;
    private float mAngle = 0.0f;
    private float mPresentationAngle = 0.0f;
    private Point3f mScale;
    private Point3f mPosition;
    // private Vector<Model> mModelVector = new Vector<Model>();
    // private HashMap<Integer, Model> mModelMap = new HashMap<Integer,
    // Model>();
    private SparseArray<t3DModel> spAy = new SparseArray<t3DModel>(5);

}
