package com.albertogomez.ewend.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Component that represents that the entity has dead and needs to be deleted
 * @author Alberto Gómez
 */
public class DeadComponent implements Component, Pool.Poolable {

    /**
     * Time that lasts the entity to dissapear
     */
    public float deadDelay=2;

    /**
     * Constructor of the component
     * @param deadDelay
     */
    public DeadComponent(float deadDelay) {
        this.deadDelay = deadDelay;
    }

    /**
     * Set all values to 0
     */
    @Override
    public void reset() {
        deadDelay=0;
    }
}
