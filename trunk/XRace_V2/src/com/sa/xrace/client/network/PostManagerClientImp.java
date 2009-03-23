///:~
/**
 * XRace V2
 * <p/>Use for ... ...
 * <p/>ID: $Id$
 * Last Commit:  $Author$
 * @version $Revision: $
 * 
 */
package com.sa.xrace.client.network;

import java.io.DataOutputStream;
import java.io.IOException;

import android.util.Log;

import com.sa.xrace.client.pool.CarInforClient;
import com.sa.xrace.client.pool.InforPoolClient;
import com.sa.xrace.client.toolkit.DataToolKit;
import com.sa.xrace.client.toolkit.ObjectPool;

/**
 * @author Changpeng Pan
 * @version $Id$
 */
public class PostManagerClientImp implements PostManagerClient {
    private static final String TAG = "-- PostMangerClientImp --";
    private InforPoolClient cPool;
    private DataOutputStream cOutput;

    // private Socket cSocket;

    /**
     * 只发现一个调用
     * 
     * @param socket
     * @param pool
     */
    public PostManagerClientImp() {
        this.cPool = ObjectPool.inPoolClient;
        // this.cSocket = ObjectPool.mSocket;
        try {
            cOutput = new DataOutputStream(ObjectPool.mSocket.getOutputStream());
        } catch (IOException e) {
            // e.printStackTrace();
            Log.e(TAG, "PostManagerClientImp() IOException", e);
        }
    }

    public void sendLoginPostToServer() {
        try {
            cOutput.writeByte(DataToolKit.LOGIN);
            cOutput.writeUTF(ObjectPool.myCar.getNName());
            cOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "sendLoginPostToServer()" + "IOException");
        }
    }

    public void sendCarTypePostToServer() {
        try {
            cOutput.writeByte(DataToolKit.CARTYPE);
            cOutput.writeByte(ObjectPool.myCar.getNCarID());
            cOutput.writeByte(ObjectPool.myCar.getModel().mModelID);
            cOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "sendCarTypePostToServer()" + "IOException");
        }
    }

    public void sendLogoutPostToServer() {
        try {
            cOutput.writeByte(DataToolKit.LOGOUT);
            cOutput.writeByte(ObjectPool.myCar.getNCarID());
            cOutput.writeUTF(ObjectPool.myCar.getNName());
            cOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "sendLogoutPostToServer()" + "IOException");
        }
    }

    public void sendIdlePostToServer() {
        try {
            cOutput.writeByte(DataToolKit.IDLE);
            cOutput.writeByte((byte) cPool.getMyCarIndex());
            cOutput.writeShort(ObjectPool.myCar.getTimeDelay());
            cOutput.flush();
            InforPoolClient.delayHandleOutADD(System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "sendNormalPostToServer()" + "Exception");
        }
    }

    public void sendStartPostToServer() {
        try {
            cOutput.writeByte(DataToolKit.START);
            cOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "sendStartPostToServer()" + "IOException");
        }
    }

    public void sendNormalPostToServer() {

        CarInforClient myCar = ObjectPool.myCar;
        try {
            cOutput.writeByte(DataToolKit.NORMAL);
            cOutput.writeByte(myCar.getNCarID());
            cOutput.writeFloat(myCar.getNSpeed());
            cOutput.writeFloat(myCar.getNDirection());
            cOutput.writeFloat(myCar.getNAcceleration());
            cOutput.writeFloat(myCar.getNChangeDirection());
            cOutput.writeFloat(myCar.getNXPosition());
            cOutput.writeFloat(myCar.getNYPosition());
            cOutput.writeShort(myCar.getTimeDelay());
            cOutput.flush();
            InforPoolClient.delayHandleOutADD(System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "sendNormalPostToServer()" + "Exception");
        }
    }

    public void sendAccidentPostToServer() {
        CarInforClient myCar = ObjectPool.myCar;
        try {
            cOutput.writeByte(DataToolKit.ACCIDENT);
            cOutput.writeByte(myCar.getNCarID());
            cOutput.writeFloat(myCar.getNAccidenceSpeed());
            cOutput.writeFloat(myCar.getNAccidenceDirection());
            cOutput.writeFloat(myCar.getNAccidenceAcceleration());
            cOutput.writeFloat(myCar.getNAccidenceChangeDirection());
            cOutput.writeFloat(myCar.getNXPosition());
            cOutput.writeFloat(myCar.getNYPosition());
            cOutput.writeShort(myCar.getTimeDelay());
            cOutput.flush();
            InforPoolClient.delayHandleOutADD(System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "sendNormalPostToServer()" + "Exception");
        }
    }

}
