package com.adaptive_enterprises.android.compass3d.logic;

import android.hardware.SensorManager;

import com.adaptive_enterprises.android.compass3d.model.CompassModel;
import com.adaptive_enterprises.android.compass3d.model.SettingsModel;

public class VibrateLogicImpl implements VibrateLogic {
    private double precision = 2; // degrees of precision to be aligned
    private long holdTime = 200; // milliseconds to hold aligned
    
    enum State {
        UNALIGNED,
        ALIGNED_ACUTE,
        ALIGNED_CHRONIC
    }
    private State state;
    private long alignmentChronicTime;
    private SettingsModel settings;

    public VibrateLogicImpl setSettings(SettingsModel settings) {
        this.settings = settings;
        return this;
    }
    
    @Override
    public void reset() {
        state = State.UNALIGNED;
    }
    
    @Override
    public boolean shouldVibrate(CompassModel model, long time) {
        
        if (!settings.getVibrateOnAlignment())
            return false;

        double yaw, pitch;
        synchronized (model) {
            // ignore measurements that are not highly accurate
            if (model.getAccuracy() != SensorManager.SENSOR_STATUS_ACCURACY_HIGH)
                return false;
            yaw = model.getYaw();
            pitch = model.getPitch();
        }
        boolean nowAligned = (Math.hypot(yaw, pitch) < precision);
        
        boolean vibrate = false;
        switch (state) {
        case UNALIGNED:
            if (nowAligned) {
                state = State.ALIGNED_ACUTE;
                alignmentChronicTime = time + holdTime;
            }
            break;
        case ALIGNED_ACUTE:
            if (nowAligned) {
                if (time >= alignmentChronicTime) {
                    state = State.ALIGNED_CHRONIC;
                    vibrate = true;
                } else
                    state = State.UNALIGNED;
            }
            break;
        case ALIGNED_CHRONIC:
            if (!nowAligned) {
                state = State.UNALIGNED;
            }
            break;
        }
        return vibrate;
    }
}
