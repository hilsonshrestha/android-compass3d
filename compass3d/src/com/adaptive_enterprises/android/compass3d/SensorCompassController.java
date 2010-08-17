package com.adaptive_enterprises.android.compass3d;

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
    private SensorManager sensorManager;
    private Sensor magneticSensor;
    private SensorEventListener sensorEventListener;
    private boolean registered;
    private int sensorDelay = SensorManager.SENSOR_DELAY_UI;


    public void setModel(final CompassModel model) {
        boolean wasRegistered = registered;
        stop();
        sensorEventListener = model == null ? null : new SensorEventListener() {
            public void onSensorChanged(SensorEvent sensorevent) {
                synchronized (model) {
                    model.setOrientation(sensorevent.values);
                    model.setAccuracy(sensorevent.accuracy);
                    model.setTimestamp(sensorevent.timestamp);
                }
                model.notifyObservers();
            }
            public void onAccuracyChanged(Sensor sensor, int newAccuracy) {
                // ignore;
            }
        };
        if (wasRegistered)
            start();
    }

    public SensorCompassController(Context context) {
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticSensor != null) {
            Log.d("Experiment", "sensor.name: " + magneticSensor.getName());
            Log.d("Experiment", "sensor.vendor: " + magneticSensor.getVendor());
            Log.d("Experiment", "sensor.version: " + magneticSensor.getVersion());
            Log.d("Experiment", "sensor.power: " + magneticSensor.getPower());
            Log.d("Experiment", "sensor.maximumRange: " + magneticSensor.getMaximumRange());
            Log.d("Experiment", "sensor.resolution: " + magneticSensor.getResolution());
        }
    }
    
    public void start() {
        if (!registered && magneticSensor != null) {
            registered = sensorManager.registerListener(
                    sensorEventListener, magneticSensor,
                    sensorDelay);
            if (registered) {
                Log.d("Experiment", "compass controller started");
            } else {
                Log.w("Experiment", "could not register listener");
            }
        }
    }
    
    public void stop() {
        if (registered) {
            sensorManager.unregisterListener(sensorEventListener, magneticSensor);
            Log.d("Experiment", "compass controller stopped");
            registered = false;
        }
    }
}
