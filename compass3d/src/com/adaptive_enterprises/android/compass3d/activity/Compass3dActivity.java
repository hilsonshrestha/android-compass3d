package com.adaptive_enterprises.android.compass3d.activity;


import com.adaptive_enterprises.android.compass3d.controller.CompassController;
import com.adaptive_enterprises.android.compass3d.controller.FakeCompassController;
import com.adaptive_enterprises.android.compass3d.controller.SensorCompassController;
import com.adaptive_enterprises.android.compass3d.logic.VibrateLogic;
import com.adaptive_enterprises.android.compass3d.logic.VibrateLogicImpl;
import com.adaptive_enterprises.android.compass3d.model.CompassModel;
import com.adaptive_enterprises.android.compass3d.model.SettingsModel;
import com.adaptive_enterprises.android.compass3d.opengl.CompassRenderer;
import com.adaptive_enterprises.android.compass3d.view.CompassTextViewAdapter;
import com.adaptive_enterprises.android.compass3d.view.VibrateView;
import com.adaptive_enterprises.android.compass3d.R;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class Compass3dActivity extends Activity {
    private static final String TAG = "Compass3D";
    private final CompassModel mCompass = new CompassModel();
    private final SettingsModel mSettings = new SettingsModel();
    private CompassController mCompassController;
    private CompassTextViewAdapter mCompassTextAdapter;
    private GLSurfaceView mSurfaceView;
    private VibrateLogic mVibrateLogic;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "create");
        setContentView(R.layout.main);
        
        mCompassTextAdapter = new CompassTextViewAdapter();
        mCompassTextAdapter.setTextView((TextView)findViewById(R.id.TextView01));
        mCompassTextAdapter.setCompass(mCompass);

        mSurfaceView = (GLSurfaceView)findViewById(R.id.SurfaceView01);
        mSurfaceView.setRenderer(new CompassRenderer().setModel(mCompass));

        mCompassController = new SensorCompassController(getApplicationContext());
        //mCompassController = new FakeCompassController();
        mCompassController.setCompass(mCompass);
        
        mVibrateLogic = new VibrateLogicImpl().setSettings(mSettings);
        
        VibrateView vibrateView = new VibrateView(getApplicationContext());
        vibrateView.setModel(mCompass);
        vibrateView.setVibrateLogic(mVibrateLogic);
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.i(TAG, "window focus = " + hasFocus);
        if (hasFocus) {
            mCompassController.start();
        } else {
            mCompassController.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "resumed");
        Compass3dPreferenceActivity.loadSettings(getApplicationContext(), mSettings);
        Log.d(TAG, "vibrateOnAlignment = " + mSettings.getVibrateOnAlignment());
        mVibrateLogic.reset();
        mSurfaceView.onResume();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "paused");
        mSurfaceView.onPause();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.compass_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.optionsPreferencesItem:
            Log.i(TAG, "preferences");
            startActivity(new Intent(this, Compass3dPreferenceActivity.class));
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
}