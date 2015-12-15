package com.example.owntage.es2.game_logic.components;

import com.example.owntage.es2.game_logic.Event;

/**
 * Created by Owntage on 11/27/2015.
 */
public class ClassicModeEvent extends Event {
    int mainActorID;
    public ClassicModeEvent(int mainActorID, int modeID) {
        this.mainActorID = mainActorID;
        actorID = modeID;
        global = false;
        component = "classic_mode";
    }
}
