package com.example.owntage.es2.game_logic.components;

import com.example.owntage.es2.game_logic.Event;

/**
 * Created by Owntage on 11/20/2015.
 */
public class TargetRequestEvent extends Event {
    String type;
    float radius;
    public TargetRequestEvent(String type, float radius) {
        this.type = type;
        this.radius = radius;
        component = "target_request";
    }
}
