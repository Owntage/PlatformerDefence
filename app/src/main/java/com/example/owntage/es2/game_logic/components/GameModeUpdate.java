package com.example.owntage.es2.game_logic.components;

import com.example.owntage.es2.game_logic.ComponentUpdate;

/**
 * Created by Igor on 11/26/2015.
 */
public class GameModeUpdate extends ComponentUpdate {
    public int score;
    public String mode;
    public boolean finished;
    public GameModeUpdate(int score, String mode, boolean finished) {
        this.score = score;
        this.mode = mode;
        name = "game_mode";
        this.finished = finished;
    }
}
