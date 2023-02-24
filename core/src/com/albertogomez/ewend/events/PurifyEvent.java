package com.albertogomez.ewend.events;

import com.badlogic.gdx.scenes.scene2d.Event;

/**
 * Even fired when purify is being effective
 * @author Alberto Gómez
 */
public class PurifyEvent extends Event {
    /**
     * Fury to remove
     */
    public float furyRemove;
    /**
     * Health to add
     */
    public float healthAdd;

    /**
     * Constructor that sets the values
     * @param furyRemove Fury to remove
     * @param healthAdd Health to add
     */
    public PurifyEvent(float furyRemove,float healthAdd) {
        this.furyRemove = furyRemove;
        this.healthAdd = healthAdd;
    }
}
