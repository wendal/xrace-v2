package com.sa.xrace.client.pool;

import java.util.Vector;

import android.util.Log;

import com.sa.xrace.client.model.ModelInforPool;

/**
 * @author Changpeng Pan
 * @version $Id: InforPoolClient.java,v 1.19 2008-12-10 05:10:55 cpan Exp $
 */
public class InforPoolClient {
	private static final String TAG = "-- InforPoolClient --";

	public static final byte LOGIN = 101;
	public static final byte LOGOUT = 100;
	public static final byte START = 111;
	public static final byte NORMAL = 10;
	public static final byte ACCIDENT = 20;
	public static final byte IDLE = 110;
	public static final byte DROPOUT = 30;
	public static final byte CARTYPE = 14;
	public static boolean Logined = false;
	public static final byte LOGINFAILURE =102;


	
	private static int nCarNumber;
	// The position index in car information of my car
	private static int myCarIndex;
	private static CarInforClient[] nCar;
	private static ModelInforPool modelPool;
	// Time synchronize
	private static Vector<Integer> delayHandleIn;
	private static Vector<Integer> delayHandleOut;

	private static float nowSpeed = 0.0f;
	private static float nowDirection = 0.0f;
	private static float distanceMade = 0.0f;

	private static float speedCha = 0.0f;
	private static float directCha = 0.0f;
	public static float[] mSensor = { 0.0f, 0.0f };
	
	private static int mStatus = 0;

	private static float time_ajusted = 0.0f;

	// If there is accident set it true;
	private static boolean isAccident = false;

	/**
	 * Initiates the car capacity in information pool to 6 Initiates the
	 * delayHandle ArrayLists
	 */
	public InforPoolClient() {
		nCarNumber = 1;
		myCarIndex = 0;
		nCar = new CarInforClient[6];
		for (int i = 0; i < 6; i++) {
			nCar[i] = new CarInforClient();
		}
		delayHandleIn = new Vector<Integer>();
		delayHandleOut = new Vector<Integer>();
//		setNCarNumber(4);

	}
	
	public InforPoolClient(ModelInforPool mModelInforPool) {
		nCarNumber = 1;
		myCarIndex = 0;
		nCar = new CarInforClient[6];
		for (int i = 0; i < 6; i++) {
			nCar[i] = new CarInforClient();
		}
		delayHandleIn = new Vector<Integer>();
		delayHandleOut = new Vector<Integer>();
		modelPool = mModelInforPool;
	}

	/**
	 * Sets the Index of "my" car
	 */
	public void setMyCarIndex(int input) {
		myCarIndex = input;
	}

	/**
	 * Gets the Index of this user's car
	 */
	public int getMyCarIndex() {
		return myCarIndex;
	}

	/**
	 * Returns the information of the car indicated
	 */
	public CarInforClient getOneCarInformation(int car) {
		return nCar[car];
	}

	/**
	 * Returns the number of the car online
	 */
	public int getNCarNumber() {
		return nCarNumber;
	}

	/**
	 * Sets the number of the car online
	 */
	public void setNCarNumber(int carNumber) {
		nCarNumber = carNumber;
	}

	/**
	 * Gets the flag, which indicates whether there is accident this time and we
	 * got involved
	 */
	public static boolean getIsAccident() {
		return isAccident;
	}
	
	/**
	 * Sets the accident flag whether our own car is involved
	 */
	public static void setAccident(boolean isAccident) {
		InforPoolClient.isAccident = isAccident;
	}

	/**
	 * Updates the information which describes each car in start situation
	 */
	public void updatePoolStart() {
		for (int i = 0; i < nCarNumber; i++) {
			getOneCarInformation(i).setNStatus(NORMAL);
			
			nCar[i].setNYPosition(-11500);
			nCar[i].setNXPosition(400 - i*400 );
		}
	}
	/**
	 * Updates the information which describes each car in car type situation
	 */
	public void updateCarType(byte id,byte modelID)
	{
//		getOneCarInformation(id).setModel(modelPool.getModel(model));
		int index = (int)id;
//		if (index != getMyCarIndex()) {			
//		System.out.println("updateCarType = ");
//			modelPool.setType(Model.CAR);
			nCar[index].setModel(modelPool.getModel(modelID));
			//nCar[index].setMOriginalBox(new AABBbox(modelPool.getModel(modelID)));
			nCar[index].getMOriginalBox().getAABBbox(nCar[index].getModel());
			
			
			
//		}
	}

	/**
	 * Updates the information which describes each car in login situation
	 */
	public void updateCarInformationsLogin(int carN, byte carID,String names) {
		setNCarNumber(carN);
		if (Logined == false) {
			setMyCarIndex(carID);
		}
		for(int i =0;i<carN;i++)
		{	
			nCar[i].setNName(names.substring(i*10, ((i+1)*10)-1).replace("-", ""));
			nCar[i].setNStatus(IDLE);
			nCar[i].setNCarID((byte)i);
		}
	}

	/**
	 * Updates the information which describes each car in logout situation
	 */
	public void updateCarInformationLogout(byte id) {
		nCarNumber -= 1;
		if (getMyCarIndex() > (int) id) {
			setMyCarIndex(getMyCarIndex() - 1);
		}
		moveTheRestCarForward((int) id);
	}

	/**
	 * Updates the information which describes each car in dropout situation
	 */
	public void updateCarInformationDropout(byte id) {
		nCarNumber -= 1;
		if (getMyCarIndex() > (int)id) {
			setMyCarIndex(getMyCarIndex() - 1);
		}
		moveTheRestCarForward((int)id);
	}

	/**
	 * Updates the information when Myself is drop out...
	 */
	public void cleanOtherCarInfor() {
		int nowpost =0;
		nCarNumber = 1;
		if (getMyCarIndex() != 0) {
			try {
				nCar[0].setNCarID((byte)nowpost);
				nCar[0].setNAcceleration(nCar[getMyCarIndex()].getNAcceleration());
				nCar[0].setNCarType(nCar[getMyCarIndex()].getNCarType());
				nCar[0].setNDirection(nCar[getMyCarIndex()].getNDirection());
				nCar[0].setNName(nCar[getMyCarIndex()].getNName());
				nCar[0].setNSpeed(nCar[getMyCarIndex()].getNSpeed());
				nCar[0].setNXPosition(nCar[getMyCarIndex()].getNXPosition());
				nCar[0].setTimeDelay(nCar[getMyCarIndex()].getTimeDelay());
				nCar[0].setNYPosition(nCar[getMyCarIndex()].getNYPosition());
			} catch (Exception e) {
				Log.e(TAG, "cleanOtherCarInfor():Exception");
			}
		}
	}

	/**
	 * Updates the information which describes each car in normal situation
	 */
	public void updateCarInformationsNormal(
			byte id,float speed,float direction,float acceleration,
			float dirChange,float xposition,float yposition,short timedelay) 
	{
		int index = (int)id;
		if (index != getMyCarIndex()) {			
			nCar[index].setNSpeed(speed);
			nCar[index].setNDirection(direction);
			nCar[index].setNAcceleration(acceleration);
			nCar[index].setNChangeDirection(dirChange);
			nCar[index].setNXPosition(xposition);
			nCar[index].setNYPosition(yposition);
			nCar[index].setTimeDelay(timedelay);
			
		}else{
			//calculate out the timeDelay of my client
			InforPoolClient.delayHandleInADD(System.currentTimeMillis());
			InforPoolClient.calDelayTime();
//			Log.i("getTimeDelay()",""+getOneCarInformation(getMyCarIndex()).getTimeDelay());
		}
	}

	/**
	 * Updates the information which describes each car in accident situation
	 */
	public void updateCarInformationsAccident(byte id,float aSpeed,float aDirection,float aAcceleration,
			float aDirChange,float xposition,float yposition,short timedelay) {
		int index = (int)id;
		if (index != getMyCarIndex()) {
				nCar[index].setNAccidenceSpeed(aSpeed);
				nCar[index].setNAccidenceDirection(aDirection);
				nCar[index].setNAccidenceAcceleration(aAcceleration);
				nCar[index].setNAccidenceChangeDirection(aDirChange);
				nCar[index].setNXPosition(xposition);
				nCar[index].setNYPosition(yposition);
				nCar[index].setTimeDelay(timedelay);
		}else{
			InforPoolClient.delayHandleInADD(System.currentTimeMillis());
			InforPoolClient.calDelayTime();
		}
	}
	
	/**
	 * Updates the information which describes each car in Idle situation
	 * primarily update the TimeDelay
	 */
	public void updateCarInformationsIdle(byte id, short timeDelay)
	{
		if(id != getMyCarIndex())
		{
			int index = (int)id;
			nCar[index].setTimeDelay(timeDelay);
			InforPoolClient.delayHandleInADD(System.currentTimeMillis());
			InforPoolClient.calDelayTime();
		}
	}

	/**
	 * Reads an input index. Move the carinformations behind that index forward.
	 */
	private void moveTheRestCarForward(int position) {
		for (int i = position; i < nCarNumber; i++) {
			copyCarToForwadPostion(i + 1);
		}
	}

	/**
	 * Copies the carinformation to the forward position
	 */
	private void copyCarToForwadPostion(int nowPosition) {
		try {
			nCar[nowPosition - 1].setNCarID((byte)(nCar[nowPosition].getNCarID() - 1));
			nCar[nowPosition - 1].setNAcceleration(nCar[nowPosition].getNAcceleration());
			nCar[nowPosition - 1].setNCarType(nCar[nowPosition].getNCarType());
			nCar[nowPosition - 1].setNDirection(nCar[nowPosition].getNDirection());
			nCar[nowPosition - 1].setNName(nCar[nowPosition].getNName());
			nCar[nowPosition - 1].setNSpeed(nCar[nowPosition].getNSpeed());
			nCar[nowPosition - 1].setNXPosition(nCar[nowPosition].getNXPosition());
			nCar[nowPosition - 1].setTimeDelay(nCar[nowPosition].getTimeDelay());
			nCar[nowPosition - 1].setNYPosition(nCar[nowPosition].getNYPosition());
			nCar[nowPosition - 1].setNChangeDirection(nCar[nowPosition].getNChangeDirection());
			nCar[nowPosition - 1].setCarListName(nCar[nowPosition].getCarListName());
			nCar[nowPosition - 1].setModel(nCar[nowPosition].getModel());
		} catch (Exception e) {
			Log.e(TAG, "copyCarToForwadPostion():Exception");
		}
	}

	/**
	 * Calculate out how much time it takes in network transmission Deletes the
	 * beginning points in both array when calculation successful
	 */
	public static void calDelayTime() {
//		Log.i("delayHandleIn.size",""+delayHandleIn.size());
//		Log.i("delayHandleOut.size",""+delayHandleOut.size());
		if (delayHandleIn.size() > 0 && delayHandleOut.size() > 0) {
//			Log.i("calbefore",""+(delayHandleIn.get(0) - delayHandleOut.get(0)) / 2);
//			Log.e("delayHandleIn.",""+delayHandleIn.get(0));
//			Log.e("delayHandleOut.",""+delayHandleOut.get(0));
//			Log.e("delaybefore",""+(delayHandleIn.get(0) - delayHandleOut.get(0)) / 2);

			short temp = (short)((delayHandleIn.get(0) - delayHandleOut.get(0)));
//			if(temp <0) temp =0;

			nCar[myCarIndex].setTimeDelay(temp);			
//			Log.e("delayafter",""+nCar[myCarIndex].getTimeDelay());
			delayHandleIn.remove(0);
			delayHandleOut.remove(0);
		}
	}

	/**
	 * Add time point into delayhandleIn
	 */
	public static void delayHandleInADD(Long input) {
		delayHandleIn.add(timeFixing(input));
//		Log.e("delayHandleIn","in");
	}

	/**
	 * Add time point into delayhandleOut
	 */
	public static void delayHandleOutADD(Long input) {
		delayHandleOut.add(timeFixing(input));
//		Log.e("delayHandleIn","out");
	}

	/**
	 * Updates all car speed and direction out, including my car; other car
	 * related
	 */
	public void prepareAllCarInformation(int timeElapsed, float direct_change,float speed_change) {
		if (timeElapsed == 0)
    	{
    		timeElapsed = 1;
    	}

		// we do not store nowDirection , Use the direction calculated out
		nowSpeed = 0.0f;
		nowDirection = 0.0f;
		distanceMade = 0.0f;
		time_ajusted = timeElapsed;
		speedCha = speed_change;
		directCha = (float) Math.toRadians(direct_change);
		// store the acceleration and ChangeDirection for further calculation

		for (int i = 0; i < getNCarNumber(); i++) {
			mStatus = getOneCarInformation(i).getNStatus();
			if (i == getMyCarIndex()) 
//			if (i == getOneCarInformation(i).getNCarID())
			{
				if(mStatus == NORMAL)
				{				
					
					nCar[i].updateSpeedBySensor(speedCha, time_ajusted);	//change the speed
					nCar[i].updateDirectionBySensor(directCha, time_ajusted); //change the direction
					
					// s = (v0+v1)T/2
					distanceMade = (nCar[i].getNSpeed() + nowSpeed) * time_ajusted / 2;	
									
					// put distance into coordinates X,Y
//					nCar[i].setNXPosition(nCar[i].getNXPosition() + distanceMade * (float) Math.sin(-nCar[i].getNDirection()));
//					nCar[i].setNYPosition(nCar[i].getNYPosition() + distanceMade * (float) Math.cos(-nCar[i].getNDirection()));
					// the 3d date of the x and y 
					nCar[i].setNXPosition(nCar[i].getNXPosition() + distanceMade * (float) Math.sin(nCar[i].getNDirection()));
					nCar[i].setNYPosition(nCar[i].getNYPosition() + distanceMade * (float) Math.cos(nCar[i].getNDirection()));
				}
				else if(mStatus == ACCIDENT)
				{
					
				}
				
				
//				Log.i("setNXPosition",""+nCar[i].getNXPosition());
//				Log.i("setNYPosition",""+nCar[i].getNYPosition());
			} 
			else 
			{
//				// Speed
//				nowSpeed = nCar[i].getNSpeed() + nCar[i].getNAcceleration() * (time_ajusted + nCar[i].getTimeDelay()/20 + nCar[myCarIndex].getTimeDelay()/10 );
//
//				// Direction
//				if (nowSpeed >= -CarInforClient.TOP_ANTI_SPEED && nowSpeed <= CarInforClient.TOP_SPEED) {
//				// do nothing
//					
//				}else if(nowSpeed < -CarInforClient.TOP_ANTI_SPEED){
//					nowSpeed = -CarInforClient.TOP_ANTI_SPEED;
//				}
//				else if(nowSpeed > CarInforClient.TOP_SPEED){
//					nowSpeed = CarInforClient.TOP_SPEED;
//				}
//				
//				if(nowSpeed!=0){
//					nowDirection = nCar[i].getNDirection() + nCar[i].getNChangeDirection() * (time_ajusted + nCar[i].getTimeDelay()/20 + nCar[myCarIndex].getTimeDelay()/20 );
//					if(nowDirection < 0 ){
//						nowDirection = (float) (nowDirection + 2*Math.PI);
//					}else if(nowDirection > 2*Math.PI ){
//						nowDirection  = (float) (nowDirection - 2*Math.PI);
//					}
//					nCar[i].setNDirection(nowDirection);
//				}
//				
//				// s = (v0+v1)T/2
//				distanceMade = (nCar[i].getNSpeed() + nowSpeed) * time_ajusted / 2;
//				nCar[i].setNSpeed(nowSpeed);
//				nCar[i].setNXPosition(nCar[i].getNXPosition() + distanceMade * (float) Math.sin(nCar[i].getNDirection()));
//				nCar[i].setNYPosition(nCar[i].getNYPosition() + distanceMade * (float) Math.cos(nCar[i].getNDirection()));
//				
//				
//				System.out.println("distanceMade"+distanceMade);
//				System.out.println("nowSpeed"+nowSpeed);
//				System.out.println("time_ajusted"+time_ajusted);
//				System.out.println("distanceMade"+distanceMade);
			}
		}
	}

	/**
	 * Fix Long time to int
	 */
	public static int timeFixing(Long input) {
		return (int) (input % 1000000000 % 10000);
	}

	public void LogAllCarInfor() {

		 Log.v("car MyCarIndex", "" + getMyCarIndex());
		 if (getNCarNumber() == 0) {
		 Log.e("one car infor", "=====================================");
		 Log.v("car NAcceleration", ""
		 + nCar[0].getNAcceleration());
		 Log.v("car NAccidenceDirection", ""
		 + nCar[0].getNAccidenceDirection());
		 Log.v("car NAccidenceSpeed", ""
		 + nCar[0].getNAccidenceSpeed());
		 Log.v("car NCarID", "" + nCar[0].getNCarID());
		 Log.v("car NCarType", "" + nCar[0].getNCarType());
		 Log.v("car NDirection", "" + nCar[0].getNDirection());
		 Log.v("car NName", "" + nCar[0].getNName());
		 Log.v("car NSpeed", "" + nCar[0].getNSpeed());
		 Log.v("car NStatus", "" + nCar[0].getNStatus());
		 Log.v("car NXPosition", "" + nCar[0].getNXPosition());
		 Log.v("car NYPosition", "" + nCar[0].getNYPosition());
		 Log.v("car TimeDelay", "" + nCar[0].getTimeDelay());
		 Log.v("car ListName", "" + nCar[0].getCarListName());
		 Log.e("one car infor", "=====================================");
		 } else {
		 for (int i = 0; i < getNCarNumber(); i++) {
		 Log.e("multiple car infor",
		 "=====================================");
		 Log.v("car NAcceleration", ""
		 + nCar[i].getNAcceleration());
		 Log.v("car NAccidenceDirection", ""
		 + nCar[i].getNAccidenceDirection());
		 Log.v("car NAccidenceSpeed", ""
		 + nCar[i].getNAccidenceSpeed());
		 Log.v("car NCarID", "" + nCar[i].getNCarID());
		 Log.v("car NCarType", "" + nCar[i].getNCarType());
		 Log
		 .v("car NDirection", ""
		 + nCar[i].getNDirection());
		 Log.v("car NName", "" + nCar[i].getNName());
		 Log.v("car NSpeed", "" + nCar[i].getNSpeed());
		 Log.v("car NStatus", "" + nCar[i].getNStatus());
		 Log
		 .v("car NXPosition", ""
		 + nCar[i].getNXPosition());
		 Log
		 .v("car NYPosition", ""
		 + nCar[i].getNYPosition());
		 Log.v("car TimeDelay", "" + nCar[i].getTimeDelay());
		 Log.v("car ListName", "" + nCar[i].getCarListName());
		 Log.e("multiple car infor",
		 "=====================================");
		 }
		 }
	}


	public void setLoginedFlag()
	{
		if (Logined == false) {
			Logined = true;
		}
	}
}
