package com.albertogomez.ewend.input;

import com.badlogic.gdx.Input;

public enum GameKeys {
    LEFT(Input.Keys.A),
    RIGHT(Input.Keys.D),
    JUMP(Input.Keys.SPACE),
    ATTACK(Input.Keys.F),
    LOAD(Input.Keys.SHIFT_LEFT),
    INTERACT(Input.Keys.E);

    final int[] keyCode;

    GameKeys(final int... keyCode) {
        this.keyCode = keyCode;
    }

    public int[] getKeyCode() {
        return keyCode;
    }
}
