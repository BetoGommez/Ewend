package com.albertogomez.ewend.input;

public interface GameKeyInputListener {
    public void keyPressed(final InputManager inputManager,final GameKeys key);
    public boolean keyUp(final InputManager inputManager,final GameKeys key);

}
