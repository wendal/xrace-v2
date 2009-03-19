///:~
/**
 * XRace V2
 * <p/>Use for ... ...
 * <p/>ID: $Id$
 * Last Commit:  $Author$
 * @version $Revision: $
 * 
 */
package com.sa.xrace.client.model;

import java.util.ArrayList;

import android.util.Log;

import com.sa.xrace.client.math.Point3f;
import com.sa.xrace.client.toolkit.ObjectPool;

/**
 * @author sliao
 * @version $Id$
 */
/**
 * This class is used to store the model data which is created by 3DS MAX.
 */
public final class t3DModel {
	/**
	 * 合成两个类 : Model t3DModel
	 */
	public int mModelID; // model's ID
    public int mType; // model's type

    public float mScale_x;
    public float mScale_y;
    public float mScale_z;

    public void scale() {
        ObjectPool.gl.glScalef(mScale_x, mScale_y, mScale_z); // Scale the object on x, y
                                                   // and z axis
    }
    int numOfObjects; // number of objects
    int numOfMaterials; // number of material
    ArrayList<tMaterialInfo> Materials = new ArrayList<tMaterialInfo>(); // vectors keeping the materials
    public ArrayList<t3DObject> _3Dobjects = new ArrayList<t3DObject>(); // vectors keeping the objects
    // private Activity mainAcivity;

    public t3DModel(int modelID, int type,  Point3f scale) {
    	
//    	this.mModel = this;
        this.mModelID = modelID;
        this.mType = type;
        this.mScale_x = scale.x;
        this.mScale_y = scale.y;
        this.mScale_z = scale.z;
    }

    /**
     * This function would generate all the buffer for render according those
     * data has been imported .
     */
    public void generate() {
    	for (t3DObject _3Dobject : _3Dobjects) {
            if (_3Dobject.materialID >= 0) {
                String strFilename = Materials.get(_3Dobject.materialID).strFile;
                if (strFilename != null && !"".equals(strFilename)) {
                    _3Dobject.loadBitmap(strFilename);
                }
            }
            _3Dobject.createBuffer();
        }
    }

    /**
     * This function would render all objects in the model with the buffer that
     * has been generated by function generate().
     */
    public void draw() {
        for(t3DObject _3Dobject : _3Dobjects){
            _3Dobject.draw();
        }
    }

    public t3DObject getObject(String objectName) {
        for(t3DObject _3Dobject : _3Dobjects){
            if (_3Dobject.strName.equals(objectName)) {
                return _3Dobject;
            }
        }
        return null;
    }
    
    
    @Override
    protected void finalize() throws Throwable {
        Log.e("Object finalize",this.getClass().getName());
        super.finalize();
    }
}

/**
 * This class is used to keep the material data.
 */
class tMaterialInfo {
    String strName; // name of material
    String strFile; // texture's filename
    byte[] color = new byte[3];; // RGB color
}