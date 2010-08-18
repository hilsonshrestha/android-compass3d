package com.adaptive_enterprises.android.compass3d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Cone {
    double height = 1;
    double radius = 1;
    int segments = 4;
    FloatBuffer vertexBuffer;
    ByteBuffer coneIndexBuffer;
    FloatBuffer normalBuffer;
    ByteBuffer baseIndexBuffer;
    private boolean drawBase;
    
    public Cone() {
        calculateGeometry();
    }

    public void setHeight(double height) {
        this.height = height;
        calculateGeometry();
    }
    
    public void setRadius(double radius) {
        this.radius = radius;
        calculateGeometry();
    }
    
    public void setSegments(int segments) {
        this.segments = segments;
        calculateGeometry();
    }
    
    public void drawBase(boolean enable) {
        drawBase = enable;
    }
    
    void calculateGeometry() {
        assert segments < 255;

        float[] verticies = new float[3 * (segments + 1)];
        byte[] coneIndicies = new byte[segments + 2];
        byte[] baseIndicies = new byte[segments];

        // Nose of the cone
        verticies[0] =  0; 
        verticies[1] = (float)height; 
        verticies[2] =  0;
        
        coneIndicies[0] = 0;
        double perAngle = 2 * Math.PI / segments;
        for (int i = 0; i < segments; i++) {
            double angle = i * perAngle;
            int offset = 3 * i + 3;
            verticies[offset + 0] = (float)(Math.cos(angle) * radius);
            verticies[offset + 1] = 0;
            verticies[offset + 2] = (float)(Math.sin(angle) * radius);
            coneIndicies[i + 1] = (byte)(i + 1);
            baseIndicies[i] = (byte)(i + 1);
        }
        coneIndicies[segments + 1] = 1;
        
        vertexBuffer = asBuffer(verticies);
        normalBuffer = vertexBuffer;  // turns out to be the same
        coneIndexBuffer = asBuffer(coneIndicies);
        baseIndexBuffer = asBuffer(baseIndicies);
    }
    
    public void draw(GL10 gl) {
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLE_FAN, segments + 2, GL10.GL_UNSIGNED_BYTE, 
                coneIndexBuffer);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        if (drawBase)
            gl.glDrawElements(GL10.GL_TRIANGLE_FAN, segments, GL10.GL_UNSIGNED_BYTE, 
                    baseIndexBuffer);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
    
    static ByteBuffer asBuffer(byte[] bytes) {
        ByteBuffer buf = ByteBuffer.allocateDirect(bytes.length);
        buf.put(bytes);
        buf.position(0);
        return buf;
    }

    static FloatBuffer asBuffer(float[] floats) {
        ByteBuffer buf = ByteBuffer.allocateDirect(floats.length 
                * (Float.SIZE/Byte.SIZE));
        buf.order(ByteOrder.nativeOrder());
        FloatBuffer fbuf = buf.asFloatBuffer();
        fbuf.put(floats);
        fbuf.position(0);
        return fbuf;
    }

}
