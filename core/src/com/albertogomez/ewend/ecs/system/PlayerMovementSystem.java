package com.albertogomez.ewend.ecs.system;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.PlayerComponent;
import com.albertogomez.ewend.input.GameKeyInputListener;
import com.albertogomez.ewend.input.GameKeys;
import com.albertogomez.ewend.input.InputManager;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class PlayerMovementSystem extends IteratingSystem implements GameKeyInputListener {
    private boolean directionChange;
    private int xFactor;
    private int yFactor;

    public PlayerMovementSystem(final EwendLauncher context) {
        super(Family.all(PlayerComponent.class, B2DComponent.class).get());
        context.getInputManager().addInputListener(this);
        directionChange=false;
        xFactor = yFactor = 0;

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        final PlayerComponent playerComponent = ECSEngine.playerCmpMapper.get(entity);
        final B2DComponent b2DComponent = ECSEngine.b2dCmpMapper.get(entity);

        if(directionChange){
            directionChange=false;
            b2DComponent.body.applyLinearImpulse(
                    ((xFactor*3-b2DComponent.body.getLinearVelocity().x) ),
                    ((yFactor-b2DComponent.body.getLinearVelocity().y)  ),
                    b2DComponent.body.getWorldCenter().x,b2DComponent.body.getWorldCenter().y,true
            );
        }

    }

    @Override
    public void keyPressed(InputManager inputManager, GameKeys key) {
        switch (key){
            case JUMP:
                directionChange=true;
                yFactor=5;
                break;
            case LEFT:
                directionChange=true;
                xFactor=-1;
                break;
            case RIGHT:
                directionChange=true;
                xFactor=1;
                break;
            case ATTACK:
                //ATTACK
                break;
            case LOAD:
                    //LOAD LIFE
                break;
            default:
                return;
        }


    }

    @Override
    public void keyUp(InputManager inputManager, GameKeys key) {
        switch (key){
            case JUMP:
                directionChange=true;
                yFactor=inputManager.isKeyPressed(key)?0:0;
                break;
            case LEFT:
                directionChange=true;
                xFactor=inputManager.isKeyPressed(key)?-1:0;
                break;
            case RIGHT:
                directionChange=true;
                xFactor=inputManager.isKeyPressed(key)?1:0;
                break;
            case ATTACK:
                //ATTACK
                break;
            case LOAD:
                //LOAD LIFE
                break;
            default:
                return;
        }
    }
}
