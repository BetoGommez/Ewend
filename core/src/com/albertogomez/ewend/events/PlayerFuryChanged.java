package com.albertogomez.ewend.events;

import com.badlogic.gdx.scenes.scene2d.Event;

/**
 * Even fired when player fury chenge
 * @author Alberto Gómez
 */
public class PlayerFuryChanged extends Event {
    /**
     * Player actual mana
     */
    public float mana;

    /**
     * Constructor
     * @param mana Actual mana
     */
    public PlayerFuryChanged(float mana) {
        this.mana = mana;
    }
}
