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

import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.sa.xrace.client.math.Point2f;
import com.sa.xrace.client.math.Point3f;
import com.sa.xrace.client.toolkit.MethodsPool;

/**
 * @author sliao
 * @version $Id$
 */
/**
 * This class is used to import the model which is created by 3DS MAX. And the
 * data imported is stored in the class t3DModel.java
 */

public final class ModelImport {

    // Primary Chunk，the first two bytes in the 3DS file
    private static final int PRIMARY = 0x4D4D;

    // Main Chunks
    private static final int OBJECTINFO = 0x3D3D;
    private static final int VERSION = 0x0002; // version of .3ds file
    private static final int EDITKEYFRAME = 0xB000; // header of the key frames

    // Secondary chunks of main chunks, including material, object
    private static final int MATERIAL = 0xAFFF; // material
    private static final int OBJECT = 0x4000; // face and vertex

    // Secondary chunks of material
    private static final int MATNAME = 0xA000; // name
    private static final int MATDIFFUSE = 0xA020; // color
    private static final int MATMAP = 0xA200; // header
    private static final int MATMAPFILE = 0xA300; // filename

    // Secondary chunks of object
    private static final int OBJECT_MESH = 0x4100; // new object mesh

    // Secondary chunks of object mesh
    private static final int OBJECT_VERTICES = 0x4110; // vertex
    private static final int OBJECT_FACES = 0x4120; // face
    private static final int OBJECT_MATERIAL = 0x4130; // material
    private static final int OBJECT_UV = 0x4140; // UV coordinate

    private static tChunk m_CurrentChunk = new tChunk();
    private static tChunk m_TempChunk = new tChunk();

    private static DataInputStream dis;

    /**
     * import the 3DS model file into the t3DModel object Model: t3DModel
     * instance storing the data inputS: file input stream of the 3DS file
     * @throws IOException 
     */
    public static t3DModel import3DS(String filename, int modelID, int type,  Point3f scale) throws IOException {
        t3DModel model = new t3DModel(modelID ,type , scale );
        dis = new DataInputStream(MethodsPool.getFileInputStream(filename));
        readChunk(m_CurrentChunk);

        if (m_CurrentChunk.ID != PRIMARY) {
            System.out.println("Unable to load PRIMARY chunk");
            // return ;
        }

        processNextChunk(model, m_CurrentChunk);
        
        // close input
        closeInput();

        return model;
    }

    /**
     * process the next chunk Model: t3DModel instance storing the data
     * PreviousChunk: tChunk instance keeping the ID and length of chunk
     */
    private static void processNextChunk(t3DModel model, tChunk previousChunk) {
        m_CurrentChunk = new tChunk();
        long version = 0;

        while (previousChunk.bytesRead < previousChunk.length) {
            // byte[] buffer = new byte[200000];
            readChunk(m_CurrentChunk);
            switch (m_CurrentChunk.ID) {
            case VERSION:
                try {
                    m_CurrentChunk.bytesRead += dis.read(buffer, 0,
                            m_CurrentChunk.length - m_CurrentChunk.bytesRead);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                version = (long) ((buffer[0] & 0xff)
                        | ((buffer[1] & 0xff) << 8)
                        | ((buffer[2] & 0xff) << 16) | ((buffer[3] & 0xff) << 24));
                if (version > 0x03) {
                    System.out
                            .println("This 3DS file is over version3, so it may load incorrectly.");
                }
                break;
            case OBJECTINFO:
                readChunk(m_TempChunk);
                try {
                    m_TempChunk.bytesRead += dis.read(buffer, 0,
                            m_TempChunk.length - m_TempChunk.bytesRead);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                m_CurrentChunk.bytesRead += m_TempChunk.bytesRead;
                processNextChunk(model, m_CurrentChunk);
                break;
            case MATERIAL:
                model.numOfMaterials++;
                tMaterialInfo newTexture = new tMaterialInfo();
                model.Materials.add(newTexture);
                processNextMaterialChunk(model, m_CurrentChunk);
                break;
            case OBJECT:
                model.numOfObjects++;
                t3DObject newObject = new t3DObject();
                model._3Dobjects.add(newObject);
                int index = 0;
                try {
                    m_CurrentChunk.bytesRead += dis.read(buffer, 0, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (buffer[index++] != 0) {
                    try {
                        m_CurrentChunk.bytesRead += dis.read(buffer, index, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    model._3Dobjects.get(model.numOfObjects - 1).strName = new String(
                            buffer, 0, index - 1, "GBK");
                } catch (UnsupportedEncodingException e2) {
                    e2.printStackTrace();
                }
                processNextObjectChunk(model, model._3Dobjects
                        .get(model.numOfObjects - 1), m_CurrentChunk);
                break;
            case EDITKEYFRAME:
                try {
                    m_CurrentChunk.bytesRead += dis.read(buffer, 0,
                            m_CurrentChunk.length - m_CurrentChunk.bytesRead);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;
            default:
                try {
                    m_CurrentChunk.bytesRead += dis.read(buffer, 0,
                            m_CurrentChunk.length - m_CurrentChunk.bytesRead);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            previousChunk.bytesRead += m_CurrentChunk.bytesRead;
        }
        m_CurrentChunk = previousChunk;
    }

    private static byte[] buffer = new byte[200000];

    /**
     * process the Object chunk under the OBJECTINFO chunk Model: t3DModel
     * instance storing the data Object: t3DObject instance storing the data of
     * Object chunk PreviousChunk: tChunk instance keeping the ID and length of
     * the previous chunk
     */
    private static void processNextObjectChunk(t3DModel Model, t3DObject Object,
            tChunk PreviousChunk) {

        m_CurrentChunk = new tChunk();

        while (PreviousChunk.bytesRead < PreviousChunk.length) {
            readChunk(m_CurrentChunk);
            switch (m_CurrentChunk.ID) {
            case OBJECT_MESH:
                processNextObjectChunk(Model, Object, m_CurrentChunk);
                break;
            case OBJECT_VERTICES:
                readVertices(Object, m_CurrentChunk);
                break;
            case OBJECT_FACES:
                readVertexIndices(Object, m_CurrentChunk);
                break;
            case OBJECT_MATERIAL:
                readObjectMaterial(Model, Object, m_CurrentChunk);
                break;
            case OBJECT_UV:
                readUVCoordinates(Object, m_CurrentChunk);
                break;
            default:
                try {
                    m_CurrentChunk.bytesRead += dis.read(buffer, 0,
                            m_CurrentChunk.length - m_CurrentChunk.bytesRead);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            PreviousChunk.bytesRead += m_CurrentChunk.bytesRead;
        }
        m_CurrentChunk = PreviousChunk;
    }

    /**
     * read the vertex chunk under the OBJECT_MESH Object: t3DObject instance
     * storing the data of Object chunk PreviousChunk: tChunk instance keeping
     * the ID and length of the previous chunk
     */
    private static void readVertices(t3DObject Object, tChunk PreviousChunk) {
        byte[] temp = new byte[4];
        int intTemp;
        try {
            PreviousChunk.bytesRead += dis.read(temp, 0, 2);
            Object.numOfVerts = ((temp[0] & 0xff) | ((temp[1] & 0xff) << 8));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Object.Verts = new Point3f[Object.numOfVerts];
        for (int index = 0; index < Object.numOfVerts; index++) {
            Object.Verts[index] = new Point3f();
        }
        ;
        // read the vertex into the vertex array
        for (int index = 0; PreviousChunk.bytesRead < PreviousChunk.length
                && index < Object.numOfVerts; index++) {
            try {
                PreviousChunk.bytesRead += dis.read(temp, 0, 4);
                intTemp = ((temp[0] & 0xff) | ((temp[1] & 0xff) << 8)
                        | ((temp[2] & 0xff) << 16) | ((temp[3] & 0xff) << 24));
                Object.Verts[index].x = Float.intBitsToFloat(intTemp);
                PreviousChunk.bytesRead += dis.read(temp, 0, 4);
                intTemp = ((temp[1] & 0xff) | ((temp[1] & 0xff) << 8)
                        | ((temp[2] & 0xff) << 16) | ((temp[3] & 0xff) << 24));
                Object.Verts[index].z = -Float.intBitsToFloat(intTemp);
                PreviousChunk.bytesRead += dis.read(temp, 0, 4);
                intTemp = ((temp[2] & 0xff) | ((temp[1] & 0xff) << 8)
                        | ((temp[2] & 0xff) << 16) | ((temp[3] & 0xff) << 24));
                Object.Verts[index].y = Float.intBitsToFloat(intTemp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * read the vertex index of faces under the OBJECT_MESH Object: t3DObject
     * instance storing the data of Object chunk PreviousChunk: tChunk instance
     * keeping the ID and length of the previous chunk
     */
    private static void readVertexIndices(t3DObject Object, tChunk PreviousChunk) {
        byte[] temp = new byte[4];
        try {
            PreviousChunk.bytesRead += dis.read(temp, 0, 2);
            Object.numOfFaces = ((temp[0] & 0xff) | ((temp[1] & 0xff) << 8));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Object.Faces = new tFace[Object.numOfFaces];
        for (int i = 0; i < Object.numOfFaces; i++) {
            Object.Faces[i] = new tFace();
        }
        for (int i = 0; PreviousChunk.bytesRead < PreviousChunk.length
                && i < Object.numOfFaces; i++) {
            try {
                PreviousChunk.bytesRead += dis.read(temp, 0, 2);
                Object.Faces[i].verIndex[2] = ((temp[0] & 0xff) | ((temp[1] & 0xff) << 8)); // 顶点索引1
                PreviousChunk.bytesRead += dis.read(temp, 0, 2);
                Object.Faces[i].verIndex[1] = ((temp[0] & 0xff) | ((temp[1] & 0xff) << 8)); // 顶点索引2
                PreviousChunk.bytesRead += dis.read(temp, 0, 2);
                Object.Faces[i].verIndex[0] = ((temp[0] & 0xff) | ((temp[1] & 0xff) << 8)); // 顶点索引3
                PreviousChunk.bytesRead += dis.read(temp, 0, 2);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * read the material name used by the object under the OBJECT_MESH Model:
     * t3DModel instance storing the data Object: t3DObject instance storing the
     * data of Object chunk PreviousChunk: tChunk instance keeping the ID and
     * length of the previous chunk
     */
    private static void readObjectMaterial(t3DModel _3Dmodel, t3DObject _3Dobject,
            tChunk PreviousChunk) {
        String strMaterial = null;

        // byte[] buffer = new byte[200000];
        int index = 0;
        int length = 0;
        try {
            length = dis.read(buffer, 0, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (buffer[index++] != 0) {
            try {
                length += dis.read(buffer, index, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            strMaterial = new String(buffer, 0, length - 1, "GBK");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        m_CurrentChunk.bytesRead += length;

        for (int i = 0; i < _3Dmodel.numOfMaterials; i++) {
            if (strMaterial != null
                    && strMaterial.equals(_3Dmodel.Materials.get(i).strName)) // which
                                                                           // Material
                                                                           // to
                                                                           // be
                                                                           // used？
            {
                _3Dobject.materialID = i;
                String strFile = _3Dmodel.Materials.get(i).strFile;
                if (strFile != null && !strFile.equals("")) {
                    _3Dobject.bHasTexture = true;
                }
                break;
            } else {
                _3Dobject.materialID = -1;
            }
        }
        try {
            PreviousChunk.bytesRead += dis.read(buffer, 0, PreviousChunk.length
                    - PreviousChunk.bytesRead);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * read the UV coordinates used by the object under the OBJECT_MESH Object:
     * t3DObject instance storing the data of Object chunk PreviousChunk: tChunk
     * instance keeping the ID and length of the previous chunk
     */
    private static void readUVCoordinates(t3DObject Object, tChunk PreviousChunk) {
        int intTemp;
        byte[] temp = new byte[4];
        try {
            PreviousChunk.bytesRead += dis.read(temp, 0, 2);
            Object.numTexVertex = ((temp[0] & 0xff) | ((temp[1] & 0xff) << 8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Object.TexVerts = new Point2f[Object.numTexVertex];
        for (int i = 0; i < Object.numTexVertex; i++) {
            Object.TexVerts[i] = new Point2f();
        }
        ;
        for (int index = 0; PreviousChunk.bytesRead < PreviousChunk.length
                && index < Object.numOfVerts; index++) {
            try {
                PreviousChunk.bytesRead += dis.read(temp, 0, 4);
                intTemp = ((temp[0] & 0xff) | ((temp[1] & 0xff) << 8)
                        | ((temp[2] & 0xff) << 16) | ((temp[3] & 0xff) << 24));
                Object.TexVerts[index].x = Float.intBitsToFloat(intTemp);
                PreviousChunk.bytesRead += dis.read(temp, 0, 4);
                intTemp = ((temp[0] & 0xff) | ((temp[1] & 0xff) << 8)
                        | ((temp[2] & 0xff) << 16) | ((temp[3] & 0xff) << 24));
                Object.TexVerts[index].y = 1 - Float.intBitsToFloat(intTemp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * read the material chunk under the OBJECTINFO Model: t3DModel instance
     * storing the data PreviousChunk: tChunk instance keeping the ID and length
     * of the previous chunk
     */
    private static void processNextMaterialChunk(t3DModel Model, tChunk PreviousChunk) {
        // byte[] buffer = new byte[200000];
        m_CurrentChunk = new tChunk();
        while (PreviousChunk.bytesRead < PreviousChunk.length) {
            readChunk(m_CurrentChunk);
            switch (m_CurrentChunk.ID) {
            case MATNAME:
                try {
                    int length;
                    length = dis.read(buffer, 0, m_CurrentChunk.length
                            - m_CurrentChunk.bytesRead);
                    Model.Materials.get(Model.numOfMaterials - 1).strName = new String(
                            buffer, 0, length - 1, "GBK");
                    m_CurrentChunk.bytesRead += length;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case MATDIFFUSE:
                readColorChunk(Model.Materials.get(Model.numOfMaterials - 1),
                        m_CurrentChunk);
                break;
            case MATMAP:
                processNextMaterialChunk(Model, m_CurrentChunk);
                break;
            case MATMAPFILE:
                try {
                    int length;
                    length = dis.read(buffer, 0, m_CurrentChunk.length
                            - m_CurrentChunk.bytesRead);
                    Model.Materials.get(Model.numOfMaterials - 1).strFile = new String(
                            buffer, 0, length - 1, "GBK");
                    m_CurrentChunk.bytesRead += length;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                try {
                    m_CurrentChunk.bytesRead += dis.read(buffer, 0,
                            m_CurrentChunk.length - m_CurrentChunk.bytesRead);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            PreviousChunk.bytesRead += m_CurrentChunk.bytesRead;
        }
        m_CurrentChunk = PreviousChunk;
    }

    /**
     * read the RGB color chunk under the OBJECT_MESH Model: t3DModel instance
     * storing the data chunk: tChunk instance keeping the ID and length of the
     * previous chunk
     */
    private static void readColorChunk(tMaterialInfo Material, tChunk chunk) {
        readChunk(m_TempChunk);
        try {
            m_TempChunk.bytesRead += dis.read(Material.color, 0,
                    m_TempChunk.length - m_TempChunk.bytesRead);
        } catch (IOException e) {
            e.printStackTrace();
        }
        chunk.bytesRead += m_TempChunk.bytesRead;
    }

    /**
     * read the ID and length of chunk chunk: tChunk instance keeping the ID and
     * length of chunk
     */
    private static void readChunk(tChunk chunk) {
        byte[] temp = new byte[4];

        try {
            chunk.bytesRead = dis.read(temp, 0, 2);
            chunk.ID = ((temp[0] & 0xff) | ((temp[1] & 0xff) << 8));
            chunk.bytesRead += dis.read(temp, 0, 4);
            chunk.length = (int) ((temp[0] & 0xff) | ((temp[1] & 0xff) << 8)
                    | ((temp[2] & 0xff) << 16) | ((temp[3] & 0xff) << 24));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // close the dataInput
    private static void closeInput() {
        try {
            dis.close();
            dis = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行该方法后,本类的全部方法都将无须!!!
     */
    public static void release(){
        buffer = null;
        dis = null;
        m_CurrentChunk = null;
        m_TempChunk = null;
    }
    
    /**
     * This class is used to keep the ID, length and bytesRead of chunk.
     */
    private static class tChunk {
        int ID; // ID
        int length; // length
        int bytesRead; // bytes that have been read
    }
    
//    @Override
//    protected void finalize() throws Throwable {
//        Log.e("Object finalize",this.getClass().getName());
//        super.finalize();
//    }

}