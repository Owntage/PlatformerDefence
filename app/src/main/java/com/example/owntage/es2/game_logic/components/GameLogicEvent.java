package com.example.owntage.es2.game_logic.components;

import com.example.owntage.es2.game_logic.Event;
import com.example.owntage.es2.game_logic.GameLogic;

/**
 * Created by Owntage on 11/27/2015.
 */
public class GameLogicEvent extends Event {
    GameLogic gameLogic;
    public GameLogicEvent(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
        component = "game_logic_set";
        global = true;
    }
}
