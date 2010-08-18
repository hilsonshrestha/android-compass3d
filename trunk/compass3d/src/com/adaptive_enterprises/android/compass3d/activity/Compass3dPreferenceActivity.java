package com.adaptive_enterprises.android.compass3d.activity;

import com.adaptive_enterprises.android.compass3d.R;
import com.adaptive_enterprises.android.compass3d.model.SettingsModel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Compass3dPreferenceActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
    
    public static void loadSettings(Context context, SettingsModel settings) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        settings.setVibrateOnAlignment(prefs.getBoolean("vibrateOnAlignment", true));
    }
}
