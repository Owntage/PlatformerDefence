package com.example.owntage.es2.game_logic.components;

import android.util.Log;

import com.example.owntage.es2.game_logic.ComponentUpdate;
import com.example.owntage.es2.game_logic.Event;
import com.example.owntage.es2.game_logic.IComponent;

import org.w3c.dom.Node;

import java.util.LinkedList;

/**
 * Created by Owntage on 11/27/2015.
 */
public class CheckpointComponent implements IComponent {

    LinkedList<Event> globalEvents = new LinkedList<>();
    LinkedList<Event> localEvents = new LinkedList<>();
    Event checkpointEvent = new Event("checkpoint");

    @Override
    public void onEvent(Event event) {
        if(event.component.equals("physics")) {
            PhysicsEvent physicsEvent = (PhysicsEvent) event;
            if(!physicsEvent.raycast && physicsEvent.collidedWith.equals("player")) {
                globalEvents.add(checkpointEvent);
            }
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public boolean hasLocalEvents() {
        return !localEvents.isEmpty();
    }

    @Override
    public boolean hasGlobalEvents() {
        return !globalEvents.isEmpty();
    }

    @Override
    public boolean hasUpdate(int systemID) {
        return false;
    }

    @Override
    public LinkedList<Event> getLocalEvents() {
        return localEvents;
    }

    @Override
    public LinkedList<Event> getGlobalEvents() {
        return globalEvents;
    }

    @Override
    public String getName() {
        return "checkpoint";
    }

    @Override
    public ComponentUpdate getUpdate(int systemID) {
        return null;
    }

    @Override
    public IComponent loadFromXml(Node node) {
        return new CheckpointComponent();
    }
}
