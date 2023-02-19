package com.albertogomez.ewend.ecs.ai;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.AttackComponent;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.enemy.EnemyComponent;
import com.albertogomez.ewend.ecs.system.PlayerAnimationSystem;
import com.albertogomez.ewend.ecs.system.PlayerCollisionSystem;
import com.albertogomez.ewend.events.PlayerDied;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import static com.albertogomez.ewend.view.AnimationType.SHEEP_ATTACK;
import static javax.swing.UIManager.get;

public class AISystem extends IteratingSystem implements EventListener {

    private boolean playerIsDead=false;
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

        if(playerIsDead){
            aiComp.state = AIState.IDLE;
        }

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
                        0, enemyBody.getWorldCenter().x, enemyBody.getWorldCenter().y, true);
                break;
            case ATTACKING:

                if (attackComponent.delayAccum > attackComponent.delay*1.3&& aiComp.attacked==true) {
                    aiComp.state = AIState.RUNNING;
                    aiComp.attacked=false;
                    enemyBody.setLinearVelocity(0, 0);

                } else {

                    enemyBody.setLinearVelocity(0, 0);
                    if (attackComponent.delay  < attackComponent.delayAccum&&aiComp.attacked==false) {
                        attackComponent.attacking = true;
                        aiComp.attacked=true;
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
        float distanceVision = aiComponent.maxDistanceFactor*8;
        if (posPlayer != null) {
            if ((enemy.renderPosition.x - distanceVision < posPlayer.x
                    && enemy.renderPosition.x + distanceVision > posPlayer.x) &&
                    (enemy.renderPosition.y + distanceVision > posPlayer.y
                            && enemy.renderPosition.y - distanceVision < posPlayer.y)) {
                aiComponent.state = AIState.RUNNING;
                Gdx.input.vibrate(new long[]{200,800},-1);
            }

        }

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
        if(event instanceof PlayerDied){
            playerIsDead = true;
        }
        return false;
    }
}
