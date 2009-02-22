package com.sa.xrace.client.manager;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import android.util.Log;

import com.sa.xrace.client.pool.InforPoolClient;

/**
 * @author Changpeng Pan
 * @version $Id: PostManagerClientImp.java,v 1.1 2008-11-17 07:32:26 cpan Exp $
 */
public class PostManagerClientImp implements PostManagerClient {
	private static final String TAG = "-- PostMangerClientImp --";
	private InforPoolClient cPool;
	private DataOutputStream cOutput;
	private Socket cSocket;

	public PostManagerClientImp(Socket socket, InforPoolClient pool) {
		this.cPool = pool;
		this.cSocket = socket;
		try {
			cOutput = new DataOutputStream(cSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "PostManagerClientImp()" + "IOException");
		}
	}

	public void sendLoginPostToServer() {
		try {
			cOutput.writeByte(InforPoolClient.LOGIN);
			cOutput.writeUTF(cPool.getOneCarInformation(cPool.getMyCarIndex()).getNName());
			cOutput.flush();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "sendLoginPostToServer()" + "IOException");
		}
	}
	
	public void sendCarTypePostToServer() {
		try{
			cOutput.writeByte(InforPoolClient.CARTYPE);
			cOutput.writeByte(cPool.getOneCarInformation(cPool.getMyCarIndex()).getNCarID());
			cOutput.writeByte(cPool.getOneCarInformation(cPool.getMyCarIndex()).getModel().getID());
			cOutput.flush();
		}catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "sendCarTypePostToServer()" + "IOException");
		}
	}

	public void sendLogoutPostToServer() {
		try {
			cOutput.writeByte(InforPoolClient.LOGOUT);
			cOutput.writeByte(cPool.getOneCarInformation(cPool.getMyCarIndex()).getNCarID());
			cOutput.writeUTF(cPool.getOneCarInformation(cPool.getMyCarIndex()).getNName());
			cOutput.flush();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "sendLogoutPostToServer()" + "IOException");
		}
	}

	public void sendIdlePostToServer() {
		try {
			cOutput.writeByte(InforPoolClient.IDLE);
			cOutput.writeByte((byte) cPool.getMyCarIndex());
			cOutput.writeShort(cPool.getOneCarInformation(cPool.getMyCarIndex()).getTimeDelay());
			cOutput.flush();
			InforPoolClient.delayHandleOutADD(System.currentTimeMillis());
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "sendNormalPostToServer()" + "Exception");
		}
	}
	
	public void sendStartPostToServer() {
		try {
			cOutput.writeByte(InforPoolClient.START);
			cOutput.flush();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "sendStartPostToServer()" + "IOException");
		}
	}

	public void sendNormalPostToServer() {

		try {
			cOutput.writeByte(InforPoolClient.NORMAL);
			cOutput.writeByte(cPool.getOneCarInformation(cPool.getMyCarIndex()).getNCarID());
			cOutput.writeFloat(cPool.getOneCarInformation(cPool.getMyCarIndex()).getNSpeed());
			cOutput.writeFloat(cPool.getOneCarInformation(cPool.getMyCarIndex()).getNDirection());
			cOutput.writeFloat(cPool.getOneCarInformation(cPool.getMyCarIndex()).getNAcceleration());
			cOutput.writeFloat(cPool.getOneCarInformation(cPool.getMyCarIndex()).getNChangeDirection());
			cOutput.writeFloat(cPool.getOneCarInformation(cPool.getMyCarIndex()).getNXPosition());
			cOutput.writeFloat(cPool.getOneCarInformation(cPool.getMyCarIndex()).getNYPosition());
			cOutput.writeShort(cPool.getOneCarInformation(cPool.getMyCarIndex()).getTimeDelay());
			cOutput.flush();
			InforPoolClient.delayHandleOutADD(System.currentTimeMillis());
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "sendNormalPostToServer()" + "Exception");
		}
	}

	public void sendAccidentPostToServer() {
		try {
			cOutput.writeByte(InforPoolClient.ACCIDENT);
			cOutput.writeByte(cPool.getOneCarInformation(cPool.getMyCarIndex()).getNCarID());
			cOutput.writeFloat(cPool.getOneCarInformation(cPool.getMyCarIndex()).getNAccidenceSpeed());
			cOutput.writeFloat(cPool.getOneCarInformation(cPool.getMyCarIndex()).getNAccidenceDirection());
			cOutput.writeFloat(cPool.getOneCarInformation(cPool.getMyCarIndex()).getNAccidenceAcceleration());
			cOutput.writeFloat(cPool.getOneCarInformation(cPool.getMyCarIndex()).getNAccidenceChangeDirection());
			cOutput.writeFloat(cPool.getOneCarInformation(cPool.getMyCarIndex()).getNXPosition());
			cOutput.writeFloat(cPool.getOneCarInformation(cPool.getMyCarIndex()).getNYPosition());
			cOutput.writeShort(cPool.getOneCarInformation(cPool.getMyCarIndex()).getTimeDelay());
			cOutput.flush();
			InforPoolClient.delayHandleOutADD(System.currentTimeMillis());
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "sendNormalPostToServer()" + "Exception");
		}
	}
	
}
