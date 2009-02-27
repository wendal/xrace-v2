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
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.sa.xrace.client.math.Point2f;
import com.sa.xrace.client.math.Point3f;

/**
 * @author sliao
 * @version $Id$
 */
/**
 * This class is used to import the model which is created by 3DS MAX. And the
 * data imported is stored in the class t3DModel.java
 */

public final class ModelImport {

    // Primary Chunk��the first two bytes in the 3DS file
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

    private tChunk m_CurrentChunk;
    private tChunk m_TempChunk;

    private DataInputStream dis;

    /**
     * This class is used to import the model which is created by 3DS MAX. And
     * the data imported is stored in the class t3DModel.java
     */
    public ModelImport() {
        m_CurrentChunk = new tChunk();
        m_TempChunk = new tChunk();
        // ObjectNumber.regNew(this);
    }

    /**
     * import the 3DS model file into the t3DModel object Model: t3DModel
     * instance storing the data inputS: file input stream of the 3DS file
     */
    public t3DModel import3DS(DataInputStream inputS, int modelID, int type,  Point3f scale,
            float radius) {
        t3DModel model = new t3DModel(modelID ,type , scale ,radius);
        dis = inputS;
        readChunk(m_CurrentChunk);

        if (m_CurrentChunk.ID != PRIMARY) {
            System.out.println("Unable to load PRIMARY chunk");
            // return ;
        }

        processNextChunk(model, m_CurrentChunk);

        // computeNormals(Model);

        // Model.generate();

        // close input
        closeInput();

        return model;
    }

    /**
     * process the next chunk Model: t3DModel instance storing the data
     * PreviousChunk: tChunk instance keeping the ID and length of chunk
     */
    private void processNextChunk(t3DModel model, tChunk previousChunk) {
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
                model.objects.add(newObject);
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
                    model.objects.get(model.numOfObjects - 1).strName = new String(
                            buffer, 0, index - 1, "GBK");
                } catch (UnsupportedEncodingException e2) {
                    e2.printStackTrace();
                }
                processNextObjectChunk(model, model.objects
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
    private void processNextObjectChunk(t3DModel Model, t3DObject Object,
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
    private void readVertices(t3DObject Object, tChunk PreviousChunk) {
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
    private void readVertexIndices(t3DObject Object, tChunk PreviousChunk) {
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
                Object.Faces[i].verIndex[2] = ((temp[0] & 0xff) | ((temp[1] & 0xff) << 8)); // ��������1
                PreviousChunk.bytesRead += dis.read(temp, 0, 2);
                Object.Faces[i].verIndex[1] = ((temp[0] & 0xff) | ((temp[1] & 0xff) << 8)); // ��������2
                PreviousChunk.bytesRead += dis.read(temp, 0, 2);
                Object.Faces[i].verIndex[0] = ((temp[0] & 0xff) | ((temp[1] & 0xff) << 8)); // ��������3
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
    private void readObjectMaterial(t3DModel Model, t3DObject Object,
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

        for (int i = 0; i < Model.numOfMaterials; i++) {
            if (strMaterial != null
                    && strMaterial.equals(Model.Materials.get(i).strName)) // which
                                                                           // Material
                                                                           // to
                                                                           // be
                                                                           // used��
            {
                Object.materialID = i;
                String strFile = Model.Materials.get(i).strFile;
                if (strFile != null && !strFile.equals("")) {
                    Object.bHasTexture = true;
                }
                break;
            } else {
                Object.materialID = -1;
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
    private void readUVCoordinates(t3DObject Object, tChunk PreviousChunk) {
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
    private void processNextMaterialChunk(t3DModel Model, tChunk PreviousChunk) {
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
    private void readColorChunk(tMaterialInfo Material, tChunk chunk) {
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
    private void readChunk(tChunk chunk) {
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

    /**
     * compute the normals of all vertex Model: t3DModel instance storing the
     * data
     */
    // private void computeNormals(t3DModel Model)
    // {
    // Point3f vector1, vector2, vNormal;
    // Point3f[] vPoly = new Point3f[3];
    // Point3f vSumOfVectors = new Point3f();
    // int sharedFaces = 0;
    //		
    //		
    // if (Model.numOfObjects <=0 )
    // {
    // return;
    // }
    // for (int index=0; index<Model.numOfObjects; index++)
    // {
    //			
    // t3DObject Object = Model.Objects.get(index);
    //			
    // Point3f[] Normals = new Point3f[Object.numOfFaces];
    // Point3f[] tempNormals = new Point3f[Object.numOfFaces];
    // Object.Normals = new Point3f[Object.numOfVerts];
    //			
    //			
    // for (int i=0; i <Object.numOfFaces; i++)
    // {
    // //get the three vertex of each face
    // vPoly[0] = Object.Verts[Object.Faces[i].verIndex[0]];
    // vPoly[1] = Object.Verts[Object.Faces[i].verIndex[1]];
    // vPoly[2] = Object.Verts[Object.Faces[i].verIndex[2]];
    //				
    // //compute the normal
    // vector1= Point3f.getVector(vPoly[0], vPoly[2]);
    // vector2= Point3f.getVector(vPoly[2], vPoly[1]);
    // vNormal = Point3f.cross(vector1, vector2);
    // tempNormals[i] = vNormal.clone();
    // Point3f.normalize(vNormal);
    // Normals[i] = vNormal; //keep the normals
    // }
    //			
    // //get the normals of all vertex
    //			
    // for (int i = 0; i<Object.numOfVerts; i++) //for each vertex, get its
    // normal
    // {
    // for (int j=0; j<Object.numOfFaces; j++)
    // {
    // if (Object.Faces[j].verIndex[0] == i ||
    // Object.Faces[j].verIndex[1] == i ||
    // Object.Faces[j].verIndex[2] == i )
    // {
    // vSumOfVectors = Point3f.addVector(vSumOfVectors, tempNormals[j]);
    // sharedFaces++;
    // }
    // }
    // Object.Normals[i] = Point3f.scaleVector(vSumOfVectors,
    // (float)-sharedFaces);
    // Point3f.normalize(Object.Normals[i]);
    // vSumOfVectors.setValues(0.0f, 0.0f, 0.0f);
    // sharedFaces = 0;
    // }
    // }
    //		
    // }
    // close the dataInput
    private void closeInput() {
        try {
            dis.close();
            dis = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void release(){
        buffer = null;
        dis = null;
    }
    
    /**
     * This class is used to keep the ID, length and bytesRead of chunk.
     */
    private static class tChunk {
        int ID; // ID
        int length; // length
        int bytesRead; // bytes that have been read
    };

}// class ModelImport

