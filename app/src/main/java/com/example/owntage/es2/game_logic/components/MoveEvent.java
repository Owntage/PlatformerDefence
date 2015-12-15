package com.example.owntage.es2.game_logic.components;

import com.example.owntage.es2.game_logic.Event;

/**
 * Created by Owntage on 10/12/2015.
 */
public class MoveEvent extends Event {
    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;
    public MoveEvent() {
        component = "move";
    }
}
