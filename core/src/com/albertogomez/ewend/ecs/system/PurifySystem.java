package com.albertogomez.ewend.ecs.system;

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
     * Game stage
     */
    private final Stage stage;

    /**
     * Constructor that indicates which entities to process
     * @param stage Game stage
     */
    public PurifySystem(Stage stage) {
        super(Family.all(PurifyComponent.class, PlayerComponent.class).get());
        this.stage = stage;
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
            if(Gdx.input.getAccelerometerZ()<7||Gdx.input.getAccelerometerZ()>13){
                playerComponent.milisecAccum=0;
                purifyComponent.purifyingTimeAccum += deltaTime;
                playerComponent.playerState = PlayerState.PURIFYING;
                if(purifyComponent.purifyingTimeAccum>purifyComponent.purifyingTimeActivation){
                    stage.getRoot().fire(new PurifyEvent(-0.5f,0.25f));
                }
            }else{
                if(playerComponent.milisecAccum>0.5f){
                    purifyComponent.purifyingTimeAccum=0;
                    playerComponent.playerState = PlayerState.IDLE;
                }
            }
        }
    }
}
