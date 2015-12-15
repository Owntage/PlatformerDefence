package com.example.owntage.es2.game_logic.components;

import android.util.Log;
import android.util.Pair;

import com.example.owntage.es2.game_logic.ComponentUpdate;
import com.example.owntage.es2.game_logic.Event;
import com.example.owntage.es2.game_logic.GameLogic;
import com.example.owntage.es2.game_logic.IComponent;

import org.w3c.dom.Node;

import java.util.LinkedList;
import java.util.TreeMap;


/**
 * Created by Owntage on 11/26/2015.
 */
public class ClassicModeComponent implements IComponent {

    LinkedList<Event> globalEvents = new LinkedList<>();
    TreeMap<Integer, Pair<Float, Float>> checkpoints = new TreeMap<>();
    TreeMap<Integer, GameModeUpdate> systems = new TreeMap<>();
    int score = 0;
    int mainActorID;
    int lastCheckpoint = -1;
    int lifes = 3;
    int maxLifes = 3;
    boolean finished = false;
    GameLogic gameLogic;


    private void toCheckpoint() {
        Event regenerateEvent = new Event("regenerate", false, mainActorID);
        Pair<Float, Float> checkpoint = checkpoints.get(lastCheckpoint);
        PositionSetEvent positionEvent = new PositionSetEvent(checkpoint.first, checkpoint.second, "position_set");
        positionEvent.global = false;
        positionEvent.actorID = mainActorID;
        gameLogic.onEvent(positionEvent);
        gameLogic.onEvent(regenerateEvent);
    }

    @Override
    public void onEvent(Event event) {
        switch(event.component) {
            case "health":
                if(lifes > 0) {
                    lifes--;
                    toCheckpoint();
                } else {
                    finished = true;
                }
                break;
            case "classic_mode":
                ClassicModeEvent classicModeEvent = (ClassicModeEvent) event;
                mainActorID = classicModeEvent.mainActorID;
                break;
            case "checkpoint_set":
                PositionSetEvent checkpointCoords = (PositionSetEvent) event;
                checkpoints.put(checkpointCoords.checkpointID, new Pair<>(checkpointCoords.x, checkpointCoords.y));
                break;
            case "coin_picked":
                score++;
                break;
            case "finish":
                finished = true;
                break;
            case "checkpoint":
                lastCheckpoint = event.actorID;
                break;
            case "game_logic_set":
                GameLogicEvent gameLogicEvent = (GameLogicEvent) event;
                gameLogic = gameLogicEvent.gameLogic;
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
        return !globalEvents.isEmpty();
    }

    @Override
    public boolean hasUpdate(int systemID) {
        GameModeUpdate mappedUpdate = systems.get(systemID);
        if(mappedUpdate == null) {
            return true;
        } else {
            return mappedUpdate.finished != finished || mappedUpdate.score != score;
        }
    }

    @Override
    public LinkedList<Event> getLocalEvents() {
        return new LinkedList<>();
    }

    @Override
    public LinkedList<Event> getGlobalEvents() {
        return globalEvents;
    }

    @Override
    public String getName() {
        return "classic_mode";
    }

    @Override
    public ComponentUpdate getUpdate(int systemID) {
        GameModeUpdate newUpdate = new GameModeUpdate(score, "classic_mode", finished);
        if(lifes == 0) {
            newUpdate.score = 0;
        }
        systems.put(systemID, newUpdate);
        return newUpdate;
    }

    @Override
    public IComponent loadFromXml(Node node) {
        return new ClassicModeComponent();
    }
}
