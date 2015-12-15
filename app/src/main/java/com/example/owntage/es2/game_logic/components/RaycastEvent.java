package com.example.owntage.es2.game_logic.components;

import com.example.owntage.es2.game_logic.Event;

/**
 * Created by Owntage on 11/17/2015.
 */
public class RaycastEvent extends Event {
    float angle;
    float length;
    public RaycastEvent(float angle, float length) {
        component = "raycast";
        this.angle = angle;
        this.length = length;
    }
}
