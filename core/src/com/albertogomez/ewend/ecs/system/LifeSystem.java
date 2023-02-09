package com.albertogomez.ewend.ecs.system;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.AttackComponent;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.DeadComponent;
import com.albertogomez.ewend.ecs.components.LifeComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class LifeSystem extends IteratingSystem {


    private World world;

    public LifeSystem(EwendLauncher context) {
        super(Family.all(B2DComponent.class, LifeComponent.class).get());

        world = context.getWorld();

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Body entityBody = ECSEngine.b2dCmpMapper.get(entity).body;

        LifeComponent lifeComponent = ECSEngine.lifeCmpMapper.get(entity);
        if(lifeComponent.health<=0){
            entity.add(new DeadComponent());
        }

    }

}