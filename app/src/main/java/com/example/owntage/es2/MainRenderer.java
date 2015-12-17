package com.example.owntage.es2;


import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Handler;
import android.util.Log;

import com.example.owntage.es2.controllers.LevelLoader;
import com.example.owntage.es2.controllers.PlayerController;
import com.example.owntage.es2.game_logic.ActorFactory;
import com.example.owntage.es2.game_logic.Event;
import com.example.owntage.es2.game_logic.GameLogic;
import com.example.owntage.es2.game_logic.components.ClassicModeEvent;
import com.example.owntage.es2.game_logic.components.MoveEvent;
import com.example.owntage.es2.game_logic.components.PhysicsComponent;
import com.example.owntage.es2.game_logic.components.WeaponEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class MainRenderer implements GLSurfaceView.Renderer {

    Shader shader;
    String vertexShaderCode;
    String fragmentShaderCode;
    ButtonManager buttonManager;
    PlayerController playerController;
    MainActivity mainActivity;

    GameLogic gameLogic;
    ActorFactory actorFactory;
    RenderSystem renderSystem;
    LevelLoader levelLoader;

    public static float screenWidth;
    public static float screenHeight;
    public static float cameraX;
    public static float cameraY;
    int levelNumber = 1;

    float angle = 0.0f;




    public MainRenderer(String vertexShaderCode, String fragmentShaderCode, MainActivity mainActivity, int levelNumber) {
        this.vertexShaderCode = vertexShaderCode;
        this.fragmentShaderCode = fragmentShaderCode;
        this.mainActivity = mainActivity;
        this.levelNumber = levelNumber;
    }

    public void onUp(float x, float y, int touchID) {
        buttonManager.onTouchUp((x - screenWidth * 0.5f) / 64.0f, (screenHeight * 0.5f - y) / 64.0f, touchID);
    }

    public void onDown(float x, float y, int touchID) {

        buttonManager.onTouchDown((x - screenWidth * 0.5f) / 64.0f, (screenHeight * 0.5f - y) / 64.0f, touchID);
    }

    public void onMove(float x, float y, int touchID) {

        buttonManager.onTouchMove((x - screenWidth * 0.5f) / 64.0f, (screenHeight * 0.5f - y) / 64.0f, touchID);
    }

    private void onFinish(int score) {

        mainActivity.onGameFinished(score);
    }


    @Override
    public void onDrawFrame(GL10 arg0) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        shader.setColor(1.0f, 0.9f, 1.0f, 1.0f);
        shader.updateCamera();
        shader.updateProjection();

        PhysicsComponent.step();

        if(playerController.getIsJumping()) {
            playerController.jump();
        }

        gameLogic.onEvent(new Event("timer", true, 0));

        renderSystem.getUpdates();
        levelLoader.drawLevel();
        renderSystem.draw();
        buttonManager.drawAll();
        if(renderSystem.isFinished()) {
            onFinish(renderSystem.getScore());
        }
    }

    @Override
    public void onSurfaceChanged(GL10 arg0, int arg1, int arg2) {
        GLES20.glClearColor(0.15f, 0.05f, 0.2f, 1.0f);
        Matrix.setIdentityM(shader.getProjection(), 0);
        Matrix.scaleM(shader.getProjection(), 0, 1.0f / (float) arg1 * 128.0f, 1.0f / (float) arg2 * 128.0f, 1.0f);

        screenWidth = (float) arg1;
        screenHeight = (float) arg2;
        if(playerController == null) {
            Texture texture = new Texture(mainActivity.getResources(), "button");
            playerController = new PlayerController(shader, texture, buttonManager, gameLogic, "main_actor", levelLoader.getSpawnX(), levelLoader.getSpawnY());
            ClassicModeEvent classicModeEvent = new ClassicModeEvent(playerController.getActorID(), levelLoader.getGameModeID());
            gameLogic.onEvent(classicModeEvent);
            renderSystem = new RenderSystem(shader, texture, gameLogic, playerController.getActorID());
        }
    }



    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        PhysicsComponent.initWorld();
        actorFactory = new ActorFactory(mainActivity.getResources());
        gameLogic = new GameLogic(actorFactory);
        shader = new Shader(vertexShaderCode, fragmentShaderCode);
        levelLoader = new LevelLoader(shader, gameLogic, "level_tile", "level_object", levelNumber);
        buttonManager = new ButtonManager(shader);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glEnable(GLES20.GL_TEXTURE);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }
}
