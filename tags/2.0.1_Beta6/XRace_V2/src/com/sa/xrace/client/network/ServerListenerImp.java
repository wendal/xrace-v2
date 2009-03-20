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

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import android.os.HandlerThread;
import android.util.Log;

import com.sa.xrace.client.GLThread_Room;
import com.sa.xrace.client.pool.InforPoolClient;
import com.sa.xrace.client.pool.WRbarPool;
import com.sa.xrace.client.toolkit.DataToolKit;
import com.sa.xrace.client.toolkit.ObjectPool;
import com.sa.xrace.client.toolkit.StateValuePool;

/**
 * @author yyang
 * @version $Id$
 */
public class ServerListenerImp extends HandlerThread {
    private static final String TAG = "-- ServerListenerImp -- ";
    private DataInputStream input;
    private InforPoolClient pool;
    private byte postReceived;
    private int tempNamesI;
    private int tempLName;
    private byte tempID;
    private byte tempModelID;
    private short tempTimeDelay;
    private String tempNameStr;

    public ServerListenerImp() {
        super("ServerListenerImp");
        pool = ObjectPool.inPoolClient;
        try {
            input = new DataInputStream(ObjectPool.mSocket.getInputStream());
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
            Log.e(TAG, "StreamCorruptedException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "IOException");
        }
    }

    public void run() {
        boolean listenerON = true;
        while (listenerON) {
            try {
                postReceived = input.readByte(); // Strut of the car
                if (postReceived == DataToolKit.NORMAL) {
                    // Log.v(TAG, "~~~~~~~~NORMAL Post Received");
                    pool.updateCarInformationsNormal(input.readByte(), input
                            .readFloat(), input.readFloat(), input.readFloat(),
                            input.readFloat(), input.readFloat(), input
                                    .readFloat(), input.readShort());
                } else if (postReceived == DataToolKit.ACCIDENT) {
                    // Log.v(TAG, "ACCIDENT Post Received");
                    pool.updateCarInformationsAccident(input.readByte(),// id
                            input.readFloat(),// Accident speed
                            input.readFloat(),// Accident Direction
                            input.readFloat(),// Accident Acceleration
                            input.readFloat(),// Accident ChangeDirection
                            input.readFloat(),// X position
                            input.readFloat(),// Y position
                            input.readShort() // Time Delay
                            );
                } else if (postReceived == DataToolKit.IDLE) {
                    // Log.v(TAG, "IDLE Post Received");
                    tempID = input.readByte();
                    tempTimeDelay = input.readShort();
                    pool.updateCarInformationsIdle(tempID, tempTimeDelay);
                    InforPoolClient
                            .delayHandleInADD(System.currentTimeMillis());
                    InforPoolClient.calDelayTime();
                } else if (postReceived == DataToolKit.LOGIN) {
                    // Log.v(TAG, "LOGIN Post Received");
                    tempNamesI = input.readInt();
                    tempID = input.readByte();
                    tempNameStr = input.readUTF();
                    for (int i = 0; i < tempNamesI; i++) {
                        tempLName = input.readInt();
                        pool.getOneCarInformation(i).setCarListName(tempLName);
                        WRbarPool.setListCar(tempLName);
                    }
                    pool.updateCarInformationsLogin(tempNamesI, tempID,
                            tempNameStr);
                    // Log.v(TAG, "LOGIN Post Received");
                    // pool.LogAllCarInfor();

                    // barPool.addBar_Login(tempID);
                    // pool.setLoginedFlag();

                    StateValuePool.isLogin = true;
                    GLThread_Room.addBar = false;
                    GLThread_Room.bindex = tempID;

                } else if (postReceived == DataToolKit.LOGINFAILURE) {
                    GLThread_Room.loginFailure = true;
                    Log.v("-- Loging Failure -- ", "-- Loging Failure --");
                } else if (postReceived == DataToolKit.LOGOUT) {
                    // Log.v(TAG,"LOGOUT post Received");
                    tempID = input.readByte();
                    tempNamesI = input.readInt();
                    pool.updateCarInformationLogout(tempID);
                    if(ObjectPool.barPool != null)
                        ObjectPool.barPool.deleteBar_Logout(tempNamesI);
                } else if (postReceived == DataToolKit.CARTYPE) {
                    tempID = input.readByte();
                    tempModelID = input.readByte();
                    pool.updateCarType(tempID, tempModelID);
                    StateValuePool.isCarType = true;
                } else if (postReceived == DataToolKit.START) {
                    // Log.v(TAG, "START Post Received");
                    pool.updatePoolStart();
                    StateValuePool.isStart = true;
                    // ////////////////////////////////////////
                    // jump to game start interface
                    // ////////////////////////////////////////

                    ObjectPool.activity.initGameRunning();
                } else if (postReceived == DataToolKit.DROPOUT) {
                    // Log.v(TAG, "DROPOUT Post Received");
                    tempID = input.readByte();
                    tempNamesI = input.readInt();
                    pool.updateCarInformationDropout(tempID);
                    if(ObjectPool.barPool != null)
                        ObjectPool.barPool.deleteBar_Dropout(tempNamesI);
                } else {
                    Log.e(TAG, "Invalid message!");
                }
            } catch (SocketTimeoutException e) {
                Log.e(TAG, "TimeoutException ServerListener");
            } catch (EOFException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    // Just print it
                    e1.printStackTrace();
                }
                Log.e(TAG, "EOFEXception ServerListener",e);
            } catch (SocketException e) {
                Log.e(TAG, "SocketException ServerListener");
            } catch (Exception e) {
                Log.e(TAG, "Exception ServerListener",e);
                
            }
        }
    }

}
