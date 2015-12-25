package com.example.owntage.es2;

import android.opengl.Matrix;
import android.util.Log;

import com.example.owntage.es2.game_logic.ActorUpdate;
import com.example.owntage.es2.game_logic.ComponentUpdate;
import com.example.owntage.es2.game_logic.GameLogic;
import com.example.owntage.es2.game_logic.DeleteActorUpdate;
import com.example.owntage.es2.game_logic.components.BillboardUpdate;
import com.example.owntage.es2.game_logic.components.GameModeUpdate;
import com.example.owntage.es2.game_logic.components.HealthUpdate;
import com.example.owntage.es2.game_logic.components.MoveUpdate;
import com.example.owntage.es2.game_logic.components.SentryGunUpdate;
import com.example.owntage.es2.game_logic.components.TextureUpdate;
import com.example.owntage.es2.game_logic.components.WeaponUpdate;

import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Created by Owntage on 10/13/2015.
 */

class DrawableActor {
    Quad quad;
    Quad tripodQuad;
    Shader shader;
    float lastCameraX = 0.0f;
    float lastCameraY = 0.0f;
    boolean main = false;
    boolean isGameMode = false;
    int score = 0;
    boolean finished = false;

    TreeMap<String, Texture> textures;

    Quad traceQuad;
    Quad reloadQuad;
    Quad healthQuad;
    Quad billboardQuad;


    public DrawableActor(Shader shader, TreeMap<String, Texture> textures) {
        quad = new Quad(shader, textures.get("brick"), 1.0f, 1.0f);
        this.shader = shader;
        this.textures = textures;
    }


    public void onUpdate(ActorUpdate update) {
        for(ComponentUpdate componentUpdate : update.updates) {
           switch(componentUpdate.name) {
               case "move":
                   MoveUpdate moveUpdate = (MoveUpdate) componentUpdate;
                   quad.setPosition(moveUpdate.x, moveUpdate.y);
                   quad.setScale(moveUpdate.scaleX, moveUpdate.scaleY);
                   break;
               case "texture":
                   TextureUpdate textureUpdate = (TextureUpdate) componentUpdate;
                   //Log.e("update", "textureUpdate: " + textureUpdate.textureName);
                   Texture texture = textures.get(textureUpdate.textureName);
                   if(texture == null) {
                       texture = new Texture(MainActivity.CONTEXT.getResources(), textureUpdate.textureName);
                       textures.put(textureUpdate.textureName, texture);
                   }
                   quad.setTexture(texture);
                   break;
               case "weapon":
                   WeaponUpdate weaponUpdate = (WeaponUpdate) componentUpdate;
                   if(traceQuad == null) {
                       traceQuad = new Quad(shader, textures.get("trace"), weaponUpdate.length, 1.0f);
                       reloadQuad = new Quad(shader, textures.get("reload"), 1.0f, 0.1f);
                   }
                   traceQuad.setAngle(weaponUpdate.angle / 3.1416f * 180.0f);
                   float l = weaponUpdate.length / 2.0f;
                   traceQuad.setPosition(quad.x + l * (float) Math.cos(weaponUpdate.angle), quad.y + l * (float) Math.sin(weaponUpdate.angle));
                   traceQuad.setScale(weaponUpdate.length, 1.0f - weaponUpdate.shootingPeriod / weaponUpdate.maxShootingPeriod);

                   reloadQuad.setPosition(quad.x, quad.y + quad.scaleY / 2.0f + 0.25f);
                   reloadQuad.setScale(weaponUpdate.reloadTime / weaponUpdate.maxReloadTime, 0.5f);
                   break;
               case "sentry":
                   SentryGunUpdate sentryGunUpdate = (SentryGunUpdate) componentUpdate;
                   if(tripodQuad == null) {
                       Texture tripodTexture = new Texture(MainActivity.CONTEXT.getResources(), sentryGunUpdate.tripodTexture);
                       tripodQuad = new Quad(shader, tripodTexture, 1.0f, 1.0f);
                   }
                   quad.setAngle((float) Math.toDegrees(sentryGunUpdate.angle));
                   break;
               case "health":
                   HealthUpdate healthUpdate = (HealthUpdate) componentUpdate;
                   if(healthQuad == null) {
                       healthQuad = new Quad(shader, textures.get("health"), 1.0f, 0.5f);
                   }
                   healthQuad.scaleX = Math.max(2.0f * healthUpdate.currentHealth / healthUpdate.health, 0.0f);
                   break;
               case "billboard":
                   BillboardUpdate billboardUpdate = (BillboardUpdate) componentUpdate;
                   if(billboardQuad == null) {
                       Texture billboardTexture = new Texture(MainActivity.CONTEXT.getResources(), billboardUpdate.billboardTexture);
                       billboardQuad = new Quad(shader, billboardTexture, 4.0f, 2.0f);
                   }
                   float d = billboardUpdate.displayTime - billboardUpdate.timeSinceCollided;
                   if(d < 0.3f) {
                       billboardQuad.scaleY = Math.max(d / 0.3f * 4.0f, 0);
                   } else {
                       billboardQuad.scaleY = 4.0f;
                   }

                   break;
               case "game_mode":
                   isGameMode = true;
                   GameModeUpdate gameModeUpdate = (GameModeUpdate) componentUpdate;
                   score = gameModeUpdate.score;
                   if(gameModeUpdate.finished) {
                       finished = true;
                   }
                   break;
           }
        }
    }

    public void draw() {
        if(main) {
            Matrix.setIdentityM(shader.getCamera(), 0);
            MainRenderer.cameraX +=  (quad.x - MainRenderer.cameraX) / 10.0f;
            MainRenderer.cameraY += (quad.y - MainRenderer.cameraY) / 10.0f;
            Matrix.translateM(shader.getCamera(), 0, -MainRenderer.cameraX, -MainRenderer.cameraY, 0.0f);
            shader.updateCamera();
        }
        if(traceQuad != null) {
            if(traceQuad.scaleY < 0.1f) {
                traceQuad.scaleY = 0.0f;
            }
            if(reloadQuad.scaleX > 0.9f) {
                reloadQuad.scaleX = 0.0f;
            }
            traceQuad.draw(true);
            reloadQuad.draw(true);
        }

        if(tripodQuad != null) {
            tripodQuad.x = quad.x;
            tripodQuad.y = quad.y;
            tripodQuad.draw(true);
        }
        if(billboardQuad != null) {
            billboardQuad.x = quad.x;
            billboardQuad.y = quad.y + quad.scaleY / 2.0f + billboardQuad.scaleY / 2.0f;
            billboardQuad.draw(true);
        }
        quad.draw(true);
        if(healthQuad != null) {
            shader.setColor(1.0f, 1.0f, 1.0f, 0.5f);
            healthQuad.x = quad.x;
            healthQuad.y = quad.y + quad.scaleY / 2.0f + 0.75f;
            healthQuad.draw(true);
            shader.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}


public class RenderSystem {
    Shader shader;
    Texture texture;
    GameLogic gameLogic;
    int systemID;
    int mainActor;
    boolean finished = false;
    int score;
    TreeMap<String, Texture> textures = new TreeMap<>();
    TreeMap<Integer, DrawableActor> actors = new TreeMap<>();


    public RenderSystem(Shader shader, Texture texture, GameLogic gameLogic, int mainActor) {
        this.shader = shader;
        this.gameLogic = gameLogic;
        this.texture = texture;
        this.mainActor = mainActor;
        systemID = gameLogic.registerSystem();


        //default textures
        textures.put("brick", new Texture(MainActivity.CONTEXT.getResources(), "brick"));
        textures.put("trace", new Texture(MainActivity.CONTEXT.getResources(), "trace"));
        textures.put("reload", new Texture(MainActivity.CONTEXT.getResources(), "reload"));
        textures.put("health", new Texture(MainActivity.CONTEXT.getResources(), "health"));
    }

    public void getUpdates() {
        LinkedList<ActorUpdate> updates = gameLogic.getUpdates(systemID);
        for(ActorUpdate actorUpdate : updates) {

            if(actorUpdate.number == -1) {
                DeleteActorUpdate deleteActorUpdate = (DeleteActorUpdate) actorUpdate;
                actors.remove(deleteActorUpdate.deleteID);
                continue;
            }

            DrawableActor actor = actors.get(actorUpdate.number);
            if(actor == null) {

                actor = new DrawableActor(shader, textures);
                actors.put(actorUpdate.number, actor);
                if(actorUpdate.number == mainActor) {
                    actor.main = true;
                }
            }
            actor.onUpdate(actorUpdate);
            if(actor.isGameMode) {
                score = actor.score;
                finished = actor.finished;
            }
        }
    }

    public void draw() {
        for(DrawableActor actor : actors.values()) {
            actor.draw();
        }
    }

    public int getScore() {
        return score;
    }

    public boolean isFinished() {
        return finished;
    }
}
