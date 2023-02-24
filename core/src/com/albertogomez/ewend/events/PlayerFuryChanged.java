package com.albertogomez.ewend.events;

import com.badlogic.gdx.scenes.scene2d.Event;

/**
 * Even fired when player fury chenge
 * @author Alberto Gómez
 */
public class PlayerFuryChanged extends Event {
    public float mana;

    public PlayerFuryChanged(float mana) {
        this.mana = mana;
    }
}
