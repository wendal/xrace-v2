package com.sa.xrace.client.toolkit;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sa.xrace.client.loader.LocationObj;
import com.sa.xrace.client.loader.ModelObj;
import com.sa.xrace.client.loader.SenceParser2;
import com.sa.xrace.client.model.ModelImport;
import com.sa.xrace.client.model.t3DModel;
import com.sa.xrace.client.scene.AppearableObject;

public final class MethodsPool {

    public static Bitmap getBitmap(int resID) {
//    	Log.e("Get one Bitmap", DataUnti.getNameByID(resID));
//    	long time = System.currentTimeMillis();
//         Bitmap tem =;
//         Log.e("Finish one bitmap", "Time" +(System.currentTimeMillis() - time));
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
//        long start = System.currentTimeMillis();
//        InputStream fis;
//        DataInputStream dis;
        t3DModel t3Dmodel;
//        Model model;
        AppearableObject appearableObject;

        // SenceParser2 senceParser;
        // SenceObj sence;

        try {
            // 处理XML,并统计时间
            // long xml_start = System.currentTimeMillis();
            InputStream is = ObjectPool.assetManager.open(filename);
            // Document document = new SAXReader()
            // .read(new InputStreamReader(fis));
            // senceParser = new SenceParser2(fis);
            // SenceObj sence = SenceParser2.parse(fis);

            ArrayList<ModelObj> modelList = SenceParser2.parse(is);
            is  = null;
            // sence.getLModelList();
            // Log.i("Time in XML parse", ""+(System.currentTimeMillis() -
            // xml_start));
            ModelObj modelObj = null;
            LocationObj locationObj = null;
            int size = modelList.size();
            ModelImport modelImport = new ModelImport();
            for (int i = 0; i < size; i++) {
                // 处理图片,并统计时间,原本的时间为 17700ms
//                long image_start = System.currentTimeMillis();
                modelObj = (ModelObj) modelList.get(i);
//                fis = ObjectPool.assetManager.open(modelObj.Filename);
//                dis = new DataInputStream(fis);
                
//                dis = new DataInputStream(
//                        ObjectPool.assetManager.open(modelObj.filename));
                
                // t3Dmodel = new t3DModel();
                // modelImport.import3DS(t3Dmodel, dis);

                t3Dmodel = modelImport.import3DS(modelObj.filename, modelObj.ID,
                        modelObj.type, modelObj.scale);
                
                //释放两个流
//                dis = null;
//                fis = null;
                
                ObjectPool.mModelInforPool.addModel(t3Dmodel);
                
                locationObj = modelObj.getLocation();
                //只有road和road_line具有Location
                for (int index = 0; index < locationObj.size; index++) {
                    appearableObject = new AppearableObject(t3Dmodel, locationObj.points[index]);
                    locationObj.points[index] = null;
                    if (modelObj.type == DataToolKit.COLLISION) {
                        ObjectPool.cList.add(appearableObject);
//                        appearableObject.updateTransformMatrix();
                    }else{
                    ObjectPool.mWorld.addObject(appearableObject);
//                    modelObj.generate();
                    }
                }
                
//                Log.i("Time in Image parse", modelObj.filename + " "
//                        + (System.currentTimeMillis() - image_start));
            }
            //释放modelObj
//            locationObj = null;
//            modelObj = null;
            //释放ModelImport
//            modelImport.release();
//            modelImport = null;
//            modelList = null;
            //
//            long gcm_time = System.currentTimeMillis();
            //移到到开始倒计时之后
//            ObjectPool.mWorld.generateCollisionMap();
            
            
//            Log.i("Time in generateCollisionMap", ""
//                    + (System.currentTimeMillis() - gcm_time));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            // 
            e.printStackTrace();
        } catch (SAXException e) {
            // 
            e.printStackTrace();
        } 
//        finally {
//            // sence = null;
//            // senceParser = null;
//
//        }
//        Log.i("Load Image Time", "" + (System.currentTimeMillis() - start));
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
