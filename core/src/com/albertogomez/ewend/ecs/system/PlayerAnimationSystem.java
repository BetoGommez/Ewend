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

    public static B2DComponent playerB2dComp;
    public PlayerAnimationSystem(final EwendLauncher context) {

        super(Family.all(AnimationComponent.class, PlayerComponent.class, B2DComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final B2DComponent b2DComponent = ECSEngine.b2dCmpMapper.get(entity);
        final AnimationComponent animationComponent = ECSEngine.aniCmpMapper.get(entity);
        final PlayerComponent playerComponent = ECSEngine.playerCmpMapper.get(entity);
        playerB2dComp = b2DComponent;
        //orientation calc
        if (b2DComponent.body.getLinearVelocity().x > 0) {
            b2DComponent.orientation = 1;
        } else if(b2DComponent.body.getLinearVelocity().x < 0) {
            b2DComponent.orientation = -1;
        }

        if (b2DComponent.body.getLinearVelocity().y<0.05f&&b2DComponent.body.getLinearVelocity().y>-0.05f
                &&(b2DComponent.body.getLinearVelocity().x<0.1f&&b2DComponent.body.getLinearVelocity().x>-0.1f)) {
            //player doesn't move
            animationComponent.aniType = AnimationType.PLAYER_IDLE;
        } else if (b2DComponent.body.getLinearVelocity().x != 0&&
                (b2DComponent.body.getLinearVelocity().y<0.5f&&b2DComponent.body.getLinearVelocity().y>-0.3f)&&playerComponent.touchingGround) {
            //player moves and is on earth
            animationComponent.aniType = AnimationType.PLAYER_RUNNING;
        }else if(b2DComponent.body.getLinearVelocity().y!=0&& !playerComponent.touchingGround){
            if (b2DComponent.body.getLinearVelocity().y > 0.1) {
                //starts jump
                animationComponent.aniType = AnimationType.PLAYER_JUMP_START;
            } else if (b2DComponent.body.getLinearVelocity().y < -0.1) {
                //land
                animationComponent.aniType = AnimationType.PLAYER_LANDING;
            }
        }



    }
}
