package com.example.owntage.es2.game_logic.components;

import com.example.owntage.es2.game_logic.Event;

/**
 * Created by Owntage on 10/27/2015.
 */
public class PhysicsEvent extends Event {
    String collidedWith;
    boolean raycast;
    boolean isSender;
    float length;
    public PhysicsEvent(String collidedWith, boolean raycast, boolean isSender, float length) {
        component = "physics";
        this.collidedWith = collidedWith;
        this.raycast = raycast;
        this.isSender = isSender;
        this.length = length;
    }
}
