package com.example.owntage.es2.game_logic.components;

import android.util.Log;

import com.example.owntage.es2.game_logic.Event;
import com.example.owntage.es2.game_logic.IComponent;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Created by Owntage on 10/12/2015.
 */



public class MoveComponent implements IComponent {

    TreeMap<Integer, MoveUpdate> systemUpdates = new TreeMap<>();

    float x = 0.0f;
    float y = 0.0f;
    float defaultX = 0.0f;
    float defaultY = 0.0f;

    float speed = 2.0f;
    static final float FPS = 60.0f;

    boolean up = false;
    boolean down = false;
    boolean left = false;
    boolean right = false;

    private void onTimer() {
        if(up) y += speed / FPS;
        if(down) y -= speed / FPS;
        if(left) x -= speed / FPS;
        if(right) x += speed / FPS;
    }

    public void onEvent(Event event) {
        switch(event.component) {
            case "move":
                MoveEvent moveEvent = (MoveEvent) event;
                up = moveEvent.up;
                down = moveEvent.down;
                left = moveEvent.left;
                right = moveEvent.right;
                break;
            case "timer":
                onTimer();
                break;
            case "to_default":
                x = defaultX;
                y = defaultY;
                break;
            case "default_position_set":
                PositionSetEvent positionSetEvent = (PositionSetEvent) event;
                defaultX = positionSetEvent.x;
                defaultY = positionSetEvent.y;
                break;
            case "position_set":
                PositionSetEvent positionSetEvent2 = (PositionSetEvent) event;
                x = positionSetEvent2.x;
                y = positionSetEvent2.y;
                break;
        }
    }

    public void onDestroy() {

    }

    public boolean hasLocalEvents() {
        return false;
    }

    public boolean hasGlobalEvents() {return false; }

    public boolean hasUpdate(int systemID) {
        MoveUpdate update = systemUpdates.get(systemID);
        if(update == null) {
            return true;
        } else {
            if(update.x != x || update.y != y) {
                return true;
            }
        }
        return false;
    }

    public LinkedList<Event> getLocalEvents() {
        return null;
    }

    public LinkedList<Event> getGlobalEvents() {return null; }

    public String getName() {
        return "move";
    }

    public MoveUpdate getUpdate(int systemID) {
        MoveUpdate update = new MoveUpdate();
        update.x = x;
        update.y = y;
        update.name = "move";
        systemUpdates.put(systemID, update);
        return update;
    }

    private float getNodeValue(Node node) {
        NodeList nodeList = node.getChildNodes();
        Float result = new Float(0.0f);
        for(int i = 0; i < nodeList.getLength(); i++) {
            if(nodeList.item(i).getNodeType() == Node.TEXT_NODE) {
                String text = nodeList.item(i).getNodeValue();
                result = Float.parseFloat(text);
            }
        }
        return result;
    }

    public IComponent loadFromXml(Node node) {
        MoveComponent result = new MoveComponent();
        NodeList nodeList = node.getChildNodes();
        for(int i = 0; i < nodeList.getLength(); i++) {
            if(nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                String name = nodeList.item(i).getNodeName();
                switch(name) {
                    case "speed":
                        result.speed = getNodeValue(nodeList.item(i));
                        break;
                    case "x":
                        result.x = getNodeValue(nodeList.item(i));
                        break;
                    case "y":
                        result.y = getNodeValue(nodeList.item(i));
                        break;
                }
            }
        }
        return result;
    }
}
