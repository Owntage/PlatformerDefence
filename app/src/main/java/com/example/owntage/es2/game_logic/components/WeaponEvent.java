package com.example.owntage.es2.game_logic.components;

import com.example.owntage.es2.game_logic.Event;

/**
 * Created by Owntage on 11/17/2015.
 */
public class WeaponEvent extends Event {
    float angle;
    public WeaponEvent(float angle) {
        this.angle = angle;
        component = "shoot";
    }
    public WeaponEvent() {
        component = "reload";
    }
}
