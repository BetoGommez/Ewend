package com.albertogomez.ewend.screen;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.audio.AudioType;
import com.albertogomez.ewend.input.GameKeys;
import com.albertogomez.ewend.input.InputManager;
import com.albertogomez.ewend.map.MapType;
import com.albertogomez.ewend.view.AnimationType;
import com.albertogomez.ewend.view.LoadingUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;

import static com.albertogomez.ewend.constants.Constants.BACKGROUND_PATH;

/**
 * Represent the screen when the game is loading
 */
public class LoadingScreen extends AbstractScreen<LoadingUI> {

    private final AssetManager assetManager;
    private final Array<Texture> backgroundLayers ;

    private boolean isMusicLoaded;

    
    public LoadingScreen(final EwendLauncher context) {
        super(context);

        this.assetManager = context.getAssetManager();
        backgroundLayers = new Array<Texture>();
        //load hud
        assetManager.load("ui/game_ui/game_hud.atlas",TextureAtlas.class);


        //load characters and effects
        assetManager.load("character/character_effects.atlas", TextureAtlas.class);
        for(final AnimationType animationType : AnimationType.values()){
            assetManager.load(animationType.getAtlasPath(),TextureAtlas.class);
        }


        //load background

        for (int i = 1; i < 5; i++) {
            assetManager.load(BACKGROUND_PATH+i+".png", Texture.class);
        }


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
        /*Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);*/

        assetManager.update();

        if(!isMusicLoaded&& assetManager.isLoaded(AudioType.LEVEL.getFilePath())){
            isMusicLoaded=true;
            audioManager.playAudio(AudioType.LEVEL);
        }
        screenUI.setProgress(assetManager.getProgress());

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
