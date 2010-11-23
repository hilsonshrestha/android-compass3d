package com.adaptive_enterprises.android.compass3d.logic;

import android.hardware.SensorManager;

import com.adaptive_enterprises.android.compass3d.model.CompassModel;
import com.adaptive_enterprises.android.compass3d.model.SettingsModel;

public class VibrateLogicImpl implements VibrateLogic {
    private double mPrecision = 2; // degrees of precision to be aligned
    private long mHoldTime = 200; // milliseconds to hold aligned
    private State mState;
    private long mAlignmentChronicTime;
    private SettingsModel mSettings;

    enum State {
        UNALIGNED,
        ALIGNED_ACUTE,
        ALIGNED_CHRONIC
    }

    public VibrateLogicImpl setSettings(SettingsModel settings) {
        mSettings = settings;
        return this;
    }
    
    @Override
    public void reset() {
        mState = State.UNALIGNED;
    }
    
    @Override
    public boolean shouldVibrate(CompassModel model, long time) {
        
        if (!mSettings.getVibrateOnAlignment())
            return false;

        double yaw, pitch;
        synchronized (model) {
            // ignore measurements that are not highly accurate
            if (model.getAccuracy() != SensorManager.SENSOR_STATUS_ACCURACY_HIGH)
                return false;
            yaw = model.getYaw();
            pitch = model.getPitch();
        }
        
        boolean nowAligned = (Math.hypot(yaw, pitch) < mPrecision);
        boolean vibrate = false;
        switch (mState) {
        case UNALIGNED:
            if (nowAligned) {
                mState = State.ALIGNED_ACUTE;
                mAlignmentChronicTime = time + mHoldTime;
            }
            break;
        case ALIGNED_ACUTE:
            if (nowAligned && time >= mAlignmentChronicTime) {
                mState = State.ALIGNED_CHRONIC;
                vibrate = true;
            } else if (!nowAligned) {
                mState = State.UNALIGNED;
            }
            break;
        case ALIGNED_CHRONIC:
            if (!nowAligned) {
                mState = State.UNALIGNED;
            }
            break;
        }
        return vibrate;
    }
}
