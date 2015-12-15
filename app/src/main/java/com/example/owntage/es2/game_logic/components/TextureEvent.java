package com.example.owntage.es2.game_logic.components;

import com.example.owntage.es2.game_logic.Event;

/**
 * Created by Owntage on 10/23/2015.
 */
public class TextureEvent extends Event {
    String textureName;
    public TextureEvent(String textureName) {
        component = "set_texture";
        this.textureName = textureName;
    }
}
