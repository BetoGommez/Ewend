package com.albertogomez.ewend.screen;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.audio.AudioType;
import com.albertogomez.ewend.input.GameKeys;
import com.albertogomez.ewend.input.InputManager;
import com.albertogomez.ewend.map.MapType;
import com.albertogomez.ewend.view.AnimationType;
import com.albertogomez.ewend.ui.LoadingUI;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;

import static com.albertogomez.ewend.constants.Constants.BACKGROUND_PATH;
import static com.albertogomez.ewend.constants.Constants.PLAYER_SPRITE_PATH;

/**
 * Represent the screen when the game is loading
 * @author Alberto Gómez
 */
public class LoadingScreen extends AbstractScreen<LoadingUI> {

    /**
     * Game asset manager
     */
    private final AssetManager assetManager;

    /**
     * Creates the loading screen and loads all assets
     * @param context Game Main class
     */
    public LoadingScreen(final EwendLauncher context) {
        super(context);

        this.assetManager = context.getAssetManager();


        //load characters and effects
        assetManager.load(PLAYER_SPRITE_PATH, TextureAtlas.class);
        for (final AnimationType animationType : AnimationType.values()) {
            assetManager.load(animationType.getAtlasPath(), TextureAtlas.class);
        }

        assetManager.update();
        //load background

        for (int i = 1; i < 5; i++) {
            assetManager.load(BACKGROUND_PATH + i + ".png", Texture.class);
        }

        assetManager.update();
        //load maps
        for (final MapType mapType : MapType.values()) {
            assetManager.load(mapType.getFilePath(), TiledMap.class);
        }
        assetManager.update();

    }

    /**
     * Done when the screen is shown
     */
    @Override
    public void show() {
        super.show();


    }

    /**
     * Done when hidding screen, also stops the menu music
     */
    @Override
    public void hide() {
        super.hide();
        audioManager.stopCurrentMusic();
    }

    /**
     * Updates the assetManager progress and tells the overlay how much it has
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        assetManager.update();
        screenUI.setProgress(assetManager.getProgress(),delta);
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
        if (assetManager.getProgress() >= 1) {

            audioManager.playAudio(AudioType.MENU_BUTTON);
            context.setScreen(ScreenType.GAME);
        }
    }

    @Override
    public boolean keyUp(InputManager inputManager, GameKeys key) {
        return false;
    }


}
