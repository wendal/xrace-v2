///:~
/**
 * XRace V2
 * <p/>Use for ... ...
 * <p/>ID: $Id$
 * Last Commit:  $Author$
 * @version $Revision: $
 * 
 */
package com.wendal.java.xrace.toolkit.bmpconvert;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

import android.util.Log;

import com.sa.xrace.client.R;
import com.sa.xrace.client.toolkit.NetworkToolKit;
import com.sa.xrace.client.toolkit.ObjectPool;

public final class DataUnti {

    private DataUnti() {

    }

    // private static AssetManager assetManager;

    // private static final HashMap<String, ByteBuffer> image_bytebuffer = new
    // HashMap<String, ByteBuffer>();

    // private static boolean isReady = false;

    // public static void init(Activity activity) {
    // if (!isReady) {
    // assetManager = activity.getAssets();
    // try {
    // String[] list = assetManager.list("rgb");
    // for (String filename : list) {
    // // if (filename.endsWith(".rgb")) {
    // // Log.i("Reading File", filename);
    // InputStream is = assetManager.open(filename);
    // byte data[] = new byte[is.available()];
    // int len = is.read(data);
    // if (len != data.length) {
    // throw new IOException("Size error!");
    // }
    // // Log.e("DataUnit", filename +"  " +(len));
    // ByteBuffer buffer = ByteBuffer.allocateDirect(data.length);
    // buffer.order(ByteOrder.nativeOrder());
    // buffer.put(data);
    // buffer.position(0);
    // image_bytebuffer.put(filename.toUpperCase(), buffer);
    // is.close();
    // // Log.i("Finsih File", filename);
    // // }
    // }
    // isReady = true;
    // // Log.w("DatUnit Map status", "Size "+image_bytebuffer.size());
    // // Set<String> set = image_bytebuffer.keySet();
    // // for (String string : set) {
    // // Log.e("key in map : ", string);
    // // }
    //				
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    // }

    public static ByteBuffer getByteBuffer_ByFileName(String srcFilename) {

        // if (null == image_bytebuffer.get(srcFilename.toUpperCase()+".RBG"))
        // Log.e("DataUnit", srcFilename);
        ByteBuffer buffer = null;
        {
            try {
                buffer = readData(ObjectPool.assetManager.open(srcFilename
                        + ".rbg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer;

    }

    public static final int[] getBmpSize(String srcFilename) {
        int[] size = new int[2];
        try {
            InputStream is = ObjectPool.assetManager
                    .open(srcFilename + ".size");
            DataInputStream dis = new DataInputStream(is);
            size[0] = dis.readInt();
            size[1] = dis.readInt();
            // byte [] data = new byte[2];
            // if (is.read(data) != 2) {
            // throw new IOException("Size error!");
            // }
            // size[0] = data[0] + 256;
            // size[1] = data[1] + 256;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return size;
    }

    public static ByteBuffer getByteBuffer_ByID(int id) {

        // if (null == image_bytebuffer.get(srcFilename.toUpperCase()+".RBG"))
        // Log.e("DataUnit", srcFilename);
        ByteBuffer buffer = getByteBuffer_ByFileName(getNameByID(id));
        // try {
//        if (buffer == null) {
//            buffer = MethodsPool.getImageReadyfor(id);
//        }
        return buffer;
        // } catch (IOException e) {
        // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // }
        // return buffer;
    }

    private static ByteBuffer readData(InputStream is) {
        ByteBuffer buffer = null;
        try {
            // InputStream
            // is = ObjectPool.assetManager.open(srcFilename);
            byte data[] = new byte[is.available()];
            int len = is.read(data);
            if (len != data.length) {
                throw new IOException("Size error!");
            }
            // Log.e("DataUnit", filename +"  " +(len));
            buffer = ByteBuffer.allocateDirect(data.length);
            buffer.order(ByteOrder.nativeOrder());
            buffer.put(data);
            buffer.position(0);
            // image_bytebuffer.put(filename.toUpperCase(), buffer);
            is.close();
        } catch (IOException e) {
            Log.e("In DataUnti", "Read Error", e);
            // String [] str = srcFilename.split(".");
            // srcFilename = str[0] + "bmp";
            // buffer = MethodsPool.getImageReadyfor(resname);
        }
        return buffer;
    }

    public static final void sendOut(String filename, ByteBuffer byteBuffer) {
        // 把数据传出去
        Log.e("Start sending : ", filename);
        BmpMessage message = new BmpMessage();
        message.setFilename(filename);

        byte[] data = new byte[byteBuffer.limit()];
        for (int i = 0; i < data.length; i++) {
            data[i] = byteBuffer.get(i);
        }
        byteBuffer.position(0);
        // intBuffer.position(0);
        message.setData(data);
        try {
            Socket socket = new Socket(NetworkToolKit.SERVERIP, 6666);
            ObjectOutputStream oos = new ObjectOutputStream(socket
                    .getOutputStream());
            oos.writeObject(message);
            oos.flush();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.e("End sending : ", filename);
    }

    public static final void sendOut(String filename, int x, int y) {
        // 把数据传出去
        Log.e("Start sending : ", filename);
        BmpSizeMessage message = new BmpSizeMessage();
        message.setName(filename);

        // byte[] data = new byte[byteBuffer.limit()];
        // for (int i = 0; i < data.length; i++) {
        // data[i] = byteBuffer.get(i);
        // }
        // byteBuffer.position(0);
        // // intBuffer.position(0);
        // message.setData(data);
        int[] data = new int[2];
        data[0] = x;
        data[1] = y;
        message.setSize(data);
        try {
            Socket socket = new Socket(NetworkToolKit.SERVERIP, 6666);
            ObjectOutputStream oos = new ObjectOutputStream(socket
                    .getOutputStream());
            oos.writeObject(message);
            oos.flush();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.e("End sending : ", filename);
    }

    private static final HashMap<Integer, String> drawable_resourse_map = new HashMap<Integer, String>();
    static {
        drawable_resourse_map.put(R.drawable.car_down, "car_down");
        drawable_resourse_map.put(R.drawable.mapchoice_pic, "mapchoice_pic");
        drawable_resourse_map.put(R.drawable.mapview1, "mapview1");
        drawable_resourse_map.put(R.drawable.load1, "load1");
        drawable_resourse_map.put(R.drawable.mapview2, "mapview2");
        drawable_resourse_map.put(R.drawable.carchoice, "carchoice");
        drawable_resourse_map.put(R.drawable.triangle, "triangle");
        drawable_resourse_map.put(R.drawable.load2, "load2");
        drawable_resourse_map.put(R.drawable.upleft_pic, "upleft_pic");
        drawable_resourse_map.put(R.drawable.carchoice, "carchoice");
        drawable_resourse_map.put(R.drawable.mysite_pic, "mysite_pic");
        drawable_resourse_map.put(R.drawable.speedometer, "speedometer");
        drawable_resourse_map.put(R.drawable.carpointpic, "carpointpic");
        drawable_resourse_map.put(R.drawable.minimap, "minimap");
        drawable_resourse_map.put(R.drawable.alert_dialog_icon,
                "alert_dialog_icon");
        drawable_resourse_map.put(R.drawable.car, "car");
        drawable_resourse_map.put(R.drawable.carview1_pic, "carview1_pic");
        drawable_resourse_map.put(R.drawable.down_pic, "down_pic");
        drawable_resourse_map.put(R.drawable.icon, "icon");
        drawable_resourse_map.put(R.drawable.laod, "laod");
        drawable_resourse_map.put(R.drawable.load3, "load3");
        drawable_resourse_map.put(R.drawable.logo, "logo");
        drawable_resourse_map.put(R.drawable.logo1, "logo1");
        drawable_resourse_map.put(R.drawable.multiple, "multiple");
        drawable_resourse_map.put(R.drawable.mysite, "mysite");
        drawable_resourse_map.put(R.drawable.setting, "setting");
        drawable_resourse_map.put(R.drawable.single, "single");
        drawable_resourse_map.put(R.drawable.triangle, "triangle");
        drawable_resourse_map.put(R.drawable.xrace, "xrac");
        // drawable_resourse_map.put(R.drawable.logo1,"logo1");
        // drawable_resourse_map.put(R.drawable.logo1,"logo1");
        // drawable_resourse_map.put(R.drawable.logo1,"logo1");
        // drawable_resourse_map.put(R.drawable.logo1,"logo1");
        // drawable_resourse_map.put(R.drawable.logo1,"logo1");

    }

    public static String getNameByID(int id) {
        return drawable_resourse_map.get(id);
    }
}
