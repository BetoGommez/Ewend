package com.albertogomez.ewend.screen;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.audio.AudioType;
import com.albertogomez.ewend.input.GameKeys;
import com.albertogomez.ewend.input.InputManager;
import com.albertogomez.ewend.map.MapType;
import com.albertogomez.ewend.view.LoadingUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;

/**
 * Represent the screen when the game is loading
 */
public class LoadingScreen extends AbstractScreen<LoadingUI> {

    private final AssetManager assetManager;
    private boolean isMusicLoaded;
    public LoadingScreen(final EwendLauncher context) {
        super(context);

        this.assetManager = context.getAssetManager();

        //load characters and effects
        assetManager.load("character/character_effects.atlas", TextureAtlas.class);

        //load maps
        for(final MapType mapType : MapType.values()){
            assetManager.load(mapType.getFilePath(),TiledMap.class);
        }

        //load audio
        isMusicLoaded=false;
        for(final AudioType audioType: AudioType.values()){
            assetManager.load(audioType.getFilePath(), audioType.isMusic()? Music.class: Sound.class);
        }


    }

    @Override
    public void show() {
        super.show();


    }

    @Override
    public void hide() {
        super.hide();
        audioManager.stopCurrentMusic();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        assetManager.update();
        screenUI.setProgress(assetManager.getProgress());

        if(!isMusicLoaded&& assetManager.isLoaded(AudioType.LEVEL.getFilePath())){
            isMusicLoaded=true;
            audioManager.playAudio(AudioType.LEVEL);
        }

    }

    @Override
    protected LoadingUI getScreenUI(EwendLauncher context) {

        return new LoadingUI(context);
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

    @Override
    public void keyPressed(InputManager inputManager, GameKeys key) {
        if(assetManager.getProgress()>=1){
            audioManager.playAudio(AudioType.SELECT);
            context.setScreen(ScreenType.GAME);
        }
    }

    @Override
    public boolean keyUp(InputManager inputManager, GameKeys key) {
        return false;
    }


}
