package com.albertogomez.ewend.screen;

import box2dLight.RayHandler;
import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.audio.AudioManager;
import com.albertogomez.ewend.input.ButtonListener;
import com.albertogomez.ewend.input.GameKeys;
import com.albertogomez.ewend.input.GameKeyInputListener;
import com.albertogomez.ewend.input.InputManager;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Screen interface for all game screens
 * @author Alberto Gómez
 */
public abstract class AbstractScreen<T extends Table> implements Screen, GameKeyInputListener {
    /**
     * Main game class
     */
    protected final EwendLauncher context;
    /**
     * Viewport of the screen
     */
    protected final ExtendViewport viewport;
    /**
     * Game world
     */
    protected final World world;
    /**
     * Renderer for collision areas
     */
    protected final Box2DDebugRenderer box2DDebugRenderer;

    /**
     * Game stage
     */
    protected final Stage stage;
    /**
     * Overlay on stage for the current screen
     */
    protected final T screenUI;
    /**
     * Map lights handler
     */
    protected final RayHandler rayHandler;
    /**
     * Game keys input handler
     */
    protected final InputManager manager;
    /**
     * Audio manager for playing sounds and music
     */
    protected final AudioManager audioManager;

    /**
     * Constructor of the Abstract Screen setting all values
     * @param context Main Launcher class
     */
    public AbstractScreen(EwendLauncher context) {
        this.context = context;
        this.viewport =context.getScreenViewport();
        box2DDebugRenderer = new Box2DDebugRenderer();
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

    /**
     * When the screen is shown adds ass listener to input manager and adds the overlay to the stage
     */
    @Override
    public void show() {
        manager.addInputListener(this);
        stage.addActor(screenUI);
    }

    /**
     * Done when the screen hides, removes the input listener and the overlay from the stage
     */
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
