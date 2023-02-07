package com.albertogomez.ewend.screen;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.PreferenceManager;
import com.albertogomez.ewend.input.GameKeys;
import com.albertogomez.ewend.input.InputManager;
import com.albertogomez.ewend.map.*;
import com.albertogomez.ewend.view.GameUI;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.*;

import static com.albertogomez.ewend.constants.Constants.*;

/**
 * Screen of the gameplay
 */
public class GameScreen extends AbstractScreen<GameUI> implements MapListener {
    private final MapManager mapManager;
    private final PreferenceManager prefMgr;
    private final Entity player;

    public GameScreen(final EwendLauncher context) {
        super(context);

        //Lanza el renderizador de mapa

        mapManager = context.getMapManager();
        mapManager.setMap(MapType.MAP_1);
        prefMgr = context.getPreferenceManager();



        //SE ENCARGA EL MAPMANAGER
        player = context.getEcsEngine().createPlayer(mapManager.getCurrentMap().getStartLocation(),0.75f,0.75f);
        ////////////
    }

    @Override
    public void show() {
        super.show();

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void render(float delta) {

        if(Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
            mapManager.setMap(MapType.MAP_1);

        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            mapManager.setMap(MapType.MAP_2);

        } else if(Gdx.input.isKeyPressed(Input.Keys.NUM_3)){
            prefMgr.saveGameState(player);
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


