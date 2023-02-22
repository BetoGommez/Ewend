package com.albertogomez.ewend.ecs.system.player;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.AttackComponent;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.player.PlayerComponent;
import com.albertogomez.ewend.ecs.components.player.PlayerState;
import com.albertogomez.ewend.input.GameKeyInputListener;
import com.albertogomez.ewend.input.GameKeys;
import com.albertogomez.ewend.input.InputManager;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import static com.albertogomez.ewend.constants.Constants.DASH_DELAY;


public class PlayerMovementSystem extends IteratingSystem implements GameKeyInputListener {
    private float dashAccum;
    private boolean dash;
    private boolean attack;
    private int jumpCount;
    private boolean jump;
    private int xFactor;
    private final int dashMultiplier;


    public PlayerMovementSystem(final EwendLauncher context) {
        super(Family.all(PlayerComponent.class, B2DComponent.class, AttackComponent.class).get());
        context.getInputManager().addInputListener(this);

        jump = false;
        dash = false;
        attack = false;

        xFactor = 0;
        dashAccum = DASH_DELAY;
        dashMultiplier = 5;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final PlayerComponent playerComponent = ECSEngine.playerCmpMapper.get(entity);
        final B2DComponent b2DComponent = ECSEngine.b2dCmpMapper.get(entity);
        final AttackComponent attackComponent = ECSEngine.attCmpMapper.get(entity);
        playerComponent.milisecAccum+=deltaTime;
        if(playerComponent.playerState==PlayerState.DAMAGED) {
            if(playerComponent.milisecAccum> playerComponent.knockedTime){
                playerComponent.playerState=PlayerState.IDLE;
            }
        }else{
            if (attackComponent.delayAccum > attackComponent.delay/2) {
                playerComponent.playerState=PlayerState.IDLE;
                if (dash&&dashAccum>=DASH_DELAY) {
                    dashMovement(b2DComponent, playerComponent, deltaTime);
                } else {
                    dash=false;
                    horizontalMovement(b2DComponent, playerComponent);
                    jumpMovement(b2DComponent, playerComponent);
                }
            }else{
                b2DComponent.body.setLinearVelocity(0,0);
            }
            if (attack&&attackComponent.delayAccum>attackComponent.delay) {
                attackComponent.attacking = true;
                playerComponent.playerState= PlayerState.ATTACKING;
            }
            attack = false;
        }

        if (dashAccum < DASH_DELAY) {
            dashAccum += deltaTime;
        }
    }


    private void jumpMovement(B2DComponent b2DComponent, PlayerComponent playerComponent) {
        float speedY = b2DComponent.body.getLinearVelocity().y;
        if (jump && speedY > -10) {
            playerComponent.touchingGround = false;
            jump = false;
            jumpCount++;
            b2DComponent.body.applyLinearImpulse(
                    (b2DComponent.body.getLinearVelocity().x) * b2DComponent.orientation,
                    ((playerComponent.speed.y - speedY)),
                    b2DComponent.body.getWorldCenter().x, b2DComponent.body.getWorldCenter().y, true
            );

        }
        setJumpingAnimation(speedY,playerComponent);
        if (playerComponent.touchingGround) {
            jumpCount = 0;
        }
    }

    private void setJumpingAnimation(float speedY,PlayerComponent playerComponent){
        if(speedY!=0){
            if(jumpCount>1){
                playerComponent.playerState = PlayerState.DOUBLE_JUMP;
            }else{
                playerComponent.playerState=PlayerState.JUMPING;
            }
        }
    }

    private void horizontalMovement(B2DComponent b2DComponent, PlayerComponent playerComponent) {
        //X MOVEMENT
        if (xFactor != 0) {
            playerComponent.playerState=PlayerState.RUNNING;
            b2DComponent.body.applyLinearImpulse(
                    (playerComponent.speed.x * xFactor - b2DComponent.body.getLinearVelocity().x),
                    0,
                    b2DComponent.body.getWorldCenter().x, b2DComponent.body.getWorldCenter().y, true
            );
            b2DComponent.orientation=xFactor;
        } else {
            b2DComponent.body.setLinearVelocity(0, b2DComponent.body.getLinearVelocity().y);

        }
    }

    private void dashMovement(B2DComponent b2DComponent, PlayerComponent playerComponent, float deltaTime) {
        dashAccum += deltaTime;
        if (!playerComponent.touchingGround) {
            if (jumpCount < 3) {
                jumpCount++;
                if (xFactor == 0) {
                    b2DComponent.body.setLinearVelocity(playerComponent.speed.x * dashMultiplier, 0);
                } else {
                    b2DComponent.body.setLinearVelocity(playerComponent.speed.x * dashMultiplier * xFactor, 0);
                }
            }
            playerComponent.playerState=PlayerState.DASHING;
        }
        if (dashAccum > 0.2+DASH_DELAY) {
            dash = false;
            dashAccum = 0f;
            b2DComponent.body.setLinearVelocity(0, 0);
        }

    }


    @Override
    public void keyPressed(InputManager inputManager, GameKeys key) {
        switch (key) {
            case JUMP:
                if (jumpCount < 2) {
                    jump = true;
                }
                break;
            case LEFT:
                xFactor = -1;
                break;
            case RIGHT:
                xFactor = 1;
                break;
            case ATTACK:
                attack = true;
                //ATTACK
                break;
            case LOAD:
                //LOAD LIFE
                break;
            case DASH:
                if (!dash && dashAccum >= 0) {
                    dash = true;
                }
                break;
            default:
                return;
        }


    }

    @Override
    public boolean keyUp(InputManager inputManager, GameKeys key) {
        switch (key) {
            case LEFT:
                xFactor = inputManager.isKeyPressed(GameKeys.RIGHT) ? 1 : 0;
                break;
            case RIGHT:
                xFactor = inputManager.isKeyPressed(GameKeys.LEFT) ? -1 : 0;
                break;
            case LOAD:
                //LOAD LIFE
                break;
            default:
                return true;
        }
        return true;
    }

}
