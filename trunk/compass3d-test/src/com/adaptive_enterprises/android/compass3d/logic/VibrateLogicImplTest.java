package com.adaptive_enterprises.android.compass3d.logic;

import org.junit.Test;

import com.adaptive_enterprises.android.compass3d.logic.VibrateLogicImpl;
import com.adaptive_enterprises.android.compass3d.model.CompassModel;
import com.adaptive_enterprises.android.compass3d.model.SettingsModel;

import static org.junit.Assert.*;

public class VibrateLogicImplTest {
    @Test
    public void testVibrateOnAlignment() throws Exception {
        SettingsModel settings = new SettingsModel();
        settings.setVibrateOnAlignment(true);
        
        CompassModel compass = new CompassModel();
        VibrateLogicImpl impl = new VibrateLogicImpl();
        impl.setSettings(settings);
        
        impl.reset();
        
        compassTo(compass, new float[] {1,1,1});
        assertFalse(impl.shouldVibrate(compass, 1000));
        
        compassTo(compass, new float[] {0,1,0});
        assertFalse(impl.shouldVibrate(compass, 1100));
        
        compassTo(compass, new float[] {0,1.001f,0});
        assertTrue(impl.shouldVibrate(compass, 1400));

        compassTo(compass, new float[] {0,1.002f,0});
        assertFalse(impl.shouldVibrate(compass, 1500));

        compassTo(compass, new float[] {0,1.003f,0});
        assertFalse(impl.shouldVibrate(compass, 1600));

        compassTo(compass, new float[] {0,1.004f,0});
        assertFalse(impl.shouldVibrate(compass, 1700));

        compassTo(compass, new float[] {0,0,1});
        assertFalse(impl.shouldVibrate(compass, 1900));

        compassTo(compass, new float[] {0,1,0});
        assertFalse(impl.shouldVibrate(compass, 2000));
        
        compassTo(compass, new float[] {0,1.001f,0});
        assertTrue(impl.shouldVibrate(compass, 2200));
    }
    
    private void compassTo(CompassModel compass, float[] v) {
        compass.setAccuracy(3);
        compass.setOrientation(v);
        compass.notifyObservers();
    }

}
