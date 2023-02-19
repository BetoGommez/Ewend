package com.albertogomez.ewend.ecs.system;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.AnimationComponent;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.DeadComponent;
import com.albertogomez.ewend.ecs.components.LifeComponent;
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
        DeadComponent deadComponent = ECSEngine.deadCmpMapper.get(entity);
        AnimationComponent animationComponent = ECSEngine.aniCmpMapper.get(entity);
        LifeComponent lifeComponent = ECSEngine.lifeCmpMapper.get(entity);

        if (ECSEngine.playerCmpMapper.get(entity) != null) {
            animationComponent.aniType = AnimationType.PLAYER_DEAD;
            context.getStage().getRoot().fire(new PlayerDied("none"));
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
