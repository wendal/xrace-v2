package com.sa.xrace.client.toolkit;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.sa.xrace.client.loader.LocationObj;
import com.sa.xrace.client.loader.ModelObj;
import com.sa.xrace.client.loader.SenceParser2;
import com.sa.xrace.client.model.ModelImport;
import com.sa.xrace.client.model.t3DModel;
import com.sa.xrace.client.scene.Object;

public final class MethodsPool {

    public static Bitmap getBitmap(int resID) {
        // Bitmap tem =
        return BitmapFactory.decodeResource(ObjectPool.resources, resID);
        // return tem;
    }

//    /**
//     * 这个方法耗时严重,需要改进
//     * 
//     * @param resname
//     * @return
//     */
//    public final static ByteBuffer getImageReadyfor(int resname) {
//        ByteBuffer tempBuffer;
//        Bitmap mBitmap = BitmapFactory.decodeResource(ObjectPool.resources,
//                resname);
//        int pic_width = mBitmap.getWidth();
//        int pic_height = mBitmap.getHeight();
//        // Log.e("pic_width",""+pic_width+" "+pic_height);
//        tempBuffer = ByteBuffer.allocateDirect(pic_width * pic_height * 4);
//        tempBuffer.order(ByteOrder.nativeOrder());
//        IntBuffer tempIB = tempBuffer.asIntBuffer();
//
//        for (int y = 0; y < pic_width; y++) {
//            for (int x = 0; x < pic_height; x++) {
//                tempIB.put(mBitmap.getPixel(x, y));
//            }
//        }
//
//        for (int i = 0; i < pic_width * pic_height * 4; i += 4) {
//            byte temp = tempBuffer.get(i);
//            tempBuffer.put(i, tempBuffer.get(i + 2));
//            tempBuffer.put(i + 2, temp);
//
//        }
//        tempBuffer.position(0);
//        DataUnti.sendOut(DataUnti.getNameByID(resname), tempBuffer);
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        Log.e("Here", "Shall not show me !");
//        return tempBuffer;
//    }

    public static void LoadMapFromXML(String filename) {
        long start = System.currentTimeMillis();
        InputStream fis;
        DataInputStream dis;
        t3DModel t3Dmodel;
//        Model model;
        Object object;

        // SenceParser2 senceParser;
        // SenceObj sence;

        try {
            // 处理XML,并统计时间
            // long xml_start = System.currentTimeMillis();
            fis = ObjectPool.assetManager.open(filename);
            // Document document = new SAXReader()
            // .read(new InputStreamReader(fis));
            // senceParser = new SenceParser2(fis);
            // SenceObj sence = SenceParser2.parse(fis);

            ArrayList<ModelObj> modelList = SenceParser2.parse(fis);
            // sence.getLModelList();
            // Log.i("Time in XML parse", ""+(System.currentTimeMillis() -
            // xml_start));
            ModelObj modelObj = null;
            LocationObj locationObj = null;
            int size = modelList.size();
            ModelImport modelImport = new ModelImport();
            for (int i = 0; i < size; i++) {
                // 处理图片,并统计时间,原本的时间为 17700ms
                long image_start = System.currentTimeMillis();
                modelObj = (ModelObj) modelList.get(i);
                fis = ObjectPool.assetManager.open(modelObj.getFilename());
                dis = new DataInputStream(fis);
                // t3Dmodel = new t3DModel();
                // modelImport.import3DS(t3Dmodel, dis);

                t3Dmodel = modelImport.import3DS(dis, Integer
                        .parseInt(modelObj.getID()), Integer.parseInt(modelObj
                                .getType()), modelObj.getScale(), modelObj
                                .getRadius());
                ObjectPool.mModelInforPool.addModel(t3Dmodel);
                // model = new Model(Integer.parseInt(modelObj.getID()), Integer
                // .parseInt(modelObj.getType()), t3Dmodel, modelObj
                // .getScale(), modelObj.getRadius());
//                 ObjectPool.mModelInforPool.addModel(t3Dmodel);
//                model = ObjectPool.mModelInforPool.addModel(Integer
//                        .parseInt(modelObj.getID()), Integer.parseInt(modelObj
//                        .getType()), t3Dmodel, modelObj.getScale(), modelObj
//                        .getRadius());
                locationObj = modelObj.getLocation();
                for (int index = 0; index < locationObj.size; index++) {
                    object = new Object(t3Dmodel, locationObj.points[index]);
                    if (Integer.parseInt(modelObj.getType()) == DataToolKit.COLLISION) {
                        object.updateTransformMatrix();
                    }
                    ObjectPool.mWorld.addObject(object);
                }
                Log.i("Time in Image parse", modelObj.getFilename() + " "
                        + (System.currentTimeMillis() - image_start));
            }
            //
            long gcm_time = System.currentTimeMillis();
            ObjectPool.mWorld.generateCollisionMap();
            Log.i("Time in generateCollisionMap", ""
                    + (System.currentTimeMillis() - gcm_time));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            // 
            e.printStackTrace();
        } catch (SAXException e) {
            // 
            e.printStackTrace();
        } finally {
            // sence = null;
            // senceParser = null;

        }
        Log.i("Load Image Time", "" + (System.currentTimeMillis() - start));
    }

    static public int toFixed(float x) {
        return (int) (x * 65536);
    }

    static public int toFixed(int x) {
        return x * 65536;
    }
    // public static int toFixed(float x) {
    // return (int) (x * 65536.0f);
    // }

}
