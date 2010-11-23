package com.adaptive_enterprises.android.compass3d.view;

import java.util.Observable;
import java.util.Observer;

import com.adaptive_enterprises.android.compass3d.logic.VibrateLogic;
import com.adaptive_enterprises.android.compass3d.model.CompassModel;

import android.content.Context;
import android.os.Vibrator;

/** 
 * A view that activates the phone's hardware vibrator according to
 * the {@link VibrateLogic}.
 */
public class VibrateView implements Observer {
    private static final int SHORT_INTERVAL = 100; // msec
    private CompassModel mCompass;
    private Vibrator mVibrator;
    private VibrateLogic mVibrateLogic;

    public VibrateView(Context context) {
        mVibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
    }
    
    public void setModel(CompassModel compass) {
        if (mCompass != null)
            mCompass.deleteObserver(this);
        mCompass = compass;
        if (compass != null)
            compass.addObserver(this);
    }
    
    public void setVibrateLogic(VibrateLogic vibrateLogic) {
        mVibrateLogic = vibrateLogic;
    }
    
    @Override
    public void update(Observable observable, Object data) {
        if (mVibrator != null
                && mVibrateLogic.shouldVibrate(mCompass,
                        System.currentTimeMillis()))
            mVibrator.vibrate(SHORT_INTERVAL);
    }

}
