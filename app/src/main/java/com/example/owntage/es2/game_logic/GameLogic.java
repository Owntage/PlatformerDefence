package com.example.owntage.es2.game_logic;

import android.util.Log;

import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Created by Owntage on 10/10/2015.
 */
public class GameLogic {
    private TreeMap<Integer, Actor> actors = new TreeMap<>();
    private static int systemCount = 0;
    private static int actorCount = 0;
    private ActorFactory actorFactory;
    private LinkedList<ActorUpdate> deleteUpdates = new LinkedList<>();

    public GameLogic(ActorFactory actorFactory) {
        this.actorFactory = actorFactory;
    }

    public void onEvent(Event event) {

        if(event.component == "delete") {
            Actor actor = actors.get(event.actorID);
            actor.onDestroy();
            actor.id = "delete";
            deleteUpdates.add(new DeleteActorUpdate(actor.number));
            return;
        }

        if(event.global) {
            for (Actor actor : actors.values()) {
                actor.onEvent(event);
                LinkedList<Event> globalEvents = actor.getEvents();
                for(Event globalEvent : globalEvents) {
                    globalEvent.global = true;
                    globalEvent.actorID = actor.number;
                    onEvent(globalEvent);
                }
            }
        } else {
            Actor actor = actors.get(event.actorID);
            actor.onEvent(event);
            if(actor.hasEvents()) {
                LinkedList<Event> globalEvents = actor.getEvents();
                for(Event globalEvent : globalEvents) {
                    globalEvent.global = true;
                    globalEvent.actorID = actor.number;
                    onEvent(globalEvent);
                }
            }
        }

        LinkedList<Actor> deletedActors = new LinkedList<>();
        for(Actor actor : actors.values()) {
            if(actor.id.equals("delete")) {
                deletedActors.add(actor);
            }
        }

        for(Actor actor : deletedActors) {
            if(actor.id.equals("delete")) {
                actors.remove(actor.number);
            }
        }


    }

    public LinkedList<ActorUpdate> getUpdates(int systemID) {
        LinkedList<ActorUpdate> result = new LinkedList<>();
        for(Actor actor : actors.values()) {
            if(actor.hasUpdates(systemID)) {
                ActorUpdate update = actor.getUpdates(systemID);
                result.add(update);
            }
        }
        result.addAll(deleteUpdates);
        deleteUpdates.clear();
        return result;
    }

    public int registerSystem() {
        systemCount++;
        return systemCount;
    }

    public int createActor(String id) {
        Actor result = actorFactory.createActor(id);
        result.id = id;
        result.number = actorCount;
        actorCount++;
        actors.put(result.number, result);
        return result.number;
    }

    public void destroyActor(int number) {
        Actor actor = actors.get(number);
        if(actor != null) {
            actor.onDestroy();
            actors.remove(number);
            //TODO I need to inform all systems about destroyed actor
            //now they just think that there are no updates for this actor
        }
    }
}
