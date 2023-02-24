package com.albertogomez.ewend.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;

/**
 * Game key input manager
 * @author Alberto Gómez
 */
public class InputManager extends InputAdapter {
    /**
     * All in game keys existing
     */
    private final GameKeys[] keyMapping;
    /**
     * Which key is pressed
     */
    private final boolean[] keyState;

    /**
     * All game key listener used to notify them
     */
    private final Array<GameKeyInputListener> listeners;


    /**
     * Constructor that create all the keys planned to exist on game
     */
    public InputManager() {
        this.keyMapping = new GameKeys[256];
        for (final GameKeys gameKey : GameKeys.values()) {
            for (final int code : gameKey.keyCode) {
                keyMapping[code] = gameKey;

            }
        }
        keyState = new boolean[GameKeys.values().length];
        listeners = new Array<GameKeyInputListener>();

    }

    /**
     * Key has been released
     * @param keycode one of the constants in {@link Input.Keys}
     * @return Always false
     */
    @Override
    public boolean keyUp(int keycode) {

        final GameKeys gameKey = keyMapping[keycode];
        if (gameKey == null) {
            //no mapping -> nothing to do
            return false;
        }
        notifyKeyUp(gameKey);
        return false;
    }

    /**
     * Key has been pressed
     * @param keycode one of the constants in {@link Input.Keys}
     * @return if not null true
     */
    @Override
    public boolean keyDown(int keycode) {
        final GameKeys gameKey = keyMapping[keycode];
        if (gameKey == null) {
            //no mapping -> nothing to do
            return false;
        }
        notifyKeyDown(gameKey);
        return true;
    }


    /**
     * Notify all listener which key has been executed
     * @param gameKey Game key executed
     */
    public void notifyKeyDown(GameKeys gameKey) {
        keyState[gameKey.ordinal()] = true;
        for (GameKeyInputListener listener : listeners) {
            listener.keyPressed(this, gameKey);
        }
    }

    /**
     * Notify all listener which key has been released
     * @param gameKey Game key released
     */
    public void notifyKeyUp(GameKeys gameKey) {
        keyState[gameKey.ordinal()] = false;

        for (final GameKeyInputListener listener : listeners) {

            listener.keyUp(this, gameKey);
        }

    }


    public boolean isKeyPressed(final GameKeys gameKey) {
        return keyState[gameKey.ordinal()];
    }

    public void addInputListener(final GameKeyInputListener listener) {
        listeners.add(listener);
    }

    public void removeInputListener(final GameKeyInputListener listener) {
        listeners.removeValue(listener, true);
    }


}
