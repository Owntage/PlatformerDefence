package com.example.owntage.es2.game_logic.components;

import android.util.Log;

import com.example.owntage.es2.game_logic.ComponentUpdate;
import com.example.owntage.es2.game_logic.Event;
import com.example.owntage.es2.game_logic.IComponent;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;

/**
 * Created by Owntage on 12/17/2015.
 */

//description: waits for player hitting of the signal block.
//after that starts moving left until reverse-sensor is reached.
    //when reverse-sensor is reached, direction is changed and so on...

public class MonsterMoveComponent implements IComponent {

    boolean signal = false;
    boolean movingLeft = true;
    String signalName = "monster_signal";
    LinkedList<Event> localEvents = new LinkedList<>();
    float delta = 0.0f;
    final float maxDelta = 0.5f;
    final String REVERSE_NAME = "reverse";

    @Override
    public void onEvent(Event event) {
        switch (event.component) {
            case "physics":
                boolean reverse = false;
                PhysicsEvent physicsEvent = (PhysicsEvent) event;
                if(!physicsEvent.raycast && physicsEvent.collidedWith.equals(REVERSE_NAME)) {
                    reverse = true;
                } else {
                    Log.e("monster", "collided with: " + physicsEvent.collidedWith);
                }
                if(signal && delta > maxDelta * 0.8f && reverse) {
                    delta = 0.0f;
                    Log.e("monster", "reverse!");
                    MoveEvent moveEvent = new MoveEvent();
                    moveEvent.left = movingLeft;
                    moveEvent.right = !movingLeft;
                    movingLeft = !movingLeft;
                    localEvents.add(moveEvent);
                }
                break;
            case "timer":
                delta = Math.min(delta + 1.0f / 60.0f, maxDelta);
                break;
            default:
                if(event.component.equals(signalName) && !signal) {
                    signal = true;
                    MoveEvent moveEvent = new MoveEvent();
                    moveEvent.left = movingLeft;
                    moveEvent.right = !movingLeft;
                    movingLeft = !movingLeft;
                    localEvents.add(moveEvent);
                }
                break;

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
        return false;
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
        return null;
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
        MonsterMoveComponent result = new MonsterMoveComponent();
        NodeList childNodes = node.getChildNodes();
        for(int i = 0; i < childNodes.getLength(); i++) {
            if(childNodes.item(i).getNodeType() == Node.TEXT_NODE) {
                result.signalName = childNodes.item(i).getNodeValue();
            }
        }
        return result;
    }
}
