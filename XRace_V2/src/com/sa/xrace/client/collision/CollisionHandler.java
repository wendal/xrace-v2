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

import com.sa.xrace.client.math.Point3f;
import com.sa.xrace.client.pool.CarInforClient;
import com.sa.xrace.client.scene.Camera;

public class CollisionHandler {

    private AABBbox box;
    private Report report;

    private Line2f tempWallLine;
    private Line2f tempVerticalLine;

    // /////////////////collision handle section///////////////////////////

    public CollisionHandler() {
        tempVerticalLine = new Line2f();
    }

    /**
     * Now this method is not fully completed. This version collision handle
     * take HORN,CORNER,FACE collision the same. CollisionHandler does not give
     * them distinguished handle methods.
     */
    public void wallCollisionHandle(CarInforClient myCarInfor,
            CollisionMap collisionMap, Camera camera) {
        // get our AABBbox and Report ready for use;
        box = myCarInfor.getMTranformedBox();
        report = box.getReport();
        // collsionResulter(box.getCollisionStyle(),report,oneCarInfor);
        // nearWallChecker(collisionMap.wallLines.get(report.targetLineID.get(0)-1),oneCarInfor);
        // nearWallChecker(collisionMap.wallLines.get(report.targetLineID.get(0)+1),oneCarInfor);

        // calculate out the line vertical to the first collision wall
        tempWallLine = report.targetLines.get(0);
        if (tempWallLine.lineType == Line2f.VERTICAL) {
            tempVerticalLine.lineType = Line2f.HORIZONTAL;
            tempVerticalLine.b = myCarInfor.getNYPosition();
        } else if (tempWallLine.lineType == Line2f.HORIZONTAL) {
            tempVerticalLine.lineType = Line2f.VERTICAL;
            tempVerticalLine.b = myCarInfor.getNXPosition();
        } else {
            tempVerticalLine.lineType = Line2f.ORDINARY;
            tempVerticalLine.k = -1 / (tempWallLine.k);
            tempVerticalLine.b = myCarInfor.getNYPosition()
                    - tempVerticalLine.k * myCarInfor.getNXPosition();
        }

        switch (tempVerticalLine.lineType) {
        case Line2f.ORDINARY: {
            float pointY = myCarInfor.getNYPosition();
            float lineX = (pointY - tempWallLine.b) / tempWallLine.k;

            float wallDiretion = (float) Math.atan(tempWallLine.k);// (-PI/2~~~~
                                                                   // PI/2)

            if (wallDiretion < 0) {
                wallDiretion = (float) (Math.PI + wallDiretion);
            }
            float walltocar = myCarInfor.getNDirection() - wallDiretion;

            if (walltocar == Math.PI / 2 || walltocar == -Math.PI / 2) {

            } else if (walltocar > -Math.PI / 2 && walltocar < Math.PI / 2) {
                myCarInfor.setNDirection(wallDiretion);
                camera.setDirection(wallDiretion);
                camera.setOffset(0);
            } else {
                myCarInfor.setNDirection((float) (Math.PI + wallDiretion));
                camera.setDirection((float) (Math.PI + wallDiretion));
                camera.setOffset(0);
            }

            float temX = (float) (Math.abs(myCarInfor.getMOriginalBox().mWidth
                    / 2 / Math.sin(wallDiretion)));

            float x1 = lineX + temX;
            float x2 = lineX - temX;

            if (Math.abs(x1 - myCarInfor.getNXPosition()) > Math.abs(x2
                    - myCarInfor.getNXPosition())) {
                myCarInfor.setNXPosition(x2);
            } else {
                myCarInfor.setNXPosition(x1);
            }
            break;
        }
        case Line2f.HORIZONTAL: {

            float tempMyDir = myCarInfor.getNDirection();

            float cos = (float) Math.cos(tempMyDir);
            // float sin = (float) Math.sin(tempMyDir);

            if (cos > 0) {
                myCarInfor.setNDirection(0);
                camera.setDirection(0);
                camera.setOffset(0);

            } else {
                myCarInfor.setNDirection((float) Math.PI);
                camera.setDirection((float) Math.PI);
                camera.setOffset(0);
            }

            float x1 = tempWallLine.b + myCarInfor.getMOriginalBox().mWidth / 2;
            float x2 = tempWallLine.b - myCarInfor.getMOriginalBox().mWidth / 2;
            Log.v("-------------------------", "mWidth = "
                    + myCarInfor.getMOriginalBox().mWidth);

            if (Math.abs(x1 - myCarInfor.getNXPosition()) > Math.abs(x2
                    - myCarInfor.getNXPosition())) {
                myCarInfor.setNXPosition(x2);
            } else {
                myCarInfor.setNXPosition(x1);
            }

            myCarInfor.setNSpeed(myCarInfor.getNSpeed() / 3);

            break;

        }
        case Line2f.VERTICAL: {
            float tempMyDir = myCarInfor.getNDirection();

            float sin = (float) Math.sin(tempMyDir);

            if (sin > 0) {
                myCarInfor.setNDirection((float) Math.PI / 2);
                camera.setDirection((float) Math.PI / 2);
                camera.setOffset(0);
            } else {
                myCarInfor.setNDirection((float) Math.PI * 3 / 2);
                camera.setDirection((float) Math.PI * 3 / 2);
                camera.setOffset(0);
            }

            float y1 = tempWallLine.b + myCarInfor.getMOriginalBox().mWidth / 2;
            float y2 = tempWallLine.b - myCarInfor.getMOriginalBox().mWidth / 2;

            Log.v("-------------------------", "mWidth = "
                    + myCarInfor.getMOriginalBox().mWidth);

            if (Math.abs(y1 - myCarInfor.getNYPosition()) > Math.abs(y2
                    - myCarInfor.getNYPosition())) {
                myCarInfor.setNYPosition(y2);
            } else {
                myCarInfor.setNYPosition(y1);
            }

            myCarInfor.setNSpeed(myCarInfor.getNSpeed() / 3);

            // myCarInfor.setNSpeed(0);
            break;
        }
        }
    }

    /**
     * Now this method is not fully completed. This version collision handle
     * take HORN,CORNER,FACE collision the same. CollisionHandler does not give
     * them distinguished handle methods.
     */

    public void carCollisionHandle(CarInforClient myCarInfor, float targetX,
            float targetY, Camera camera) {
        float tempRaidus = myCarInfor.getMOriginalBox().radius;

        Point3f myCenter = new Point3f(myCarInfor.getNXPosition(), 0,
                myCarInfor.getNYPosition());
        Point3f targetCenter = new Point3f(targetX, 0, targetY);

        Point3f dirVector = myCenter.getVector(targetCenter);
        dirVector.normalize();
        dirVector.scaleVector(2 * tempRaidus);

        myCenter = dirVector.addVector(targetCenter);
        myCarInfor.setNXPosition(myCenter.x);
        myCarInfor.setNYPosition(myCenter.z);
        myCarInfor.setNSpeed(myCarInfor.getNSpeed() / 2);
    }

    public void finishLineCollisonHandle(CarInforClient myCarInfor) {

    }


    
    @Override
    protected void finalize() throws Throwable {
        Log.e("Object finalize",this.getClass().getName());
        super.finalize();
    }

}
