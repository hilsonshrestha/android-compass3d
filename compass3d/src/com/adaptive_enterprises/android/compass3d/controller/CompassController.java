package com.adaptive_enterprises.android.compass3d.controller;

import com.adaptive_enterprises.android.compass3d.model.CompassModel;

public interface CompassController {
    void setModel(CompassModel model);
    void start();
    void stop();
}
