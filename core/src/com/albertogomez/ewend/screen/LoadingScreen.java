package com.albertogomez.ewend.screen;

import com.albertogomez.ewend.EwendLauncher;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Represent the screen when the game is loading
 */
public class LoadingScreen extends AbstractScreen{

    private final AssetManager assetManager;
    public LoadingScreen(final EwendLauncher context) {
        super(context);

        this.assetManager = context.getAssetManager();
        assetManager.load("maps/mapa.tmx", TiledMap.class);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,1,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(assetManager.update()){
            context.setScreen(ScreenType.GAME);
        }
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
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
