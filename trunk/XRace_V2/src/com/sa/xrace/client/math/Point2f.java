///:~
/**
 * XRace V2
 * <p/>Use for ... ...
 * <p/>ID: $Id$
 * Last Commit:  $Author$
 * @version $Revision: $
 * 
 */
package com.sa.xrace.client.math;

/**
 * @author sliao
 * @version $Id$
 */
/**
 * This class is used to store a point in a 3D space
 */
public class Point2f implements Cloneable{
    public float x = 0.f;
    public float y = 0.f;

    @Override
    public Point2f clone() {
        Point2f p2f = new Point2f();
        p2f.x = this.x;
        p2f.y = this.y;
        return p2f;
    }

}
