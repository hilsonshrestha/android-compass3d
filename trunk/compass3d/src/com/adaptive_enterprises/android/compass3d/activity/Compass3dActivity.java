package com.adaptive_enterprises.android.compass3d.activity;


import com.adaptive_enterprises.android.compass3d.controller.CompassController;
import com.adaptive_enterprises.android.compass3d.controller.SensorCompassController;
import com.adaptive_enterprises.android.compass3d.logic.VibrateLogic;
import com.adaptive_enterprises.android.compass3d.logic.VibrateLogicImpl;
import com.adaptive_enterprises.android.compass3d.model.CompassModel;
import com.adaptive_enterprises.android.compass3d.model.SettingsModel;
import com.adaptive_enterprises.android.compass3d.opengl.CompassRenderer;
import com.adaptive_enterprises.android.compass3d.view.CompassSurfaceView;
import com.adaptive_enterprises.android.compass3d.view.CompassTextViewAdapter;
import com.adaptive_enterprises.android.compass3d.view.VibrateView;
import com.adaptive_enterprises.android.compass3d.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Compass3dActivity extends Activity {
    private final CompassModel compass = new CompassModel();
    private final SettingsModel settings = new SettingsModel();
    private CompassController compassController;
    private CompassTextViewAdapter compassTextAdapter;
    private CompassSurfaceView surfaceView;
    private VibrateLogic vibrateLogic;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Experiment", "create");
        setContentView(R.layout.main);
        
        settings.setVibrateOnAlignment(true);
        
        compassTextAdapter = new CompassTextViewAdapter();
        compassTextAdapter.setTextView((TextView)findViewById(R.id.TextView01));
        compassTextAdapter.setCompass(compass);

        surfaceView = (CompassSurfaceView)findViewById(R.id.SurfaceView01);
        surfaceView.setRenderer(new CompassRenderer().setModel(compass));

        compassController = new SensorCompassController(getApplicationContext());
        //compassController = new FakeCompassController();
        compassController.setCompass(compass);
        
        vibrateLogic = new VibrateLogicImpl().setSettings(settings);
        
        VibrateView vibrateView = new VibrateView(getApplicationContext());
        vibrateView.setModel(compass);
        vibrateView.setVibrateLogic(vibrateLogic);
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.i("Experiment", "window focus = " + hasFocus);
        if (hasFocus)
            compassController.start();
        else
            compassController.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Experiment", "resumed");
        vibrateLogic.reset();
        surfaceView.onResume();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Experiment", "paused");
        surfaceView.onPause();
    }
    
}