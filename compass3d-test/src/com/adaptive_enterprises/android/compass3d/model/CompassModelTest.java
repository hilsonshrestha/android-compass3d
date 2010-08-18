package com.adaptive_enterprises.android.compass3d.model;

import org.junit.Test;

import com.adaptive_enterprises.android.compass3d.model.CompassModel;

import static org.junit.Assert.*;

public class CompassModelTest {

    private static void assertYawPitch(float x, float y, float z, double yaw, double pitch) {
        CompassModel model = new CompassModel();
        model.setOrientation(new float[] { x, y, z} );
        assertEquals(yaw, model.getYaw(), 0.01);
        assertEquals(pitch, model.getPitch(), 0.01);
    }

    @Test
    public void testYawAndPitch() throws Exception {
        /*
         *     z
         *     ^  y
         *     | / 
         *     |/
         *   --O-----> x
         *    /|
         *
         *
         */
        assertYawPitch( 0, +1,  0,    0,   0);
        assertYawPitch(+1, +1,  0,  +45,   0);
        assertYawPitch(+1,  0,  0,  +90,   0);
        assertYawPitch(+1, -1,  0, +135,   0);
        assertYawPitch( 0, -1,  0, +180,   0);
        assertYawPitch(-1, -1,  0, -135,   0);
        assertYawPitch(-1,  0,  0,  -90,   0);
        assertYawPitch(-1, +1,  0,  -45,   0);

        // The angle the diagonal of a cube makes with a face
        double CA = Math.toDegrees(Math.atan2(1, Math.sqrt(2)));
        
        assertYawPitch( 0, +1, +1,    0,   +45);
        assertYawPitch(+1, +1, +1,  +45,   +CA);
        assertYawPitch(+1,  0, +1,  +90,   +45);
        assertYawPitch(+1, -1, +1, +135,   +CA);
        assertYawPitch( 0, -1, +1, +180,   +45);
        assertYawPitch(-1, -1, +1, -135,   +CA);
        assertYawPitch(-1,  0, +1,  -90,   +45);
        assertYawPitch(-1, +1, +1,  -45,   +CA);

        assertYawPitch( 0, +1, -1,    0,   -45);
        assertYawPitch(+1, +1, -1,  +45,   -CA);
        assertYawPitch(+1,  0, -1,  +90,   -45);
        assertYawPitch(+1, -1, -1, +135,   -CA);
        assertYawPitch( 0, -1, -1, +180,   -45);
        assertYawPitch(-1, -1, -1, -135,   -CA);
        assertYawPitch(-1,  0, -1,  -90,   -45);
        assertYawPitch(-1, +1, -1,  -45,   -CA);

        assertYawPitch( 0,  0, +1,    0,   +90);
        assertYawPitch( 0,  0, -1,    0,   -90);
    }
}
