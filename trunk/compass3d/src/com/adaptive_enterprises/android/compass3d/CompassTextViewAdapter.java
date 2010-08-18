package com.adaptive_enterprises.android.compass3d;

import java.util.Observable;
import java.util.Observer;

import com.adaptive_enterprises.android.compass3d.R;

import android.widget.TextView;

/**
 * Observes a compass model and updates a text view
 */
public class CompassTextViewAdapter implements Observer {
    private CompassModel model;
    private TextView textView;
    private boolean diagnostic;
    
    public void setModel(CompassModel model) {
        if (this.model != null)
            this.model.deleteObserver(this);
        this.model = model;
        model.addObserver(this);
    }
    
    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    @Override
    public void update(Observable observable, Object obj) {
        if (textView != null) {
            float[] orientation;
            float yaw, pitch;
            float strength;
            boolean available;
            synchronized (model) {
                available = model.getAccuracy() > 0;
                orientation = available ? model.getOrientation() : null;
                yaw = available ? (float)model.getYaw() : 0;
                pitch = available ? (float)model.getPitch() : 0;
                strength = available ? (float)model.getStrength() : 0;
            }
            if (available) {
                CharSequence s;
                if (diagnostic) {
                    s = String.format(
                        "%3.0f|%+4.0fp%+4.0fy[%+5.1fx%+5.1fy%+5.1fz]",
                        strength, pitch, yaw,
                        orientation[0], orientation[1],  orientation[2]);
                } else {
                    s = String.format(
                        "%4.0f\u00b0yaw %+4.0f\u00b0pitch %5.1f \u03bcT",
                        yaw, pitch, strength);
                }
                textView.setText(s);
            } else {
                textView.setText(R.string.no_sensors);
            }
        }
    }
}
