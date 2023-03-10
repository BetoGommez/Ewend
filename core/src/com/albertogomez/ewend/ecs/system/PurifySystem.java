package com.albertogomez.ewend.ecs.system;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.PurifyComponent;
import com.albertogomez.ewend.ecs.components.player.PlayerComponent;
import com.albertogomez.ewend.ecs.components.player.PlayerState;
import com.albertogomez.ewend.events.PurifyEvent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 *
 * @author Alberto Gómez
 */
public class PurifySystem extends IteratingSystem {

    /**
     * Game main class
     */
    private final EwendLauncher context;

    /**
     * Constructor that indicates which entities to process
     * @param context Game main class
     */
    public PurifySystem(EwendLauncher context) {
        super(Family.all(PurifyComponent.class, PlayerComponent.class).get());
        this.context = context;
    }

    /**
     * Handles the purifying state, if the time elapsed is greater than the necessary starts the life add and fury remove
     * @param entity The current Entity being processed
     * @param deltaTime The delta time between the last and current frame
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final PlayerComponent playerComponent = ECSEngine.playerCmpMapper.get(entity);
        final PurifyComponent purifyComponent = ECSEngine.purifyCmpMapper.get(entity);

        if(purifyComponent.isPuryfing){
            if(context.getConfig().Acceloremeter){
                if(Gdx.input.getAccelerometerZ()<7||Gdx.input.getAccelerometerZ()>13){
                    purifyAction(playerComponent,deltaTime,purifyComponent);
                }else {
                    if (playerComponent.milisecAccum > 0.5f) {
                        purifyComponent.purifyingTimeAccum = 0;
                        playerComponent.playerState = PlayerState.IDLE;
                    }
                }
            }else{
                purifyAction(playerComponent,deltaTime,purifyComponent);
            }

        }
    }

    /**
     * Sets the player pruify state and fires the purify event
     * @param playerComponent Player Component
     * @param deltaTime Time elapsed
     * @param purifyComponent Purify Component
     */
    public void purifyAction(PlayerComponent playerComponent, float deltaTime, PurifyComponent purifyComponent){
        playerComponent.milisecAccum=0;
        purifyComponent.purifyingTimeAccum += deltaTime;
        playerComponent.playerState = PlayerState.PURIFYING;
        if(purifyComponent.purifyingTimeAccum>purifyComponent.purifyingTimeActivation){
            context.getStage().getRoot().fire(new PurifyEvent(-0.5f,0.25f));
        }
    }
}
