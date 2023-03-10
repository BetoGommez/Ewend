package com.albertogomez.ewend.ecs.system.player;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.WorldContactListener;
import com.albertogomez.ewend.audio.AudioType;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.*;
import com.albertogomez.ewend.ecs.components.player.PlayerComponent;
import com.albertogomez.ewend.events.LevelWon;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;

/**
 *
 * @author Alberto Gómez
 */
public class PlayerObjectCollisionSystem extends IteratingSystem implements WorldContactListener.PlayerObjectCollisionListener {

    /**
     * Main game class
     */
    private final EwendLauncher context;

    /**
     * Constructor that indicates which entities to process
     * @param context
     */
    public PlayerObjectCollisionSystem(EwendLauncher context) {
        super(Family.all(PlayerComponent.class).get());
        context.getWcLstnr().addPlayerCollisionListener(this);
        this.context= context;
    }

    /**
     * Process the entity
     * @param entity The current Entity being processed
     * @param deltaTime The delta time between the last and current frame
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    /**
     * Executed on player collision with objects
     * @param player Player entity
     * @param gameObject Game Object entity
     */
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
                context.getPreferenceManager().saveTakenFireflys(playerComponent.takenFireflys);
                break;
            case LAMP:
                context.getAudioManager().playAudio(AudioType.LAMP_TOUCH);
                context.getStage().getRoot().fire(new LevelWon());
                break;
        }
    }
}
