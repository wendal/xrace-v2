///:~
/**
 * XRace V2
 * <p/>Use for ... ...
 * <p/>ID: $Id$
 * Last Commit:  $Author$
 * @version $Revision: $
 * 
 */
package com.sa.xrace.client.loader;

import android.util.Log;

import com.sa.xrace.client.math.Point3f;

public class ModelObj {
    public String filename;
    public String ID;
    public String type;
//    private float Radius;
    public Point3f scale = new Point3f();

    public LocationObj location;

//    public String getFilename() {
//        // System.out.println("In ModelBeans " + Filename);
//        return Filename;
//    }

//    public void setFilename(String filename) {
//        Filename = filename;
//    }
//
//    public String getID() {
//        return ID;
//    }
//
//    public void setID(String id) {
//        ID = id;
//    }
//
//    public String getType() {
//        return Type;
//    }
//
//    public void setType(String type) {
//        Type = type;
//    }

//    public float getRadius() {
//        return Radius;
//    }
//
//    public void setRadius(float radius) {
//        Radius = radius;
//    }

    public LocationObj getLocation() {
        if (this.location != null) {
            return location;
        } else {
            return null;
        }
    }

    public void setLocation(LocationObj location) {
        this.location = location;
    }

    public void setScale(Point3f scale) {
        this.scale = scale;
    }

    public Point3f getScale() {
        return scale;
    }

    public void setScale(String floatValues) {

        String[] xyz = floatValues.split(",");
        this.scale.x = Float.parseFloat(xyz[0]);
        this.scale.y = Float.parseFloat(xyz[1]);
        this.scale.z = Float.parseFloat(xyz[2]);

    }

    @Override
    protected void finalize() throws Throwable {
        Log.e("Object finalize",this.getClass().getName());
        super.finalize();
    }
}
