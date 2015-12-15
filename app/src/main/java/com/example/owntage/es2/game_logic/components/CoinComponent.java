package com.example.owntage.es2.game_logic.components;

import android.util.Log;

import com.example.owntage.es2.game_logic.ComponentUpdate;
import com.example.owntage.es2.game_logic.Event;
import com.example.owntage.es2.game_logic.IComponent;

import org.w3c.dom.Node;

import java.util.LinkedList;

/**
 * Created by Owntage on 10/27/2015.
 */
public class CoinComponent implements IComponent {

    LinkedList<Event> globalEvents = new LinkedList<>();

    public void onEvent(Event event) {
        switch(event.component) {
            case "physics":
                PhysicsEvent physicsEvent = (PhysicsEvent) event;
                if(physicsEvent.collidedWith.equals("player")) {
                    globalEvents.add(new Event("delete"));
                }
                break;
        }
    }

    public void onDestroy() {

    }

    public boolean hasLocalEvents() {
        return false;
    }

    public boolean hasGlobalEvents() {
        return !globalEvents.isEmpty();
    }

    public boolean hasUpdate(int systemID) {
        return false;
    }

    public LinkedList<Event> getLocalEvents() {
        return new LinkedList<>();
    }

    public LinkedList<Event> getGlobalEvents() {
        return globalEvents;
    }

    public String getName() {
        return "coin";
    }

    public ComponentUpdate getUpdate(int systemID) {
        return new ComponentUpdate();
    }

    public IComponent loadFromXml(Node node) {
        return new CoinComponent();
    }
}
