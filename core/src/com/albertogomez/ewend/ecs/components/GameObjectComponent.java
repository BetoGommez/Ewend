package com.albertogomez.ewend.ecs.components;

import com.albertogomez.ewend.map.GameObjectType;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 *
 * @author Alberto Gómez
 */
public class GameObjectComponent implements Component, Pool.Poolable {
    /**
     * Represents the object type
     */
    public GameObjectType type;
    /**
     * Represents the if the entity has an animation (if not -1) and its index on atlas file
     */
    public int animationIndex;

    /**
     * Represent if the object has been touched before
     */
    public boolean touched = false;

    /**
     * Index used to represent the different gameObjects on map (In this case differences the )
     */
    public int index = -1;

    /**
     * Sets all value to default
     */
    @Override
    public void reset() {
        type = null;
        animationIndex = -1;
    }


}
