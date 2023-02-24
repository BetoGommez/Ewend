package com.albertogomez.ewend;

import com.albertogomez.ewend.audio.AudioManager;
import com.albertogomez.ewend.audio.AudioType;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.ai.AIComponent;
import com.albertogomez.ewend.ecs.ai.AIState;
import com.albertogomez.ewend.ecs.components.AttackComponent;
import com.albertogomez.ewend.ecs.components.LifeComponent;
import com.albertogomez.ewend.ecs.components.player.PlayerComponent;
import com.albertogomez.ewend.ecs.components.player.PlayerState;
import com.albertogomez.ewend.events.PlayerHit;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import static com.albertogomez.ewend.constants.Constants.*;

/**
 * Game collisions handler
 * @author Alberto Gómez
 */
public class WorldContactListener implements ContactListener {
    /**
     * Stores all listeners
     */
    private final Array<PlayerObjectCollisionListener> listeners;
    /**
     * Audio manager for playing sounds and music
     */
    private final AudioManager audioManager;
    /**
     * Game stage
     */
    private final Stage stage;

    /**
     * Creates the handler
     * @param context Main Game Class
     */
    public WorldContactListener(EwendLauncher context) {
        listeners = new Array<PlayerObjectCollisionListener>();
        this.stage = context.getStage();
        audioManager = context.getAudioManager();
    }

    /**
     * Handles all the beginning collisions
     * @param contact Contact info
     */
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

    /**
     * Handles the player starting collisions depending on the collision source
     * @param playerFixture Player Fixture
     * @param fixtureB Fixture of the other soucre
     * @param contact Contact info
     */
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
                ECSEngine.attCmpMapper.get((Entity) fixtureB.getBody().getUserData()).attackDelayAccum =0;
                break;
            case  BIT_ENEMY_ATTACK:
                playerComponent.playerState = PlayerState.DAMAGED;
                playerComponent.milisecAccum=0;
                applyHit(8,8,bodyB,playerBody);
                stage.getRoot().fire(new PlayerHit(((AttackComponent)bodyB.getUserData()).damage));
                audioManager.playAudio(AudioType.EWEND_DAMAGED);
                break;
            case  BIT_GROUND:

                if(contact.getWorldManifold().getNormal().angleDeg()<170f&&contact.getWorldManifold().getNormal().angleDeg()>5f){
                    playerComponent.touchingGround=true;
                }
                break;
            case BIT_GAME_OBJECT:
                gameObj = (Entity) bodyB.getUserData();
                ECSEngine.gameObjCmpMapper.get(gameObj).touched=true;
                for(PlayerObjectCollisionListener listener : listeners){
                    listener.playerCollision(player,gameObj);
                }
                break;
            default:
                break;
        }

    }

    /**
     * Handles all the enemy starting collisions depending on the source
     * @param enemyFixture Enemy Fixture
     * @param fixtureB Other source entity
     * @param contact Conctact info
     */
    private void enemyCollions(Fixture enemyFixture,Fixture fixtureB,Contact contact){
        short categoryBitsB= fixtureB.getFilterData().categoryBits;
        final Body bodyB =fixtureB.getBody();
        final Entity enemy=(Entity) enemyFixture.getBody().getUserData();
        final AIComponent aiComponent = ECSEngine.aiCmoMapper.get(enemy);
        switch (categoryBitsB){
            case BIT_PLAYER_ATTACK:
                LifeComponent enemyLifeComp = ECSEngine.lifeCmpMapper.get(enemy);
                enemyLifeComp.applyHealth(((AttackComponent) fixtureB.getBody().getUserData()).damage);
                aiComponent.state=AIState.HITTED;
                applyHit(12,12,bodyB,enemyFixture.getBody());
                aiComponent.milisecAccum=0;
                audioManager.playAudio(AudioType.SHEEP_DAMAGED);
                break;
            case BIT_BOUND:
                aiComponent.state = AIState.IDLE;
                enemyFixture.getBody().setGravityScale(0.1f);
                aiComponent.milisecAccum=0;
                break;
            default:
                break;
        }

    }

    /**
     * Applys a hit on the body selected
     * @param forceY Force on y
     * @param forceX Force on x
     * @param bodyHitter Body that hits
     * @param bodyReceiver Body that gets the hit
     */
    private void applyHit(float forceY, float forceX,Body bodyHitter,Body bodyReceiver){
        float hitDirection = 1;
        if(bodyReceiver.getPosition().x<bodyHitter.getPosition().x){
            hitDirection=-1;
        }
        bodyReceiver.applyLinearImpulse(forceX*hitDirection,forceY,bodyReceiver.getWorldCenter().x,bodyReceiver.getWorldCenter().y,true);
    }

    /**
     * Handles all the contact ends
     * @param contact Contact info
     */
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

    /**
     * Handles the situation before the collision, usually used to avoid the collision
     * @param contact Contact info
     * @param oldManifold Old Manifold
     */
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

    /**
     * Handles the moment after the collision ended, in this case used to stick the player to the ground when going upwards a ramp
     * @param contact Contact info
     * @param impulse Impulse
     */
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
            bodyPlayer.setLinearVelocity(ECSEngine.playerCmpMapper.get((Entity) bodyPlayer.getUserData()).speed.x*2*ECSEngine.b2dCmpMapper.get((Entity)bodyPlayer.getUserData()).orientation,0);

        }



    }

    /**
     * Used to diasble the collision of two bodies
     * @param maskBitA Mask bit from A
     * @param maskBitB Mask bit from B
     * @param catFixA Fixture from A
     * @param catFixB Fixture from B
     * @return True if disable
     */
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

    /**
     * Adds the listener to the array
     * @param listener Listener
     */
    public void addPlayerCollisionListener(final PlayerObjectCollisionListener listener){
        listeners.add(listener);
    }

    /**
     * Listener for the playerCollsion 
     */
    public interface PlayerObjectCollisionListener {
        void playerCollision(final Entity player, final Entity gameObject);
    }
}
