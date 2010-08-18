package com.adaptive_enterprises.android.compass3d.controller;

import com.adaptive_enterprises.android.compass3d.model.CompassModel;

import android.os.CountDownTimer;

/**
 * A fake compass controller that rotates the apparent magnetic field in a known
 * way. The field rotates about the three axes. Imagine the phone is lying flat
 * on a table. The top of the phone is away from you.
 * <p>
 * First the field starts to the right side of the phone (3 o'clock) and moves
 * around the plane of the display, to the top edge (12 o'clock), then the left
 * edge (9 o'clock), then the bottom edge (6 o'clock) and then to the right edge again.
 * <p>
 * Then the intensity peaks for a moment (causing the display to flash) for the
 * next part. The field starts at the top of the phone (12 o'clock), then rises up
 * to be directly above the display, then comes down to the bottom of the phone (6 o'clock)
 * then continues to go down and diretcly under the phone, then finally back to the
 * top (12 o'clock).
 * <p>
 * Finally (after another intensity flash), the field starts at 3 o'clock, rises
 * to be above the phone, then sinks to be at 9 o'clock, then sinks further to
 * end up under the phone, until finally it returns to the 3 o'clock position.
 * <p>
 * After this, the whole process repeats.
 * <p>
 * The process is restarted whenever the application loses focus.
 */
public class FakeCompassController implements CompassController {
    private CompassModel model;
    private long startTime;
    private CountDownTimer timer;
    
    public void setModel(CompassModel model) {
        this.model = model;
    }
    
    void step() {
        long time = System.currentTimeMillis() - startTime;
        double x, y, z;
        double r = 60; // reasonable strength

        if (time % 10000 > 9800 ||
            time % 10000 <  200)
            r = 120;
        
        double s = Math.sin(time * Math.PI * 2 / 10000);
        double c = Math.cos(time * Math.PI * 2 / 10000); 
        if (time % 30000 < 10000) {
            // circle, counterclockwise starting at 3 o'clock
            x = r * c;
            y = r * s;
            z = 0;
        } else if (time % 30000 < 20000) {
            // inclining, starting at 12 o'clock first inclining
            x = 0; 
            y = r * c;
            z = r * s;
        } else {
            // inclining, starting at 3 o'clock first inclining
            x = r * c; 
            y = 0;
            z = r * s;
        }
        
        model.setAccuracy(3);
        model.setTimestamp(time);
        model.setOrientation(new float[] {(float)x, (float)y, (float)z});
        model.notifyObservers();
    }
    
    public void start() {
        assert timer == null;
        timer = new CountDownTimer(1000 * 60 * 60 * 24, 1000 / 15) {
            public void onTick(long l) {
                step();
            }
            public void onFinish() {
            }
        };
        startTime = System.currentTimeMillis();
        timer.start();
    }
    
    public void stop() {
        timer.cancel();
        timer = null;
    }
}
