package com.example.owntage.es2;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Owntage on 10/6/2015.
 */
public class Shader {

    private int shaderProgram;
    private int colorUniform;
    private int transformUniform;
    private int cameraUniform;
    private int projectionUniform;
    private float[] transform = new float[16];
    private float[] camera = new float[16];
    private float[] projection = new float[16];
    private float[] color = new float[] {1.0f, 1.0f, 1.0f, 1.0f};

    private static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }



    public void setColor(float r, float g, float b, float a) {
        float[] temp = new float[] {r, g, b, a};
        boolean colorCompare = true;
        for(int i = 0; i < 4; i++) {
            if(temp[i] != color[i]) {
                colorCompare = false;
            }
        }
        if(!colorCompare) {
            GLES20.glUniform4fv(colorUniform, 1, temp, 0);
        }
    }

    public void bind() {
        GLES20.glUseProgram(shaderProgram);
    }

    public void updateTransform() {
        GLES20.glUniformMatrix4fv(transformUniform, 1, false, transform, 0);
    }

    public void updateCamera() {
        GLES20.glUniformMatrix4fv(cameraUniform, 1, false, camera, 0);
    }

    public void updateProjection() {
        GLES20.glUniformMatrix4fv(projectionUniform, 1, false, projection, 0);
    }

    public float[] getTransform() {
        return transform;
    }

    public float[] getCamera() {
        return camera;
    }

    public float[] getProjection() {
        return projection;
    }

    int getShaderProgram() {
        return shaderProgram;
    }

    public Shader(String vertexShaderCode, String fragmentShaderCode) {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        shaderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(shaderProgram, vertexShader);
        GLES20.glAttachShader(shaderProgram, fragmentShader);
        GLES20.glLinkProgram(shaderProgram);

        Matrix.setIdentityM(transform, 0);
        Matrix.setIdentityM(camera, 0);
        Matrix.setIdentityM(projection, 0);

        transformUniform = GLES20.glGetUniformLocation(shaderProgram, "vTransform");
        cameraUniform = GLES20.glGetUniformLocation(shaderProgram, "vCamera");
        colorUniform = GLES20.glGetUniformLocation(shaderProgram, "vColor");
        projectionUniform = GLES20.glGetUniformLocation(shaderProgram, "vProjection");


        updateTransform();
        updateCamera();
        updateProjection();
        setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
