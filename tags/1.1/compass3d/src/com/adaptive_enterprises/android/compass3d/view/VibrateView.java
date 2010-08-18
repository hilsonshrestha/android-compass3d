package com.adaptive_enterprises.android.compass3d.view;

import java.util.Observable;
import java.util.Observer;

import com.adaptive_enterprises.android.compass3d.model.CompassModel;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Vibrator;

/** 
 * Vibrates the phone when it is exactly oriented in the
 * direction of the magnetic field.
 */
public class VibrateView implements Observer {
    private CompassModel model;
    private double precision = 2; // degrees of precision to be aligned
    private long holdTime = 200; // milliseconds to hold aligned
    
    enum State {
        UNALIGNED,
        ALIGNED_ACUTE,
        ALIGNED_CHRONIC
    }
    private State state = State.UNALIGNED;
    private long alignmentChronicTime;
    private Vibrator vibrator;

    public VibrateView(Context context) {
        vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
    }
    
    public void setModel(CompassModel model) {
        if (this.model != null)
            this.model.deleteObserver(this);
        this.model = model;
        if (model != null)
            model.addObserver(this);
    }

    @Override
    public void update(Observable arg0, Object arg1) {
        double yaw, pitch;
        synchronized (model) {
            // ignore measurements that are not highly accurate
            if (model.getAccuracy() != SensorManager.SENSOR_STATUS_ACCURACY_HIGH)
                return;
            yaw = model.getYaw();
            pitch = model.getPitch();
        }
        boolean nowAligned = (Math.hypot(yaw, pitch) < precision);
        
        switch (state) {
        case UNALIGNED:
            if (nowAligned) {
                state = State.ALIGNED_ACUTE;
                alignmentChronicTime = System.currentTimeMillis() + holdTime;
            }
            break;
        case ALIGNED_ACUTE:
            if (nowAligned) {
                if (System.currentTimeMillis() >= alignmentChronicTime) {
                    state = State.ALIGNED_CHRONIC;
                    vibrate();
                } else
                    state = State.UNALIGNED;
            }
            break;
        case ALIGNED_CHRONIC:
            if (!nowAligned) {
                state = State.UNALIGNED;
                // vibrate();
            }
            break;
        }
    }

    private void vibrate() {
        vibrator.vibrate(100);
    }
}
