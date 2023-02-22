package com.albertogomez.ewend;

import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.ai.AIComponent;
import com.albertogomez.ewend.ecs.ai.AIState;
import com.albertogomez.ewend.ecs.components.AttackComponent;
import com.albertogomez.ewend.ecs.components.LifeComponent;
import com.albertogomez.ewend.ecs.components.player.PlayerComponent;
import com.albertogomez.ewend.ecs.components.player.PlayerState;
import com.albertogomez.ewend.events.PlayerHealthChange;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import static com.albertogomez.ewend.constants.Constants.*;

public class WorldContactListener implements ContactListener {
    private final Array<PlayerCollisionListener> listeners;
    private final Stage stage;
    public WorldContactListener(Stage stage) {
        listeners = new Array<PlayerCollisionListener>();
        this.stage = stage;
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
        final Body playerBody = playerFixture.getBody();
        final Entity player=(Entity) playerFixture.getBody().getUserData();
        final Entity gameObj;
        final PlayerComponent playerComponent = ECSEngine.playerCmpMapper.get(player);

        switch (categoryBitsB){
            case BIT_ENEMY:
                ECSEngine.aiCmoMapper.get((Entity) fixtureB.getBody().getUserData()).state = AIState.ATTACKING;
                ECSEngine.attCmpMapper.get((Entity) fixtureB.getBody().getUserData()).delayAccum=0;
                break;
            case  BIT_ENEMY_ATTACK:
                playerComponent.playerState = PlayerState.DAMAGED;
                playerComponent.milisecAccum=0;
                applyHit(8,8,bodyB,playerBody);

                final LifeComponent lifeComponent = ECSEngine.lifeCmpMapper.get(player);
                lifeComponent.removeHealth((AttackComponent) fixtureB.getBody().getUserData());
                stage.getRoot().fire(new PlayerHealthChange(lifeComponent.health));
                break;
            case  BIT_GROUND:
                if(contact.getWorldManifold().getNormal().angleDeg()<170f&&contact.getWorldManifold().getNormal().angleDeg()>5f){
                    playerComponent.touchingGround=true;
                }
                break;
            case BIT_GAME_OBJECT:
                gameObj = (Entity) bodyB.getUserData();
                ECSEngine.gameObjCmpMapper.get(gameObj).touched=true;
                for(PlayerCollisionListener listener : listeners){
                    listener.playerCollision(player,gameObj);
                }
                break;
            default:
                break;
        }

    }

    private void enemyCollions(Fixture enemyFixture,Fixture fixtureB,Contact contact){
        short categoryBitsB= fixtureB.getFilterData().categoryBits;
        final Body bodyB =fixtureB.getBody();
        final Entity enemy=(Entity) enemyFixture.getBody().getUserData();
        final AIComponent aiComponent = ECSEngine.aiCmoMapper.get(enemy);
        switch (categoryBitsB){
            case BIT_PLAYER_ATTACK:
                LifeComponent enemyLifeComp = ECSEngine.lifeCmpMapper.get(enemy);
                enemyLifeComp.removeHealth((AttackComponent) fixtureB.getBody().getUserData());
                aiComponent.state=AIState.HITTED;
                aiComponent.milisecAccum = 0;
                applyHit(12,12,bodyB,enemyFixture.getBody());
                break;
            case BIT_BOUND:
                aiComponent.state = AIState.IDLE;
                break;
            default:
                break;
        }

    }

    private void applyHit(float forceY, float forceX,Body bodyHitter,Body bodyReceiver){
        float hitDirection = 1;
        if(bodyReceiver.getPosition().x<bodyHitter.getPosition().x){
            hitDirection=-1;
        }
        bodyReceiver.applyLinearImpulse(forceX*hitDirection,forceY,bodyReceiver.getWorldCenter().x,bodyReceiver.getWorldCenter().y,true);
    }



    @Override
    public void endContact(Contact contact) {
        final int catFixA = contact.getFixtureA().getFilterData().categoryBits;
        final int catFixB = contact.getFixtureB().getFilterData().categoryBits;
        PlayerComponent playerComponent = null;
        //player collision cases

        if((catFixB & BIT_GROUND) == BIT_GROUND||(catFixA & BIT_GROUND) == BIT_GROUND){

        }else{
            return;
        }
        if((catFixB & BIT_PLAYER) == BIT_PLAYER){
            playerComponent = ECSEngine.playerCmpMapper.get((Entity) contact.getFixtureB().getBody().getUserData());
            if(playerComponent!=null){
                playerComponent.touchingGround=false;
            }
        }else if((catFixA & BIT_PLAYER) == BIT_PLAYER){
            playerComponent = ECSEngine.playerCmpMapper.get((Entity) contact.getFixtureA().getBody().getUserData());
            if(playerComponent!=null){
                playerComponent.touchingGround=false;
            }
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

        if(defaultPresolver(BIT_ENEMY_ATTACK,BIT_PLAYER,catFixA,catFixB)){
            contact.setEnabled(false);
        }

        if(defaultPresolver(BIT_PLAYER,BIT_GAME_OBJECT,catFixA,catFixB)){
            contact.setEnabled(false);
        }

        if(defaultPresolver(BIT_PLAYER,BIT_ENEMY,catFixA,catFixB)){
            contact.setEnabled(false);
        }

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

        if((catFixB & BIT_GROUND) != BIT_GROUND&&(catFixA & BIT_GROUND) != BIT_GROUND){
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
            player = (Entity)bodyPlayer.getUserData();
            bodyPlayer.setLinearVelocity(ECSEngine.playerCmpMapper.get((Entity) bodyPlayer.getUserData()).speed.x*3*ECSEngine.b2dCmpMapper.get((Entity)bodyPlayer.getUserData()).orientation,0);

        }


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

    public void addPlayerCollisionListener(final PlayerCollisionListener listener){
        listeners.add(listener);
    }

    public interface PlayerCollisionListener{
        void playerCollision(final Entity player, final Entity gameObject);
    }
}
