package com.albertogomez.ewend.events;

import com.badlogic.gdx.scenes.scene2d.Event;

/**
 * Even fired when player health has changed
 * @author Alberto Gómez
 */
public class PlayerHealthChanged extends Event {
    /**
     * Player health
     */
    public float health;

    /**
     * Constructor
     * @param health Player health
     */
    public PlayerHealthChanged(float health) {
        this.health = health;
    }
}
