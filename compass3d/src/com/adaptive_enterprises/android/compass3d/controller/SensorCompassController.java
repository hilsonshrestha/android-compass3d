package com.adaptive_enterprises.android.compass3d.controller;

import com.adaptive_enterprises.android.compass3d.model.CompassModel;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Receives updates from a hardware compass and updates a CompassModel.
 */
public class SensorCompassController implements CompassController {
    private static final String TAG = "Compass3D";
    private SensorManager mSensorManager;
    private Sensor mMagneticSensor;
    private SensorEventListener mSensorEventListener;
    private boolean mRegistered;
    private int mSensorDelay = SensorManager.SENSOR_DELAY_UI;

    public void setCompass(final CompassModel compass) {
        boolean wasRegistered = mRegistered;
        stop();
        mSensorEventListener = compass == null ? null : new SensorEventListener() {
            public void onSensorChanged(SensorEvent sensorevent) {
                synchronized (compass) {
                    compass.setOrientation(sensorevent.values);
                    compass.setAccuracy(sensorevent.accuracy);
                    compass.setTimestamp(sensorevent.timestamp);
                }
                compass.notifyObservers();
            }
            public void onAccuracyChanged(Sensor sensor, int newAccuracy) {
                // ignore;
            }
        };
        if (wasRegistered)
            start();
    }

    public SensorCompassController(Context context) {
        mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        mMagneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (mMagneticSensor != null) {
            Log.d(TAG, "sensor.name: " + mMagneticSensor.getName());
            Log.d(TAG, "sensor.vendor: " + mMagneticSensor.getVendor());
            Log.d(TAG, "sensor.version: " + mMagneticSensor.getVersion());
            Log.d(TAG, "sensor.power: " + mMagneticSensor.getPower());
            Log.d(TAG, "sensor.maximumRange: " + mMagneticSensor.getMaximumRange());
            Log.d(TAG, "sensor.resolution: " + mMagneticSensor.getResolution());
        }
    }
    
    public void start() {
        if (!mRegistered && mMagneticSensor != null) {
            mRegistered = mSensorManager.registerListener(
                    mSensorEventListener, mMagneticSensor,
                    mSensorDelay);
            if (mRegistered) {
                Log.d(TAG, "compass controller started");
            } else {
                Log.w(TAG, "could not register listener");
            }
        }
    }
    
    public void stop() {
        if (mRegistered) {
            mSensorManager.unregisterListener(mSensorEventListener, mMagneticSensor);
            Log.d(TAG, "compass controller stopped");
            mRegistered = false;
        }
    }
}
