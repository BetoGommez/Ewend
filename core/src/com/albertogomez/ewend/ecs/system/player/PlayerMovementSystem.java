package com.albertogomez.ewend.ecs.system.player;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.audio.AudioManager;
import com.albertogomez.ewend.audio.AudioType;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.AttackComponent;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.LifeComponent;
import com.albertogomez.ewend.ecs.components.PurifyComponent;
import com.albertogomez.ewend.ecs.components.player.PlayerComponent;
import com.albertogomez.ewend.ecs.components.player.PlayerState;
import com.albertogomez.ewend.input.GameKeyInputListener;
import com.albertogomez.ewend.input.GameKeys;
import com.albertogomez.ewend.input.InputManager;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import static com.albertogomez.ewend.constants.Constants.DASH_DELAY;


/**
 *
 * @author Alberto Gómez
 */
public class PlayerMovementSystem extends IteratingSystem implements GameKeyInputListener {
    /**
     * Time being on dash accumulator
     */
    private float dashAccum;
    /**
     * if true the player is ordered to dash
     */
    private boolean doDash;
    /**
     * if true the player is ordered to attack
     */
    private boolean doAttack;
    /**
     * Times jumped being on air for handling the double jump
     */
    private int jumpCount;
    /**
     * If true the player is ordered to jump
     */
    private boolean doJump;
    /**
     * If true the player is ordered to purify
     */
    private boolean doPurify;
    /**
     * If true is indicates the instant that purify is ordered to start
     */
    private boolean purifyStart;
    /**
     * Indicates if the player has to move and the direction, if 1 move to right , else if -1 to left
     */
    private int directionMove;

    /**
     * Indicates if the phone has accelerometer in the hardware
     */
    private boolean hasAccelerometer;
    /**
     * Indicates the speed on the dash multiplying the base player speed
     */
    private final int dashMultiplier;
    /**
     * Audio manager that play sounds and music
     */
    private final AudioManager audioManager;


    /**
     * Constructor that indicates which entities to process and sets all values
     * @param context Main game class
     */
    public PlayerMovementSystem(final EwendLauncher context) {
        super(Family.all(PlayerComponent.class, B2DComponent.class, AttackComponent.class).get());
        context.getInputManager().addInputListener(this);
        audioManager = context.getAudioManager();
        doJump = false;
        doDash = false;
        doAttack = false;
        doPurify = false;
        directionMove = 0;
        dashAccum = DASH_DELAY;
        dashMultiplier = 5;
        hasAccelerometer =  Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
    }

    /**
     * Process all player movement depending on the inputs
     * @param entity The current Entity being processed
     * @param deltaTime The delta time between the last and current frame
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final PlayerComponent playerComponent = ECSEngine.playerCmpMapper.get(entity);
        final B2DComponent b2DComponent = ECSEngine.b2dCmpMapper.get(entity);
        final AttackComponent attackComponent = ECSEngine.attCmpMapper.get(entity);
        final LifeComponent lifeComponent = ECSEngine.lifeCmpMapper.get(entity);
        final PurifyComponent purifyComponent = ECSEngine.purifyCmpMapper.get(entity);;
        playerComponent.milisecAccum+=deltaTime;

        if(purifyStart){
            purifyComponent.purifyingTimeAccum=0;
            purifyStart=false;
            doPurify =true;
        }

        if(doPurify &&lifeComponent.fury >0.25f&&lifeComponent.health<100){
                purifyComponent.isPuryfing=true;
                b2DComponent.body.setLinearVelocity(0,b2DComponent.body.getLinearVelocity().y);
        }else{
            if(playerComponent.playerState==PlayerState.DAMAGED) {
                if(playerComponent.milisecAccum> playerComponent.knockedTime){
                    playerComponent.playerState=PlayerState.IDLE;
                }
            }else{
                if (attackComponent.attackDelayAccum > attackComponent.attackDelay /2) {
                    playerComponent.playerState=PlayerState.IDLE;
                    if (doDash &&dashAccum>=DASH_DELAY) {
                        dashMovement(b2DComponent, playerComponent, deltaTime);
                    } else {
                        doDash =false;
                        horizontalMovement(b2DComponent, playerComponent);
                        jumpMovement(b2DComponent, playerComponent);
                    }
                }else{
                    b2DComponent.body.setLinearVelocity(0,0);
                }
                if (doAttack &&attackComponent.attackDelayAccum >attackComponent.attackDelay) {
                    attackComponent.attacking = true;
                    playerComponent.playerState= PlayerState.ATTACKING;
                    audioManager.playAudio(AudioType.EWEND_HIT);

                }
                doAttack = false;
                purifyComponent.isPuryfing=false;
            }
        }

        if (dashAccum < DASH_DELAY) {
            dashAccum += deltaTime;
        }
    }


    /**
     * Process the jump movement depending on if touching ground and how many times player has jumped
     * @param b2DComponent
     * @param playerComponent
     */
    private void jumpMovement(B2DComponent b2DComponent, PlayerComponent playerComponent) {
        float speedY = b2DComponent.body.getLinearVelocity().y;
        if (doJump && speedY > -10) {
            playerComponent.touchingGround = false;
            doJump = false;
            jumpCount++;
            b2DComponent.body.applyLinearImpulse(
                    (b2DComponent.body.getLinearVelocity().x) * b2DComponent.orientation,
                    ((playerComponent.speed.y - speedY)),
                    b2DComponent.body.getWorldCenter().x, b2DComponent.body.getWorldCenter().y, true
            );
            audioManager.playAudio(AudioType.EWEND_JUMP);
        }
        setJumpingPlayerState(speedY,playerComponent);
        if (playerComponent.touchingGround) {
            jumpCount = 0;
        }
    }

    /**
     * Sets the jumping player state depending on jumped times
     * @param speedY Player y actual speed
     * @param playerComponent Player component
     */
    private void setJumpingPlayerState(float speedY,PlayerComponent playerComponent){
        if(speedY!=0){
            if(jumpCount>1){
                playerComponent.playerState = PlayerState.DOUBLE_JUMP;
            }else{
                playerComponent.playerState=PlayerState.JUMPING;
            }
        }
    }

    /**
     * Process the player horizontal movement setting his direction and impulse
     * @param b2DComponent Player b2dComponent
     * @param playerComponent Player Component
     */
    private void horizontalMovement(B2DComponent b2DComponent, PlayerComponent playerComponent) {
        //X MOVEMENT
        if (directionMove != 0) {
            playerComponent.playerState=PlayerState.RUNNING;
            b2DComponent.body.applyLinearImpulse(
                    (playerComponent.speed.x * directionMove - b2DComponent.body.getLinearVelocity().x),
                    0,
                    b2DComponent.body.getWorldCenter().x, b2DComponent.body.getWorldCenter().y, true
            );
            b2DComponent.orientation= directionMove;
        } else {
            b2DComponent.body.setLinearVelocity(0, b2DComponent.body.getLinearVelocity().y);

        }
    }

    /**
     * Handles the player dash movement, if the accumulator has reached necesary time it activates and sets the dash player state
     * @param b2DComponent Player b2dComponent
     * @param playerComponent Player Component
     * @param deltaTime Time to be accumulated
     */
    private void dashMovement(B2DComponent b2DComponent, PlayerComponent playerComponent, float deltaTime) {
        dashAccum += deltaTime;
        if (!playerComponent.touchingGround) {
            if (jumpCount < 3) {
                jumpCount++;
                if (directionMove == 0) {
                    b2DComponent.body.setLinearVelocity(playerComponent.speed.x * dashMultiplier, 0);
                } else {
                    b2DComponent.body.setLinearVelocity(playerComponent.speed.x * dashMultiplier * directionMove, 0);
                }
                audioManager.playAudio(AudioType.EWEND_DASH);
            }
            playerComponent.playerState=PlayerState.DASHING;
        }
        if (dashAccum > 0.2+DASH_DELAY) {
            doDash = false;
            dashAccum = 0f;
            b2DComponent.body.setLinearVelocity(0, 0);
        }

    }

    /**
     * Handles the key press input and activates the order
     * @param inputManager Game keys input manager
     * @param key Key has been pressed
     */
    @Override
    public void keyPressed(InputManager inputManager, GameKeys key) {
        switch (key) {
            case JUMP:
                if (jumpCount < 2) {
                    doJump = true;
                }
                break;
            case LEFT:
                directionMove = -1;
                break;
            case RIGHT:
                directionMove = 1;
                break;
            case ATTACK:
                doAttack = true;
                //ATTACK
                break;
            case PURIFY:
                purifyStart=true;

                //LOAD LIFE
                break;
            case DASH:
                if (!doDash && dashAccum >= 0) {
                    doDash = true;
                }
                break;
            default:
                return;
        }


    }

    /**
     * Handles the key release input and activates the order
     * @param inputManager Game keys input manager
     * @param key Key has been released
     */
    @Override
    public boolean keyUp(InputManager inputManager, GameKeys key) {
        switch (key) {
            case LEFT:
                directionMove = inputManager.isKeyPressed(GameKeys.RIGHT) ? 1 : 0;
                break;
            case RIGHT:
                directionMove = inputManager.isKeyPressed(GameKeys.LEFT) ? -1 : 0;
                break;
            case PURIFY:
                doPurify =false;
                //LOAD LIFE
                break;
            default:
                return true;
        }
        return true;
    }

}
