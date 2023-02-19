package com.albertogomez.ewend.ecs.system;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.WorldContactListener;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.*;
import com.albertogomez.ewend.events.FireflyTaken;
import com.albertogomez.ewend.map.GameObjectType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;

public class PlayerCollisionSystem extends IteratingSystem implements WorldContactListener.PlayerCollisionListener {

    EwendLauncher context;
    public PlayerCollisionSystem(EwendLauncher context) {
        super(Family.all(RemoveComponent.class).get());
        context.getWcLstnr().addPlayerCollisionListener(this);
        this.context= context;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //getEngine().removeEntity(entity);
    }

    @Override
    public void playerCollision(Entity player, Entity gameObject) {
        final GameObjectComponent gameObjCmp = ECSEngine.gameObjCmpMapper.get(gameObject);
        final PlayerComponent playerComponent = ECSEngine.playerCmpMapper.get(player);
        final Body playerBody =  ECSEngine.b2dCmpMapper.get(player).body;
        switch (gameObjCmp.type){
            case FIREFLY:
                playerComponent.takenFireflys.add(ECSEngine.gameObjCmpMapper.get(gameObject).index);
                context.getStage().getRoot().fire(new FireflyTaken(playerComponent));
                gameObject.add(new DespawnObjectComponent());

                break;
        }
    }
}
