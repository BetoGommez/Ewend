package com.albertogomez.ewend.ecs.system;

import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;

/**
 * Handles the entity light working
 * @author Alberto Gómez
 */
public class LightSystem extends IteratingSystem {

    /**
     * Constructor that indicates which entities to process
     */
    public LightSystem(){
        super(Family.all(B2DComponent.class).get());
    }

    /**
     * Does the light animation process
     * @param entity The current Entity being processed
     * @param deltaTime The delta time between the last and current frame
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final B2DComponent b2DComponent = ECSEngine.b2dCmpMapper.get(entity);
        if(b2DComponent.light != null && b2DComponent.lightFluctuationDistance > 0){
            b2DComponent.lightFluctuationTime += (b2DComponent.lightFluctuationSpeed * deltaTime);
            if(b2DComponent.lightFluctuationTime> MathUtils.PI2){
                b2DComponent.lightFluctuationTime = 0;
            }
            b2DComponent.light.setDistance(b2DComponent.lightDistance + MathUtils.sin(b2DComponent.lightFluctuationTime) * b2DComponent.lightFluctuationDistance);

        }
    }
}
