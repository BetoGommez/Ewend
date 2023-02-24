package com.albertogomez.ewend.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

/**
 * Component that represent the entity damage system
 * @author Alberto Gómez
 */
public class AttackComponent implements Component, Pool.Poolable {

    /**
     * Damage that the entity or player deals
     */
    public float damage;
    /**
     * On true initiates the action of attack
     */
    public boolean attacking;

    /**
     * Attack delay time
     */
    public float attackDelay =1f;
    /**
     * Time accumulator
     */
    public float attackDelayAccum =0f;

    /**
     * Body for attack box that generates the damage on entity touched
     */
    public Body attackBox;
    /**
     * Attack box body width
     */
    public float attackHitboxWidth;
    /**
     * Attack box body height
     */
    public float attackHitboxHeight;

    /**
     *Reset the values to default
     */
    @Override
    public void reset() {
        damage=0;
        attacking=false;
        attackHitboxHeight=0;
        attackHitboxWidth=0;
    }
    
}
