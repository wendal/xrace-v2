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
    public int ID;
    public int type;
    public Point3f scale = new Point3f();

    public LocationObj location;

    public LocationObj getLocation() {
//        if (this.location != null) {
            return location;
//        } else {
//            return null;
//        }
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
