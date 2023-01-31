package com.albertogomez.ewend.screen;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ui.LoadingUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Screen interface for all game screens
 * @see com.badlogic.gdx.Screen
 */
public abstract class AbstractScreen<T extends Table> implements Screen {
    /**
     * It's the main class launcher
     */
    protected final EwendLauncher context;
    /**
     * Viewport of the screen
     */
    protected final FitViewport viewport;



    protected final World world;

    protected final Box2DDebugRenderer box2DDebugRenderer;

    protected final Stage stage;

    protected final T screenUI;

    /**
     * Constructor of the Abstract Screen
     * @param context Main Launcher class
     */
    public AbstractScreen(EwendLauncher context) {
        this.context = context;
        this.viewport =context.getScreenViewport();
        box2DDebugRenderer = context.getBox2DDebugRenderer();
        world = context.getWorld();

        stage = context.getStage();
        screenUI = getScreenUI(context);
    }

    /**
     * Resize the game to its size
     * @param width Width of screen
     * @param height Height of screen
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
        stage.getViewport().update(width,height,true);
    }

    @Override
    public void show() {
        stage.addActor(screenUI);
    }

    @Override
    public void hide() {

        stage.getRoot().removeActor(screenUI);
    }



    protected abstract T getScreenUI(final EwendLauncher context);

}
