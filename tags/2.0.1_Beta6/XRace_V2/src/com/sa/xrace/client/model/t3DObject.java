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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.sa.xrace.client.math.Point2f;
import com.sa.xrace.client.math.Point3f;
import com.sa.xrace.client.toolkit.MethodsPool;
import com.sa.xrace.client.toolkit.ObjectPool;
import com.wendal.java.xrace.toolkit.bmpconvert.DataUnti;

/**
 * @author sliao
 * @version $Id$
 */
/**
 * This class is used to keep the data of object.
 */
public final class t3DObject implements Cloneable {
    public int numOfVerts; // number of vertex
    public int numOfFaces; // number of faces
    public int numTexVertex; // number of coordinate
    public int materialID; // material's ID
    public boolean bHasTexture; // is there any texture?
    public String strName; // name of object
    public Point3f[] Verts; // vertex
    public Point3f[] Normals; // normals of vertex
    public Point2f[] TexVerts; // UV coordinate
    public tFace[] Faces; // faces's vertex index

    /**
     * create the buffer for render according the data imported
     */
    public void createBuffer() {
        ByteBuffer bb = ByteBuffer.allocateDirect(numOfVerts * 3 * 4);
        bb.order(ByteOrder.nativeOrder());
        mVertexBuffer = bb.asIntBuffer();

        bb = ByteBuffer.allocateDirect(numTexVertex * 2 * 4);
        bb.order(ByteOrder.nativeOrder());
        mTexVertBuffer = bb.asFloatBuffer();

        bb = ByteBuffer.allocateDirect(numOfFaces * 3 * 2);
        bb.order(ByteOrder.nativeOrder());
        mIndexBuffer = bb.asShortBuffer();

        for (int index = 0; index < numOfVerts; index++) {
            mVertexBuffer.put(MethodsPool.toFixed(Verts[index].x));
            mVertexBuffer.put(MethodsPool.toFixed(Verts[index].y));
            mVertexBuffer.put(MethodsPool.toFixed(Verts[index].z));
        }
        for (int index = 0; index < numTexVertex; index++) {
            mTexVertBuffer.put(TexVerts[index].x);
            mTexVertBuffer.put(TexVerts[index].y);
        }
        for (int index = 0; index < numOfFaces; index++) {
            mIndexBuffer.put((short) Faces[index].verIndex[0]);
            mIndexBuffer.put((short) Faces[index].verIndex[1]);
            mIndexBuffer.put((short) Faces[index].verIndex[2]);
        }
    }

    public void createVertexBuffer() {
        ByteBuffer bb = ByteBuffer.allocateDirect(numOfVerts * 3 * 4);
        bb.order(ByteOrder.nativeOrder());
        mVertexBuffer = bb.asIntBuffer();
        for (int index = 0; index < numOfVerts; index++) {
            mVertexBuffer.put(MethodsPool.toFixed(Verts[index].x));
            mVertexBuffer.put(MethodsPool.toFixed(Verts[index].y));
            mVertexBuffer.put(MethodsPool.toFixed(Verts[index].z));
        }
    }

    public Point3f getCenter() {
        float BIG_NUMBER = 1e37f;
        Point3f center = new Point3f();
        Point3f leftLower = new Point3f(BIG_NUMBER, BIG_NUMBER, -BIG_NUMBER);
        Point3f rightUpper = new Point3f(-BIG_NUMBER, -BIG_NUMBER, +BIG_NUMBER);

        for (int index = 0; index < this.numOfVerts; index++) {
            if (this.Verts[index].x < leftLower.x) // get the minimize on x axis
            {
                leftLower.x = this.Verts[index].x;
            } else if (this.Verts[index].x > rightUpper.x) // get the maximize
                                                           // on x axis
            {
                rightUpper.x = this.Verts[index].x;
            }
            if (this.Verts[index].y < leftLower.y) // get the minimize on y axis
            {
                leftLower.y = this.Verts[index].y;
            } else if (this.Verts[index].y > rightUpper.y) // get the maximize
                                                           // on y axis
            {
                rightUpper.y = this.Verts[index].y;
            }
            if (this.Verts[index].z < rightUpper.z) // get the minimize on z
                                                    // axis
            {
                rightUpper.z = this.Verts[index].z;
            } else if (this.Verts[index].z > leftLower.z) // get the maximize on
                                                          // z axis
            {
                leftLower.z = this.Verts[index].z;
            }
        }

        center.x = (leftLower.x + rightUpper.x) / 2;
        center.y = (leftLower.y + rightUpper.y) / 2;
        center.z = (leftLower.z + rightUpper.z) / 2;

        System.out.println("wheel's " + center.toString());
        return center;
    }

    public void resetVertice(Point3f point) {
        Point3f center = getCenter();
        System.out.println(center.toString());
        for (int index = 0; index < this.numOfVerts; index++) {
            // Verts[index].x = Verts[index].x - center.x + point.x;
            // Verts[index].y = Verts[index].y - center.y + point.y;
            // Verts[index].z = Verts[index].z - center.z + point.z;
            Verts[index].x = Verts[index].x - center.x;
            Verts[index].y = Verts[index].y - center.y;
            Verts[index].z = Verts[index].z - center.z;
        }
    }

    /**
     * ����÷�����Ϊ��ʱ!
     * <p/>
     * �ѸĽ�,�ı����,ֱ�Ӷ�ȡ�Ѿ�����õ�����,����ÿ�ζ����¼���
     */
    public void loadBitmap(String filename) {
        GL10 gl = ObjectPool.gl;
        /* ��һλΪ Width,�ڶ�λΪHeight */
        int[] size = DataUnti.getBmpSize(filename);
        ByteBuffer byteBuffer = DataUnti.getByteBuffer_ByFileName(filename);

        byteBuffer.position(0);

        gl.glGenTextures(1, textureID, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID[0]);
        gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, size[0], size[1],
                0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, byteBuffer);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
                GL10.GL_LINEAR);
    }

    public t3DObject clone() {
        t3DObject new_3Dobject = null;
        try {
            new_3Dobject = (t3DObject) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println(e.toString());
        }
        if (strName != null) {
            new_3Dobject.strName = strName.toString();
        }
        if (Verts != null) {
            new_3Dobject.Verts = new Point3f[Verts.length];
            for (int i = 0; i < Verts.length; i++) {
                new_3Dobject.Verts[i] = Verts[i].clone();
            }
        }
        if (Normals != null) {
            new_3Dobject.Normals = new Point3f[Normals.length];
            for (int i = 0; i < Normals.length; i++) {
                new_3Dobject.Normals[i] = Normals[i].clone();
            }
        }
        if (TexVerts != null) {
            new_3Dobject.TexVerts = new Point2f[TexVerts.length];
            for (int i = 0; i < TexVerts.length; i++) {
                new_3Dobject.TexVerts[i] = (Point2f) TexVerts[i].clone();
            }
        }
        if (Faces != null) {
            new_3Dobject.Faces = new tFace[Faces.length];
            for (int i = 0; i < Faces.length; i++) {
                new_3Dobject.Faces[i] = (tFace) Faces[i].clone();
            }
        }
        if (textureID != null) {
            new_3Dobject.textureID = textureID.clone();
        }
        new_3Dobject.createBuffer();
        return new_3Dobject;
    }

    public void draw() {
    	GL10 gl = ObjectPool.gl;
        mTexVertBuffer.position(0);
        mVertexBuffer.position(0);
        mIndexBuffer.position(0);

        if (textureID[0] != -1) {
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID[0]);
//            Log.e("Here","Mode ID= " );
        }
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexVertBuffer);// This Line ?
        gl.glDrawElements(GL10.GL_TRIANGLES, numOfFaces * 3,
                GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
//        Log.e("Here","---------------------?" );
    }

    private IntBuffer mVertexBuffer;
    private FloatBuffer mTexVertBuffer;
    private ShortBuffer mIndexBuffer;

    private int[] textureID = new int[] { -1 };
    
    
}

/**
 * This class is used to keep the vertex index and coordinate index.
 */
class tFace implements Cloneable {
    public int[] verIndex; // vertex index
    public int[] coordIndex; // coordinate index

    public tFace() {
        verIndex = new int[3];
        coordIndex = new int[2];
    }

    public Object clone() {
        tFace face = null;
        try {
            face = (tFace) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println(e.toString());
        }
        face.verIndex = verIndex.clone();
        face.coordIndex = coordIndex.clone();
        return face;
    }
}
