///:~
/**
 * XRace V2
 * <p/>Use for ... ...
 * <p/>ID: $Id$
 * Last Commit:  $Author$
 * @version $Revision: $
 * 
 */
package com.sa.xrace.client.collision;

import android.util.Log;

import com.sa.xrace.client.math.Matrix4f;
import com.sa.xrace.client.math.Point3f;
import com.sa.xrace.client.model.t3DModel;
import com.sa.xrace.client.model.t3DObject;
import com.sa.xrace.client.toolkit.DataToolKit;

public class AABBbox {
    public Point3f mLeftLower; // left lower point of the box
    public Point3f mRightUpper; // right upper point of the box

    public Rectangle rectangle;

    public float mWidth;
    public float mHeight;
    public float mLength;
    public float radius;
    // private int recLineCount=0;
    // private int wallLineCount=0;
    // private int collisionLine[] = new int[2];

//    private Point3f tempWallPointF, tempWallPointS;
    private Line2f tempWallLine;
    private Line2f tempCarLine;

    private Report report = new Report();

    // private Point3f collisionPoint;
    // private int collisionDir;

    public static final int NONE = 0;
    public static final int FRONT = 1;
    public static final int MIDDLE = 2;
    public static final int BACK = 3;

    private int collisionStyle;
    // public static final int CORNER=10;
    // public static final int FACE=11;
    // public static final int HORN=12;

    public static final float BIG_NUMBER = 1e37f;

    public AABBbox() {
//        Log.e("New AABBbox", "Create.....................");
        mLeftLower = new Point3f(BIG_NUMBER, BIG_NUMBER, -BIG_NUMBER);
        mRightUpper = new Point3f(-BIG_NUMBER, -BIG_NUMBER, +BIG_NUMBER);
        mWidth = mHeight = mLength = 0.0f;
        rectangle = new Rectangle();
    }

    public AABBbox(t3DModel model) {
//        Log.e("New AABBbox", "Create..222...................");
        mLeftLower = new Point3f(BIG_NUMBER, BIG_NUMBER, -BIG_NUMBER);
        mRightUpper = new Point3f(-BIG_NUMBER, -BIG_NUMBER, +BIG_NUMBER);
        mWidth = mHeight = mLength = 0.0f;
        getAABBbox(model);
    }

    public void getAABBbox(t3DModel model) {
        try {
//            Iterator<t3DObject> t3DObjectIterator = model._3Dobjects
//                    .iterator();
//            while (t3DObjectIterator.hasNext()) {
//                t3DObject object = t3DObjectIterator.next();
            for (t3DObject _3Dobject : model._3Dobjects) {
                if (_3Dobject.materialID >= 0) 
                for (int index = 0; index < _3Dobject.numOfVerts; index++) {
                    if (_3Dobject.Verts[index].x < mLeftLower.x) // get the
                                                              // minimize on x
                                                              // axis
                    {
                        mLeftLower.x = _3Dobject.Verts[index].x;
                    } else if (_3Dobject.Verts[index].x > mRightUpper.x) // get the
                                                                      // maximize
                                                                      // on x
                                                                      // axis
                    {
                        mRightUpper.x = _3Dobject.Verts[index].x;
                    }
                    if (_3Dobject.Verts[index].y < mLeftLower.y) // get the
                                                              // minimize on y
                                                              // axis
                    {
                        mLeftLower.y = _3Dobject.Verts[index].y;
                    } else if (_3Dobject.Verts[index].y > mRightUpper.y) // get the
                                                                      // maximize
                                                                      // on y
                                                                      // axis
                    {
                        mRightUpper.y = _3Dobject.Verts[index].y;
                    }
                    if (_3Dobject.Verts[index].z < mRightUpper.z) // get the
                                                               // minimize on z
                                                               // axis
                    {
                        mRightUpper.z = _3Dobject.Verts[index].z;
                    } else if (_3Dobject.Verts[index].z > mLeftLower.z) // get the
                                                                     // maximize
                                                                     // on z
                                                                     // axis
                    {
                        mLeftLower.z = _3Dobject.Verts[index].z;
                    }
                }
            }
            mWidth = mRightUpper.x - mLeftLower.x;
            mHeight = mRightUpper.y - mLeftLower.y;
            mLength = mRightUpper.z - mLeftLower.z;

            radius = (float) Math.sqrt(mRightUpper.x * mRightUpper.x
                    + mRightUpper.z * mRightUpper.z);

            rectangle = new Rectangle(new Point3f(mLeftLower.x, 0.0f,
                    mRightUpper.z), new Point3f(mRightUpper.x, 0.0f,
                    mRightUpper.z), new Point3f(mLeftLower.x, 0.0f,
                    mLeftLower.z), new Point3f(mRightUpper.x, 0.0f,
                    mLeftLower.z));
            // //////////////////////////////
            // //////////////////////////////
            // //////////////////////////////
            printlRectangle();// ////
            // //////////////////////////////
            // //////////////////////////////
            // //////////////////////////////

        } catch (Exception e) {
            Log.e("getAABBbox", "getAABBbox");
            e.printStackTrace();
        }
    }

    public boolean containPoint(Point3f point) {
        return (point.x >= mLeftLower.x) && (point.x <= mRightUpper.x)
                && (point.y >= mLeftLower.y) && (point.y <= mRightUpper.y)
                && (point.z >= mLeftLower.z) && (point.z <= mRightUpper.z);
    }

    public void transformBox(AABBbox box, Matrix4f matrix) {
        matrix.transformPoint(box.rectangle.mLeftLower,
                this.rectangle.mLeftLower);
        matrix.transformPoint(box.rectangle.mLeftUpper,
                this.rectangle.mLeftUpper);
        matrix.transformPoint(box.rectangle.mRightLower,
                this.rectangle.mRightLower);
        matrix.transformPoint(box.rectangle.mRightUpper,
                this.rectangle.mRightUpper);

        rectangle.generateLines();
        rectangle.generateCenter();
    }

    public boolean checkCollisionWithCar(AABBbox box) {
        report.refreshReport();
        // for every line in rectangle check whether it collide with walls
        for (int i = DataToolKit.UP; i <= DataToolKit.RIGHT; i++) {
            for (int n = 0; n < box.rectangle.lines.length; n++) {
                tempCarLine = box.rectangle.lines[n];
                if (rectangle.lines[i].isCross(tempCarLine)) {
                    report.validReport(Report.CAR_REPORT);
                    report.selfLineID.add(i);
                    report.targetLineID.add(n);
                    report.targetLines.add(tempWallLine);
                }
            }
        }
        collisionStyle = report.checkReport();
        switch (collisionStyle) {
        case Report.INVALID_REPORT: {
            // do nothing
            return false;
        }
        case Report.RCORNER: {
            return true;
        }
        case Report.RFACE: {
            return true;
        }
        case Report.RHORN: {
            return true;
        }
        default:
            return false;
        }
    }

    public boolean checkCollisionWithScene(CollisionMap map) {
        if (map.walls == null) {
            return false;
        }
        report.refreshReport();
        // for every line in rectangle check whether it collide with walls
        for (int i = DataToolKit.UP; i <= DataToolKit.RIGHT; i++) {

            for (int n = 0; n < map.wallLines.size(); n++) {
                tempWallLine = map.wallLines.get(n);
                // see if this wall collide with this line of rectangle
                if (rectangle.lines[i].isCross(tempWallLine)) {
                    report.validReport(Report.WALL_REPORT);
                    report.selfLineID.add(i);
                    report.targetLineID.add(n);
                    report.targetLines.add(tempWallLine);
                }
            }

        }

        collisionStyle = report.checkReport();
        switch (collisionStyle) {
        case Report.INVALID_REPORT: {
            // do nothing
            return false;
        }
        case Report.RCORNER: {
            return true;
        }
        case Report.RFACE: {
            return true;
        }
        case Report.RHORN: {
            return true;
        }
        default:
            return false;
        }
    }

    public boolean checkCollisionWithFinishLine(CollisionMap map) {
//        int reportCount = 0;
        report.refreshReport();
        // for every line in rectangle check whether it collide with walls
        for (int i = DataToolKit.UP; i <= DataToolKit.RIGHT; i++) {
            // calculate out the formula of this wall line
//            Point3f tempWallPointF = map.finishLine[0];
//            Point3f tempWallPointS = map.finishLine[1];
            Line2f.addObj(tempWallLine);
            tempWallLine = Line2f.getInstance(map.finishLine[0], map.finishLine[1]);
            if (rectangle.lines[i].isCross(tempWallLine)) {
                report.validReport(Report.FINISH_REPORT);
                report.selfLineID.add(i);
                report.targetLineID.add(0);
                report.targetLines.add(tempWallLine);
//                reportCount++;
//            }else{
//            	Line2f.addObj(tempWallLine);
            }
        }
        collisionStyle = report.checkReport();
        switch (collisionStyle) {
        case Report.FINISH_REPORT:
            return true;
        default:
            return false;
        }
    }

    public float getLength() {
        return mLength;
    }

    public float getWidth() {
        return mWidth;
    }

    public float getHeight() {
        return mHeight;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public int getCollisionStyle() {
        return collisionStyle;
    }

    public void setCollisionStyle(int collisionStyle) {
        this.collisionStyle = collisionStyle;
    }

    public void printlRectangle() {
        // Log.e(""," mLeftLower.x"+rectangle.mLeftLower.x+" mLeftLower.y"+rectangle.mLeftLower.y+" mLeftLower.z"+rectangle.mLeftLower.z);
        // Log.e(""," mLeftUpper.x"+rectangle.mLeftUpper.x+" mLeftUpper.y"+rectangle.mLeftUpper.y+" mLeftUpper.z"+rectangle.mLeftUpper.z);
        // Log.e(""," mRightLower.x"+rectangle.mRightLower.x+" mRightLower.y"+rectangle.mRightLower.y+" mRightLower.z"+rectangle.mRightLower.z);
        // Log.e(""," mRightUpper.x"+rectangle.mRightUpper.x+" mRightUpper.y"+rectangle.mRightUpper.y+" mRightUpper.z"+rectangle.mRightUpper.z);
    }
    
    @Override
    protected void finalize() throws Throwable {
        Log.e("Object finalize",this.getClass().getName());
        super.finalize();
    }
}
