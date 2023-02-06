package com.albertogomez.ewend.ecs.system;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.PlayerComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class PlayerCameraSystem extends IteratingSystem {
    private final OrthographicCamera gameCamera;
    public PlayerCameraSystem(final EwendLauncher context) {
        super(Family.all(PlayerComponent.class, B2DComponent.class).get());
        gameCamera = context.getGameCamera();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        gameCamera.position.set(ECSEngine.b2dCmpMapper.get(entity).renderPosition,0);
        gameCamera.update();
    }
}
