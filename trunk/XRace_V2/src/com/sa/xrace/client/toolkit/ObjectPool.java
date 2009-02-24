package com.sa.xrace.client.toolkit;

import java.net.Socket;

import android.content.res.AssetManager;
import android.content.res.Resources;

import com.sa.xrace.client.GameActivity;
import com.sa.xrace.client.manager.PostManagerClient;
import com.sa.xrace.client.model.ModelInforPool;
import com.sa.xrace.client.pool.GIPool;
import com.sa.xrace.client.pool.InforPoolClient;
import com.sa.xrace.client.pool.RoomPicPool;
import com.sa.xrace.client.pool.WRbarPool;
import com.sa.xrace.client.scene.GLWorld;

public final class ObjectPool {
	
	public static GameActivity activity ;
	
	public static Resources resources;
	
	public static PostManagerClient mPostManager;
	
	public static Socket mSocket;
	
	public static InforPoolClient inPoolClient;
	
	public static AssetManager assetManager;
	
	public static ModelInforPool mModelInforPool;
	
	public static WRbarPool barPool;
	
	public static RoomPicPool rpPool;
	
	public static GLWorld mWorld;
	
	public static GIPool giPool;

}
