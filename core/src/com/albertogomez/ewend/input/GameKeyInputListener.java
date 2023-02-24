package com.albertogomez.ewend.input;

/**
 *  Interface with pressed keys methods
 * @author Alberto Gómez
 */
public interface GameKeyInputListener {
    /**
     * Key has been pressed
     * @param inputManager Game Input Manager that handles inputs
     * @param key Which key has been pressed
     */
    public void keyPressed(final InputManager inputManager, final GameKeys key);

    /**
     * Key has been released
     * @param inputManager Game Input Manager that handles inputs
     * @param key Which key has been released
     */
    public boolean keyUp(final InputManager inputManager, final GameKeys key);

}
