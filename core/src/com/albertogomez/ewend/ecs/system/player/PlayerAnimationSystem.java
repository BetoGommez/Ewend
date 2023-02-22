package com.albertogomez.ewend.ecs.system.player;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.AnimationComponent;
import com.albertogomez.ewend.ecs.components.AttackComponent;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.player.PlayerComponent;
import com.albertogomez.ewend.ecs.components.player.PlayerState;
import com.albertogomez.ewend.view.AnimationType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import static com.albertogomez.ewend.constants.Constants.UNIT_SCALE;

public class PlayerAnimationSystem extends IteratingSystem {

    public static B2DComponent playerB2dComp;
    public PlayerAnimationSystem(final EwendLauncher context) {

        super(Family.all(AnimationComponent.class, PlayerComponent.class, B2DComponent.class, AttackComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final B2DComponent b2DComponent = ECSEngine.b2dCmpMapper.get(entity);
        final AnimationComponent animationComponent = ECSEngine.aniCmpMapper.get(entity);
        final PlayerComponent playerComponent = ECSEngine.playerCmpMapper.get(entity);
        final AttackComponent attackComponent = ECSEngine.attCmpMapper.get(entity);
        playerB2dComp = b2DComponent;
        AnimationType aniType = animationComponent.aniType;
        //orientation calc


        switch (playerComponent.playerState){
            case IDLE:
                aniType = AnimationType.PLAYER_IDLE;
                break;
            case RUNNING:
                aniType = AnimationType.PLAYER_RUNNING;
                break;
            case JUMPING:
            case DOUBLE_JUMP:
                if (b2DComponent.body.getLinearVelocity().y > 0) {
                    //starts jump
                    if(playerComponent.playerState== PlayerState.JUMPING){
                        aniType = AnimationType.PLAYER_JUMP_START;
                    }else{
                        aniType = AnimationType.PLAYER_DOUBLE_JUMP;
                    }
                } else  {
                    //land
                    aniType = AnimationType.PLAYER_FALL;
                }
                break;
            case ATTACKING:
                animationComponent.aniTime = attackComponent.delayAccum;
                aniType=AnimationType.PLAYER_ATTACK;
                break;
            case DASHING:
                aniType = AnimationType.PLAYER_DASH;
                break;
            case DEAD:
                aniType = AnimationType.PLAYER_DEAD;
                break;
            case DAMAGED:
                aniType = AnimationType.PLAYER_DAMAGED;
                break;
        }
        animationSet(animationComponent,aniType);
    }

    private void animationSet(AnimationComponent aniComp, AnimationType animationType){
        aniComp.width = animationType.getWidth()*UNIT_SCALE/4.5f*2;
        aniComp.height = animationType.getHeight()*UNIT_SCALE/4.5f*2;
        aniComp.aniType=animationType;
    }
}
