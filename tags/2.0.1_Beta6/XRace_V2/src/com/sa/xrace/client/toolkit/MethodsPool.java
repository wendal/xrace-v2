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
        return BitmapFactory.decodeResource(ObjectPool.resources, resID);
    }

    public static void LoadMapFromXML(String filename) {
        t3DModel t3Dmodel;
        AppearableObject appearableObject;

        try {
            InputStream is = ObjectPool.assetManager.open(filename);
            ArrayList<ModelObj> modelList = SenceParser2.parse(is);
            is  = null;
            ModelObj modelObj = null;
            LocationObj locationObj = null;
            int size = modelList.size();
            for (int i = 0; i < size; i++) {
                modelObj = modelList.get(i);

                t3Dmodel = ModelImport.import3DS(modelObj.filename, modelObj.ID,
                        modelObj.type, modelObj.scale);
                
                ObjectPool.mModelInforPool.addModel(t3Dmodel);
                
                locationObj = modelObj.getLocation();
                //只有road和road_line具有Location
                for (int index = 0; index < locationObj.size; index++) {
                    appearableObject = new AppearableObject(t3Dmodel, locationObj.points[index]);
                    locationObj.points[index] = null;
                    if (modelObj.type == DataToolKit.COLLISION) {
                        ObjectPool.cList.add(appearableObject);
                    }else{
                    	ObjectPool.mWorld.addObject(appearableObject);
                    }
                }
            }
            //释放ModelImport
            ModelImport.release();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            // 
            e.printStackTrace();
        } catch (SAXException e) {
            // 
            e.printStackTrace();
        }
    }

    static public int toFixed(float x) {
        return (int) (x * 65536);
    }

    static public int toFixed(int x) {
        return x * 65536;
    }
    

//  /**
//   * 这个方法耗时严重,需要改进
//   * 
//   * @param resname
//   * @return
//   */
//  public final static ByteBuffer getImageReadyfor(int resname) {
//      ByteBuffer tempBuffer;
//      Bitmap mBitmap = BitmapFactory.decodeResource(ObjectPool.resources,
//              resname);
//      int pic_width = mBitmap.getWidth();
//      int pic_height = mBitmap.getHeight();
//      // Log.e("pic_width",""+pic_width+" "+pic_height);
//      tempBuffer = ByteBuffer.allocateDirect(pic_width * pic_height * 4);
//      tempBuffer.order(ByteOrder.nativeOrder());
//      IntBuffer tempIB = tempBuffer.asIntBuffer();
//
//      for (int y = 0; y < pic_width; y++) {
//          for (int x = 0; x < pic_height; x++) {
//              tempIB.put(mBitmap.getPixel(x, y));
//          }
//      }
//
//      for (int i = 0; i < pic_width * pic_height * 4; i += 4) {
//          byte temp = tempBuffer.get(i);
//          tempBuffer.put(i, tempBuffer.get(i + 2));
//          tempBuffer.put(i + 2, temp);
//
//      }
//      tempBuffer.position(0);
//      DataUnti.sendOut(DataUnti.getNameByID(resname), tempBuffer);
//      try {
//          Thread.sleep(1000);
//      } catch (InterruptedException e) {
//          // TODO Auto-generated catch block
//          e.printStackTrace();
//      }
//      Log.e("Here", "Shall not show me !");
//      return tempBuffer;
//  }
}
