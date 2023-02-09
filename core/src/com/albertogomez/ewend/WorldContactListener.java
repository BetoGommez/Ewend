package com.albertogomez.ewend;

import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.LifeComponent;
import com.albertogomez.ewend.ecs.components.PlayerComponent;
import com.albertogomez.ewend.ecs.system.PlayerMovementSystem;
import com.badlogic.ashley.core.Component;
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

    private short[] maskBits={BIT_GROUND,BIT_GAME_OBJECT,BIT_PLAYER,BIT_ENEMY,BIT_PLAYER_ATTACK,BIT_ENEMY_ATTACK};
    private final Array<PlayerCollisionListener> listeners;
    public WorldContactListener() {
        listeners = new Array<PlayerCollisionListener>();

    }
    @Override
    public void beginContact(Contact contact) {

        final Body bodyA = contact.getFixtureA().getBody();
        final Body bodyB = contact.getFixtureB().getBody();
        final int catFixA = contact.getFixtureA().getFilterData().categoryBits;
        final int catFixB = contact.getFixtureB().getFilterData().categoryBits;
        int colType = catFixA+catFixB;



        //player collision cases
        if((catFixB & BIT_PLAYER) == BIT_PLAYER){
            playerCollions(contact.getFixtureB(),contact.getFixtureA(),contact);
        }else{
            if((catFixB & BIT_PLAYER) == BIT_PLAYER){
                playerCollions(contact.getFixtureA(),contact.getFixtureB(),contact);
            }
        }

        //enemy collion

        if((catFixB & BIT_ENEMY) == BIT_ENEMY){
            Gdx.app.debug("COLISION: ","Player is colliding with a object");

            enemyCollions(contact.getFixtureB(),contact.getFixtureA(),contact);
        }else{
            if((catFixA & BIT_ENEMY) == BIT_ENEMY){
                Gdx.app.debug("COLISION: ","Player is colliding with a object");

                enemyCollions(contact.getFixtureA(),contact.getFixtureB(),contact);
            }
        }






    }

    private void playerCollions(Fixture playerFixture,Fixture fixtureB,Contact contact){
        short categoryBitsB= fixtureB.getFilterData().categoryBits;
        final Body bodyPlayer = playerFixture.getBody();
        final Body bodyB =fixtureB.getBody();
        final Entity player=(Entity) playerFixture.getBody().getUserData();
        final Entity gameObj;
        final short bitParse;


        switch (categoryBitsB){
            case BIT_ENEMY:
                break;
            case  BIT_ENEMY_ATTACK:
                break;
            case  BIT_GROUND:
                if(checkCollisionGround()&&contact.getWorldManifold().getNormal().angleDeg()<91f){

                    ECSEngine.playerCmpMapper.get(player).touchingGround=true;
                }else {
                    return;
                }
                break;
            case BIT_GAME_OBJECT:

                gameObj = (Entity) bodyB.getUserData();
                for(final PlayerCollisionListener listener : listeners){
                    listener.playerCollision(player,gameObj);
                }
                break;
            default:
                break;
        }

    }

    private void enemyCollions(Fixture enemyFixture,Fixture fixtureB,Contact contact){
        short categoryBitsB= fixtureB.getFilterData().categoryBits;
        final Body bodyPlayer = enemyFixture.getBody();
        final Body bodyB =fixtureB.getBody();
        final Entity enemy=(Entity) enemyFixture.getBody().getUserData();
        final Entity gameObj;
        final short bitParse;


        switch (categoryBitsB){
            case BIT_ENEMY:

                break;
            case BIT_PLAYER_ATTACK:

                LifeComponent enemyLifeComp = ECSEngine.lifeCmpMapper.get(enemy);
                enemyLifeComp.health-=(float)bodyB.getUserData();
                break;
            default:
                break;
        }

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


        if ((int) (catFixA & BIT_PLAYER_ATTACK) == BIT_PLAYER_ATTACK) {

        } else if((int) (catFixB & BIT_PLAYER_ATTACK)==BIT_PLAYER_ATTACK){

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
