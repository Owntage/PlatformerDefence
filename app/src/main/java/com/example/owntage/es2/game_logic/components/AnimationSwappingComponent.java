package com.example.owntage.es2.game_logic.components;

import android.util.Log;

import com.example.owntage.es2.game_logic.ComponentUpdate;
import com.example.owntage.es2.game_logic.Event;
import com.example.owntage.es2.game_logic.IComponent;

import org.w3c.dom.Node;

import java.util.LinkedList;

/**
 * Created by Owntage on 11/15/2015.
 */
public class AnimationSwappingComponent implements IComponent {


    LinkedList<Event> globalEvents = new LinkedList<>();
    LinkedList<Event> localEvents = new LinkedList<>();

    String runningTexture = "running";
    String standingTexture = "standing";
    String jumpingTexture = "jumping";

    TextureEvent textureEvent = new TextureEvent(standingTexture);

    boolean up, down, left, right;

    public AnimationSwappingComponent() {
        textureEvent.component = "set_animation";
    }

    @Override
    public void onEvent(Event event) {
        switch(event.component) {
            case "move":
                MoveEvent moveEvent = (MoveEvent) event;
                up = moveEvent.up;
                down = moveEvent.down;
                left = moveEvent.left;
                right = moveEvent.right;
                boolean result = up || down || left || right;

                if(result) {
                    textureEvent.textureName = runningTexture;
                } else {
                    textureEvent.textureName = standingTexture;
                }
                localEvents.add(textureEvent);
                break;
            case "jump":
                textureEvent.textureName = jumpingTexture;
                localEvents.add(textureEvent);
                break;
            case "jump_stop":
                boolean result1 = up || down || left || right;
                if(result1) {
                    textureEvent.textureName = runningTexture;
                } else {
                    textureEvent.textureName = standingTexture;
                }
                localEvents.add(textureEvent);
                break;
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public boolean hasLocalEvents() {
        return localEvents.size() != 0;
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
        return new LinkedList<>();
    }

    @Override
    public String getName() {
        return "animation_swap";
    }

    @Override
    public ComponentUpdate getUpdate(int systemID) {
        return null;
    }

    @Override
    public IComponent loadFromXml(Node node) {
        return new AnimationSwappingComponent();
    }
}
