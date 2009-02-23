package com.sa.xrace.client.toolkit;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public final class MethodsPool {

	public static Bitmap getBitmap(int resID) {
	//		Bitmap tem = 
			return	BitmapFactory.decodeResource(ObjectPool.resources,
					resID);
	//		return tem;
		}

	/**
	 * 这个方法耗时严重,需要改进
	 * @param resname
	 * @return
	 */
	public static ByteBuffer getImageReadyfor(int resname) {
		ByteBuffer tempBuffer;
		Bitmap mBitmap = BitmapFactory.decodeResource(ObjectPool.resources, resname);
		int pic_width = mBitmap.getWidth();
		int pic_height = mBitmap.getHeight();
		// Log.e("pic_width",""+pic_width+" "+pic_height);
		tempBuffer = ByteBuffer.allocateDirect(pic_width * pic_height * 4);
		tempBuffer.order(ByteOrder.nativeOrder());
		IntBuffer tempIB = tempBuffer.asIntBuffer();
	
		for (int y = 0; y < pic_width; y++) {
			for (int x = 0; x < pic_height; x++) {
				tempIB.put(mBitmap.getPixel(x, y));
			}
		}
	
		for (int i = 0; i < pic_width * pic_height * 4; i += 4) {
			byte temp = tempBuffer.get(i);
			tempBuffer.put(i, tempBuffer.get(i + 2));
			tempBuffer.put(i + 2, temp);
	
		}
		tempBuffer.position(0);
		return tempBuffer;
	}

}
