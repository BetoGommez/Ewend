package com.albertogomez.ewend;

import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.PlayerComponent;
import com.albertogomez.ewend.ecs.system.PlayerMovementSystem;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;
import java.util.Comparator;

import static com.albertogomez.ewend.constants.Constants.*;
import static java.lang.Math.min;

public class WorldContactListener implements ContactListener {
    private final Array<PlayerCollisionListener> listeners;
    public WorldContactListener() {
        listeners = new Array<PlayerCollisionListener>();

    }
    @Override
    public void beginContact(Contact contact) {
        final Entity player;
        final Entity gameObj;
        final Body bodyA = contact.getFixtureA().getBody();
        final Body bodyB = contact.getFixtureB().getBody();
        final int catFixA = contact.getFixtureA().getFilterData().categoryBits;
        final int catFixB = contact.getFixtureB().getFilterData().categoryBits;
        Gdx.app.debug("COLISION: ","Player is colliding with a object");


        if ((int) (catFixA & BIT_PLAYER) == BIT_PLAYER) {
            player = (Entity) bodyA.getUserData();
        } else if((int) (catFixB & BIT_PLAYER)==BIT_PLAYER){
            player = (Entity) bodyB.getUserData();
        } else{
            return;
        }

        Gdx.app.debug("colision normal:",contact.getWorldManifold().getNormal().y+"::"+contact.getWorldManifold().getNormal().angleDeg());
        if(checkCollisionGround()&&contact.getWorldManifold().getNormal().y>0.5){

            ECSEngine.playerCmpMapper.get(player).touchingGround=true;
        }else {
            return;
        }

        if ((int) (catFixA & BIT_GAME_OBJECT) == BIT_GAME_OBJECT) {
            gameObj = (Entity) bodyA.getUserData();
        } else if((int) (catFixB & BIT_GAME_OBJECT)==BIT_GAME_OBJECT){
            gameObj = (Entity) bodyB.getUserData();
        } else{
            return;
        }

        for(final PlayerCollisionListener listener : listeners){
            listener.playerCollision(player,gameObj);
        }


        Gdx.app.debug("COLISION: ","Player is colliding with a object");
    }

    private boolean checkCollisionGround(){
        return true;
    }

    @Override
    public void endContact(Contact contact) {
    }


    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

        final Entity player;
        final Entity enemyEntity;
        final Body bodyA = contact.getFixtureA().getBody();
        final Body bodyB = contact.getFixtureB().getBody();
        final int catFixA = contact.getFixtureA().getFilterData().categoryBits;
        final int catFixB = contact.getFixtureB().getFilterData().categoryBits;


        if ((int) (catFixA & BIT_PLAYER) == BIT_PLAYER) {
            player = (Entity) bodyA.getUserData();
        } else if((int) (catFixB & BIT_PLAYER)==BIT_PLAYER){
            player = (Entity) bodyB.getUserData();
        } else{
            return;
        }

        if ((int) (catFixA & BIT_ENEMY) == BIT_ENEMY) {
            enemyEntity = (Entity) bodyA.getUserData();

        } else if((int) (catFixB & BIT_ENEMY)==BIT_ENEMY){
            enemyEntity = (Entity) bodyB.getUserData();

        } else{
            return;
        }

        contact.setEnabled(false);


        //ATAQUE


    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    public void addPlayerCollisionListener(final PlayerCollisionListener listener){
        listeners.add(listener);
    }

    public interface PlayerCollisionListener{
        void playerCollision(final Entity player, final Entity gameObject);
    }
}
