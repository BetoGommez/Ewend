package com.albertogomez.ewend.ecs.system;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.ai.AIComponent;
import com.albertogomez.ewend.ecs.ai.AIState;
import com.albertogomez.ewend.ecs.ai.AISystem;
import com.albertogomez.ewend.ecs.components.AnimationComponent;
import com.albertogomez.ewend.ecs.components.AttackComponent;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.PlayerComponent;
import com.albertogomez.ewend.ecs.components.enemy.EnemyComponent;
import com.albertogomez.ewend.view.AnimationType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import static com.albertogomez.ewend.view.AnimationType.SHEEP_ATTACK;
import static com.albertogomez.ewend.view.AnimationType.SHEEP_IDLE;

public class EnemyAnimationSystem extends IteratingSystem {
    public EnemyAnimationSystem(final EwendLauncher context) {

        super(Family.all(AnimationComponent.class, EnemyComponent.class, B2DComponent.class, AIComponent.class,AttackComponent.class).get());
    }

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


        if(aiComponent.state== AIState.ATTACKING){
                final AttackComponent attackComponent = ECSEngine.attCmpMapper.get(entity);
             if(attackComponent.delay/2  > attackComponent.delayAccum&&!aiComponent.attacked){
                 animationComponent.aniTime=attackComponent.delayAccum;
                 animationComponent.aniType=SHEEP_ATTACK;
             }

        }else if (aiComponent.state==AIState.RUNNING) {
            //enemy doesn't move
            animationComponent.aniType = AnimationType.SHEEP_RUN;
        } else if (aiComponent.state==AIState.IDLE) {
            //enemy  moves and is on earth
            if(b2DComponent.body.getLinearVelocity().x!=0){
                animationComponent.aniType = AnimationType.SHEEP_WALK;
            }else{
                animationComponent.aniType = AnimationType.SHEEP_IDLE;
            }
        }





    }
}
