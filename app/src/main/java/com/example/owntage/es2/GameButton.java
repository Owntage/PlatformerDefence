package com.example.owntage.es2;

import android.util.Log;

/**
 * Created by Owntage on 10/9/2015.
 */
public class GameButton {

    public interface ITouchCallback {
        void callback();
    }

    private class DefaultCallback implements ITouchCallback {
        public void callback() {}
    }

    enum State {
        UP,
        DOWN
    }

    State state = State.UP;

    float x;
    float y;
    float scaleX = 1.0f;
    float scaleY = 1.0f;
    float r = 0.7f;
    float g = 0.7f;
    float b = 0.7f;
    int touchID = -1;

    ITouchCallback downCallback = new DefaultCallback();
    ITouchCallback upCallback = new DefaultCallback();

    Quad quad;
    Shader shader;

    public GameButton(Shader shader, Texture texture, float x, float y, float scaleX, float scaleY) {
        this.x = x;
        this.y = y;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.shader = shader;

        quad = new Quad(shader, texture, scaleX, scaleY);
        quad.setPosition(x, y);
    }

    public void onTouchDown(float touchX, float touchY, int touchID) {
        Log.e("button", "onTouchDown called with coords: " + touchX + " " + touchY);
        if(touchX >= x - 0.5 * scaleX && touchX <= x + 0.5 * scaleX && touchY >= y - 0.5 * scaleY && touchY <= y + 0.5 * scaleY) {
            Log.e("button", "hit for button" + x + " " + y);
            if(state == State.UP) {
                downCallback.callback();
                state = State.DOWN;
                this.touchID = touchID;
            }
        }
    }


    public void onTouchUp(float touchX, float touchY, int touchID) {
        if(state == State.DOWN && this.touchID == touchID) {
            upCallback.callback();
            state = State.UP;
            touchID = -1;
        }
    }

    public void onTouchMove(float touchX, float touchY, int touchID) {
        if(touchX >= x - 0.5 * scaleX && touchX <= x + 0.5 * scaleX && touchY >= y - 0.5 * scaleY && touchY <= y + 0.5 * scaleY && this.touchID == touchID) {
            if(state == State.UP) {
                state = State.DOWN;
                downCallback.callback();
            }
        } else {
            if(state == State.DOWN) {
                state = State.UP;
                upCallback.callback();
            }
        }
    }

    public void setUpCallback(ITouchCallback upCallback) {
        this.upCallback = upCallback;
    }

    public void setDownCallback(ITouchCallback downCallback) {
        this.downCallback = downCallback;
    }



    public void draw() {
        if(state == State.DOWN) {
            r = 0.5f;
            g = 0.0f;
            b = 0.0f;
        } else {
            r = 0.7f;
            g = 0.7f;
            b = 0.7f;
        }
        shader.setColor(r, g, b, 1.0f);
        quad.draw(false);
    }

}
