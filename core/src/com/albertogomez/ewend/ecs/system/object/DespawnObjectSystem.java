package com.albertogomez.ewend.ecs.system.object;

import com.albertogomez.ewend.ecs.components.DespawnObjectComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

/**
 * Class that remove game objects from the game
 * @author Alberto Gómez
 */
public class DespawnObjectSystem extends IteratingSystem {

    /**
     * Constructor that indicates that entity has to have the DespawnObjectComponent to be processed
     */
    public DespawnObjectSystem() {
        super(Family.all(DespawnObjectComponent.class).get());
    }

    /**
     * Does the entity removeAll()
     * @param entity The current Entity being processed
     * @param deltaTime The delta time between the last and current frame
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        entity.removeAll();
    }
}
