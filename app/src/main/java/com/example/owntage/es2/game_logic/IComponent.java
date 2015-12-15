package com.example.owntage.es2.game_logic;

import org.w3c.dom.Node;

import java.util.LinkedList;

/**
 * Created by Owntage on 10/10/2015.
 */
public interface IComponent {
    void onEvent(Event event);
    void onDestroy();
    boolean hasLocalEvents();
    boolean hasGlobalEvents();
    boolean hasUpdate(int systemID);
    LinkedList<Event> getLocalEvents();
    LinkedList<Event> getGlobalEvents();
    String getName();
    ComponentUpdate getUpdate(int systemID);
    IComponent loadFromXml(Node node);
}
