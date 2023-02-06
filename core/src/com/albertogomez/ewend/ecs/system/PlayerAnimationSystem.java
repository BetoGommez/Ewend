package com.albertogomez.ewend.ecs.system;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.AnimationComponent;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.PlayerComponent;
import com.albertogomez.ewend.view.AnimationType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

public class PlayerAnimationSystem extends IteratingSystem {
    public PlayerAnimationSystem(final EwendLauncher context) {

        super(Family.all(AnimationComponent.class, PlayerComponent.class, B2DComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final B2DComponent b2DComponent = ECSEngine.b2dCmpMapper.get(entity);
        final AnimationComponent animationComponent = ECSEngine.aniCmpMapper.get(entity);

        //orientation calc
        if (b2DComponent.body.getLinearVelocity().x > 0) {
            b2DComponent.orientation = 1;
        } else if(b2DComponent.body.getLinearVelocity().x < 0) {
            b2DComponent.orientation = -1;
        }

        if (b2DComponent.body.getLinearVelocity().equals(Vector2.Zero)) {
            //player doesn't move
            animationComponent.aniType = AnimationType.PLAYER_IDLE;
        } else if (b2DComponent.body.getLinearVelocity().x != 0&&b2DComponent.body.getLinearVelocity().y==0) {
            //player moves and is on earth
            animationComponent.aniType = AnimationType.PLAYER_RUNNING;
        }else if(b2DComponent.body.getLinearVelocity().y!=0){
            if (b2DComponent.body.getLinearVelocity().y > 0) {
                //starts jump
                animationComponent.aniType = AnimationType.PLAYER_JUMP_START;
            } else if (b2DComponent.body.getLinearVelocity().y < 0) {
                //land
                animationComponent.aniType = AnimationType.PLAYER_LANDING;
            }
        }



    }
}
