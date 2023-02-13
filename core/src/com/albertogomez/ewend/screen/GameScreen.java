package com.albertogomez.ewend.screen;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.PreferenceManager;
import com.albertogomez.ewend.events.MessageEvent;
import com.albertogomez.ewend.input.GameKeys;
import com.albertogomez.ewend.input.InputManager;
import com.albertogomez.ewend.map.*;
import com.albertogomez.ewend.view.GameUI;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.albertogomez.ewend.constants.Constants.UNIT_SCALE;

/**
 * Screen of the gameplay
 */
public class GameScreen extends AbstractScreen<GameUI> implements MapListener {
    private final MapManager mapManager;
    private final PreferenceManager prefMgr;
    private final Entity player;
    private final Stage stage;




    public GameScreen(final EwendLauncher context) {
        super(context);


        //Lanza el renderizador de mapa

        mapManager = context.getMapManager();
        mapManager.setMap(MapType.MAP_1);
        prefMgr = context.getPreferenceManager();
        stage = context.getStage();




        //SE ENCARGA EL MAPMANAGER
        player = context.getEcsEngine().createPlayer(mapManager.getCurrentMap().getStartLocation(),0.75f,0.75f);
        ////////////
    }

    public Entity getPlayer() {
        return player;
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

        if(Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
            mapManager.setMap(MapType.MAP_1);

        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            mapManager.setMap(MapType.MAP_2);

        } else if(Gdx.input.isKeyPressed(Input.Keys.NUM_3)){
            prefMgr.saveGameState(player);
        } else if(Gdx.input.isKeyPressed(Input.Keys.NUM_4)){
            prefMgr.loadGameState(player);
        }else if(Gdx.input.isKeyPressed(Input.Keys.NUM_5)){
            stage.getRoot().fire(new MessageEvent("hola"));

        }



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


