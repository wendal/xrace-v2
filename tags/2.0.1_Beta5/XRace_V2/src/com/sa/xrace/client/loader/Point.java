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

public class Point {
    public float x;
    public float y;
    public float z;
    public float angle;
    // public float getX() {
    // return x;
    // }
    // public void setX(float x) {
    // this.x = x;
    // }
    // public float getY() {
    // return y;
    // }
    // public void setY(float y) {
    // this.y = y;
    // }
    // public float getZ() {
    // return z;
    // }
    // public void setZ(float z) {
    // this.z = z;
    // }
    // public float getAngle() {
    // return angle;
    // }
    // public void setAngle(float andle) {
    // this.angle = andle;
    // }
    
    @Override
    protected void finalize() throws Throwable {
        Log.e("Object finalize",this.getClass().getName());
        super.finalize();
    }
}
