package com.example.owntage.es2.game_logic.components.physics;

/**
 * Created by Owntage on 11/20/2015.
 */
public class Utility {
    public static float getAngle(float dx, float dy) {
        double result = Math.atan(dy / dx);
        if(dx < 0.0) {
            result += Math.PI;
        } else {
            if(dy < 0.0) {
                result += Math.PI * 2.0;
            }
        }
        return (float) result;
    }
    public static boolean angleInRange(float angle, float min, float max) {
        float fp = (float) Math.PI;
        while(min < 0) {
            min += fp * 2.0f;
        }
        while(min > fp * 2.0f) {
            min -= fp * 2.0f;
        }
        while(max < 0) {
            max += fp * 2.0f;
        }
        while(max > fp * 2.0f) {
            max -= fp * 2.0f;
        }
        if(min < max) {
            return angle >= min && angle <= max;
        } else {
            return angle >= min || angle <= max;
        }
    }
}
