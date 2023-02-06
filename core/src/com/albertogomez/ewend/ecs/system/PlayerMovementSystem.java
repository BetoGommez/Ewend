package com.albertogomez.ewend.ecs.system;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.WorldContactListener;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.PlayerComponent;
import com.albertogomez.ewend.input.GameKeyInputListener;
import com.albertogomez.ewend.input.GameKeys;
import com.albertogomez.ewend.input.InputManager;
import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;


public class PlayerMovementSystem extends IteratingSystem implements GameKeyInputListener {
    private float dashDelay;
    private boolean dash;
    private int dashMultiplier;
    private boolean jump;
    private int xFactor;



    public PlayerMovementSystem(final EwendLauncher context) {
        super(Family.all(PlayerComponent.class, B2DComponent.class).get());
        context.getInputManager().addInputListener(this);
        jump=false;
        dash=false;
        xFactor =0;
        dashDelay=0;
        dashMultiplier=5;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final PlayerComponent playerComponent = ECSEngine.playerCmpMapper.get(entity);
        final B2DComponent b2DComponent = ECSEngine.b2dCmpMapper.get(entity);

        if(dashDelay<0){
            dashDelay+=deltaTime;
        }
        jumpMovement(b2DComponent,playerComponent);

        if(dash) {
            dashMovement(b2DComponent, playerComponent, deltaTime);
        }else{
            horizontalMovement(b2DComponent,playerComponent);
        }
    }


    private void jumpMovement(B2DComponent b2DComponent,PlayerComponent playerComponent){
        if(jump){
            jump=false;
            b2DComponent.body.applyLinearImpulse(
                    (b2DComponent.body.getLinearVelocity().x),
                    ((playerComponent.speed.y-b2DComponent.body.getLinearVelocity().y)  ),
                    b2DComponent.body.getWorldCenter().x,b2DComponent.body.getWorldCenter().y,true
            );
        }
    }

    private void horizontalMovement(B2DComponent b2DComponent,PlayerComponent playerComponent){
        //X MOVEMENT
        if(xFactor!=0){
            b2DComponent.body.applyLinearImpulse(
                    (playerComponent.speed.x*xFactor-b2DComponent.body.getLinearVelocity().x),
                    (0),
                    b2DComponent.body.getWorldCenter().x,b2DComponent.body.getWorldCenter().y,true
            );
        }else{
            b2DComponent.body.setLinearVelocity(0,b2DComponent.body.getLinearVelocity().y);
        }
    }

    private void dashMovement(B2DComponent b2DComponent,PlayerComponent playerComponent,float deltaTime){
            dashDelay+=deltaTime;
            b2DComponent.body.setLinearVelocity(playerComponent.speed.x*dashMultiplier*xFactor,0);

            if(dashDelay>0.2){//Time that lasts the dash
                dash=false;
                dashDelay=-3;
                b2DComponent.body.setLinearVelocity(0,0);
            }

    }




    @Override
    public void keyPressed(InputManager inputManager, GameKeys key) {
        switch (key){
            case JUMP:
                jump=true;
                break;
            case LEFT:
                xFactor=-1;
                break;
            case RIGHT:

                xFactor=1;
                break;
            case ATTACK:
                //ATTACK
                break;
            case LOAD:
                    //LOAD LIFE
                break;
            case DASH:
                    if(!dash&&dashDelay>=0){
                        dash=true;
                    }
                break;
            default:
                return;
        }


    }

    @Override
    public boolean keyUp(InputManager inputManager, GameKeys key) {
        Gdx.app.debug("STOP MOVE", "stop moving");

        switch (key){
            case LEFT:

                xFactor=inputManager.isKeyPressed(GameKeys.RIGHT)?1:0;

            break;
            case RIGHT:




               xFactor=inputManager.isKeyPressed(GameKeys.LEFT)?-1:0;
            break;
            case ATTACK:
                //ATTACK
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
