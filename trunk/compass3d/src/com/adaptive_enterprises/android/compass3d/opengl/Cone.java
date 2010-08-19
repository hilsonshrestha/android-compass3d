package com.adaptive_enterprises.android.compass3d.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Cone {
    private double mHeight = 1;
    private double mRadius = 1;
    private int mSegments = 4;
    private FloatBuffer mVertexBuffer;
    private ByteBuffer mConeIndexBuffer;
    private FloatBuffer mNormalBuffer;
    private ByteBuffer mBaseIndexBuffer;
    private boolean mDrawBase;
    private boolean mGeometryValid;
    
    public void setHeight(double height) {
        mHeight = height;
        mGeometryValid = false;
    }
    
    public void setRadius(double radius) {
        mRadius = radius;
        mGeometryValid = false;
    }
    
    public void setSegments(int segments) {
        mSegments = segments;
        mGeometryValid = false;
    }
    
    public void drawBase(boolean enable) {
        mDrawBase = enable;
    }
    
    private void calculateGeometry() {
        if (mGeometryValid) return;

        assert mSegments < 255;

        float[] verticies = new float[3 * (mSegments + 1)];
        byte[] coneIndicies = new byte[mSegments + 2];
        byte[] baseIndicies = new byte[mSegments];

        // Nose of the cone
        verticies[0] =  0; 
        verticies[1] = (float)mHeight; 
        verticies[2] =  0;
        
        coneIndicies[0] = 0;
        double perAngle = 2 * Math.PI / mSegments;
        for (int i = 0; i < mSegments; i++) {
            double angle = i * perAngle;
            int offset = 3 * i + 3;
            verticies[offset + 0] = (float)(Math.cos(angle) * mRadius);
            verticies[offset + 1] = 0;
            verticies[offset + 2] = (float)(Math.sin(angle) * mRadius);
            coneIndicies[i + 1] = (byte)(i + 1);
            baseIndicies[i] = (byte)(i + 1);
        }
        coneIndicies[mSegments + 1] = 1;
        
        mVertexBuffer = asBuffer(verticies);
        mNormalBuffer = mVertexBuffer;  // turns out to be the same
        mConeIndexBuffer = asBuffer(coneIndicies);
        mBaseIndexBuffer = asBuffer(baseIndicies);
        mGeometryValid = true;
    }
    
    public void draw(GL10 gl) {
        calculateGeometry();
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLE_FAN, mSegments + 2, GL10.GL_UNSIGNED_BYTE, 
                mConeIndexBuffer);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        if (mDrawBase)
            gl.glDrawElements(GL10.GL_TRIANGLE_FAN, mSegments, GL10.GL_UNSIGNED_BYTE, 
                    mBaseIndexBuffer);
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
