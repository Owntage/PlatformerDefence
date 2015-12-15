package com.example.owntage.es2.game_logic.components;

import com.example.owntage.es2.game_logic.Event;

/**
 * Created by Owntage on 10/23/2015.
 */
public class PositionSetEvent extends Event {
    float x;
    float y;
    int checkpointID;
    public PositionSetEvent(float x, float y, String component) {
        this.component = component;
        this.x = x;
        this.y = y;
    }
    public PositionSetEvent(float x, float y, String component, int checkpointID) {
        this(x, y, component);
        this.checkpointID = checkpointID;
    }
}
