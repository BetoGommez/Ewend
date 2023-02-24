package com.albertogomez.ewend.events;

import com.badlogic.gdx.scenes.scene2d.Event;

/**
 *  Even fired when enemy died
 * @author Alberto Gómez
 */
public class EnemyDied extends Event {

    /**
     * Mana to add to player
     */
    public float mana = 0;

    /**
     * Constructor of event
     * @param mana Mana to add to player
     */
    public EnemyDied(float mana) {
        this.mana = mana;
    }
}
