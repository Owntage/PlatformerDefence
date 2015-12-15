package com.example.owntage.es2.controllers;

import android.widget.Button;

import com.example.owntage.es2.ButtonManager;
import com.example.owntage.es2.GameButton;
import com.example.owntage.es2.MainRenderer;
import com.example.owntage.es2.Shader;
import com.example.owntage.es2.Texture;
import com.example.owntage.es2.game_logic.Event;
import com.example.owntage.es2.game_logic.GameLogic;
import com.example.owntage.es2.game_logic.components.MoveEvent;
import com.example.owntage.es2.game_logic.components.PositionSetEvent;

/**
 * Created by Owntage on 10/16/2015.
 */
public class PlayerController {

    Shader shader;
    GameLogic gameLogic;
    int actorID;
    MoveEvent event;


    public PlayerController(Shader shader, Texture texture, ButtonManager buttonManager, GameLogic gameLogic, String id, float spawnX, float spawnY) {
        float buttonsX = -4.0f;
        float buttonsY = -2.0f;
        actorID = gameLogic.createActor(id);

        PositionSetEvent position = new PositionSetEvent(spawnX, spawnY, "position_set");
        position.global = false;
        position.actorID = actorID;
        gameLogic.onEvent(position);

        event = new MoveEvent();
        event.component = "move";
        event.global = false;
        event.actorID = actorID;

        class Callback implements GameButton.ITouchCallback {

            MoveEvent event;
            GameLogic gameLogic;
            boolean down;
            int type;

            public Callback(int type, boolean down, GameLogic gameLogic, MoveEvent event) {
                this.type = type;
                this.down = down;
                this.gameLogic = gameLogic;
                this.event = event;
            }

            @Override
            public void callback() {
                switch(type) {
                    case 0:
                        event.left = down;
                        break;
                    case 1:
                        event.right = down;
                        break;
                    case 2:
                        event.up = down;
                        break;
                    case 3:
                        event.down = down;
                        break;

                }
                gameLogic.onEvent(event);
            }
        }

        GameButton leftButton = new GameButton(shader, texture, 2.0f - MainRenderer.screenWidth / 128.0f, 1.5f - MainRenderer.screenHeight / 128.0f, 3.0f, 2.0f);
        leftButton.setDownCallback(new Callback(0, true, gameLogic, event));
        leftButton.setUpCallback(new Callback(0, false, gameLogic, event));
        GameButton rightButton = new GameButton(shader, texture, 6.0f - MainRenderer.screenWidth / 128.0f, 1.5f - MainRenderer.screenHeight / 128.0f, 3.0f, 2.0f);
        rightButton.setDownCallback(new Callback(1, true, gameLogic, event));
        rightButton.setUpCallback(new Callback(1, false, gameLogic, event));

        buttonManager.createButton(1, leftButton);
        buttonManager.createButton(2, rightButton);


        class JumpCallback implements GameButton.ITouchCallback {

            GameLogic gameLogic;
            int actorID;
            public JumpCallback(GameLogic gameLogic, int actorID) {
                this.gameLogic = gameLogic;
                this.actorID = actorID;
            }
            public void callback() {
                Event event = new Event();
                event.global = false;
                event.component = "jump";
                event.actorID = actorID;
                gameLogic.onEvent(event);
            }
        }

        GameButton jumpButton = new GameButton(shader, texture, MainRenderer.screenWidth / 128.0f - 2.0f, 1.5f - MainRenderer.screenHeight / 128.0f, 3.0f, 2.0f);
        jumpButton.setDownCallback(new JumpCallback(gameLogic, actorID));
        buttonManager.createButton(10, jumpButton);
    }

    public int getActorID() {
        return actorID;
    }

}
