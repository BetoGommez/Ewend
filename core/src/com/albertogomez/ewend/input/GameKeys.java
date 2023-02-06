package com.albertogomez.ewend.input;

import com.badlogic.gdx.Input;

public enum GameKeys {
    LEFT(Input.Keys.A, Input.Keys.LEFT),
    RIGHT(Input.Keys.D, Input.Keys.RIGHT),
    JUMP(Input.Keys.W, Input.Keys.UP, Input.Keys.SPACE),
    ATTACK(Input.Keys.F),
    LOAD(Input.Keys.CONTROL_LEFT),
    INTERACT(Input.Keys.E),

    DASH(Input.Keys.SHIFT_LEFT);

    final int[] keyCode;

    GameKeys(final int... keyCode) {
        this.keyCode = keyCode;
    }

    public int[] getKeyCode() {
        return keyCode;
    }
}
