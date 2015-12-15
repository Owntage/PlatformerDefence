package com.example.owntage.es2.game_logic.components;

import com.example.owntage.es2.game_logic.ComponentUpdate;
import com.example.owntage.es2.game_logic.Event;
import com.example.owntage.es2.game_logic.IComponent;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;

/**
 * Created by Owntage on 12/14/2015.
 */
public class SignalComponent implements IComponent {


    LinkedList<Event> globalEvents = new LinkedList<>();
    String signalMessage;

    @Override
    public void onEvent(Event event) {
        if(event.component.equals("physics")) {
            PhysicsEvent physicsEvent = (PhysicsEvent) event;
            if(!physicsEvent.raycast && physicsEvent.collidedWith.equals("player")) {
                globalEvents.add(new Event(signalMessage));
            }
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public boolean hasLocalEvents() {
        return false;
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
        return null;
    }

    @Override
    public LinkedList<Event> getGlobalEvents() {
        return globalEvents;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public ComponentUpdate getUpdate(int systemID) {
        return null;
    }

    @Override
    public IComponent loadFromXml(Node node) {
        SignalComponent result = new SignalComponent();
        NodeList childNodes = node.getChildNodes();
        for(int i = 0; i < childNodes.getLength(); i++) {
            if(childNodes.item(i).getNodeType() == Node.TEXT_NODE) {
                result.signalMessage = childNodes.item(i).getNodeValue();
            }
        }
        return result;
    }
}
