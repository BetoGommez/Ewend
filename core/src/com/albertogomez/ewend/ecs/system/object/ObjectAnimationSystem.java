package com.albertogomez.ewend.ecs.system.object;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.AnimationComponent;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.GameObjectComponent;
import com.albertogomez.ewend.view.AnimationType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class ObjectAnimationSystem extends IteratingSystem {
    final RayHandler rayHandler;

    public ObjectAnimationSystem(EwendLauncher context) {
        super(Family.all(GameObjectComponent.class, AnimationComponent.class, B2DComponent.class).get());
        rayHandler = context.getRayHandler();

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final GameObjectComponent gameObjectComponent = ECSEngine.gameObjCmpMapper.get(entity);
        final AnimationComponent animationComponent = ECSEngine.aniCmpMapper.get(entity);
        final B2DComponent b2DComponent = ECSEngine.b2dCmpMapper.get(entity);
        //orientation calc

        switch (gameObjectComponent.type) {
            case LAMP:
                if (gameObjectComponent.touched && b2DComponent.light == null) {
                    b2DComponent.renderPosition = new Vector2(b2DComponent.renderPosition.x - animationComponent.width*2 , b2DComponent.renderPosition.y - animationComponent.height*1.3f);
                    animationComponent.width = animationComponent.width * 4;
                    animationComponent.height = animationComponent.height * 3f;
                    animationComponent.aniType = AnimationType.LAMP_EFFECT;
                    b2DComponent.lightDistance = 3;
                    b2DComponent.lightFluctuationSpeed = 2;
                    b2DComponent.lightFluctuationDistance = 0.6f;

                    b2DComponent.light = new PointLight(rayHandler, 32, new Color(0,0.1f, 0.4f, 1f), 0, b2DComponent.body.getPosition().x, b2DComponent.body.getPosition().y);
                    b2DComponent.light.attachToBody(b2DComponent.body);

                }

                break;
            case FIREFLY:
                if(b2DComponent.light==null){
                    b2DComponent.renderPosition = new Vector2(b2DComponent.renderPosition.x , b2DComponent.renderPosition.y);
                    animationComponent.aniType = AnimationType.FIREFLY_EFFECT;
                    b2DComponent.light = new PointLight(rayHandler, 32, new Color(0,0.1f, 0.4f, 1f), 0, b2DComponent.body.getPosition().x, b2DComponent.body.getPosition().y);
                    b2DComponent.lightDistance = 3;
                    b2DComponent.lightFluctuationDistance = 2;
                }
                break;
        }
    }


}
