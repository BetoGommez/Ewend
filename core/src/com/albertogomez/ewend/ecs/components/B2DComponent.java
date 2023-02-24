package com.albertogomez.ewend.ecs.components;

import box2dLight.Light;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

/**
 * Component that represents the body and light of the entity to process
 * @author Alberto Gómez
 */
public class B2DComponent implements Component, Pool.Poolable  {

    /**
     * Entity body
     */
    public Body body;
    /**
     * Entity width
     */
    public float width;
    /**
     * Entity height
     */
    public float height;
    /**
     * Represents where the entity is facing, if 1 facing right, -1 facing left
     */
    public int orientation=1;
    /**
     * Light element of the entity
     */
    public Light light;
    /**
     * Light distance
     */
    public float lightDistance;
    /**
     * Distance that the light fluctuation travels
     */
    public float lightFluctuationDistance;
    /**
     * Time the light lasts to fluctuate from the init to the max distance
     */
    public float lightFluctuationTime;
    /**
     * How fast the light fluctuation travel
     */
    public float lightFluctuationSpeed;
    /**
     * Position of the entity to draw
     */
    public Vector2 renderPosition = new Vector2();

    /**
     * Set all values to its default
     */
    @Override
    public void reset() {
        lightDistance=0;
        lightFluctuationDistance = 0;
        lightFluctuationTime = 0;
        lightFluctuationSpeed = 0;

        if(light!=null){
            light.remove(true);
            light = null;
        }

        if(body!=null){
            body.getWorld().destroyBody(body);
            body=null;
        }
        width = height = 0;
        renderPosition.set(0,0);
        orientation = 1;
    }
}
