package com.example.owntage.es2.game_logic.components;

import android.util.Log;

import com.example.owntage.es2.game_logic.ComponentUpdate;
import com.example.owntage.es2.game_logic.Event;
import com.example.owntage.es2.game_logic.IComponent;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Created by Owntage on 11/17/2015.
 */
public class WeaponComponent implements IComponent {

    LinkedList<Event> localEvents = new LinkedList<>();
    float radius = 5.0f;
    float reloadTime = 1.0f;
    float shootingPeriod = 0.2f;
    int maxShots = 10;

    int shots = 10;
    int holders = 5;
    float currentReloadTime = 0.0f;
    float currentShootingPeriod = 0.0f;

    public enum WeaponState {
        READY,
        PREPARING_NEXT_SHOT,
        PREPARING_NEXT_HOLDER
    }

    WeaponState state = WeaponState.READY;

    TreeMap<Integer, WeaponUpdate> systems = new TreeMap<>();
    WeaponUpdate weaponUpdate = new WeaponUpdate(0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f);
    float angle;

    void onRaycastFinished(PhysicsEvent physicsEvent) {
        weaponUpdate = new WeaponUpdate(angle, physicsEvent.length, 0.0f, shootingPeriod, 0.0f, reloadTime);
    }

    @Override
    public void onEvent(Event event) {
        switch(state) {

            case READY:
                switch(event.component) {
                    case "shoot":
                        state = WeaponState.PREPARING_NEXT_SHOT;
                        WeaponEvent weaponEvent = (WeaponEvent) event;
                        angle = weaponEvent.angle;
                        localEvents.add(new RaycastEvent(weaponEvent.angle, radius));
                        break;
                    case "reload":
                        state = WeaponState.PREPARING_NEXT_HOLDER;
                        break;
                    case "physics":
                        PhysicsEvent physicsEvent = (PhysicsEvent) event;
                        if(physicsEvent.raycast && physicsEvent.isSender) {
                            onRaycastFinished(physicsEvent);
                        }
                        break;
                }
                break;

            case PREPARING_NEXT_SHOT:
                switch(event.component) {
                    case "timer":
                        currentShootingPeriod += 1.0f / 60.0f;
                        weaponUpdate.shootingPeriod += 1.0f / 60.0f;
                        if(currentShootingPeriod > shootingPeriod) {
                            currentShootingPeriod = 0.0f;
                            if(shots == 0) {
                                state = WeaponState.PREPARING_NEXT_HOLDER;
                            } else {
                                shots--;
                                state = WeaponState.READY;
                            }
                        }
                        break;
                    case "physics":
                        PhysicsEvent physicsEvent = (PhysicsEvent) event;
                        if(physicsEvent.raycast && physicsEvent.isSender) {
                            onRaycastFinished(physicsEvent);
                        }
                        break;
                }
                break;
            case PREPARING_NEXT_HOLDER:
                switch(event.component) {
                    case "timer":
                        currentReloadTime += 1.0f / 60.0f;
                        weaponUpdate.reloadTime += 1.0f / 60.0f;
                        if(currentReloadTime > reloadTime) {
                                currentReloadTime = 0.0f;
                            if(holders != 0) {
                                    holders--;
                                shots = maxShots;
                                    state = WeaponState.READY;
                            }
                        }
                        break;
                    case "physics":
                        PhysicsEvent physicsEvent = (PhysicsEvent) event;
                        if(physicsEvent.raycast && physicsEvent.isSender) {
                            onRaycastFinished(physicsEvent);
                        }
                        break;
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
        WeaponUpdate mappedUpdate = systems.get(systemID);
        if(mappedUpdate == null) {
            return true;
        } else {
            return !mappedUpdate.isEqualTo(weaponUpdate);
        }
    }

    @Override
    public LinkedList<Event> getLocalEvents() {
        return localEvents;
    }

    @Override
    public LinkedList<Event> getGlobalEvents() {
        return new LinkedList<>();
    }

    @Override
    public String getName() {
        return "weapon";
    }

    @Override
    public ComponentUpdate getUpdate(int systemID) {
        WeaponUpdate mappedUpdate = new WeaponUpdate(weaponUpdate.angle, weaponUpdate.length,
                weaponUpdate.shootingPeriod, weaponUpdate.maxShootingPeriod, weaponUpdate.reloadTime, weaponUpdate.maxReloadTime);
        systems.put(systemID, mappedUpdate);
        return weaponUpdate;
    }

    @Override
    public IComponent loadFromXml(Node node) {
        WeaponComponent result = new WeaponComponent();
        NodeList childNodes = node.getChildNodes();
        for(int i = 0; i < childNodes.getLength(); i++) {
            if(childNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                String nodeString = "";
                NodeList elementChildren = childNodes.item(i).getChildNodes();
                for(int j = 0; j < elementChildren.getLength(); j++) {
                    if(elementChildren.item(j).getNodeType() == Node.TEXT_NODE) {
                        nodeString = elementChildren.item(j).getNodeValue();
                    }
                }

                switch(childNodes.item(i).getNodeName()) {
                    case "radius":
                        result.radius = Float.parseFloat(nodeString);
                        break;
                    case "reload_time":
                        result.reloadTime = Float.parseFloat(nodeString);
                        break;
                    case "shooting_period":
                        result.shootingPeriod = Float.parseFloat(nodeString);
                        break;
                    case "shots":
                        result.maxShots = Integer.parseInt(nodeString);
                        break;
                    case "holders":
                        result.holders = Integer.parseInt(nodeString);
                        break;
                }
            }
        }
        return result;
    }
}
