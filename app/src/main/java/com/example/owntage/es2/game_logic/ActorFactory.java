package com.example.owntage.es2.game_logic;

import android.content.res.Resources;
import android.util.Log;

import com.example.owntage.es2.R;
import com.example.owntage.es2.game_logic.components.AnimationSwappingComponent;
import com.example.owntage.es2.game_logic.components.BillboardComponent;
import com.example.owntage.es2.game_logic.components.CheckpointComponent;
import com.example.owntage.es2.game_logic.components.ClassicModeComponent;
import com.example.owntage.es2.game_logic.components.CoinComponent;
import com.example.owntage.es2.game_logic.components.MonsterMoveComponent;
import com.example.owntage.es2.game_logic.components.SignalComponent;
import com.example.owntage.es2.game_logic.components.HealthComponent;
import com.example.owntage.es2.game_logic.components.MoveComponent;
import com.example.owntage.es2.game_logic.components.PhysicsComponent;
import com.example.owntage.es2.game_logic.components.SentryGunComponent;
import com.example.owntage.es2.game_logic.components.TextureComponent;
import com.example.owntage.es2.game_logic.components.WeaponComponent;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Owntage on 10/10/2015.
 */
public class ActorFactory {

    TreeMap<String, Node> actors;

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

    private IComponent loadComponent(Node node) {
        String name = node.getNodeName();
        IComponent component;
        switch(name) {
            case "move":
                component = new MoveComponent();
                break;
            case "physics":
                component = new PhysicsComponent();
                break;
            case "texture":
                component = new TextureComponent();
                break;
            case "coin":
                component = new CoinComponent();
                break;
            case "animation_swap":
                component = new AnimationSwappingComponent();
                break;
            case "weapon":
                component = new WeaponComponent();
                break;
            case "sentry":
                component = new SentryGunComponent();
                break;
            case "health":
                component = new HealthComponent();
                break;
            case "billboard":
                component = new BillboardComponent();
                break;
            case "checkpoint":
                component = new CheckpointComponent();
                break;
            case "classic_mode":
                component = new ClassicModeComponent();
                break;
            case "signal":
                component = new SignalComponent();
                break;
            case "monster":
                component = new MonsterMoveComponent();
                break;
            default:
                return null;
        }
        return component.loadFromXml(node);
    }



    public ActorFactory(Resources resources) {

        actors = new TreeMap<>();

        Document properties = loadDocument(resources.openRawResource(R.raw.properties));
        NodeList actorsList = properties.getElementsByTagName("actor");
        for(int i = 0; i < actorsList.getLength(); i++) {
            String id;
            NamedNodeMap attributes = actorsList.item(i).getAttributes();
            Node idNode = attributes.getNamedItem("id");
            id = idNode.getNodeValue();
            actors.put(id, actorsList.item(i));
        }
    }
    public Actor createActor(String id) {
        Actor result = new Actor();
        Node actorNode = actors.get(id);
        NodeList components = actorNode.getChildNodes();
        for(int i = 0; i < components.getLength(); i++) {
            if(components.item(i).getNodeType() == Node.ELEMENT_NODE) {
                result.components.add(loadComponent(components.item(i)));
            }
        }
        return result;
    }
}
