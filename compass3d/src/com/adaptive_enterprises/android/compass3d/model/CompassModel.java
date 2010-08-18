package com.adaptive_enterprises.android.compass3d.model;

import java.util.Arrays;
import java.util.Observable;

/**
 * Observable model of a compass.
 */
public class CompassModel extends Observable {
    private float[] orientation = { 0, 0, 0 };
    private long timestamp;
    private int accuracy;
    
    /** 
     * Returns the current orientation of the magnetic field as a micro Tesla
     * vector with origin at the lower left of the device, with z being out
     * of the screen. 
     * @return a float with three components
     */
    public float[] getOrientation() {
        return orientation.clone();
    }
    
    /**
     * Returns the time when the sensor reading was taken
     * @return
     */
    public long getTimestamp() {
        return timestamp;
    }
    
    /**
     * Returns an accuracy from 0 (unavailable) to 3 (most accurate)
     */
    public int getAccuracy() {
        return accuracy;
    }
    
    public void setOrientation(float[] orientation) {
        assert orientation.length == 3;
        if (!Arrays.equals(this.orientation, orientation)) {
            System.arraycopy(orientation, 0, this.orientation, 0, 3);
            setChanged();
        }
    }
    
    /** Returns the yaw in degrees clockwise, in range (-180, +180] */
    public double getYaw() {
        return -Math.toDegrees(Math.atan2(-orientation[0], orientation[1]));
    }
    
    /** Returns the inclination in degrees, in range [-90,90] */
    public double getPitch() {
        return Math.toDegrees(Math.atan2(orientation[2], Math.hypot(orientation[0], orientation[1])));
    }

    public void setAccuracy(int accuracy) {
        if (this.accuracy != accuracy) {
            this.accuracy = accuracy;
            setChanged();
        }
    }

    public void setTimestamp(long timestamp) {
        if (this.timestamp != timestamp) {
            this.timestamp = timestamp;
            setChanged();
        }
    }

    public double getStrength() {
        double s = Math.sqrt(orientation[0] * orientation[0]
                           + orientation[1] * orientation[1]
                           + orientation[2] * orientation[2]);
        return s;
    }
}
