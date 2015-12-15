package com.example.owntage.es2;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Owntage on 10/6/2015.
 */
public class Quad {
    private float[] coords;
    private float[] texCoords = new float[] {   1.0f, 0.0f,
                                                0.0f, 0.0f,
                                                0.0f, 1.0f,
                                                1.0f, 0.0f,
                                                0.0f, 1.0f,
                                                1.0f, 1.0f};

    private FloatBuffer coordBuffer;
    private FloatBuffer textureBuffer;

    Shader shader;
    Texture texture;
    float x = 0.0f;
    float y = 0.0f;
    float angle = 0.0f;
    float scaleX = 1.0f;
    float scaleY = 1.0f;

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void draw(boolean check) {
        if(!check || x + scaleX - MainRenderer.cameraX > -MainRenderer.screenWidth / 128.0f && x - scaleX - MainRenderer.cameraX < MainRenderer.screenWidth / 128.0f &&
                y + scaleY - MainRenderer.cameraY > -MainRenderer.screenHeight / 128.0f && y - scaleY - MainRenderer.cameraY < MainRenderer.screenHeight / 128.0f) {
            shader.bind();
            texture.bind();
            Matrix.setIdentityM(shader.getTransform(), 0);

            Matrix.translateM(shader.getTransform(), 0, x, y, 0.0f);
            Matrix.rotateM(shader.getTransform(), 0, 180.0f - angle, 0.0f, 0.0f, -1.0f);
            Matrix.scaleM(shader.getTransform(), 0, scaleX, scaleY, 1.0f);


            shader.updateTransform();

            int positionAttrib = GLES20.glGetAttribLocation(shader.getShaderProgram(), "vPosition");

            GLES20.glEnableVertexAttribArray(positionAttrib);
            GLES20.glVertexAttribPointer(positionAttrib, 3, GLES20.GL_FLOAT, false, 0, coordBuffer);

            int textureAttrib = GLES20.glGetAttribLocation(shader.getShaderProgram(), "vTexture");

            GLES20.glEnableVertexAttribArray(textureAttrib);
            GLES20.glVertexAttribPointer(textureAttrib, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, coords.length / 3);
            GLES20.glDisableVertexAttribArray(positionAttrib);
        }
    }

    public Quad(Shader shader, Texture texture, float sizeX, float sizeY) {

        coords = new float[] {   -0.5f, -0.5f, 0.0f,
                                0.5f, -0.5f, 0.0f,
                                0.5f, 0.5f, 0.0f,
                                -0.5f, -0.5f, 0.0f,
                                0.5f, 0.5f, 0.0f,
                                -0.5f, 0.5f, 0.0f};

        this.shader = shader;
        this.texture = texture;
        this.scaleX = sizeX;
        this.scaleY = sizeY;

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(coords.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        coordBuffer = byteBuffer.asFloatBuffer();
        coordBuffer.put(coords);
        coordBuffer.position(0);

        byteBuffer = ByteBuffer.allocateDirect(texCoords.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuffer.asFloatBuffer();
        textureBuffer.put(texCoords);
        textureBuffer.position(0);
    }

}
