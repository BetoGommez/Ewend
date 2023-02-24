package com.albertogomez.ewend.ecs.system.enemy;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.ai.AIComponent;
import com.albertogomez.ewend.ecs.components.AnimationComponent;
import com.albertogomez.ewend.ecs.components.AttackComponent;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.enemy.EnemyComponent;
import com.albertogomez.ewend.view.AnimationType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import static com.albertogomez.ewend.view.AnimationType.*;

/**
 *  Class that handles all enemies animations depending on the state they are
 * @author Alberto Gómez
 */
public class EnemyAnimationSystem extends IteratingSystem {
    /**
     * Constructor that indicates which entities to process
     * @param context
     */
    public EnemyAnimationSystem(final EwendLauncher context) {
        super(Family.all(AnimationComponent.class, EnemyComponent.class, B2DComponent.class, AIComponent.class,AttackComponent.class).get());
    }

    /**
     * Process the current entity switching on his state to set the corresponding animation
     * @param entity The current Entity being processed
     * @param deltaTime The delta time between the last and current frame
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final B2DComponent b2DComponent = ECSEngine.b2dCmpMapper.get(entity);
        final AnimationComponent animationComponent = ECSEngine.aniCmpMapper.get(entity);
        final AIComponent aiComponent = ECSEngine.aiCmoMapper.get(entity);

        //orientation calc
        if (b2DComponent.body.getLinearVelocity().x > 0) {
            b2DComponent.orientation = 1;
        } else if(b2DComponent.body.getLinearVelocity().x < 0) {
            b2DComponent.orientation = -1;
        }

        switch (aiComponent.state) {
            case HITTED:
                animationComponent.aniType = SHEEP_DAMAGED;
                b2DComponent.orientation = -b2DComponent.orientation;
                break;
            case TRANSFORM:
                animationComponent.aniTime= aiComponent.milisecAccum;
                animationComponent.aniType= SHEEP_TRANSFORM;
                break;
            case ATTACKING:
                final AttackComponent attackComponent = ECSEngine.attCmpMapper.get(entity);
                if (attackComponent.attackDelay / 2 > attackComponent.attackDelayAccum && !aiComponent.attacked) {
                    animationComponent.aniTime = attackComponent.attackDelayAccum;
                    animationComponent.aniType = SHEEP_ATTACK;
                }
                break;
            case RUNNING:
                animationComponent.aniType = AnimationType.SHEEP_RUN;
                break;
            case IDLE:
                if (b2DComponent.body.getLinearVelocity().x != 0) {
                    animationComponent.aniType = AnimationType.SHEEP_WALK;
                } else {
                    animationComponent.aniType = AnimationType.SHEEP_IDLE;
                }
                break;
            default:
                break;
        }
    }

}
