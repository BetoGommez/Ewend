package com.albertogomez.ewend.ecs.system;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.WorldContactListener;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.GameObjectComponent;
import com.albertogomez.ewend.ecs.components.PlayerComponent;
import com.albertogomez.ewend.ecs.components.RemoveComponent;
import com.albertogomez.ewend.map.GameObjectType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;

public class PlayerCollisionSystem extends IteratingSystem implements WorldContactListener.PlayerCollisionListener {


    public PlayerCollisionSystem(EwendLauncher context) {
        super(Family.all(RemoveComponent.class).get());
        context.getWcLstnr().addPlayerCollisionListener(this);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //getEngine().removeEntity(entity);
    }

    @Override
    public void playerCollision(Entity player, Entity gameObject) {
        final GameObjectComponent gameObjCmp = ECSEngine.gameObjCmpMapper.get(gameObject);

        final Body playerBody =  ECSEngine.b2dCmpMapper.get(player).body;
        switch (gameObjCmp.type){
            case FIREFLY:
                break;
            case LAMP:

                break;
        }
    }
}
