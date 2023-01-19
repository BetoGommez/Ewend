package com.albertogomez.ewend.screen;

import com.badlogic.gdx.Screen;

/**
 * Has the different type of screens with enum making reference to each class (Ex: GAME=GameScreen.class).
 */
public enum ScreenType {
    GAME(GameScreen.class),
    LOADING(LoadingScreen.class);

    private final Class<? extends Screen> screenClass;

    ScreenType(Class<? extends Screen> screenClass) {
        this.screenClass = screenClass;
    }

    public Class<? extends Screen> getScreenClass() {
        return screenClass;
    }
}
