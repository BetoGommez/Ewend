package com.albertogomez.ewend.screen;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.PreferenceManager;
import com.albertogomez.ewend.audio.AudioType;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.events.LevelWon;
import com.albertogomez.ewend.events.PlayerDied;
import com.albertogomez.ewend.events.ResetLevel;
import com.albertogomez.ewend.events.ReturnToMenu;
import com.albertogomez.ewend.input.GameKeys;
import com.albertogomez.ewend.input.InputManager;
import com.albertogomez.ewend.map.*;
import com.albertogomez.ewend.ui.GameUI;
import com.albertogomez.ewend.ui.GameUIOverlay;
import com.albertogomez.ewend.ui.LevelEnd;
import com.albertogomez.ewend.view.GameRenderer;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Screen of the gameplay
 * @author Alberto Gómez
 */
public class GameScreen extends AbstractScreen<GameUI> implements MapListener, EventListener {

    /**
     * Game preference manager
     *
     */
    private final PreferenceManager prefMgr;
    /**
     * Game stage
     */
    private final Stage stage;
    /**
     * Overlay for the actors that need to be placed above this overlay
     */
    private final GameUIOverlay gameUIOverlay;

    /**
     * Constructor that set the values
     * @param context Game main class
     */
    public GameScreen(final EwendLauncher context) {
        super(context);

        prefMgr = context.getPreferenceManager();
        stage = context.getStage();
        gameUIOverlay = new GameUIOverlay(context,screenUI);
    }

    /**
     * Done when the screen hides, removes the input listener and the overlay from the stage
     * Also removes the overlay above this one
     */
    @Override
    public void hide() {
        super.hide();
        stage.getRoot().removeActor(gameUIOverlay);
    }

    /**
     * When the screen is shown adds ass listener to input manager and adds the overlay to the stage
     * Also adds the overlay abovee this one and creates the level
     */
    @Override
    public void show() {
        super.show();
        context.getStage().addListener(this);
        context.getStage().addListener(screenUI);
        screenUI.resetBars();

        context.getStage().addActor(gameUIOverlay);
        if(context.getConfig().Music){
            context.getAudioManager().playAudio(AudioType.LEVEL);
        }

        createLevel();
    }

    /**
     *  Creates the game level
     */
    private void createLevel(){
        context.setEcsEngine(new ECSEngine(context));
        context.setMapManager(new MapManager(context));
        context.setGameRenderer(new GameRenderer(context));
        context.mapManager.setMap(MapType.MAP_1);
    }

    /**
     * Removes all from the game level
     */
    private void removeLevel(){

        context.gameRenderer=null;
        context.mapManager.mapDispose();
        context.mapManager = null;
        context.getEcsEngine().clear();
        context.ecsEngine=null;
    }

    /**
     * Restarts the game level from the init
     */
    public void resetLevel(){
        context.getEcsEngine().clear();
        context.mapManager.setMap(context.mapManager.getCurrentMapType());
        context.getEcsEngine().addSystems();
    }

    @Override
    public void resize(int width, int height) {
       super.resize(width,height);
    }

    @Override
    public void render(float delta) {
        screenUI.draw(delta);
    }

    /**
     * Incoming event handling
     * @param event Event executed
     * @return Always false
     */
    @Override
    public boolean handle(Event event) {
        if(event instanceof ResetLevel){
            stage.getRoot().addActor(screenUI);
            stage.getRoot().addActor(gameUIOverlay);
            resetLevel();
        }else if(event instanceof PlayerDied) {
            stage.addActor(new LevelEnd(context,false));
            stage.getRoot().removeActor(screenUI);
            stage.getRoot().removeActor(gameUIOverlay);
        }else if(event instanceof ReturnToMenu){
            stage.clear();
            context.getAudioManager().stopCurrentMusic();
            removeLevel();
            context.setScreen(ScreenType.MENU);
        }else if(event instanceof LevelWon){
            stage.addActor(new LevelEnd(context,true));
            stage.getRoot().removeActor(screenUI);
            stage.getRoot().removeActor(gameUIOverlay);
        }
        return false;
    }

    @Override
    protected GameUI getScreenUI(EwendLauncher context) {
        return new GameUI(context);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
    @Override
    public void dispose() {

    }

    @Override
    public boolean keyUp(InputManager inputManager, GameKeys key) {
        return false;
    }

    @Override
    public void mapChange(Map map) {

    }
}


