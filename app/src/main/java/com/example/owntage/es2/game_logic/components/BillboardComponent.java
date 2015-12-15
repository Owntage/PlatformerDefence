package com.example.owntage.es2.game_logic.components;

import com.example.owntage.es2.game_logic.ComponentUpdate;
import com.example.owntage.es2.game_logic.Event;
import com.example.owntage.es2.game_logic.IComponent;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Created by Owntage on 11/23/2015.
 */
public class BillboardComponent implements IComponent {

    float timeSinceCollided;
    float displayTime;
    String actorType;
    String billboardTexture;

    TreeMap<Integer, BillboardUpdate> systems = new TreeMap<>();

    @Override
    public void onEvent(Event event) {
        switch(event.component) {
            case "timer":
                timeSinceCollided += 1.0f / 60.0f;
                break;
            case "physics":
                PhysicsEvent physicsEvent = (PhysicsEvent) event;
                if(!physicsEvent.raycast && physicsEvent.collidedWith.equals(actorType)) {
                    timeSinceCollided = 0;
                }
                break;
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
        return false;
    }

    @Override
    public boolean hasUpdate(int systemID) {
        BillboardUpdate mappedUpdate = systems.get(systemID);
        if(mappedUpdate == null) {
            return true;
        }
        return mappedUpdate.timeSinceCollided != timeSinceCollided || mappedUpdate.displayTime != displayTime;
    }

    @Override
    public LinkedList<Event> getLocalEvents() {
        return new LinkedList<>();
    }

    @Override
    public LinkedList<Event> getGlobalEvents() {
        return new LinkedList<>();
    }

    @Override
    public String getName() {
        return "billboard";
    }

    @Override
    public ComponentUpdate getUpdate(int systemID) {
        BillboardUpdate update = new BillboardUpdate(timeSinceCollided, displayTime, billboardTexture);
        systems.put(systemID, update);
        return update;
    }

    @Override
    public IComponent loadFromXml(Node node) {
        BillboardComponent result = new BillboardComponent();
        NodeList childNodes = node.getChildNodes();
        for(int i = 0; i < childNodes.getLength(); i++) {
            if(childNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                String nodeValue = null;
                NodeList elementChildNodes = childNodes.item(i).getChildNodes();
                for(int j = 0; j < elementChildNodes.getLength(); j++) {
                    if(elementChildNodes.item(j).getNodeType() == Node.TEXT_NODE) {
                        nodeValue = elementChildNodes.item(j).getNodeValue();
                    }
                }
                switch(childNodes.item(i).getNodeName()) {
                    case "display_time":
                        result.displayTime = Float.parseFloat(nodeValue);
                        break;
                    case "actor_type":
                        result.actorType = nodeValue;
                        break;
                    case "texture":
                        result.billboardTexture = nodeValue;
                        break;
                }
            }
        }
        return result;
    }
}
