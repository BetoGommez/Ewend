package com.albertogomez.ewend.ecs.system;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.audio.AudioType;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.AnimationComponent;
import com.albertogomez.ewend.ecs.components.DeadComponent;
import com.albertogomez.ewend.ecs.components.LifeComponent;
import com.albertogomez.ewend.ecs.components.player.PlayerComponent;
import com.albertogomez.ewend.ecs.components.player.PlayerState;
import com.albertogomez.ewend.events.EnemyDied;
import com.albertogomez.ewend.events.PlayerDied;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

/**
 * Handles the entities dead states
 * @author Alberto Gómez
 */
public class DeadSystem extends IteratingSystem implements EventListener {

    /**
     * Game main class
     */
    private final EwendLauncher context;


    /**
     * Constructor that indicates which entities to process
     * @param context
     */
    public DeadSystem(EwendLauncher context) {
        super(Family.all(DeadComponent.class, AnimationComponent.class, LifeComponent.class).get());
        this.context = context;
        context.getStage().addListener(this);
    }

    /**
     * Removes the entities and handle the dead previous actions
     * @param entity The current Entity being processed
     * @param deltaTime The delta time between the last and current frame
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        LifeComponent lifeComponent = ECSEngine.lifeCmpMapper.get(entity);
        PlayerComponent playerComponent=null;
        if (( playerComponent=ECSEngine.playerCmpMapper.get(entity)) != null ) {
            if(playerComponent.playerState!=PlayerState.DEAD){
                ECSEngine.aniCmpMapper.get(entity).aniTime=0;
                ECSEngine.b2dCmpMapper.get(entity).body.setLinearVelocity(0,0);
                context.getAudioManager().playAudio(AudioType.EWEND_DEAD);

                context.getStage().getRoot().fire(new PlayerDied());
                playerComponent.playerState = PlayerState.DEAD;
            }
        } else {
            context.getAudioManager().playAudio(AudioType.SHEEP_DEAD);
            context.getStage().getRoot().fire(new EnemyDied(lifeComponent.fury));
            entity.remove(DeadComponent.class);
            entity.removeAll();

        }
    }

    /**
     * Incoming event handling
     * @param event Event executed
     * @return Always true
     */
    @Override
    public boolean handle(Event event) {
        return true;
    }


}
