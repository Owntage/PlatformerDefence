package com.example.owntage.es2;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Owntage on 10/6/2015.
 */
public class Triangle {
    private FloatBuffer vertexBuffer;
    private float verticles[] = new float[]{
            0.0f, 0.0f, 0.0f,
            0.5f, 0.0f, 0.0f,
            0.5f, 0.5f, 0.0f
    };

    private float color[] = new float[]{0.0f, 0.5f, 1.0f, 1.0f};
    private int shaderProgram;
    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    public Triangle(String vertexShaderCode, String fragmentShaderCode) {
        //this.vertexShaderCode = vertexShaderCode;
        //this.fragmentShaderCode = fragmentShaderCode;
        ByteBuffer bb = ByteBuffer.allocateDirect(verticles.length * 4);
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(verticles);
        vertexBuffer.position(0);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        shaderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(shaderProgram, vertexShader);
        GLES20.glAttachShader(shaderProgram, fragmentShader);
        GLES20.glLinkProgram(shaderProgram);
    }

    public void draw() {
        GLES20.glUseProgram(shaderProgram);
        int positionAttrib = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(positionAttrib);
        GLES20.glVertexAttribPointer(positionAttrib, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        int colorUniform = GLES20.glGetUniformLocation(shaderProgram, "vColor");
        GLES20.glUniform4fv(colorUniform, 1, color, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, verticles.length / 3);
        GLES20.glDisableVertexAttribArray(positionAttrib);
    }
}
