package com.albertogomez.ewend.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 *  Component that handle the player state purifying
 * @author Alberto Gómez
 */
public class PurifyComponent implements Component, Pool.Poolable {

    /**
     * Time accumulator
     */
    public float purifyingTimeAccum;
    /**
     * Time that the player lasts to start the effective fury remove and health add
     */
    public float purifyingTimeActivation;
    /**
     * Represents if the player is ordered to purify
     */
    public boolean isPuryfing;

    /**
     * Sets values to default
     */
    @Override
    public void reset() {
        purifyingTimeActivation=0;
        purifyingTimeAccum=0;
    }
}