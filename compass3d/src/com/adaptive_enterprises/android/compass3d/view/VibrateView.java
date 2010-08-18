package com.adaptive_enterprises.android.compass3d.view;

import java.util.Observable;
import java.util.Observer;

import com.adaptive_enterprises.android.compass3d.logic.VibrateLogic;
import com.adaptive_enterprises.android.compass3d.model.CompassModel;

import android.content.Context;
import android.os.Vibrator;

/** 
 * Vibrates the phone when it is exactly oriented in the
 * direction of the magnetic field.
 */
public class VibrateView implements Observer {
    private CompassModel model;
    private Vibrator vibrator;
    private VibrateLogic vibrateLogic;

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
    
    public void setVibrateLogic(VibrateLogic vibrateLogic) {
        this.vibrateLogic = vibrateLogic;
    }
    
    @Override
    public void update(Observable observable, Object data) {
        if (vibrator != null
                && vibrateLogic.shouldVibrate((CompassModel) observable,
                        System.currentTimeMillis()))
            vibrator.vibrate(100);
    }

}
