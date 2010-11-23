package com.adaptive_enterprises.android.compass3d.controller;

import com.adaptive_enterprises.android.compass3d.model.CompassModel;

/**
 * Interface for controllers that asynchronously update a {@link CompassModel}.
 */
public interface CompassController {
    /** Set the compass model to update */
    void setCompass(CompassModel compass);

    /** Start asynchronously updating the compass model */
    void start();

    /** Stop asynchronously updating the compass model */
    void stop();
}
