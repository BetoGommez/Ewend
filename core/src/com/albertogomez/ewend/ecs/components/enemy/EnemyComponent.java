package com.albertogomez.ewend.ecs.components.enemy;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Component that defines that an entity is a enemy
 * @author Alberto Gómez
 */
public class EnemyComponent implements Component, Pool.Poolable {

    /**
     * Entity basic speed
     */
    public Vector2 speed=new Vector2();

    /**
     * Enemy type for different enemies
     */
    public EnemyType enemyType;

    /**
     * Sets values to 0 and null
     */
    @Override
    public void reset() {
        speed.set(0,0);
        enemyType=null;
    }
}
