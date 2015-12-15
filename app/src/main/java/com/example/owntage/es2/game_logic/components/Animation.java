package com.example.owntage.es2.game_logic.components;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Owntage on 11/14/2015.
 */
public class Animation {
    private String name;
    private double delay = 0.3;
    private ArrayList<String> textures = new ArrayList<String>();
    private int pos = 0;

    public void addTexture(String texture) {
        textures.add(texture);
    }

    public String getTexture() {
        return textures.get(pos);
    }

    public void nextTexture() {
        pos = (pos + 1) % textures.size();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDelay(double delay) {
        this.delay = delay;
    }

    public double getDelay() {
        return delay;
    }

}
