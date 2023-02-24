package com.albertogomez.ewend.ecs.ai;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Component for the entities that has an AI
 * @author Alberto Gómez
 */
public class AIComponent implements Component, Pool.Poolable {

    /**
     * Defines the entity behavior state
     */
    public AIState state = AIState.IDLE;
    /**
     * Time accumulator
     */
    public float milisecAccum=0f ;
    /**
     * Delay from AI direction changes
     */
    public float idleDelay=1f ;
    /**
     * Time that the entity is knocked
     */
    public float knockTime = 1.4f;
    /**
     * When true the entity started attack action
     */
    public boolean attacked =false;
    /**
     * Maximum distance that the entity travels on idle and its view distance
     */
    public float maxDistanceFactor = 2f;
    /**
     * Sets the spawn entity position
     */
    public Vector2 initialPosition;

    /**
     * If the value is 1 the entity moves to right else to left
     */
    public int direction = 1;

    /**
     * Reset the state to IDLE
     */
    @Override
    public void reset() {
        AIState state = AIState.IDLE;

    }
}
