package com.albertogomez.ewend.ecs.ai;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.AttackComponent;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.enemy.EnemyComponent;
import com.albertogomez.ewend.ecs.system.PlayerAnimationSystem;
import com.albertogomez.ewend.ecs.system.PlayerCollisionSystem;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import static com.albertogomez.ewend.view.AnimationType.SHEEP_ATTACK;
import static javax.swing.UIManager.get;

public class AISystem extends IteratingSystem {

    public AISystem(EwendLauncher context) {
        super(Family.all(AIComponent.class, B2DComponent.class, EnemyComponent.class, AttackComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        B2DComponent enemyB2D = ECSEngine.b2dCmpMapper.get(entity);
        Body enemyBody = enemyB2D.body;
        AttackComponent attackComponent = ECSEngine.attCmpMapper.get(entity);
        EnemyComponent enemyComp = ECSEngine.eneObjCmpMapper.get(entity);
        AIComponent aiComp = ECSEngine.aiCmoMapper.get(entity);
        aiComp.milisecAccum += deltaTime;

        switch (aiComp.state) {
            case IDLE:
                movementTrackerIdle(enemyBody, enemyComp, aiComp);
                checkPlayerPos(aiComp, enemyB2D);
                break;
            case RUNNING:


                if(getPlayerPos().x<enemyBody.getPosition().x+enemyB2D.width*1.5f&&getPlayerPos().x>enemyBody.getPosition().x-enemyB2D.width*1.5f){

                }else{
                    if (getPlayerPos().x > enemyBody.getPosition().x) {
                        aiComp.direction = 1;
                    } else {
                        aiComp.direction = -1;
                    }
                }
                enemyBody.applyLinearImpulse(enemyComp.speed.x * 5 * aiComp.direction - enemyBody.getLinearVelocity().x,
                        enemyBody.getLinearVelocity().y, enemyBody.getWorldCenter().x, enemyBody.getWorldCenter().y, true);

                break;
            case ATTACKING:
                if (attackComponent.delayAccum > attackComponent.delay&&attackComponent.attacking==true) {
                    aiComp.state = AIState.RUNNING;
                    aiComp.attacked = false;
                    attackComponent.attacking = false;
                    enemyBody.setLinearVelocity(0, 0);

                } else {
                    if (aiComp.attacked==false) {
                        attackComponent.delayAccum = 0;
                        aiComp.attacked=true;
                    }
                    enemyBody.setLinearVelocity(0, 0);
                    ECSEngine.aniCmpMapper.get(entity).aniType = SHEEP_ATTACK;
                    ECSEngine.aniCmpMapper.get(entity).aniTime = attackComponent.delayAccum;
                    if (attackComponent.delay / 2 < attackComponent.delayAccum) {
                        attackComponent.attacking = true;
                    }


                }
                break;
        }

    }

    private void movementTrackerIdle(Body enemyBody, EnemyComponent enemyComp, AIComponent aiComp) {
        float maxPositionX = aiComp.maxDistanceFactor + aiComp.initialPosition.x;
        float minPositionX = aiComp.initialPosition.x - aiComp.maxDistanceFactor;
        int oldDirection;
        if (aiComp.idleDelay < aiComp.milisecAccum) {
            oldDirection = aiComp.direction;
            aiComp.direction = (int) (Math.random() * 4 - 2);
            switch (aiComp.direction) {
                case 0:
                    aiComp.milisecAccum = -1;
                    break;
                case 1:
                    aiComp.milisecAccum = -4;
                    if (oldDirection == 1) {
                        aiComp.direction = -0;
                    }
                    break;
                case -1:
                    aiComp.milisecAccum = -4;
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

    private void checkPlayerPos(AIComponent aiComponent, B2DComponent enemy) {
        Vector2 posPlayer = getPlayerPos();
        if (posPlayer != null) {

            if ((enemy.renderPosition.x - aiComponent.maxDistanceFactor < posPlayer.x
                    || enemy.renderPosition.x * aiComponent.maxDistanceFactor > posPlayer.x) &&
                    (enemy.renderPosition.y + aiComponent.maxDistanceFactor < posPlayer.y
                            || enemy.renderPosition.y - aiComponent.maxDistanceFactor > posPlayer.y)) {
                aiComponent.state = AIState.RUNNING;
            }

        }

    }

    private void isContained() {

    }

    private Vector2 getPlayerPos() {
        B2DComponent playerB2dComp;

        if (PlayerAnimationSystem.playerB2dComp != null) {
            playerB2dComp = PlayerAnimationSystem.playerB2dComp;
            return playerB2dComp.renderPosition;
        }

        return null;
    }
}
