package com.albertogomez.ewend.ecs.system;

import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.DeadComponent;
import com.albertogomez.ewend.ecs.components.LifeComponent;
import com.albertogomez.ewend.events.*;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Handles the entities fury and health, set its values and the dead state
 * @author Alberto Gómez
 */
public class LifeSystem extends IteratingSystem implements EventListener {

    /**
     * Fury to be added or removed on player
     */
    private float furyChange;
    /**
     * Health to be added or removed on player
     */
    private float healthChange;
    /**
     * Game stage
     */
    private Stage stage;

    /**
     * Constructor that indicates which entities to process
     * @param stage Game stage
     */
    public LifeSystem(Stage stage) {
        super(Family.all(B2DComponent.class, LifeComponent.class).get());
        this.stage = stage;
        stage.addListener(this);
    }

    /**
     * Applys the health and fury values and handles if the entity has to be set as dead
     * @param entity The current Entity being processed
     * @param deltaTime The delta time between the last and current frame
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Body entityBody = ECSEngine.b2dCmpMapper.get(entity).body;
        LifeComponent lifeComponent = ECSEngine.lifeCmpMapper.get(entity);
        if (ECSEngine.playerCmpMapper.get(entity) != null) {

            if (furyChange != 0) {
                lifeComponent.applyFury(valueFix(lifeComponent.fury, furyChange));
                stage.getRoot().fire(new PlayerFuryChanged(lifeComponent.fury));
                furyChange =0;
            }

            if (healthChange != 0) {
                lifeComponent.applyHealth(valueFix(lifeComponent.health,healthChange));
                stage.getRoot().fire(new PlayerHealthChanged(lifeComponent.health));
                healthChange=0;
            }
        }
        if (entityBody.getPosition().y < -1) {
            lifeComponent.health = 0;
        }

        if (lifeComponent.health <= 0 && entity.getComponent(DeadComponent.class) == null) {
            entity.add(new DeadComponent(5));
        }

    }

    /**
     * Fixes the value so the component variable never gets less than 0 or more than 100
     * @param initialValue Value of the component
     * @param valueToApply Value to apply on change
     * @return
     */
    private float valueFix(float initialValue, float valueToApply) {
        float fixedValue=0;
        if (initialValue + healthChange <= 100) {
            fixedValue = valueToApply;
        } else {
            if (initialValue + healthChange <= 0)
                fixedValue = -initialValue;
            else {
                fixedValue = 100 - initialValue;
            }
        }
        return fixedValue;
    }

    /**
     * Incoming event handler
     * @param event Exectued event
     * @return Always false
     */
    @Override
    public boolean handle(Event event) {
        if (event instanceof EnemyDied) {
            furyChange = ((EnemyDied) event).mana;
        } else if (event instanceof PurifyEvent) {
            PurifyEvent purifyEvent = (PurifyEvent) event;
            furyChange = purifyEvent.furyRemove;
            healthChange = purifyEvent.healthAdd;
        }else if(event instanceof PlayerHit){
            healthChange = ((PlayerHit)event).damage;
        }
        return false;
    }
}
