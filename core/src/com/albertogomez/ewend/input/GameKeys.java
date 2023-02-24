package com.albertogomez.ewend.input;

import com.badlogic.gdx.Input;

/**
 * Enum for the game keys mapping
 * @author Alberto Gómez
 */
public enum GameKeys {
    LEFT(Input.Keys.A, Input.Keys.LEFT),
    RIGHT(Input.Keys.D, Input.Keys.RIGHT),
    JUMP(Input.Keys.W, Input.Keys.UP, Input.Keys.SPACE),
    ATTACK(Input.Keys.F),
    INTERACT(Input.Keys.E),
    PURIFY(Input.Keys.CONTROL_LEFT),

    DASH(Input.Keys.SHIFT_LEFT);

    /**
     * Game key code
     */
    final int[] keyCode;

    /**
     * Constructor that sets the key code
     * @param keyCode
     */
    GameKeys(final int... keyCode) {
        this.keyCode = keyCode;
    }

    public int[] getKeyCode() {
        return keyCode;
    }
}
