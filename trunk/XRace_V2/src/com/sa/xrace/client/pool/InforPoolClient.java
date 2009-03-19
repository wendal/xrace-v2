///:~
/**
 * XRace V2
 * <p/>Use for ... ...
 * <p/>ID: $Id$
 * Last Commit:  $Author$
 * @version $Revision: $
 * 
 */
package com.sa.xrace.client.pool;

import java.util.ArrayList;

import android.util.Log;

import com.sa.xrace.client.toolkit.DataToolKit;
import com.sa.xrace.client.toolkit.ObjectPool;

/**
 * @author Changpeng Pan
 * @version $Id$
 */
public class InforPoolClient {
    private static final String TAG = "-- InforPoolClient --";

    public static boolean Logined = false;

    private static int nCarNumber;
    private static int myCarIndex;
    private static CarInforClient[] nCar;
    private static ArrayList<Integer> delayHandleIn;
    private static ArrayList<Integer> delayHandleOut;

    private static float nowSpeed = 0.0f;
    private static float distanceMade = 0.0f;

    private static float speedCha = 0.0f;
    private static float directCha = 0.0f;

    public static float[] mSensor = { 0.0f, 0.0f };

    private static int mStatus = 0;

    private static float time_ajusted = 0.0f;

    // If there is accident set it true;
    private static boolean isAccident = false;

    /**
     * 只发现创建了一次该类的对象
     */
    public InforPoolClient() {
        nCarNumber = 1;
        myCarIndex = 0;
        nCar = new CarInforClient[6];
        for (int i = 0; i < 6; i++) {
            nCar[i] = new CarInforClient();
        }
        delayHandleIn = new ArrayList<Integer>();
        delayHandleOut = new ArrayList<Integer>();
    }

    /**
     * Sets the Index of "my" car
     */
    public void setMyCarIndex(int input) {
        myCarIndex = input;
        ObjectPool.myCar = nCar[myCarIndex];
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
            getOneCarInformation(i).setNStatus(DataToolKit.NORMAL);

            nCar[i].setNYPosition(-11500);
            nCar[i].setNXPosition(400 - i * 400);
        }
    }

    /**
     * Updates the information which describes each car in car type situation
     */
    public void updateCarType(byte id, byte modelID) {
        int index = (int) id;
        nCar[index].setModel(ObjectPool.mModelInforPool.getModel(modelID));
        nCar[index].getMOriginalBox().getAABBbox(nCar[index].getModel());
    }

    /**
     * Updates the information which describes each car in login situation
     */
    public void updateCarInformationsLogin(int carN, byte carID, String names) {
        setNCarNumber(carN);
        if (Logined == false) {
            setMyCarIndex(carID);
        }
        for (int i = 0; i < carN; i++) {
            nCar[i].setNName(names.substring(i * 10, ((i + 1) * 10) - 1)
                    .replace("-", ""));
            nCar[i].setNStatus(DataToolKit.IDLE);
            nCar[i].setNCarID((byte) i);
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
        if (getMyCarIndex() > (int) id) {
            setMyCarIndex(getMyCarIndex() - 1);
        }
        moveTheRestCarForward((int) id);
    }

    /**
     * Updates the information when Myself is drop out...
     */
    public void cleanOtherCarInfor() {
        int nowpost = 0;
        nCarNumber = 1;
        if (getMyCarIndex() != 0) {
            try {
                nCar[0].setNCarID((byte) nowpost);
                nCar[0].setNAcceleration(nCar[getMyCarIndex()]
                        .getNAcceleration());
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
    public void updateCarInformationsNormal(byte id, float speed,
            float direction, float acceleration, float dirChange,
            float xposition, float yposition, short timedelay) {
        int index = (int) id;
        if (index != getMyCarIndex()) {
            nCar[index].setNSpeed(speed);
            nCar[index].setNDirection(direction);
            nCar[index].setNAcceleration(acceleration);
            nCar[index].setNChangeDirection(dirChange);
            nCar[index].setNXPosition(xposition);
            nCar[index].setNYPosition(yposition);
            nCar[index].setTimeDelay(timedelay);

        } else {
            // calculate out the timeDelay of my client
            InforPoolClient.delayHandleInADD(System.currentTimeMillis());
            InforPoolClient.calDelayTime();
            // Log.i("getTimeDelay()",""+getOneCarInformation(getMyCarIndex()).getTimeDelay());
        }
    }

    /**
     * Updates the information which describes each car in accident situation
     */
    public void updateCarInformationsAccident(byte id, float aSpeed,
            float aDirection, float aAcceleration, float aDirChange,
            float xposition, float yposition, short timedelay) {
        int index = (int) id;
        if (index != getMyCarIndex()) {
            nCar[index].setNAccidenceSpeed(aSpeed);
            nCar[index].setNAccidenceDirection(aDirection);
            nCar[index].setNAccidenceAcceleration(aAcceleration);
            nCar[index].setNAccidenceChangeDirection(aDirChange);
            nCar[index].setNXPosition(xposition);
            nCar[index].setNYPosition(yposition);
            nCar[index].setTimeDelay(timedelay);
        } else {
            InforPoolClient.delayHandleInADD(System.currentTimeMillis());
            InforPoolClient.calDelayTime();
        }
    }

    /**
     * Updates the information which describes each car in Idle situation
     * primarily update the TimeDelay
     */
    public void updateCarInformationsIdle(byte id, short timeDelay) {
        if (id != getMyCarIndex()) {
            int index = (int) id;
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
            nCar[nowPosition - 1].setNCarID((byte) (nCar[nowPosition]
                    .getNCarID() - 1));
            nCar[nowPosition - 1].setNAcceleration(nCar[nowPosition]
                    .getNAcceleration());
            nCar[nowPosition - 1].setNCarType(nCar[nowPosition].getNCarType());
            nCar[nowPosition - 1].setNDirection(nCar[nowPosition]
                    .getNDirection());
            nCar[nowPosition - 1].setNName(nCar[nowPosition].getNName());
            nCar[nowPosition - 1].setNSpeed(nCar[nowPosition].getNSpeed());
            nCar[nowPosition - 1].setNXPosition(nCar[nowPosition]
                    .getNXPosition());
            nCar[nowPosition - 1]
                    .setTimeDelay(nCar[nowPosition].getTimeDelay());
            nCar[nowPosition - 1].setNYPosition(nCar[nowPosition]
                    .getNYPosition());
            nCar[nowPosition - 1].setNChangeDirection(nCar[nowPosition]
                    .getNChangeDirection());
            nCar[nowPosition - 1].setCarListName(nCar[nowPosition]
                    .getCarListName());
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
        if (delayHandleIn.size() > 0 && delayHandleOut.size() > 0) {

            int temp = delayHandleIn.get(0) - delayHandleOut.get(0);
            // if(temp <0) temp =0;

            ObjectPool.myCar.setTimeDelay(temp);
            // Log.e("delayafter",""+nCar[myCarIndex].getTimeDelay());
            delayHandleIn.remove(0);
            delayHandleOut.remove(0);
        }
    }

    /**
     * Add time point into delayhandleIn
     */
    public static void delayHandleInADD(Long input) {
        delayHandleIn.add(timeFixing(input));
        // Log.e("delayHandleIn","in");
    }

    /**
     * Add time point into delayhandleOut
     */
    public static void delayHandleOutADD(Long input) {
        delayHandleOut.add(timeFixing(input));
        // Log.e("delayHandleIn","out");
    }

    /**
     * Updates all car speed and direction out, including my car; other car
     * related
     */
    public void prepareAllCarInformation(int timeElapsed, float direct_change,
            float speed_change) {
        if (timeElapsed == 0) {
            timeElapsed = 1;
        }

        // we do not store nowDirection , Use the direction calculated out
        nowSpeed = 0.0f;
//        nowDirection = 0.0f;
        distanceMade = 0.0f;
        time_ajusted = timeElapsed;
        speedCha = speed_change;
        directCha = (float) Math.toRadians(direct_change);
        // store the acceleration and ChangeDirection for further calculation

        for (int i = 0; i < getNCarNumber(); i++) {
            mStatus = getOneCarInformation(i).getNStatus();
            if (i == getMyCarIndex())
            // if (i == getOneCarInformation(i).getNCarID())
            {
                if (mStatus == DataToolKit.NORMAL) {

                    nCar[i].updateSpeedBySensor(speedCha, time_ajusted); // change
                                                                         // the
                                                                         // speed
                    nCar[i].updateDirectionBySensor(directCha, time_ajusted); // change
                                                                              // the
                                                                              // direction

                    // s = (v0+v1)T/2
                    distanceMade = (nCar[i].getNSpeed() + nowSpeed)
                            * time_ajusted / 2;

                    // put distance into coordinates X,Y
                    // nCar[i].setNXPosition(nCar[i].getNXPosition() +
                    // distanceMade * (float)
                    // Math.sin(-nCar[i].getNDirection()));
                    // nCar[i].setNYPosition(nCar[i].getNYPosition() +
                    // distanceMade * (float)
                    // Math.cos(-nCar[i].getNDirection()));
                    // the 3d date of the x and y
                    nCar[i].setNXPosition(nCar[i].getNXPosition()
                            + distanceMade
                            * (float) Math.sin(nCar[i].getNDirection()));
                    nCar[i].setNYPosition(nCar[i].getNYPosition()
                            + distanceMade
                            * (float) Math.cos(nCar[i].getNDirection()));
                } else if (mStatus == DataToolKit.ACCIDENT) {

                }

                // Log.i("setNXPosition",""+nCar[i].getNXPosition());
                // Log.i("setNYPosition",""+nCar[i].getNYPosition());
            } else {
                // // Speed
                // nowSpeed = nCar[i].getNSpeed() + nCar[i].getNAcceleration() *
                // (time_ajusted + nCar[i].getTimeDelay()/20 +
                // nCar[myCarIndex].getTimeDelay()/10 );
                //
                // // Direction
                // if (nowSpeed >= -CarInforClient.TOP_ANTI_SPEED && nowSpeed <=
                // CarInforClient.TOP_SPEED) {
                // // do nothing
                //					
                // }else if(nowSpeed < -CarInforClient.TOP_ANTI_SPEED){
                // nowSpeed = -CarInforClient.TOP_ANTI_SPEED;
                // }
                // else if(nowSpeed > CarInforClient.TOP_SPEED){
                // nowSpeed = CarInforClient.TOP_SPEED;
                // }
                //				
                // if(nowSpeed!=0){
                // nowDirection = nCar[i].getNDirection() +
                // nCar[i].getNChangeDirection() * (time_ajusted +
                // nCar[i].getTimeDelay()/20 +
                // nCar[myCarIndex].getTimeDelay()/20 );
                // if(nowDirection < 0 ){
                // nowDirection = (float) (nowDirection + 2*Math.PI);
                // }else if(nowDirection > 2*Math.PI ){
                // nowDirection = (float) (nowDirection - 2*Math.PI);
                // }
                // nCar[i].setNDirection(nowDirection);
                // }
                //				
                // // s = (v0+v1)T/2
                // distanceMade = (nCar[i].getNSpeed() + nowSpeed) *
                // time_ajusted / 2;
                // nCar[i].setNSpeed(nowSpeed);
                // nCar[i].setNXPosition(nCar[i].getNXPosition() + distanceMade
                // * (float) Math.sin(nCar[i].getNDirection()));
                // nCar[i].setNYPosition(nCar[i].getNYPosition() + distanceMade
                // * (float) Math.cos(nCar[i].getNDirection()));
                //				
                //				
                // System.out.println("distanceMade"+distanceMade);
                // System.out.println("nowSpeed"+nowSpeed);
                // System.out.println("time_ajusted"+time_ajusted);
                // System.out.println("distanceMade"+distanceMade);
            }
        }
    }

    /**
     * Fix Long time to int
     */
    public static int timeFixing(Long input) {
        return (int) (input % 1000000000 % 10000);
    }

}
