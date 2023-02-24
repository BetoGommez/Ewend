package com.albertogomez.ewend.screen;

import com.badlogic.gdx.Screen;

/**
 * Has the different type of screens with enum making reference to each class (Ex: GAME=GameScreen.class).
 * @author Alberto Gómez
 */
public enum ScreenType {
    MENU(MenuScreen.class),
    GAME(GameScreen.class),
    LOADING(LoadingScreen.class);

    /**
     * Saves a screen that extends from AbstractScreen
     * @see AbstractScreen
     */
    private final Class<? extends AbstractScreen> screenClass;

    /**
     * Creates the screen
     * @param screenClass Screen extending from AbstractScreen
     */
    ScreenType(Class<? extends AbstractScreen> screenClass) {
        this.screenClass = screenClass;
    }

    public Class<? extends Screen> getScreenClass() {
        return screenClass;
    }
}
