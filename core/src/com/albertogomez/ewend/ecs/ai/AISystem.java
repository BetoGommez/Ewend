package com.albertogomez.ewend.ecs.ai;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.AttackComponent;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.enemy.EnemyComponent;
import com.albertogomez.ewend.ecs.system.player.PlayerAnimationSystem;
import com.albertogomez.ewend.events.PlayerDied;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import static javax.swing.UIManager.get;

public class AISystem extends IteratingSystem implements EventListener {

    private boolean playerIsDead = false;

    public AISystem(EwendLauncher context) {
        super(Family.all(AIComponent.class, B2DComponent.class, EnemyComponent.class, AttackComponent.class).get());
        context.getStage().getRoot().addListener(this);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        B2DComponent enemyB2D = ECSEngine.b2dCmpMapper.get(entity);
        Body enemyBody = enemyB2D.body;
        AttackComponent attackComponent = ECSEngine.attCmpMapper.get(entity);
        EnemyComponent enemyComp = ECSEngine.eneObjCmpMapper.get(entity);
        AIComponent aiComp = ECSEngine.aiCmoMapper.get(entity);
        aiComp.milisecAccum += deltaTime;

        if (playerIsDead) {
            aiComp.state = AIState.IDLE;
        }

        switch (aiComp.state) {
            case IDLE:
                movementTrackerIdle(enemyBody, enemyComp, aiComp);
                if(aiComp.milisecAccum>1&&checkPlayerPos(aiComp, enemyB2D,6)){
                    aiComp.state = AIState.TRANSFORM;
                    aiComp.milisecAccum=0;
                    enemyBody.setGravityScale(1f);
                    Gdx.input.vibrate(800);
                }

                break;
            case TRANSFORM:
                enemyBody.setLinearVelocity(0,0);
                if(aiComp.milisecAccum>0.6f){
                    aiComp.state=AIState.RUNNING;
                }
                break;
            case RUNNING:
                float enemyPos = enemyB2D.renderPosition.x;
                float playerPos = getPlayerPos().x;

                if (playerPos > enemyPos) {
                    aiComp.direction = 1;
                } else {
                    aiComp.direction = -1;
                }

                enemyBody.applyLinearImpulse(enemyComp.speed.x * 5 * aiComp.direction - enemyBody.getLinearVelocity().x,
                        0, enemyBody.getWorldCenter().x, enemyBody.getWorldCenter().y, true);
                if(!checkPlayerPos(aiComp,enemyB2D,10)){
                    aiComp.state = AIState.IDLE;
                }

                break;
            case ATTACKING:
                if (attackComponent.delayAccum > attackComponent.delay * 1.3 && aiComp.attacked == true) {
                    aiComp.state = AIState.RUNNING;
                    aiComp.attacked = false;
                } else {
                    enemyBody.setLinearVelocity(0, 0);
                    if (attackComponent.delay < attackComponent.delayAccum && aiComp.attacked == false) {
                        attackComponent.attacking = true;
                        aiComp.attacked = true;
                    }
                }
                break;
            case HITTED:
                aiComp.milisecAccum+=deltaTime;
                if(aiComp.milisecAccum> aiComp.knockTime){
                    aiComp.state=AIState.RUNNING;
                }
                break;
        }
    }

    private void movementTrackerIdle(Body enemyBody, EnemyComponent enemyComp, AIComponent aiComp) {
        float maxPositionX = aiComp.maxDistanceFactor + aiComp.initialPosition.x;
        float minPositionX = aiComp.initialPosition.x - aiComp.maxDistanceFactor;
        float position =enemyBody.getPosition().x;
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

        if (position > maxPositionX) {
            aiComp.direction = -1;
        } else if (minPositionX > position) {
            aiComp.direction = 1;
        }
        enemyBody.applyLinearImpulse(enemyComp.speed.x * aiComp.direction - enemyBody.getLinearVelocity().x, 0,
                enemyBody.getWorldCenter().x, enemyBody.getWorldCenter().y, true);

    }

    private boolean checkPlayerPos(AIComponent aiComponent, B2DComponent enemy,float distance) {
        Vector2 posPlayer = getPlayerPos();
        float distanceVision = aiComponent.maxDistanceFactor * distance;
        if (posPlayer != null) {
            if ((enemy.renderPosition.x - distanceVision < posPlayer.x
                    && enemy.renderPosition.x + distanceVision > posPlayer.x) &&
                    (enemy.renderPosition.y + distanceVision > posPlayer.y
                            && enemy.renderPosition.y - distanceVision < posPlayer.y)) {
                return true;
            }

        }
        return false;

    }


    private Vector2 getPlayerPos() {
        B2DComponent playerB2dComp;

        if (PlayerAnimationSystem.playerB2dComp != null) {
            playerB2dComp = PlayerAnimationSystem.playerB2dComp;
            return playerB2dComp.renderPosition;
        }

        return null;
    }

    @Override
    public boolean handle(Event event) {
        if (event instanceof PlayerDied) {
            playerIsDead = true;
        }
        return false;
    }
}
