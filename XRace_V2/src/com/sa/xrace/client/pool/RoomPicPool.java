///:~
/**
 * XRace V2
 * <p/>Use for ... ...
 * <p/>ID: $Id: $
 * Last Commit:  $Author: $
 * @version $Revision: $
 * 
 */
package com.sa.xrace.client.pool;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sa.xrace.client.GLThread_Room;
import com.sa.xrace.client.GameActivity;
import com.sa.xrace.client.R;
import com.wendal.java.xrace.toolkit.bmpconvert.DataUnti;

public class RoomPicPool {

	private static ByteBuffer down_PicB;
	private static ByteBuffer mapchoice_PicB;
	private static ByteBuffer mapview1_PicB;
	private static ByteBuffer mapview2_PicB;
	private static ByteBuffer carchoice_PicB;
	private static ByteBuffer carview1_PicB;
	public static ByteBuffer car_PicB0;
	public static ByteBuffer car_PicB1;
	public static ByteBuffer car_PicB2;
	public static ByteBuffer car_PicB3;
	private static ByteBuffer loading;

	public Bitmap carB_img, carMyB_img;
	public Bitmap speedometer, speedTriangle;
	public static Bitmap load0, load1, load2, load3;
	public Bitmap minimap;

	public GameActivity activity;
	public IntBuffer tempIB;
	public Bitmap carpoint;

	public RoomPicPool(GameActivity activity) {
		this.activity = activity;
		load0 = getBitmap(R.drawable.load0);
	}

	private Bitmap getBitmap(int resID) {
		Bitmap tem = BitmapFactory.decodeResource(activity.getResources(),
				resID);
		return tem;
	}

	public void generateEveryThing() {
		setDown_PicB(getImageReadyfor(R.drawable.car_down));
		setMapchoice_PicB(getImageReadyfor(R.drawable.mapchoice_pic));
		setMapview1_PicB(getImageReadyfor(R.drawable.mapview1));

		load1 = getBitmap(R.drawable.load1);

		GLThread_Room.makeLoading(GLThread_Room.gl, 82, 0);

		setMapview2_PicB(getImageReadyfor(R.drawable.mapview2));
		setCarchoice_PicB(getImageReadyfor(R.drawable.carchoice));
		speedTriangle = getBitmap(R.drawable.triangle);

		load2 = getBitmap(R.drawable.load2);

		GLThread_Room.makeLoading(GLThread_Room.gl, 102, 1);

		carB_img = getBitmap(R.drawable.upleft_pic);
		setCarview1_PicB(getImageReadyfor(R.drawable.carchoice));
		carMyB_img = getBitmap(R.drawable.mysite_pic);
		speedometer = getBitmap(R.drawable.speedometer);
		carpoint = getBitmap(R.drawable.carpointpic);
		minimap = getBitmap(R.drawable.minimap);
		load3 = getBitmap(R.drawable.load3);

		GLThread_Room.makeLoading(GLThread_Room.gl, 162, 1);
	}

	private ByteBuffer getImageReadyfor(int resname) {
		ByteBuffer tempBuffer;
//		if (resname == R.drawable.car_down) {

//			Bitmap mBitmap = BitmapFactory.decodeResource(activity
//					.getResources(), resname);
//			int pic_width = mBitmap.getWidth();
//			int pic_height = mBitmap.getHeight();
//			// Log.e("pic_width",""+pic_width+" "+pic_height);
//			tempBuffer = ByteBuffer.allocateDirect(pic_width * pic_height * 4);
//			tempBuffer.order(ByteOrder.nativeOrder());
//			tempIB = tempBuffer.asIntBuffer();
//
//			for (int y = 0; y < pic_width; y++) {
//				for (int x = 0; x < pic_height; x++) {
//					tempIB.put(mBitmap.getPixel(x, y));
//				}
//			}
//
//			for (int i = 0; i < pic_width * pic_height * 4; i += 4) {
//				byte temp = tempBuffer.get(i);
//				tempBuffer.put(i, tempBuffer.get(i + 2));
//				tempBuffer.put(i + 2, temp);
//
//			}
//			tempBuffer.position(0);
//			DataUnti.sendOut(DataUnti.getNameByID(resname), tempBuffer);
//		} else {
			tempBuffer = DataUnti.getByteBuffer_ByFileName(DataUnti
					.getNameByID(resname));
//		}
		return tempBuffer;
	}

	/**
	 * @param loading the loading to set
	 */
	public static void setLoading(ByteBuffer loading) {
		RoomPicPool.loading = loading;
	}

	/**
	 * @return the loading
	 */
	public static ByteBuffer getLoading() {
		return loading;
	}

	/**
	 * @param down_PicB the down_PicB to set
	 */
	public static void setDown_PicB(ByteBuffer down_PicB) {
		RoomPicPool.down_PicB = down_PicB;
	}

	/**
	 * @return the down_PicB
	 */
	public static ByteBuffer getDown_PicB() {
		return down_PicB;
	}

	/**
	 * @param mapchoice_PicB the mapchoice_PicB to set
	 */
	public static void setMapchoice_PicB(ByteBuffer mapchoice_PicB) {
		RoomPicPool.mapchoice_PicB = mapchoice_PicB;
	}

	/**
	 * @return the mapchoice_PicB
	 */
	public static ByteBuffer getMapchoice_PicB() {
		return mapchoice_PicB;
	}

	/**
	 * @param mapview1_PicB the mapview1_PicB to set
	 */
	public static void setMapview1_PicB(ByteBuffer mapview1_PicB) {
		RoomPicPool.mapview1_PicB = mapview1_PicB;
	}

	/**
	 * @return the mapview1_PicB
	 */
	public static ByteBuffer getMapview1_PicB() {
		return mapview1_PicB;
	}

	/**
	 * @param mapview2_PicB the mapview2_PicB to set
	 */
	public static void setMapview2_PicB(ByteBuffer mapview2_PicB) {
		RoomPicPool.mapview2_PicB = mapview2_PicB;
	}

	/**
	 * @return the mapview2_PicB
	 */
	public static ByteBuffer getMapview2_PicB() {
		return mapview2_PicB;
	}

	/**
	 * @param carchoice_PicB the carchoice_PicB to set
	 */
	public static void setCarchoice_PicB(ByteBuffer carchoice_PicB) {
		RoomPicPool.carchoice_PicB = carchoice_PicB;
	}

	/**
	 * @return the carchoice_PicB
	 */
	public static ByteBuffer getCarchoice_PicB() {
		return carchoice_PicB;
	}

	/**
	 * @param carview1_PicB the carview1_PicB to set
	 */
	public static void setCarview1_PicB(ByteBuffer carview1_PicB) {
		RoomPicPool.carview1_PicB = carview1_PicB;
	}

	/**
	 * @return the carview1_PicB
	 */
	public static ByteBuffer getCarview1_PicB() {
		return carview1_PicB;
	}

}
