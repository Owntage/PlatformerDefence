package com.example.owntage.es2.game_logic;

/**
 * Created by Owntage on 10/10/2015.
 */
public class Event {
    public int actorID;
    public boolean global;
    public String component;

    public Event() {

    }
    public Event(String component) {
        this.component = component;
    }

    public Event(String component, boolean global, int actorID) {
        this.component = component;
        this.global = global;
        this.actorID = actorID;
    }
}
