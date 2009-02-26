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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import com.sa.xrace.client.math.Point3f;
import com.sa.xrace.client.scene.Camera;
import com.sa.xrace.client.scene.Object;

public final class CollisionMap {

    ArrayList<Point3f> walls;
    ArrayList<Line2f> wallLines;
    Point3f[] finishLine;

    // Point3f[] mPosition;
    // Point3f[] mRadius;

    public CollisionMap() {
        walls = new ArrayList<Point3f>();
        wallLines = new ArrayList<Line2f>();
        finishLine = new Point3f[2];
        finishLine[0] = new Point3f(5, 5, 5);
        finishLine[1] = new Point3f(6, 6, 6);
    }

    public void generateWallCollisionMap(Object object) {
        if (object.mVerts != null) {
            for (int i = 0; i < object.mVerts.length; i++) {
                for (int j = 0; j < object.mVerts[i].length; j++) {
                    walls.add(object.mVerts[i][j]);
                }
            }
        }
        // Log.v("~~~~~~~~~~~~~~~~~~~~","--"+walls.size());

        // for(int i =0;i<walls.size();i+=2)
        // {
        // Log.e("first"+" x:"+walls.get(i).x+" y:"+walls.get(i).y+" z:"+walls.get(i).z,
        // "next"+" x:"+walls.get(i+1).x+" y:"+walls.get(i+1).y+" z:"+walls.get(i+1).z);
        //			
        // }
    }

    public void generateWallLines() {
        for (int n = 0; n < walls.size(); n += 2) {
            // calculate out the formula of this wall line
            wallLines.add(new Line2f(walls.get(n), walls.get(n + 1)));
        }
    }

    // public void generateFinishLine()
    // {
    //		
    // }

    private float[] wall_postion = new float[120];

    private float[] color = new float[160];

    private FloatBuffer fb, cb;
    private ByteBuffer bb;
    private int index = 0;

    public void prepare() {
        for (int i = 0; i < wall_postion.length; i += 3) {
            wall_postion[i] = walls.get(index).x;
            wall_postion[i + 1] = walls.get(index).y;
            wall_postion[i + 2] = walls.get(index).z;
            index++;
        }

        bb = ByteBuffer.allocateDirect(wall_postion.length * 4);
        bb.order(ByteOrder.nativeOrder());
        fb = bb.asFloatBuffer();
        for (int j = 0; j < wall_postion.length; j++) {
            fb.put(wall_postion[j]);
        }
        fb.position(0);

        for (int i = 0; i < color.length; i += 4) {
            color[i] = 1.0f;
            color[i + 1] = 0.0f;
            color[i + 2] = 0.0f;
            color[i + 3] = 1.0f;
        }
        bb = ByteBuffer.allocateDirect(color.length * 4);
        bb.order(ByteOrder.nativeOrder());
        cb = bb.asFloatBuffer();
        for (int j = 0; j < color.length; j++) {
            cb.put(color[j]);
        }
        cb.position(0);

    }

    public void drawWall(GL10 gl, Camera camera) {

        gl.glDisable(GL10.GL_TEXTURE_2D);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        camera.setCamera(gl);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glLineWidth(3.0f);
        fb.position(0);
        cb.position(0);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, cb);
        gl.glDrawArrays(GL10.GL_LINES, 0, 40);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }
}
