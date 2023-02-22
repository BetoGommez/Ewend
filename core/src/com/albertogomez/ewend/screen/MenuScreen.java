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

public class MenuScreen extends AbstractScreen<MenuUI> {


    private final AssetManager assetManager;
    private boolean isMusicLoaded;
    private final EwendLauncher context;

    public MenuScreen(EwendLauncher context) {
        super(context);

        this.assetManager = context.getAssetManager();
        this.context = context;

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

    @Override
    public void render(float delta) {
        assetManager.update();
        if(!isMusicLoaded&& assetManager.isLoaded(AudioType.LEVEL.getFilePath())) {
            isMusicLoaded = true;
            audioManager.playAudio(AudioType.LEVEL);
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
