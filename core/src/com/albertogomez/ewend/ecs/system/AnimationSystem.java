package com.albertogomez.ewend.ecs.system;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.AnimationComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

/**
 *
 * @author Alberto Gómez
 */
public class AnimationSystem extends IteratingSystem {

    /**
     * Constructor that indicates which entities to process
     * @param context Game main class
     */
    public AnimationSystem(final EwendLauncher context){
        super(Family.all(AnimationComponent.class).get());
    }

    /**
     * Adds time to the animation being done
     * @param entity The current Entity being processed
     * @param deltaTime The delta time between the last and current frame
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final AnimationComponent animationComponent = ECSEngine.aniCmpMapper.get(entity);

        if (animationComponent.aniType!=null){
            animationComponent.aniTime+=deltaTime;
        }

    }
}
