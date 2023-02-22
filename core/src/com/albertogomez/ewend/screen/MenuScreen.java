package com.albertogomez.ewend.screen;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.input.GameKeys;
import com.albertogomez.ewend.input.InputManager;
import com.albertogomez.ewend.ui.MenuUI;

public class MenuScreen extends AbstractScreen<MenuUI> {


    /**
     * Constructor of the Abstract Screen
     *
     * @param context Main Launcher class
     */
    public MenuScreen(EwendLauncher context) {
        super(context);
    }

    @Override
    public boolean keyUp(InputManager inputManager, GameKeys key) {
        return false;
    }

    @Override
    protected MenuUI getScreenUI(EwendLauncher context) {
        return null;
    }

    @Override
    public void render(float delta) {

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
}
