package com.albertogomez.ewend;

import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.ai.AIState;
import com.albertogomez.ewend.ecs.components.LifeComponent;
import com.albertogomez.ewend.view.AnimationType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import static com.albertogomez.ewend.constants.Constants.*;

public class WorldContactListener implements ContactListener {
    private final Array<PlayerCollisionListener> listeners;
    public WorldContactListener() {
        listeners = new Array<PlayerCollisionListener>();
    }
    @Override
    public void beginContact(Contact contact) {
        final int catFixA = contact.getFixtureA().getFilterData().categoryBits;
        final int catFixB = contact.getFixtureB().getFilterData().categoryBits;

        //player collision cases
        if((catFixB & BIT_PLAYER) == BIT_PLAYER){
            playerCollions(contact.getFixtureB(),contact.getFixtureA(),contact);
        }else{
            if((catFixA & BIT_PLAYER) == BIT_PLAYER){
                playerCollions(contact.getFixtureA(),contact.getFixtureB(),contact);
            }
        }

        //enemy collision
        if((catFixB & BIT_ENEMY) == BIT_ENEMY){
            enemyCollions(contact.getFixtureB(),contact.getFixtureA(),contact);
        }else{
            if((catFixA & BIT_ENEMY) == BIT_ENEMY){
                enemyCollions(contact.getFixtureA(),contact.getFixtureB(),contact);
            }
        }

    }

    private void playerCollions(Fixture playerFixture,Fixture fixtureB,Contact contact){
        short categoryBitsB= fixtureB.getFilterData().categoryBits;
        final Body bodyB =fixtureB.getBody();
        final Entity player=(Entity) playerFixture.getBody().getUserData();
        final Entity gameObj;

        switch (categoryBitsB){
            case BIT_ENEMY:
                ECSEngine.aiCmoMapper.get((Entity) fixtureB.getBody().getUserData()).state = AIState.ATTACKING;
                ECSEngine.lifeCmpMapper.get(player).removeHealth(ECSEngine.attCmpMapper.get((Entity)fixtureB.getBody().getUserData()));
                ECSEngine.aniCmpMapper.get(player).aniType = AnimationType.PLAYER_DAMAGED;

                break;
            case  BIT_ENEMY_ATTACK:
                break;
            case  BIT_GROUND:
                if(contact.getWorldManifold().getNormal().angleDeg()<170f&&contact.getWorldManifold().getNormal().angleDeg()>5f){
                    ECSEngine.playerCmpMapper.get(player).touchingGround=true;
                }else {
                    return;
                }
                break;
            case BIT_GAME_OBJECT:

                gameObj = (Entity) bodyB.getUserData();
                ECSEngine.gameObjCmpMapper.get(gameObj).touched=true;

                break;
            default:
                break;
        }

    }

    private void enemyCollions(Fixture enemyFixture,Fixture fixtureB,Contact contact){
        short categoryBitsB= fixtureB.getFilterData().categoryBits;
        final Body bodyB =fixtureB.getBody();
        final Entity enemy=(Entity) enemyFixture.getBody().getUserData();
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



    @Override
    public void endContact(Contact contact) {
        final int catFixA = contact.getFixtureA().getFilterData().categoryBits;
        final int catFixB = contact.getFixtureB().getFilterData().categoryBits;
        //player collision cases

        if((catFixB & BIT_GROUND) == BIT_GROUND||(catFixA & BIT_GROUND) == BIT_GROUND){

        }else{
            return;
        }
        if((catFixB & BIT_PLAYER) == BIT_PLAYER){
            ECSEngine.playerCmpMapper.get((Entity) contact.getFixtureB().getBody().getUserData()).touchingGround=false;
        }else if((catFixA & BIT_PLAYER) == BIT_PLAYER){
            ECSEngine.playerCmpMapper.get((Entity) contact.getFixtureA().getBody().getUserData()).touchingGround=false;
        }


    }


    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

        final Entity player;
        final Entity enemyEntity;
        final Body bodyA = contact.getFixtureA().getBody();
        final Body bodyB = contact.getFixtureB().getBody();
        final int catFixA = contact.getFixtureA().getFilterData().categoryBits;
        final int catFixB = contact.getFixtureB().getFilterData().categoryBits;

        if(defaultPresolver(BIT_PLAYER_ATTACK,BIT_ENEMY,catFixA,catFixB)){
            contact.setEnabled(false);
        }

        if(defaultPresolver(BIT_PLAYER,BIT_GAME_OBJECT,catFixA,catFixB)){
            contact.setEnabled(false);
        }

        if(defaultPresolver(BIT_PLAYER,BIT_ENEMY,catFixA,catFixB)){
            contact.setEnabled(false);
        }




        //ATAQUE


    }

    private boolean defaultPresolver(short maskBitA,short maskBitB,int catFixA,int catFixB){
        if ((int) (catFixA & maskBitA) == maskBitA) {

        } else if((int) (catFixB & maskBitA)==maskBitA){

        } else{
            return false;
        }

        if ((int) (catFixA & maskBitB) == maskBitB) {
        } else if((int) (catFixB & maskBitB)==maskBitB){
        } else{
            return false;
        }
        return true;
    }



    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

        final Entity player;
        final Entity enemyEntity;
        final Body bodyA = contact.getFixtureA().getBody();
        final Body bodyB = contact.getFixtureB().getBody();
        final int catFixA = contact.getFixtureA().getFilterData().categoryBits;
        final int catFixB = contact.getFixtureB().getFilterData().categoryBits;
        Body bodyPlayer;



        if((catFixB & BIT_GROUND) == BIT_GROUND||(catFixA & BIT_GROUND) == BIT_GROUND){
            // Gdx.app.debug("COLISION: ","Player is colliding with a object");

        }else{
            return;
        }
        if((catFixB & BIT_PLAYER) == BIT_PLAYER){

            bodyPlayer = contact.getFixtureB().getBody();

        }else if((catFixA & BIT_PLAYER) == BIT_PLAYER){
            bodyPlayer = contact.getFixtureA().getBody();
        }else{
            return;
        }
        float angleDeg = contact.getWorldManifold().getNormal().angleDeg();

        if((angleDeg<175&&angleDeg>95||angleDeg<85&&angleDeg>5)&&bodyPlayer.getLinearVelocity().y<5){
            bodyPlayer.setLinearVelocity(bodyPlayer.getLinearVelocity().x,0);

        }


        contact.setTangentSpeed(0);


    }

    public void addPlayerCollisionListener(final PlayerCollisionListener listener){
        listeners.add(listener);
    }

    public interface PlayerCollisionListener{
        void playerCollision(final Entity player, final Entity gameObject);
    }
}
