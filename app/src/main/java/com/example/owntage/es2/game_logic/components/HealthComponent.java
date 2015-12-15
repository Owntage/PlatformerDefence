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
public class HealthComponent implements IComponent {

    LinkedList<Event> localEvents = new LinkedList<>();
    LinkedList<Event> globalEvents = new LinkedList<>();
    TreeMap<Integer, HealthUpdate> systems = new TreeMap<>();

    float health;
    float currentHealth;
    TreeMap<String, Float> collisionDamage = new TreeMap<>();
    TreeMap<String, Float> raycastDamage = new TreeMap<>();

    @Override
    public void onEvent(Event event) {
        switch(event.component) {
            case "physics":
                PhysicsEvent physicsEvent = (PhysicsEvent) event;
                if(physicsEvent.raycast) {
                    if(!physicsEvent.isSender) {
                        Float mappedDamage = raycastDamage.get(physicsEvent.collidedWith);
                        if(mappedDamage != null) {
                            currentHealth -= mappedDamage;
                            if(currentHealth <= 0) {
                                globalEvents.add(new Event("health"));
                            }
                        }
                    }
                } else {
                    Float mappedDamage = collisionDamage.get(physicsEvent.collidedWith);
                    if(mappedDamage != null) {
                        currentHealth -= mappedDamage;
                        if(currentHealth <= 0) {
                            globalEvents.add(new Event("health"));
                        }
                    }
                }
                break;
            case "regenerate":
                currentHealth = health;
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
        return !globalEvents.isEmpty();
    }

    @Override
    public boolean hasUpdate(int systemID) {
        HealthUpdate mappedUpdate = systems.get(systemID);
        if(mappedUpdate == null) {
            return true;
        } else {
            return mappedUpdate.health != health || mappedUpdate.currentHealth != currentHealth;
        }
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
        return "health";
    }

    @Override
    public ComponentUpdate getUpdate(int systemID) {
        HealthUpdate update = new HealthUpdate(health, currentHealth);
        systems.put(systemID, update);
        return update;
    }

    private void putToMap(TreeMap<String, Float> map, Node node) {
        NodeList childNodes = node.getChildNodes();
        String value = null;
        String type = null;
        for(int j = 0; j < childNodes.getLength(); j++) {
            if(childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {

                switch(childNodes.item(j).getNodeName()) {
                    case "type":
                        NodeList typeChildNodes = childNodes.item(j).getChildNodes();
                        for(int k = 0; k < typeChildNodes.getLength(); k++) {
                            if(typeChildNodes.item(k).getNodeType() == Node.TEXT_NODE) {
                                type = typeChildNodes.item(k).getNodeValue();
                            }
                        }
                        break;
                    case "damage":
                        NodeList valueChildNodes = childNodes.item(j).getChildNodes();
                        for(int k = 0; k < valueChildNodes.getLength(); k++) {
                            if(valueChildNodes.item(k).getNodeType() == Node.TEXT_NODE) {
                                value = valueChildNodes.item(k).getNodeValue();
                            }
                        }
                        break;
                }
            }
        }
        map.put(type, Float.parseFloat(value));
    }

    @Override
    public IComponent loadFromXml(Node node) {
        HealthComponent result = new HealthComponent();
        NodeList childNodes = node.getChildNodes();
        for(int i = 0; i < childNodes.getLength(); i++) {
            if(childNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                switch(childNodes.item(i).getNodeName()) {
                    case "health":
                        String nodeValue = null;
                        NodeList healthChildNodes = childNodes.item(i).getChildNodes();
                        for(int j = 0; j < healthChildNodes.getLength(); j++) {
                            if(healthChildNodes.item(j).getNodeType() == Node.TEXT_NODE) {
                                nodeValue = healthChildNodes.item(j).getNodeValue();
                            }
                        }
                        result.health = Float.parseFloat(nodeValue);
                        result.currentHealth = result.health;
                        break;
                    case "raycast_damage":
                        putToMap(result.raycastDamage, childNodes.item(i));
                        break;
                    case "collision_damage":
                        putToMap(result.collisionDamage, childNodes.item(i));
                        break;
                }
            }
        }
        return result;
    }
}
