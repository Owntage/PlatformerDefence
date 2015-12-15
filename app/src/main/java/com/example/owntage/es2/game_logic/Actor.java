package com.example.owntage.es2.game_logic;

import android.util.Log;
import android.util.Pair;

import java.util.LinkedList;

/**
 * Created by Owntage on 10/10/2015.
 */
public class Actor {
    protected LinkedList<IComponent> components = new LinkedList<IComponent>();

    String id;
    int number;

    protected Actor() {}

    public boolean hasEvents() {
        boolean result = false;
        for(IComponent component : components) {
            result = result || component.hasGlobalEvents();
        }
        return result;
    }

    public boolean hasUpdates(int systemID) {
        boolean result = false;
        for(IComponent component : components) {
            result = result || component.hasUpdate(systemID);
        }

        return result;
    }

    public ActorUpdate getUpdates(int systemID) {
        ActorUpdate result = new ActorUpdate();
        for(IComponent component : components) {
            if(component.hasUpdate(systemID)) {
                result.updates.add(component.getUpdate(systemID));
            }
        }
        result.id = id;
        result.number = number;
        return result;
    }

    public LinkedList<Event> getEvents() {
        LinkedList<Event> result = new LinkedList<Event>();
        for(IComponent component : components) {
            if(component.hasGlobalEvents()) {
                result.addAll(component.getGlobalEvents());
                component.getGlobalEvents().clear();
            }
        }
        return result;
    }

    public void onEvent(Event event) {
        for(IComponent component : components) {
            component.onEvent(event);
        }
        for(IComponent component : components) {
            if(component.hasLocalEvents()) {
                Event localEvent = component.getLocalEvents().peek();
                component.getLocalEvents().pollFirst();
                onEvent(localEvent);
                break;
            }
        }
    }

    public void onDestroy() {
        for(IComponent component : components) {
            component.onDestroy();
        }
    }

}
