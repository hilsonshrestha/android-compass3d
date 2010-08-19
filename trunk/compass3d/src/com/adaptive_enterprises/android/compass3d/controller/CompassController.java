package com.adaptive_enterprises.android.compass3d.controller;

import com.adaptive_enterprises.android.compass3d.model.CompassModel;

/**
 * Interface for controllers that asynchronously updates a compass
 */
public interface CompassController {
    void setCompass(CompassModel compass);
    void start();
    void stop();
}
