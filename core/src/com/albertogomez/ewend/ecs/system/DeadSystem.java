package com.albertogomez.ewend.ecs.system;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.components.DeadComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class DeadSystem extends IteratingSystem {

    private final EwendLauncher context;
    public DeadSystem(EwendLauncher context) {
        super(Family.all(DeadComponent.class).get());
        this.context=context;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        context.getEcsEngine().removeEntity(entity);
    }
}
