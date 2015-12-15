package com.example.owntage.es2.game_logic;

import com.example.owntage.es2.game_logic.ActorUpdate;

/**
 * Created by Owntage on 10/27/2015.
 */
public class DeleteActorUpdate extends ActorUpdate {
    public int deleteID;
    public DeleteActorUpdate(int deleteID) {
        this.deleteID = deleteID;
        number = -1;
    }
}
