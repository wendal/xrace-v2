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

import com.sa.xrace.client.math.Point3f;

public class Rectangle {
    public Point3f mLeftUpper;
    public Point3f mRightUpper;
    public Point3f mLeftLower;
    public Point3f mRightLower;

    // Array be convenient to loop
    // 0 up 1 left 2 bottom 3 right
    public Line2f[] lines;
    public Point3f center;

    public Rectangle() {
        mLeftUpper = new Point3f();
        mRightUpper = new Point3f();
        mLeftLower = new Point3f();
        mRightLower = new Point3f();

        center = new Point3f();
        lines = new Line2f[4];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = new Line2f();
        }
    }

    public Rectangle(Point3f leftUpper, Point3f rightUpper, Point3f leftLower,
            Point3f rightLower) {
        // Log.e("leftUpper",""+leftUpper);
        // Log.e("rightUpper",""+rightUpper);
        // Log.e("leftLower",""+leftLower);
        // Log.e("rightLower",""+rightLower);
        // Log.e("center",""+center);

        mLeftUpper = leftUpper;
        mRightUpper = rightUpper;
        mLeftLower = leftLower;
        mRightLower = rightLower;

        generateLines();
        generateCenter();
    }

    public void generateLines() {
        lines = new Line2f[4];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = new Line2f();
        }
        lines[0].generateLine(mLeftUpper, mRightUpper);
        lines[1].generateLine(mLeftUpper, mLeftLower);
        lines[2].generateLine(mLeftLower, mRightLower);
        lines[3].generateLine(mRightLower, mRightUpper);
    }

    public void generateCenter() {
        center = new Point3f();
        center.x = (mLeftUpper.x + mRightLower.x) / 2;
        center.y = mLeftUpper.y;
        center.z = (mLeftUpper.z + mRightLower.z) / 2;
    }

    // private float[] car_postion = new float[4*3];
    //
    // private float[] color = new float[4*4];
    //	
    // private FloatBuffer fb, cb;
    // private ByteBuffer bb;
    // private int index = 0;

    // public void prepare(){
    //	
    // car_postion[0] = mLeftUpper.x;
    // car_postion[1] = mLeftUpper.y;
    // car_postion[2] = mLeftUpper.z;
    // //
    // Log.v("------------mLeftUpper---------",""+mLeftUpper.x+", "+mLeftUpper.y+", "+mLeftUpper.z);
    //		
    // car_postion[3] = mLeftLower.x;
    // car_postion[4] = mLeftLower.y;
    // car_postion[5] = mLeftLower.z;
    // //
    // Log.v("------------mLeftLower---------",""+mLeftLower.x+", "+mLeftLower.y+", "+mLeftLower.z);
    //		
    // car_postion[6] = mRightLower.x;
    // car_postion[7] = mRightLower.y;
    // car_postion[8] = mRightLower.z;
    // //
    // Log.v("------------mRightLower---------",""+mRightLower.x+", "+mRightLower.y+", "+mRightLower.z);
    //		
    // car_postion[9] = mRightUpper.x;
    // car_postion[10] = mRightUpper.y;
    // car_postion[11] = mRightUpper.z;
    // //
    // Log.v("------------mRightUpper---------",""+mRightUpper.x+", "+mRightUpper.y+", "+mRightUpper.z);
    //		
    //		
    //		
    // bb = ByteBuffer.allocateDirect(car_postion.length * 4);
    // bb.order(ByteOrder.nativeOrder());
    // fb = bb.asFloatBuffer();
    // for(int j=0;j<car_postion.length;j++){
    // fb.put(car_postion[j]);
    // }
    // fb.position(0);
    //		
    //		
    // for(int i =0;i<color.length;i+=4){
    // color[i] = 0.0f;
    // color[i+1] = 1.0f;
    // color[i+2] = 0.0f;
    // color[i+3] = 1.0f;
    // }
    // bb = ByteBuffer.allocateDirect(color.length * 4);
    // bb.order(ByteOrder.nativeOrder());
    // cb = bb.asFloatBuffer();
    // for(int j=0;j<color.length;j++){
    // cb.put(color[j]);
    // }
    // cb.position(0);
    //		
    // }
    // // private void drawCarLine(GL10 gl,Camera camera){
    //			
    // gl.glDisable(GL10.GL_TEXTURE_2D);
    // gl.glMatrixMode(GL10.GL_MODELVIEW);
    //			
    // gl.glLoadIdentity();
    //			
    // camera.setCamera(gl);
    // gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    // gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
    // gl.glLineWidth(3.0f);
    // fb.position(0);
    // cb.position(0);
    // gl.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);
    // gl.glColorPointer(4, GL10.GL_FLOAT, 0, cb);
    // gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, 4);
    // gl.glEnable(GL10.GL_TEXTURE_2D);
    // gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    // }
}