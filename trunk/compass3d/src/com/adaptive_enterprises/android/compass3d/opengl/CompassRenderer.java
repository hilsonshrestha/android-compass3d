package com.adaptive_enterprises.android.compass3d.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.adaptive_enterprises.android.compass3d.model.CompassModel;

import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.util.Log;

/**
 * OpenGL renderer that draws a cone with orientation based on a {@link CompassModel}.
 */
public class CompassRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "Compass3D";
    private CompassModel mModel;
    private Cone mCone;

    public CompassRenderer() {
        mCone = new Cone();
        mCone.setSegments(32);
        mCone.setHeight(2);
        mCone.setRadius(0.5);
    }
    
    public CompassRenderer setModel(CompassModel model) {
        mModel = model;
        // Don't add a listener because we use continuous render mode
        return this;
    }
    
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.d(TAG, "surface create");
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        gl.glClearColor(0.2f, 0.2f, 0.2f, 1f);
        gl.glEnable(GL10.GL_CULL_FACE);

        // Ambient lighting with a key light from the top
        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, Cone.asBuffer(new float[] {
                0, 30, 10, 0/*unidirectional*/}));
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, Cone.asBuffer(new float[] {
                0.5f, 0.5f, 0.5f, 1f}));

        // Add shininess
        gl.glEnable(GL10.GL_COLOR_MATERIAL);
        gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 64f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        Log.d(TAG, "surface change");
        gl.glViewport(0, 0, w, h);
        float aspect = (float)w / h;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        float scale = 2f;
        gl.glOrthof(-scale, scale, -scale/aspect, scale/aspect, 1, 10);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (mModel == null) {
            return;
        }

        // copy out the current orientation; avoid locking it for too long
        float strength;
        double yaw, pitch;
        int accuracy;
        synchronized (mModel) {
            accuracy = mModel.getAccuracy();
            strength = (float)mModel.getStrength();
            yaw = mModel.getYaw();
            pitch = mModel.getPitch();
        }

        // Don't draw the needle if the value is unreliable
        //if (accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
        //    return;
        //}
        
        // If the sensor is inaccurate, then add a red colour to the bg
        // TODO make the needle translucent on low accuracy?
        boolean inaccurate = accuracy < SensorManager.SENSOR_STATUS_ACCURACY_HIGH;
        // Lighten the background if the field strength is strong.
        float bg = clip(strength / 250f, 0, 1f);
        gl.glClearColor(inaccurate ? 1f : bg, bg, bg, 1f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // If the field has no strength, don't draw the needle
        if (strength == 0) {
            return;
        }
        
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -3f);

        // Next rotate about the z axis to get the direction.
        gl.glRotatef(-(float)yaw, 0, 0, 1f);
        // Rotate about the x axis to get the pitch
        gl.glRotatef(-(float)pitch, 1f, 0, 0);

        // Draw the red half then the white half of the cone (rotated 180 deg)
        red(gl);
        mCone.draw(gl);
        gl.glRotatef(180f, 0, 0, 1f);
        white(gl);
        mCone.draw(gl);
    }
    
    /** Returns the value v clipped to lie within [min,max] */ 
    static float clip(float v, float min, float max) {
        if (v < min) return min;
        if (v > max) return max;
        return v;
    }
    
    /** Sets the draw colour to red */
    static void red(GL10 gl) {
        gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
    }
    
    /** Sets the draw colour to white */
    static void white(GL10 gl) {
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
}       