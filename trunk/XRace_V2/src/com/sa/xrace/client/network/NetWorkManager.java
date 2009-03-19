package com.sa.xrace.client.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

import com.sa.xrace.client.toolkit.NetworkToolKit;
import com.sa.xrace.client.toolkit.ObjectPool;

public final class NetWorkManager implements Runnable{

    public static PostManagerClientImp mPostManager;

    @Override
    public void run() {
        try {
            // ��Socket��������
            ObjectPool.mSocket = new Socket(InetAddress
                    .getByName(NetworkToolKit.SERVERIP),
                    NetworkToolKit.SERVERPORT);
        } catch (UnknownHostException e) {
            Log.v("-- UnknownHostException --",
                    "-- UnknownHostException --");
            e.printStackTrace();
            // �Ƿ�Ӧ���˳���?
        } catch (IOException e) {
            Log.v("-- IOException --", "-- IOException --");
            e.printStackTrace();
            // �Ƿ�Ӧ���˳���?
        }
        
        // ��PostManagerClientImp��������
        NetWorkManager.mPostManager = new PostManagerClientImp();
        // mServerListener =
//        ObjectPool.mHandler.post(new ServerListenerImp());
        new ServerListenerImp().start();
    }
    

}
