package com.albertogomez.ewend.ecs.system;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.AttackComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import static com.albertogomez.ewend.EwendLauncher.BODY_DEF;
import static com.albertogomez.ewend.EwendLauncher.FIXTURE_DEF;
import static com.albertogomez.ewend.constants.Constants.*;

/**
 * Class that process the Damage Component entities attacks
 * @author Alberto Gómez
 */
public class AttackSystem extends IteratingSystem implements EventListener {


    /**
     * Game world
     */
    private World world;

    /**
     * Constructor that indicates which entities to process
     * @param context Game main class
     */
    public AttackSystem(EwendLauncher context) {
        super(Family.all(AttackComponent.class, B2DComponent.class).get());
        context.getStage().addListener(this);
        world = context.getWorld();


    }

    /**
     * Process the entity attack, if the delay has been done creates the attack box
     * @param entity The current Entity being processed
     * @param deltaTime The delta time between the last and current frame
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        AttackComponent attackComponent = ECSEngine.attCmpMapper.get(entity);
        attackComponent.attackDelayAccum += deltaTime;
        if(attackComponent.attacking){
                createAttackBox(attackComponent,entity);
                attackComponent.attackDelayAccum =0;
        }
        if(attackComponent.attackBox!=null&&attackComponent.attackDelayAccum >attackComponent.attackDelay /6){
            world.destroyBody(attackComponent.attackBox);
            attackComponent.attackBox=null;
        }
        attackComponent.attacking=false;
    }

    /**
     * Creates the attackBox that collides with other entities
     * @param attackComponent Attack Component that has the damage values
     * @param entity Entity that creates the box
     */
    private void createAttackBox(AttackComponent attackComponent,Entity entity){
        B2DComponent b2DComponent = ECSEngine.b2dCmpMapper.get(entity);
        Body entityBody = b2DComponent.body;
        EwendLauncher.resetBodyAndFixtureDefinition();
        b2DComponent.body.setLinearVelocity(0,b2DComponent.body.getLinearVelocity().y);
        float positionX = 0;
        if(b2DComponent.orientation==1){
            positionX = entityBody.getPosition().x;
        }else{
            positionX = entityBody.getPosition().x- attackComponent.attackHitboxWidth;
        }
        EwendLauncher.BODY_DEF.position.set(positionX+attackComponent.attackHitboxWidth/2,
                entityBody.getPosition().y);
        EwendLauncher.BODY_DEF.type = BodyDef.BodyType.StaticBody;
        BODY_DEF.fixedRotation=true;

        Body body = world.createBody(BODY_DEF);

        body.setUserData(attackComponent);


        FIXTURE_DEF.density=1;
        FIXTURE_DEF.isSensor=false;
        FIXTURE_DEF.restitution=0;

        FIXTURE_DEF.friction=0;
        if(ECSEngine.playerCmpMapper.get(entity)!=null){
            FIXTURE_DEF.filter.categoryBits = BIT_PLAYER_ATTACK;
            FIXTURE_DEF.filter.maskBits = BIT_ENEMY;
        }else{
            FIXTURE_DEF.filter.categoryBits = BIT_ENEMY_ATTACK;
            FIXTURE_DEF.filter.maskBits = BIT_PLAYER;
        }
        final PolygonShape pShape = new PolygonShape();
        pShape.setAsBox(attackComponent.attackHitboxWidth,attackComponent.attackHitboxHeight);
        FIXTURE_DEF.shape = pShape;
        body.createFixture(FIXTURE_DEF);
        attackComponent.attackBox= body;
        pShape.dispose();

    }

    /**
     * Event input handler
     * @param event Imcoming event
     * @return Always true
     */
    @Override
    public boolean handle(Event event) {

        return true;
    }
}
