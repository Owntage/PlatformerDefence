package com.example.owntage.es2.game_logic.components;

import android.util.Log;

import com.example.owntage.es2.game_logic.ComponentUpdate;
import com.example.owntage.es2.game_logic.Event;
import com.example.owntage.es2.game_logic.IComponent;
import com.example.owntage.es2.game_logic.components.physics.Body;
import com.example.owntage.es2.game_logic.components.physics.Utility;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Created by Owntage on 11/20/2015.
 */
public class SentryGunComponent implements IComponent {

    float angularVelocity;
    float angle = 0.0f;
    float radius;
    float refreshTime = 0.3f;
    float currentRefreshTime = 0.0f;

    float minScanAngle = -1.0f;
    float maxScanAngle = 1.0f;
    boolean scanningClockwise = true;
    Body target;
    Body self;
    String targetType;
    String tripodTexture;

    LinkedList<Event> localEvents = new LinkedList<>();

    TreeMap<Integer, SentryGunUpdate> systems = new TreeMap<>();


    @Override
    public void onEvent(Event event) {
        switch(event.component) {
            case "timer":
                currentRefreshTime += 1.0f / 60.0f;
                if(target == null || currentRefreshTime > refreshTime) {
                    localEvents.add(new TargetRequestEvent(targetType, radius));
                    currentRefreshTime = 0.0f;
                    if(target == null) {
                        if(!Utility.angleInRange(angle, minScanAngle, maxScanAngle)) {
                            float destAngle;
                            if(Utility.getDeltaAngle(angle, minScanAngle) < Utility.getDeltaAngle(angle, maxScanAngle)) {
                                destAngle = minScanAngle;
                            } else {
                                destAngle = maxScanAngle;
                            }
                            scanningClockwise = !Utility.angleInRange(destAngle, angle, angle + (float) Math.PI);
                        }
                        if(!scanningClockwise) {
                            angle += angularVelocity / 60.0f;
                        } else {
                            angle -= angularVelocity / 60.0f;
                        }
                    }
                } else {
                    float dx = target.getPosition().getX() - self.getPosition().getX();
                    float dy = target.getPosition().getY() - self.getPosition().getY();
                    float rotationDirection;
                    float targetAngle = Utility.getAngle(dx, dy);
                    float da = targetAngle - angle;
                    if(Utility.angleInRange(targetAngle, angle, angle + (float) Math.PI)) {
                        rotationDirection = 1.0f;
                    } else {
                        rotationDirection = -1.0f;
                    }
                    angle += angularVelocity * rotationDirection / 60.0f;

                    if(Utility.angleInRange(targetAngle, angle - 0.2f, angle + 0.2f)) {
                        WeaponEvent weaponEvent = new WeaponEvent(angle);
                        localEvents.add(weaponEvent);
                    }
                }
                break;
            case "target":
                TargetEvent targetEvent = (TargetEvent) event;
                target = targetEvent.target;
                self = targetEvent.self;
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
        SentryGunUpdate update = systems.get(systemID);
        return update == null || update.angle == angle && update.radius == radius && update.tripodTexture.equals(tripodTexture);

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
        return "sentry";
    }

    @Override
    public ComponentUpdate getUpdate(int systemID) {
        return new SentryGunUpdate(tripodTexture, angle, radius);
    }

    @Override
    public IComponent loadFromXml(Node node) {
        SentryGunComponent result = new SentryGunComponent();
        NodeList childNodes = node.getChildNodes();
        for(int i = 0; i < childNodes.getLength(); i++) {
            if(childNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                String nodeValue = "";
                NodeList currentChildNodes = childNodes.item(i).getChildNodes();
                for(int j = 0; j < currentChildNodes.getLength(); j++) {
                    if(currentChildNodes.item(j).getNodeType() == Node.TEXT_NODE) {
                        nodeValue = currentChildNodes.item(j).getNodeValue();
                    }
                }
                switch(childNodes.item(i).getNodeName()) {
                    case "radius":
                        result.radius = Float.parseFloat(nodeValue);
                        break;
                    case "angular_velocity":
                        result.angularVelocity = Float.parseFloat(nodeValue);
                        break;
                    case "target_type":
                        result.targetType = nodeValue;
                        break;
                    case "tripod_texture":
                        result.tripodTexture = nodeValue;
                        break;
                    case "min_scan_angle":
                        result.minScanAngle = Float.parseFloat(nodeValue);
                        break;
                    case "max_scan_angle":
                        result.maxScanAngle = Float.parseFloat(nodeValue);
                        break;
                }
            }
        }
        return result;
    }
}
