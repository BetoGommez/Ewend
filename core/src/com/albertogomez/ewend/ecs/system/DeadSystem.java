package com.albertogomez.ewend.ecs.system;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.AnimationComponent;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.DeadComponent;
import com.albertogomez.ewend.ecs.components.LifeComponent;
import com.albertogomez.ewend.ecs.components.player.PlayerComponent;
import com.albertogomez.ewend.ecs.components.player.PlayerState;
import com.albertogomez.ewend.ecs.system.player.PlayerMovementSystem;
import com.albertogomez.ewend.events.EnemyDied;
import com.albertogomez.ewend.events.PlayerDied;
import com.albertogomez.ewend.view.AnimationType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public class DeadSystem extends IteratingSystem implements EventListener {

    private final EwendLauncher context;

    public DeadSystem(EwendLauncher context) {
        super(Family.all(DeadComponent.class, AnimationComponent.class, LifeComponent.class).get());
        this.context = context;
        context.getStage().addListener(this);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        LifeComponent lifeComponent = ECSEngine.lifeCmpMapper.get(entity);
        PlayerComponent playerComponent=null;
        if (( playerComponent=ECSEngine.playerCmpMapper.get(entity)) != null ) {
            if(playerComponent.playerState!=PlayerState.DEAD){
                ECSEngine.aniCmpMapper.get(entity).aniTime=0;
                ECSEngine.b2dCmpMapper.get(entity).body.setLinearVelocity(0,0);
                context.getStage().getRoot().fire(new PlayerDied(playerComponent));
                playerComponent.playerState = PlayerState.DEAD;
            }
        } else {
            context.getStage().getRoot().fire(new EnemyDied(lifeComponent.mana));
            entity.removeAll();

        }
    }


    @Override
    public boolean handle(Event event) {
        return false;
    }


}
