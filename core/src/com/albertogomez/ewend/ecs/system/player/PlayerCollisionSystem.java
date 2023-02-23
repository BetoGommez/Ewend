package com.albertogomez.ewend.ecs.system.player;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.WorldContactListener;
import com.albertogomez.ewend.audio.AudioType;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.*;
import com.albertogomez.ewend.ecs.components.player.PlayerComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;

public class PlayerCollisionSystem extends IteratingSystem implements WorldContactListener.PlayerCollisionListener {

    private final EwendLauncher context;
    public PlayerCollisionSystem(EwendLauncher context) {
        super(Family.all(PlayerComponent.class).get());
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
                gameObject.add(new DespawnObjectComponent());
                context.getAudioManager().playAudio(AudioType.FIREFLY_TOUCH);
                break;
            case LAMP:
                context.getAudioManager().playAudio(AudioType.LAMP_TOUCH);
                break;
        }
    }
}
