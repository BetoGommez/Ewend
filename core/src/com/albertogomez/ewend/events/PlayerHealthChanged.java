package com.albertogomez.ewend.events;

import com.badlogic.gdx.scenes.scene2d.Event;

/**
 * Even fired when player health has changed
 * @author Alberto Gómez
 */
public class PlayerHealthChanged extends Event {
    public float health;

    public PlayerHealthChanged(float health) {
        this.health = health;
    }
}
