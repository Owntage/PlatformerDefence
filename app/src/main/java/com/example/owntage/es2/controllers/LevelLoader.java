package com.example.owntage.es2.controllers;

import android.util.Log;

import com.example.owntage.es2.MainActivity;
import com.example.owntage.es2.QuadMesh;
import com.example.owntage.es2.R;
import com.example.owntage.es2.Shader;
import com.example.owntage.es2.Texture;
import com.example.owntage.es2.game_logic.Event;
import com.example.owntage.es2.game_logic.GameLogic;
import com.example.owntage.es2.game_logic.components.ClassicModeEvent;
import com.example.owntage.es2.game_logic.components.GameLogicEvent;
import com.example.owntage.es2.game_logic.components.PositionSetEvent;
import com.example.owntage.es2.game_logic.components.TextureEvent;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Owntage on 10/23/2015.
 */
public class LevelLoader {

    private float spawnX;
    private float spawnY;
    private int gameModeID;
    QuadMesh levelMesh;

    private Document loadDocument(InputStream stream) {
        Document result;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = factory.newDocumentBuilder();
            result = db.parse(stream);
            return result;
        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }
    }

    public float getSpawnX() {
        return spawnX;
    }

    public float getSpawnY() {
        return spawnY;
    }

    public int getGameModeID() {
        return gameModeID;
    }

    public void drawLevel() {
        levelMesh.draw();
    }

    public LevelLoader(Shader shader, GameLogic gameLogic, String graphicActorID, String physicsActorID, Integer levelNumber) {
        String levelName = "level" + levelNumber.toString();
        int resID = MainActivity.CONTEXT.getResources().getIdentifier(levelName, "raw", MainActivity.PACKAGE_NAME);
        InputStream inputStream = MainActivity.CONTEXT.getResources().openRawResource(resID);
        Document document = loadDocument(inputStream);

        //getting tile images

        Node tileset = document.getElementsByTagName("tileset").item(0);
        NodeList tilesetChildren = tileset.getChildNodes();
        int tilesetWidth = 0;
        int tilesetHeight = 0;
        int tilesetOffset = 1;
        String imageSource = null;

        NamedNodeMap tilesetAttributes = tileset.getAttributes();
        tilesetOffset = Integer.parseInt(tilesetAttributes.getNamedItem("firstgid").getNodeValue());
        int tileWidth = Integer.parseInt(tilesetAttributes.getNamedItem("tilewidth").getNodeValue());
        int tileHeight = Integer.parseInt(tilesetAttributes.getNamedItem("tileheight").getNodeValue());

        for(int i = 0; i < tilesetChildren.getLength(); i++) {
            if(tilesetChildren.item(i).getNodeType() == Node.ELEMENT_NODE) {
                if(tilesetChildren.item(i).getNodeName().equals("image")) {

                    NamedNodeMap imageAttributes = tilesetChildren.item(i).getAttributes();
                    tilesetWidth = Integer.parseInt(imageAttributes.getNamedItem("width").getNodeValue());
                    tilesetHeight = Integer.parseInt(imageAttributes.getNamedItem("height").getNodeValue());
                    StringBuilder imageSourceBuilder = new StringBuilder();
                    imageSource = imageAttributes.getNamedItem("source").getNodeValue();
                    for(int j = 0; j < imageSource.length(); j++) {
                        if(imageSource.charAt(i) == '.') {
                            break;
                        } else {
                            imageSourceBuilder.append(imageSource.charAt(i));
                        }
                    }
                    imageSource = imageSourceBuilder.toString();
                    imageSource = "ts";
                }
            }
        }

        tilesetWidth /= tileWidth;
        tilesetHeight /= tileHeight;

        //creating tiles

        ArrayList<Float> coords = new ArrayList<>();
        ArrayList<Float> texCoords = new ArrayList<>();

        NodeList layerList = document.getElementsByTagName("layer");
        Node layer = layerList.item(0);
        NamedNodeMap layerAttributes = layer.getAttributes();
        int width;
        int height;
        Node widthNode = layerAttributes.getNamedItem("width");
        Node heightNode = layerAttributes.getNamedItem("height");
        width = Integer.parseInt(widthNode.getNodeValue());
        height = Integer.parseInt(heightNode.getNodeValue());

        NodeList dataNodeList = layer.getChildNodes();
        int count = 0;
        for(int i = 0; i < dataNodeList.getLength(); i++) {
            if(dataNodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                NodeList tileNodeList = dataNodeList.item(i).getChildNodes();
                for(int j = 0; j < tileNodeList.getLength(); j++) {
                    if(tileNodeList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        Node gidNode = tileNodeList.item(j).getAttributes().getNamedItem("gid");
                        int gid = Integer.parseInt(gidNode.getNodeValue());

                        if(gid != 0) {
                            gid -= tilesetOffset;
                            float posX = count % width + 0.5f;
                            float posY = 0.5f + height - (count / width);
                            float fTilesetWidth = (float) tilesetWidth;
                            float fTilesetHeight = (float) tilesetHeight;
                            float texCoordX = (float) (gid % tilesetWidth) / fTilesetWidth;
                            float texCoordY = (float) (gid / tilesetWidth) / fTilesetHeight;

                            coords.add(-0.5f + posX); coords.add(-0.5f + posY);
                            coords.add(0.5f + posX); coords.add(-0.5f + posY);
                            coords.add(0.5f + posX); coords.add(0.5f + posY);
                            coords.add(-0.5f + posX); coords.add(-0.5f + posY);
                            coords.add(0.5f + posX); coords.add(0.5f + posY);
                            coords.add(-0.5f + posX); coords.add(0.5f + posY);


                            texCoords.add(0.0f / fTilesetWidth + texCoordX); texCoords.add(1.0f / fTilesetWidth + texCoordY);
                            texCoords.add(1.0f / fTilesetWidth + texCoordX); texCoords.add(1.0f / fTilesetWidth + texCoordY);
                            texCoords.add(1.0f / fTilesetWidth + texCoordX); texCoords.add(0.0f / fTilesetHeight + texCoordY);
                            texCoords.add(0.0f / fTilesetWidth + texCoordX); texCoords.add(1.0f / fTilesetWidth + texCoordY);
                            texCoords.add(1.0f / fTilesetWidth + texCoordX); texCoords.add(0.0f / fTilesetHeight + texCoordY);
                            texCoords.add(0.0f / fTilesetWidth + texCoordX); texCoords.add(0.0f / fTilesetHeight + texCoordY);
                        }
                        count++;
                    }
                }
            }
        }

        Texture tilesetTexture = new Texture(MainActivity.CONTEXT.getResources(), imageSource);
        levelMesh = new QuadMesh(shader, tilesetTexture, coords, texCoords);

        Node objectGroup = document.getElementsByTagName("objectgroup").item(0);
        NodeList objects = objectGroup.getChildNodes();
        LinkedList<Event> checkpointEvents = new LinkedList<>();
        for(int i = 0; i < objects.getLength(); i++) {
            if(objects.item(i).getNodeType() == Node.ELEMENT_NODE) {
                NamedNodeMap objectAttributes = objects.item(i).getAttributes();
                float x = Float.parseFloat(objectAttributes.getNamedItem("x").getNodeValue());
                float y = Float.parseFloat(objectAttributes.getNamedItem("y").getNodeValue());
                Node typeNode = objectAttributes.getNamedItem("type");
                float objectWidth = Float.parseFloat(objectAttributes.getNamedItem("width").getNodeValue());
                float objectHeight = Float.parseFloat(objectAttributes.getNamedItem("height").getNodeValue());
                if(typeNode == null) {

                    int objectActor = gameLogic.createActor(physicsActorID);

                    PositionSetEvent position = new PositionSetEvent(objectWidth / 64.0f + x / 32.0f, height - objectHeight / 64.0f + 1.0f - y / 32.0f, "position_set");
                    position.global = false;
                    position.actorID = objectActor;

                    PositionSetEvent scale = new PositionSetEvent(objectWidth / 32.0f, objectHeight / 32.0f, "scale_set");
                    scale.global = false;
                    scale.actorID = objectActor;

                    gameLogic.onEvent(position);
                    gameLogic.onEvent(scale);
                } else {
                    String type = typeNode.getNodeValue();
                    Log.e("level", "object type: " + type);
                    switch(type) {
                        case "spawn":
                            spawnX = x / 32.0f;
                            spawnY = height - y / 32.0f;
                            break;
                        default:
                            int id = gameLogic.createActor(type);

                            PositionSetEvent positionSetEvent = new PositionSetEvent(objectWidth / 64.0f + x / 32.0f, height - objectHeight / 64.0f + 1.0f - y / 32.0f, "position_set");
                            positionSetEvent.global = false;
                            positionSetEvent.actorID = id;
                            gameLogic.onEvent(positionSetEvent);

                            if(type.equals("checkpoint")) {
                                PositionSetEvent checkpointSet = new PositionSetEvent(objectWidth / 64.0f + x / 32.0f, height - objectHeight / 64.0f + 1.0f - y / 32.0f, "checkpoint_set", id);
                                checkpointSet.global = true;
                                checkpointEvents.add(checkpointSet);
                            }
                            if(type.equals("classic_mode")) {
                                gameModeID = id;
                            }
                            break;
                    }
                }
            }
        }
        for(Event event : checkpointEvents) {
            gameLogic.onEvent(event);
        }
        gameLogic.onEvent(new GameLogicEvent(gameLogic));

    }
}
