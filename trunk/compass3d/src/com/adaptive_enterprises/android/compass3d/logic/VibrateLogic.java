package com.adaptive_enterprises.android.compass3d.logic;

import com.adaptive_enterprises.android.compass3d.model.CompassModel;

public interface VibrateLogic {
    void reset();
    
    /** Returns true if the vibrator should be activated */
    boolean shouldVibrate(CompassModel compass, long time);
}
