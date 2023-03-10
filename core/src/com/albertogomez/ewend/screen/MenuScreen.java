package com.albertogomez.ewend.screen;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.audio.AudioType;
import com.albertogomez.ewend.input.GameKeys;
import com.albertogomez.ewend.input.InputManager;
import com.albertogomez.ewend.ui.LoadingUI;
import com.albertogomez.ewend.ui.MenuUI;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 *
 * @author Alberto Gómez
 */
public class MenuScreen extends AbstractScreen<MenuUI> {


    /**
     * Game asset manager
     */
    private final AssetManager assetManager;
    /**
     * Indicates if the music has been loaded
     */
    private boolean isMusicLoaded;


    /**
     * Creates the menu skins and loads the menu music
     * @param context Main game class
     */
    public MenuScreen(EwendLauncher context) {
        super(context);
        this.assetManager = context.getAssetManager();

        //load audio
        isMusicLoaded=false;
        for(final AudioType audioType: AudioType.values()){
            assetManager.load(audioType.getFilePath(), audioType.isMusic()? Music.class: Sound.class);
        }


    }


    @Override
    public boolean keyUp(InputManager inputManager, GameKeys key) {
        return false;
    }

    @Override
    protected MenuUI getScreenUI(EwendLauncher context) {
        return new MenuUI(context);
    }

    /**
     * Done when hiding screen, also reset musicLoaded state
     */
    @Override
    public void hide() {
        super.hide();
        isMusicLoaded = false;
    }

    /**
     * Updates the asset manager state and plays the menu music when loaded
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        assetManager.update();
        if(!isMusicLoaded&& assetManager.isLoaded(AudioType.MENU_MUSIC.getFilePath())) {
            isMusicLoaded = true;
            if(context.getConfig().Music) {
                audioManager.playAudio(AudioType.MENU_MUSIC);
            }
        }
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
