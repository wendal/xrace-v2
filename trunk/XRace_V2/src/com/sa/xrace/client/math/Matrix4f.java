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

import android.util.Log;

public final class Matrix4f {

    private float matrix[][];

    public Matrix4f() {
        matrix = new float[4][4];
        setIdentity();
//        Log.e("Matrix4f", "new one");
    }

    // public Matrix4f(Matrix4f other)
    // {
    // for (int i = 0; i < 4; i++) {
    // for (int j = 0; j < 4; j++) {
    // matrix[i][j] = other.matrix[i][j];
    // }
    // }
    // }
    private void setIdentity() {
//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < 4; j++) {
//                matrix[i][j] = (i == j ? 1f : 0f);
//            }
//        }
        matrix[0][0] = 1.f;
        matrix[0][1] = 0.f;
        matrix[0][2] = 0.f;
        matrix[0][3] = 0.f;

        matrix[1][0] = 0.f;
        matrix[1][1] = 1.f;
        matrix[1][2] = 0.f;
        matrix[1][3] = 0.f;

        matrix[2][0] = 0.f;
        matrix[2][1] = 0.f;
        matrix[2][2] = 1.f;
        matrix[2][3] = 0.f;

        matrix[3][0] = 0.f;
        matrix[3][1] = 0.f;
        matrix[3][2] = 0.f;
        matrix[3][3] = 1.f;
    }

//    public Matrix4f multiply(Matrix4f other) {
//        Matrix4f result = new Matrix4f();
//        float[][] m1 = matrix;
//        float[][] m2 = other.matrix;
//
//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < 4; j++) {
//                result.matrix[i][j] = m1[i][0] * m2[0][j] + m1[i][1] * m2[1][j]
//                        + m1[i][2] * m2[2][j] + m1[i][3] * m2[3][j];
//            }
//        }
//        
//        return result;
//    }

    /**
     * 警告!!本方法将改变dest的值!!!
     * @param dest 保存计算结果的对象
     */
    public Matrix4f multiply(Matrix4f other ,Matrix4f dest) {
//        Matrix4f result = dest;
        float[][] m1 = matrix;
        float[][] m2 = other.matrix;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
            	dest.matrix[i][j] = m1[i][0] * m2[0][j] + m1[i][1] * m2[1][j]
                        + m1[i][2] * m2[2][j] + m1[i][3] * m2[3][j];
            }
        }
        
        return dest;
    }
    
    // public void getTranlateMatrix(float x,float y,float z)
    // {
    // setIdentity();
    // matrix[0][3]=x;
    // matrix[1][3]=y;
    // matrix[2][3]=z;
    // }

    public void getTranlateMatrix(Point3f point) {
        setIdentity();
        matrix[0][3] = point.x;
        matrix[1][3] = point.y;
        matrix[2][3] = point.z;
    }

    public void getRotateMatrixY(float angle) {
        setIdentity();
        float sin = (float) Math.sin(-angle);
        float cos = (float) Math.cos(-angle);
        matrix[0][0] = cos;
        matrix[1][0] = 0;
        matrix[2][0] = sin;
        matrix[3][0] = 0;
        matrix[0][1] = 0;
        matrix[1][1] = 1;
        matrix[2][1] = 0;
        matrix[3][1] = 0;
        matrix[0][2] = -sin;
        matrix[1][2] = 0;
        matrix[2][2] = cos;
        matrix[3][2] = 0;
        matrix[0][3] = 0;
        matrix[1][3] = 0;
        matrix[2][3] = 0;
        matrix[3][3] = 1;
    }

    public void getScaleMatrix(float x , float y , float z) {
        setIdentity();
        matrix[0][0] = x;
        matrix[1][0] = 0;
        matrix[2][0] = 0;
        matrix[3][0] = 0;
        matrix[0][1] = 0;
        matrix[1][1] = y;
        matrix[2][1] = 0;
        matrix[3][1] = 0;
        matrix[0][2] = 0;
        matrix[1][2] = 0;
        matrix[2][2] = z;
        matrix[3][2] = 0;
        matrix[0][3] = 0;
        matrix[1][3] = 0;
        matrix[2][3] = 0;
        matrix[3][3] = 1;

    }

    private Point3f temp = new Point3f();
    public void transformPoint(Point3f src, Point3f dest) {
//        Point3f temp = new Point3f();

        temp.clear();
        
        temp.x = src.x * matrix[0][0] + src.y * matrix[0][1] + src.z
                * matrix[0][2] + matrix[0][3];
        temp.y = src.x * matrix[1][0] + src.y * matrix[1][1] + src.z
                * matrix[1][2] + matrix[1][3];
        temp.z = src.x * matrix[2][0] + src.y * matrix[2][1] + src.z
                * matrix[2][2] + matrix[2][3];

        dest.x = temp.x;
        dest.y = temp.y;
        dest.z = temp.z;
    }

    /**
	 * 
	 */
    public String toString() {
        StringBuilder builder = new StringBuilder("[ ");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                builder.append(matrix[i][j]);
                builder.append(" ");
            }
            if (i < 2)
                builder.append("\n  ");
        }
        builder.append(" ]");
        return builder.toString();
    }
    
    @Override
    protected void finalize() throws Throwable {
        Log.e("Object finalize",this.getClass().getName());
        super.finalize();
    }
}
