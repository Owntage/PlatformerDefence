package com.example.owntage.es2.game_logic.components;

import android.util.Log;
import android.util.Pair;

import com.example.owntage.es2.game_logic.ComponentUpdate;
import com.example.owntage.es2.game_logic.Event;
import com.example.owntage.es2.game_logic.IComponent;
import com.example.owntage.es2.game_logic.components.physics.Body;
import com.example.owntage.es2.game_logic.components.physics.Vector;
import com.example.owntage.es2.game_logic.components.physics.World;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Created by Owntage on 10/10/2015.
 */



public class PhysicsComponent implements IComponent {

    static World world;
    public static void initWorld() {
        world = new World();
    }

    public static void step() {
        world.step(1.0f / 60.0f);
    }

    TreeMap<Integer, MoveUpdate> systemUpdates = new TreeMap<>();
    Body body;
    float maxSpeed = 4.0f;
    float movingForce = 120.0f;
    float jumpVelocity = 14.0f;
    float defaultX;
    float defaultY;
    Vector currentAcceleration = new Vector();
    LinkedList<Event> localEventsList = new LinkedList<>();
    LinkedList<Event> globalEventsList = new LinkedList<>();
    boolean lastIsJumping = false;


    public void onEvent(Event event) {
        switch(event.component) {
            case "move":
                Vector acceleration = new Vector();
                MoveEvent moveEvent = (MoveEvent) event;
                if(moveEvent.up) {
                    acceleration.add(new Vector(0.0f, movingForce));
                }
                if(moveEvent.down) {
                    acceleration.add(new Vector(0.0f, -movingForce));
                }
                if(moveEvent.left) {
                    acceleration.add(new Vector(-movingForce, 0.0f));
                }
                if(moveEvent.right) {
                    acceleration.add(new Vector(movingForce, 0.0f));
                }
                currentAcceleration = acceleration;
                break;
            case "timer":

                //moving

                float dotProduct = Vector.dotProduct(body.getVelocity(), currentAcceleration);
                if(dotProduct <= 0.0001f || body.getVelocity().getX() < 0 && body.getVelocity().getX() > -maxSpeed || body.getVelocity().getX() > 0 && body.getVelocity().getX() < maxSpeed) {
                    world.setBodyAcceleration(body, currentAcceleration);
                } else {
                    world.setBodyAcceleration(body, new Vector());
                }

                //generating local event
                if(body.isCollided()) {
                    localEventsList.add(new PhysicsEvent(body.collidedWith(), false, false, 0.0f));
                }
                if(body.isRaycasted()) {
                    localEventsList.add(new PhysicsEvent(body.raycastedWith(), true, false, 0.0f));
                    body.setRaycasted(false);
                }

                if(body.getJump() && !lastIsJumping) {
                    Event event1 = new Event();
                    event1.component = "jump_stop";
                    localEventsList.add(event1);
                }
                lastIsJumping = body.getJump();

                break;
            case "jump":

                if(body.getJump()) {
                    world.setBodyVelocity(body, new Vector(0.0f, jumpVelocity));
                }

                break;
            case "default_position_set":
                PositionSetEvent positionSetEvent = (PositionSetEvent) event;
                defaultX = positionSetEvent.x;
                defaultY = positionSetEvent.y;
                break;
            case "position_set":
                PositionSetEvent positionSetEvent1 = (PositionSetEvent) event;
                world.setBodyPosition(body, new Vector(positionSetEvent1.x, positionSetEvent1.y));
                break;
            case "scale_set":
                PositionSetEvent positionSetEvent2 = (PositionSetEvent) event;
                world.setBodyScale(body, new Vector(positionSetEvent2.x, positionSetEvent2.y));
                break;
            case "to_default":
                world.setBodyPosition(body, new Vector(defaultX, defaultY));
                break;
            case "raycast":
                RaycastEvent raycastEvent = (RaycastEvent) event;
                Pair<String, Float> raycastResult = world.raycast(body, raycastEvent.angle, raycastEvent.length);
                localEventsList.add(new PhysicsEvent(raycastResult.first, true, true, raycastResult.second));
                break;
            case "target_request":
                TargetRequestEvent targetRequestEvent = (TargetRequestEvent) event;
                Body target = world.findTarget(body, targetRequestEvent.type, targetRequestEvent.radius);
                TargetEvent targetEvent = new TargetEvent(target, body);
                localEventsList.add(targetEvent);
                break;

        }
    }


    public void onDestroy() {
        world.destroyBody(body);
    }


    public boolean hasGlobalEvents() {
        return !globalEventsList.isEmpty();
    }

    public boolean hasLocalEvents() { return !localEventsList.isEmpty(); }

    public boolean hasUpdate(int systemID) {
        MoveUpdate update = systemUpdates.get(systemID);
        if(update == null) {
            return true;
        } else {
            return update.x != body.getPosition().getX() || update.y != body.getPosition().getY();
        }
    }


    public LinkedList<Event> getLocalEvents() {
       return localEventsList;
    }

    public LinkedList<Event> getGlobalEvents() {
        return localEventsList;
    }


    public String getName() {
        return "physics";
    }


    public ComponentUpdate getUpdate(int systemID) {
        MoveUpdate result = new MoveUpdate();
        result.name = "move";
        result.x = body.getPosition().getX();
        result.y = body.getPosition().getY();
        result.scaleX = body.getScale().getX();
        result.scaleY = body.getScale().getY();
        systemUpdates.put(systemID, result);
        return result;
    }

    private float getNodeValue(Node node) {
        NodeList nodeList = node.getChildNodes();
        Float result = 0.0f;
        for(int i = 0; i < nodeList.getLength(); i++) {
            if(nodeList.item(i).getNodeType() == Node.TEXT_NODE) {
                String text = nodeList.item(i).getNodeValue();
                result = Float.parseFloat(text);
            }
        }
        return result;
    }

    private String getStringNodeValue(Node node) {
        NodeList nodeList = node.getChildNodes();
        String result = "";
        for(int i = 0; i < nodeList.getLength(); i++) {
            if(nodeList.item(i).getNodeType() == Node.TEXT_NODE) {
                result = nodeList.item(i).getNodeValue();
            }
        }
        return result;
    }

    public IComponent loadFromXml(Node node) {
        PhysicsComponent result = new PhysicsComponent();
        String bodyName = "";
        String bodyType = "dynamic";
        NodeList nodeList = node.getChildNodes();
        for(int i = 0; i < nodeList.getLength(); i++) {
            if(nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                String value = getStringNodeValue(nodeList.item(i));
                String name = nodeList.item(i).getNodeName();
                switch(name) {
                    case "body_type":
                        bodyType = value;
                        break;
                    case "body_name":
                        bodyName = value;
                        break;
                }
            }
        }
        if(!bodyType.equals("sensor")) {
            result.body = world.createBody(bodyType.equals("dynamic"), false, bodyName);
        } else {
            result.body = world.createBody(false, true, bodyName);
        }


        nodeList = node.getChildNodes();
        for(int i = 0; i < nodeList.getLength(); i++) {
            if(nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {

                String name = nodeList.item(i).getNodeName();
                float value = 0.0f;
                if(!name.equals("body_type") && !name.equals("body_name")) {
                    value = getNodeValue(nodeList.item(i));
                }
                switch(name) {
                    case "x":
                        world.setBodyPosition(result.body, new Vector(value, result.body.getPosition().getY()));
                        break;
                    case "y":
                        world.setBodyPosition(result.body, new Vector(result.body.getPosition().getX(), value));
                        break;
                    case "max_speed":
                        result.maxSpeed = value;
                        break;
                    case "speed_x":
                        world.setBodyVelocity(result.body, new Vector(value, result.body.getVelocity().getY()));
                        break;
                    case "speed_y":
                        world.setBodyVelocity(result.body, new Vector(result.body.getVelocity().getX(), value));
                        break;
                    case "mass":
                        world.setBodyMass(result.body, value);
                        break;
                    case "restitution":
                        world.setBodyRestitution(result.body, value);
                        break;
                    case "scale_x":
                        world.setBodyScale(result.body, new Vector(value, result.body.getScale().getY()));
                        break;
                    case "scale_y":
                        world.setBodyScale(result.body, new Vector(result.body.getScale().getX(), value));
                        break;
                    case "jump_height":
                        result.jumpVelocity = (float) Math.sqrt(2.0f * value * world.getGravity());
                        break;


                }
            }
        }
        return result;
    }
}
