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

import static com.albertogomez.ewend.EwendLauncher.BODY_DEF;
import static com.albertogomez.ewend.EwendLauncher.FIXTURE_DEF;
import static com.albertogomez.ewend.constants.Constants.*;

public class AttackSystem extends IteratingSystem {


    private World world;
    public AttackSystem(EwendLauncher context) {
        super(Family.all(AttackComponent.class, B2DComponent.class).get());

        world = context.getWorld();

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Body entityBody = ECSEngine.b2dCmpMapper.get(entity).body;
        AttackComponent attackComponent = ECSEngine.attCmpMapper.get(entity);
        attackComponent.delayAccum+=deltaTime;
        if(attackComponent.attacking&& attackComponent.canAttack()){
            if(attackComponent.canAttack()){
                attackComponent.delayAccum=0;
                attackComponent.attacking=false;
                createAttackBox(attackComponent,entity);
            }
        }
    }

    private void createAttackBox(AttackComponent attackComponent,Entity entity){
        Body entityBody = ECSEngine.b2dCmpMapper.get(entity).body;
        EwendLauncher.resetBodyAndFixtureDefinition();
        EwendLauncher.BODY_DEF.position.set(entityBody.getPosition().x+attackComponent.attackHitboxWidth/2,
                entityBody.getPosition().y);
        EwendLauncher.BODY_DEF.type = BodyDef.BodyType.StaticBody;
        BODY_DEF.fixedRotation=true;

        Body body = world.createBody(BODY_DEF);

        body.setUserData(entity);


        FIXTURE_DEF.density=1;
        FIXTURE_DEF.isSensor=false;
        FIXTURE_DEF.restitution=0;

        FIXTURE_DEF.friction=0;
        if(attackComponent.isPlayer){
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
        pShape.dispose();
    }
}
