package com.example.owntage.es2.game_logic.components;

import com.example.owntage.es2.game_logic.ComponentUpdate;

/**
 * Created by Owntage on 10/22/2015.
 */
public class TextureUpdate extends ComponentUpdate {
    public String textureName;
    public TextureUpdate(String textureName) {
        name = "texture";
        this.textureName = textureName;
    }
}
