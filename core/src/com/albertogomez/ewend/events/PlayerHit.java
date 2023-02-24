package com.albertogomez.ewend.events;

import com.badlogic.gdx.scenes.scene2d.Event;

/**
 * Even fired when player has been hit
 * @author Alberto Gómez
 */
public class PlayerHit extends Event {

    /**
     * Damage taken
     */
    public float damage=0;

    /**
     * Constructor
     * @param damage Damage taken
     */
    public PlayerHit(float damage) {
        this.damage = damage;
    }
}