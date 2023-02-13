package com.albertogomez.ewend.ecs.system;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.components.DeadComponent;
import com.albertogomez.ewend.events.PlayerDied;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public class DeadSystem extends IteratingSystem implements EventListener {

    private final EwendLauncher context;
    public DeadSystem(EwendLauncher context) {
        super(Family.all(DeadComponent.class).get());
        this.context=context;
        context.getStage().addListener(this);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        context.getStage().getRoot().fire(new PlayerDied("none"));

    }



    @Override
    public boolean handle(Event event) {
        return false;
    }


}
