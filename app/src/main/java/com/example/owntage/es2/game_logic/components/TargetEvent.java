package com.example.owntage.es2.game_logic.components;

import com.example.owntage.es2.game_logic.Event;
import com.example.owntage.es2.game_logic.components.physics.Body;

/**
 * Created by Owntage on 11/20/2015.
 */
public class TargetEvent extends Event {
    Body target;
    Body self;
    public TargetEvent(Body target, Body self) {
        this.target = target;
        this.self = self;
        component = "target";
    }
}
