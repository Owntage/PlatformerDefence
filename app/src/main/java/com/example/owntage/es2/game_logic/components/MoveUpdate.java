package com.example.owntage.es2.game_logic.components;

import com.example.owntage.es2.game_logic.ComponentUpdate;

/**
 * Created by Owntage on 10/12/2015.
 */
public class MoveUpdate extends ComponentUpdate {
    public float x;
    public float y;
    public float scaleX = 1.0f;
    public float scaleY = 1.0f;
    public MoveUpdate() {
        name = "move";
    }
}
