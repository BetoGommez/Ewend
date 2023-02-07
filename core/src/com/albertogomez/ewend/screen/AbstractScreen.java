package com.albertogomez.ewend.screen;

import box2dLight.RayHandler;
import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.audio.AudioManager;
import com.albertogomez.ewend.input.GameKeys;
import com.albertogomez.ewend.input.GameKeyInputListener;
import com.albertogomez.ewend.input.InputManager;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Screen interface for all game screens
 * @see com.badlogic.gdx.Screen
 */
public abstract class AbstractScreen<T extends Table> implements Screen, GameKeyInputListener {
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
    protected final RayHandler rayHandler;
    protected final InputManager manager;

    protected final AudioManager audioManager;

    /**
     * Constructor of the Abstract Screen
     * @param context Main Launcher class
     */
    public AbstractScreen(EwendLauncher context) {
        this.context = context;
        this.viewport =context.getScreenViewport();
        box2DDebugRenderer = context.getBox2DDebugRenderer();
        world = context.getWorld();
        manager = context.getInputManager();
        stage = context.getStage();
        screenUI = getScreenUI(context);
        rayHandler = context.getRayHandler();
        audioManager = context.getAudioManager();
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
        rayHandler.useCustomViewport(viewport.getScreenX(),viewport.getScreenY(), viewport.getScreenWidth(),viewport.getScreenHeight());
    }

    @Override
    public void show() {
        manager.addInputListener(this);
        stage.addActor(screenUI);
    }

    @Override
    public void hide() {
        manager.removeInputListener(this);
        stage.getRoot().removeActor(screenUI);
    }



    protected abstract T getScreenUI(final EwendLauncher context);

    @Override
    public void keyPressed(InputManager inputManager, GameKeys key) {

    }


}
