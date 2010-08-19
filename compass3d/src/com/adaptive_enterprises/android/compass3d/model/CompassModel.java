package com.adaptive_enterprises.android.compass3d.model;

import java.util.Arrays;
import java.util.Observable;

/**
 * Observable model of a compass.
 */
public class CompassModel extends Observable {
    private float[] mOrientation = { 0, 0, 0 };
    private long mTimestamp;
    private int mAccuracy;
    
    /** 
     * Returns the current orientation of the magnetic field as a micro Tesla
     * vector with origin at the lower left of the device, with z being out
     * of the screen. 
     * @return a float with three components
     */
    public float[] getOrientation() {
        return mOrientation.clone();
    }
    
    /**
     * Returns the time when the sensor reading was taken
     * @return
     */
    public long getTimestamp() {
        return mTimestamp;
    }
    
    /**
     * Returns an accuracy from 0 (unavailable) to 3 (most accurate)
     */
    public int getAccuracy() {
        return mAccuracy;
    }
    
    public void setOrientation(float[] orientation) {
        assert orientation.length == 3;
        if (!Arrays.equals(this.mOrientation, orientation)) {
            System.arraycopy(orientation, 0, this.mOrientation, 0, 3);
            setChanged();
        }
    }
    
    /** Returns the yaw in degrees clockwise, in range (-180, +180] */
    public double getYaw() {
        return -Math.toDegrees(Math.atan2(-mOrientation[0], mOrientation[1]));
    }
    
    /** Returns the inclination in degrees, in range [-90,90] */
    public double getPitch() {
        return Math.toDegrees(Math.atan2(mOrientation[2], Math.hypot(mOrientation[0], mOrientation[1])));
    }

    public void setAccuracy(int accuracy) {
        if (mAccuracy != accuracy) {
            mAccuracy = accuracy;
            setChanged();
        }
    }

    public void setTimestamp(long timestamp) {
        if (mTimestamp != timestamp) {
            mTimestamp = timestamp;
            setChanged();
        }
    }

    public double getStrength() {
        double s = Math.sqrt(mOrientation[0] * mOrientation[0]
                           + mOrientation[1] * mOrientation[1]
                           + mOrientation[2] * mOrientation[2]);
        return s;
    }
}
