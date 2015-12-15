package com.example.owntage.es2;

import android.opengl.Matrix;

import java.util.TreeMap;

/**
 * Created by Owntage on 10/9/2015.
 */
public class ButtonManager {

    TreeMap<Integer, GameButton> buttons = new TreeMap<Integer, GameButton>();
    Shader shader;

    float[] cameraHolder = new float[16];

    public ButtonManager(Shader shader) {
        this.shader = shader;
    }



    public void createButton(Integer id, GameButton button) {
        buttons.put(id, button);
    }

    public void drawAll() {
        for(int i = 0; i < 16; i++) {
            cameraHolder[i] = shader.getCamera()[i];
        }
        Matrix.setIdentityM(shader.getCamera(), 0);
        shader.updateCamera();

        for(GameButton current : buttons.values()) {
            current.draw();
        }

        for(int i = 0; i < 16; i++) {
            shader.getCamera()[i] = cameraHolder[i];
        }
        shader.updateCamera();
    }

    public void onTouchDown(float touchX, float touchY, int touchID) {
        for(GameButton current : buttons.values()) {
            current.onTouchDown(touchX, touchY, touchID);
        }
    }

    public void onTouchUp(float touchX, float touchY, int touchID) {
        for(GameButton current : buttons.values()) {
            current.onTouchUp(touchX, touchY, touchID);
        }
    }

    public void onTouchMove(float touchX, float touchY, int touchID) {
        for(GameButton current : buttons.values()) {
            current.onTouchMove(touchX, touchY, touchID);
        }
    }

}
