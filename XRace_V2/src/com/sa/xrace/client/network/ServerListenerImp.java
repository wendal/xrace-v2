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
    // private Socket socket;
    private InforPoolClient pool;
    private byte postReceived;
    // private byte[] tempNamesBA;
    private int tempNamesI;
    private int tempLName;
    private byte tempID;
    private byte tempModelID;
    private short tempTimeDelay;
    private String tempNameStr;

    /**
     * 暂时发现这个变量的值只设置了一次
     */
    // private boolean listenerON;
    // private static final int TIME_OUT = 10000;
//    private WRbarPool barPool;

    // private GameActivity mActivity;

    // public ServerListenerImp(Socket input) {
    // super("ServerListenerImp");
    // socket = input;
    // listenerON = true;
    //		
    // }

    // public ServerListenerImp(Socket inputSocket, InforPoolClient
    // inPool,GameActivity activity) {
    // super("ServerListenerImp");
    // socket = inputSocket;
    // pool = inPool;
    // mActivity = activity;
    // listenerON = true;
    // try {
    // input = new DataInputStream(socket.getInputStream());
    // this.start();
    // } catch (StreamCorruptedException e) {
    // e.printStackTrace();
    // Log.e(TAG,"StreamCorruptedException");
    // } catch (IOException e) {
    // e.printStackTrace();
    // Log.e(TAG,"IOException");
    // }
    // }

    public ServerListenerImp() {
        super("ServerListenerImp");
        // socket = inputSocket;
        pool = ObjectPool.inPoolClient;
        // mActivity = activity;
        // listenerON = true;
//        this.barPool = ObjectPool.barPool;
        try {
            input = new DataInputStream(ObjectPool.mSocket.getInputStream());
//            this.start();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
            Log.e(TAG, "StreamCorruptedException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "IOException");
        }
    }

    public void run() {
//        long starttime = System.currentTimeMillis();
//        long intertime;
        boolean listenerON = true;
        while (listenerON) {
            // System.out.println("here in "+getClass());
            try {
                postReceived = input.readByte(); // Strut of the car
                if (postReceived == DataToolKit.NORMAL) {
                    // Log.v(TAG, "~~~~~~~~NORMAL Post Received");
                    pool.updateCarInformationsNormal(input.readByte(), input
                            .readFloat(), input.readFloat(), input.readFloat(),
                            input.readFloat(), input.readFloat(), input
                                    .readFloat(), input.readShort());
//                    intertime = System.currentTimeMillis() - starttime;
//                    if (intertime >= 3000) {
//                        System.gc();
//                        starttime = System.currentTimeMillis();
//                        // Log.e("freeMemory",""+Runtime.getRuntime().freeMemory());
//                    }
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
//                    intertime = System.currentTimeMillis() - starttime;
//                    if (intertime >= 3000) {
//                        System.gc();
//                        starttime = System.currentTimeMillis();
//                    }
                    // pool.updateCarInformationsAccident(postReceived);
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

                    /**/
                    // ObjectPool.barPool = null;
                    

                    ObjectPool.activity.initGameRunning();
                    // GLThread_Room.setPhase(GLThread_Room.GAME_RUNNING);
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
                Log.e(TAG, "EOFEXception ServerListener");
            } catch (SocketException e) {
                Log.e(TAG, "SocketException ServerListener");
            } catch (Exception e) {
                Log.e(TAG, "Exception ServerListener");

            }
        }
    }

}
