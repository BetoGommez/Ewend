package com.albertogomez.ewend.screen;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ui.LoadingUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Represent the screen when the game is loading
 */
public class LoadingScreen extends AbstractScreen<LoadingUI>{

    private final AssetManager assetManager;
    public LoadingScreen(final EwendLauncher context) {
        super(context);

        this.assetManager = context.getAssetManager();
        assetManager.load("maps/mapa.tmx", TiledMap.class);


    }




    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(assetManager.update()){
            //context.setScreen(ScreenType.GAME);
        }
        screenUI.setProgress(assetManager.getProgress());

    }

    @Override
    protected LoadingUI getScreenUI(Skin skin) {
        return new LoadingUI(stage,skin);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
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
}
