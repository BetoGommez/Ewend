package com.albertogomez.ewend.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Pool;

/**
 * Component for identifying the entity health
 * @author Alberto Gómez
 */
public class LifeComponent implements Component, Pool.Poolable {

    /**
     * Actual entity health
     */
    public float health;

    /**
     * True if the component comes from an enemy entity
     */
    public boolean isEnemy;
    /**
     * Value of fury that the entity has inside
     */
    public float fury;

    /**
     * Sets values to default
     */
    @Override
    public void reset() {
        health=100f;
        fury =0f;
    }

    /**
     * Apply the value incoming into the entity health that it already have
     * @param healthChange Value of health to add or remove
     */
    public void applyHealth(float healthChange){
        health +=healthChange;
    }

    /**
     * Apply the value incoming into the entity fury that it already have
     * @param furyChange Value of fury to add or remove
     */
    public void applyFury(float furyChange){
        this.fury +=furyChange;
    }
}
