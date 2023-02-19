package com.albertogomez.ewend.screen;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.PreferenceManager;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.events.MessageEvent;
import com.albertogomez.ewend.events.ResetLevel;
import com.albertogomez.ewend.input.GameKeys;
import com.albertogomez.ewend.input.InputManager;
import com.albertogomez.ewend.map.*;
import com.albertogomez.ewend.view.GameRenderer;
import com.albertogomez.ewend.view.GameUI;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.albertogomez.ewend.constants.Constants.UNIT_SCALE;

/**
 * Screen of the gameplay
 */
public class GameScreen extends AbstractScreen<GameUI> implements MapListener, EventListener {
    private final MapManager mapManager;
    private final PreferenceManager prefMgr;

    private final Stage stage;




    public GameScreen(final EwendLauncher context) {
        super(context);
        //Lanza el renderizador de mapa
        mapManager = context.getMapManager();
        mapManager.setMap(MapType.MAP_1);
        prefMgr = context.getPreferenceManager();
        stage = context.getStage();
        context.getStage().addListener(this);

        //SE ENCARGA EL MAPMANAGER

        ////////////
    }

    public void resetLevel(){
        ECSEngine ecsEngine = context.getEcsEngine();
        ecsEngine.removeAllEntities();
        ecsEngine.clearPools();
        ecsEngine.removeAllSystems();
        mapManager.setMap(mapManager.getCurrentMapType());
        ecsEngine.addSystems();

    }


    @Override
    public void show() {
        super.show();

    }

    @Override
    public void resize(int width, int height) {
       super.resize(width,height);
    }

    @Override
    public void render(float delta) {
    }

    @Override
    public boolean handle(Event event) {
        if(event instanceof ResetLevel){
            resetLevel();
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


