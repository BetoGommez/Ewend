package com.albertogomez.ewend.input;

public interface InputListener {
    public void keyPressed(final InputManager inputManager,final GameKeys key);
    public void keyUp(final InputManager inputManager,final GameKeys key);

}
