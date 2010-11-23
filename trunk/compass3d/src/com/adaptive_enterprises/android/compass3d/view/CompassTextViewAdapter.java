package com.adaptive_enterprises.android.compass3d.view;

import java.util.Observable;
import java.util.Observer;

import com.adaptive_enterprises.android.compass3d.R;
import com.adaptive_enterprises.android.compass3d.model.CompassModel;

import android.widget.TextView;

/**
 * An adaptor between a {@link CompassModel} and a {@link TextView}.
 * Waits for compass changes, then updates the text view.
 */
public class CompassTextViewAdapter implements Observer {
    private CompassModel mCompass;
    private TextView mTextView;
    
    public void setCompass(CompassModel compass) {
        if (mCompass != null) {
            mCompass.deleteObserver(this);
        }
        mCompass = compass;
        compass.addObserver(this);
    }
    
    public void setTextView(TextView textView) {
        mTextView = textView;
    }

    @Override
    public void update(Observable observable, Object obj) {
        if (mTextView != null) {
            float yaw, pitch;
            float strength;
            boolean available;
            synchronized (mCompass) {
                available = mCompass.getAccuracy() > 0;
                yaw = available ? (float)mCompass.getYaw() : 0;
                pitch = available ? (float)mCompass.getPitch() : 0;
                strength = available ? (float)mCompass.getStrength() : 0;
            }
            if (available) {
                CharSequence s;
                s = String.format(
                    "%4.0f\u00b0yaw %+4.0f\u00b0pitch %5.1f \u03bcT",
                    yaw, pitch, strength);
                mTextView.setText(s);
            } else {
                mTextView.setText(R.string.no_sensors);
            }
        }
    }
}
