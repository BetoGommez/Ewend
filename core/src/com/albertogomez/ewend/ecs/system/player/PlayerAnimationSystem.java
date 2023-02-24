package com.albertogomez.ewend.ecs.system.player;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.AnimationComponent;
import com.albertogomez.ewend.ecs.components.AttackComponent;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.PurifyComponent;
import com.albertogomez.ewend.ecs.components.player.PlayerComponent;
import com.albertogomez.ewend.ecs.components.player.PlayerState;
import com.albertogomez.ewend.view.AnimationType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import static com.albertogomez.ewend.constants.Constants.UNIT_SCALE;

/**
 *
 * @author Alberto Gómez
 */
public class PlayerAnimationSystem extends IteratingSystem {

    /**
     * Player B2DComponent for handling position and body
     */
    public static B2DComponent playerB2dComp;

    /**
     * Constructor that indicates which entities to process
     */
    public PlayerAnimationSystem() {

        super(Family.all(AnimationComponent.class, PlayerComponent.class, B2DComponent.class, AttackComponent.class).get());

    }

    /**
     * Sets the player animation depending on his actual playerState
     * @param entity The current Entity being processed
     * @param deltaTime The delta time between the last and current frame
     */
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
            case PURIFYING:
                final PurifyComponent purifyComponent = ECSEngine.purifyCmpMapper.get(entity);
                if(purifyComponent.isPuryfing){
                    animationComponent.aniTime = purifyComponent.purifyingTimeAccum;
                    if(purifyComponent.purifyingTimeAccum> purifyComponent.purifyingTimeActivation){
                        aniType = AnimationType.PLAYER_CHARGING;
                    }else{
                        aniType = AnimationType.PLAYER_START_CHARGING;

                    }
                }
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
                animationComponent.aniTime = attackComponent.attackDelayAccum;
                aniType=AnimationType.PLAYER_ATTACK;
                break;
            case DASHING:
                aniType = AnimationType.PLAYER_DASH;
                break;
            case DEAD:
                aniType = AnimationType.PLAYER_DEAD;
                b2DComponent.body.setLinearVelocity(0,0);
                break;
            case DAMAGED:
                aniType = AnimationType.PLAYER_DAMAGED;
                break;
        }
        animationSet(animationComponent,aniType);
    }

    /**
     * Sets the animation with right rescaling cause of the different animation frame sizes
     * @param aniComp Player animation component
     * @param animationType Animation to be setted on AnimationComponent
     */
    private void animationSet(AnimationComponent aniComp, AnimationType animationType){
        aniComp.width = animationType.getWidth()*UNIT_SCALE/4.5f*2;
        aniComp.height = animationType.getHeight()*UNIT_SCALE/4.5f*2;
        aniComp.aniType=animationType;
    }
}
