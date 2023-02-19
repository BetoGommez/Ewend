package com.albertogomez.ewend.ecs.system;

import com.albertogomez.ewend.ecs.components.DespawnObjectComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class DespawnObjectSystem extends IteratingSystem {

    public DespawnObjectSystem() {
        super(Family.all(DespawnObjectComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        entity.removeAll();
    }
}
