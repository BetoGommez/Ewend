package com.albertogomez.ewend.ecs.components;

import com.albertogomez.ewend.view.AnimationType;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Component that represent that is an animated entity
 * @author Alberto Gómez
 */
public class AnimationComponent implements Component, Pool.Poolable{

    /**
     * Actual animation of the entity
     */
    public AnimationType aniType;
    /**
     * Time for the animation frame
     */
    public float aniTime;
    /**
     * Animation width
     */
    public float width;
    /**
     * Animation height
     */
    public float height;

    /**
     * Sets the values to its default
     */
    @Override
    public void reset() {
        aniTime=0;
        aniType=null;
        width = height = 0;
    }


}
