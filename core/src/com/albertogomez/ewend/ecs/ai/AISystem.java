package com.albertogomez.ewend.ecs.ai;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.enemy.EnemyComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;

import static javax.swing.UIManager.get;

public class AISystem extends IteratingSystem {

    public AISystem(EwendLauncher context) {
        super(Family.all(AIComponent.class, B2DComponent.class, EnemyComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        B2DComponent enemyB2D = ECSEngine.b2dCmpMapper.get(entity);
        Body enemyBody = enemyB2D.body;
        EnemyComponent enemyComp = ECSEngine.eneObjCmpMapper.get(entity);
        AIComponent aiComp = ECSEngine.aiCmoMapper.get(entity);
        aiComp.idleDelayAccum += deltaTime;

        switch (aiComp.state) {
            case IDLE:
                    movementTrackerIdle(enemyBody,enemyComp,aiComp);
                break;
            case RUNNING:
                break;
            case ATTACKING:
                break;
        }

    }

    private void movementTrackerIdle(Body enemyBody,EnemyComponent enemyComp,AIComponent aiComp) {
        float maxPositionX = aiComp.maxDistanceFactor + aiComp.initialPosition.x;
        float minPositionX = aiComp.initialPosition.x - aiComp.maxDistanceFactor;
        int oldDirection;
        if (aiComp.idleDelay < aiComp.idleDelayAccum) {
            oldDirection = aiComp.direction;
            aiComp.direction = (int) (Math.random() * 4 - 2);
            switch (aiComp.direction) {
                case 0:
                    aiComp.idleDelayAccum = -1;
                    break;
                case 1:
                    aiComp.idleDelayAccum = -4;
                    if (oldDirection == 1) {
                        aiComp.direction = -0;
                    }
                    break;
                case -1:
                    aiComp.idleDelayAccum = -4;
                    if (oldDirection == -1) {
                        aiComp.direction = 0;
                    }
                    break;
                default:
                    aiComp.direction = 0;
                    break;
            }
        }

        if (enemyBody.getPosition().x > maxPositionX) {
            aiComp.direction = -1;
        } else if (minPositionX > enemyBody.getPosition().x) {
            aiComp.direction = 1;
        }
        enemyBody.applyLinearImpulse(enemyComp.speed.x * aiComp.direction - enemyBody.getLinearVelocity().x, 0,
                enemyBody.getWorldCenter().x, enemyBody.getWorldCenter().y, true);

    }
}
