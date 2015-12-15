package com.example.owntage.es2.game_logic.components;

import android.util.Log;

import com.example.owntage.es2.game_logic.ComponentUpdate;
import com.example.owntage.es2.game_logic.Event;
import com.example.owntage.es2.game_logic.IComponent;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by Owntage on 10/22/2015.
 */
public class TextureComponent implements IComponent {

    TreeMap<Integer, String> systems = new TreeMap<>();
    TreeMap<String, Animation> animations = new TreeMap<>();
    String currentAnimation;
    double time = 0.0;



    @Override
    public void onEvent(Event event) {
        switch(event.component) {
            case "set_texture":
                TextureEvent textureEvent = (TextureEvent) event;
                Animation animation = new Animation();
                animation.setName(textureEvent.textureName);
                animation.addTexture(textureEvent.textureName);
                animations.put(textureEvent.textureName, animation);
                currentAnimation = textureEvent.textureName;
                break;
            case "timer":
                time += 1.0 / 60.0;
                if(time >= animations.get(currentAnimation).getDelay()) {
                    time = 0.0;
                    animations.get(currentAnimation).nextTexture();
                }
                break;
            case "set_animation":
                TextureEvent textureEvent1 = (TextureEvent) event;
                currentAnimation = textureEvent1.textureName;

        }
    }

    @Override
    public void onDestroy() {

    }


    public boolean hasLocalEvents() {
        return false;
    }

    public boolean hasGlobalEvents() { return false; }

    @Override
    public boolean hasUpdate(int systemID) {
        if(!systems.containsKey(systemID)) {
            return true;
        } else {
            String mappedTexture = systems.get(systemID);
            return !mappedTexture.equals(animations.get(currentAnimation).getTexture());
        }
    }


    public LinkedList<Event> getLocalEvents() {
        return null;
    }

    public LinkedList<Event> getGlobalEvents() { return null; }

    @Override
    public String getName() {
        return "texture";
    }

    @Override
    public ComponentUpdate getUpdate(int systemID) {

        TextureUpdate update = new TextureUpdate(animations.get(currentAnimation).getTexture());
        systems.put(systemID, update.textureName);
        return update;
    }

    @Override
    public IComponent loadFromXml(Node node) {
        TextureComponent result = new TextureComponent();
        NodeList nodeList = node.getChildNodes();
        for(int i = 0; i < nodeList.getLength(); i++)
        {
            if(nodeList.item(i).getNodeType() == Node.ELEMENT_NODE)
            {
                switch(nodeList.item(i).getNodeName()) {
                    case "name":
                        NodeList itemNodeList = nodeList.item(i).getChildNodes();
                        for (int j = 0; j < itemNodeList.getLength(); j++) {
                            if (itemNodeList.item(j).getNodeType() == Node.TEXT_NODE) {
                                Animation animation = new Animation();
                                animation.addTexture(itemNodeList.item(j).getNodeValue());
                                animation.setName(itemNodeList.item(j).getNodeValue());
                                result.animations.put(animation.getName(), animation);
                                result.currentAnimation = itemNodeList.item(j).getNodeValue();

                            }
                        }
                        break;
                    case "animation":
                        NodeList imageList = nodeList.item(i).getChildNodes();
                        Animation animation = new Animation();
                        for (int j = 0; j < imageList.getLength(); j++) {
                           if(imageList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                               String nodeValue = "";
                               NodeList textNodeList = imageList.item(j).getChildNodes();
                               for(int k = 0; k < textNodeList.getLength(); k++) {
                                   if(textNodeList.item(k).getNodeType() == Node.TEXT_NODE) {
                                       nodeValue = textNodeList.item(k).getNodeValue();
                                   }
                               }
                               switch (imageList.item(j).getNodeName()) {
                                   case "image":
                                       animation.addTexture(nodeValue);
                                       break;
                                   case "name":
                                       animation.setName(nodeValue);
                                       break;
                                   case "delay":
                                       animation.setDelay(Double.parseDouble(nodeValue));
                                       break;
                               }
                           }
                        }
                        result.animations.put(animation.getName(), animation);
                        result.currentAnimation = animation.getName();
                        break;
                }
            }
        }
        return result;
    }
}
