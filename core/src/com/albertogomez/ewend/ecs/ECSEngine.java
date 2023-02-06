package com.albertogomez.ewend.ecs;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.components.AnimationComponent;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.PlayerComponent;
import com.albertogomez.ewend.ecs.system.AnimationSystem;
import com.albertogomez.ewend.ecs.system.PlayerAnimationSystem;
import com.albertogomez.ewend.ecs.system.PlayerCameraSystem;
import com.albertogomez.ewend.ecs.system.PlayerMovementSystem;
import com.albertogomez.ewend.view.AnimationType;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.albertogomez.ewend.EwendLauncher.BODY_DEF;
import static com.albertogomez.ewend.EwendLauncher.FIXTURE_DEF;
import static com.albertogomez.ewend.constants.Constants.*;

public class ECSEngine extends PooledEngine {

    public static final ComponentMapper<PlayerComponent> playerCmpMapper = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<B2DComponent> b2dCmpMapper = ComponentMapper.getFor(B2DComponent.class);
    public static final ComponentMapper<AnimationComponent> aniCmpMapper = ComponentMapper.getFor(AnimationComponent.class);
    private final World world;

    public ECSEngine(final EwendLauncher context) {
        super();
        world = context.getWorld();

        this.addSystem(new PlayerMovementSystem(context));
        this.addSystem(new PlayerCameraSystem(context));
        this.addSystem(new AnimationSystem(context));
        this.addSystem(new PlayerAnimationSystem(context));

    }


    public void createPlayer(final Vector2 playerSpawnLocation,final float height,final float width){
        final Entity player = this.createEntity();

        //player component
        final  PlayerComponent playerComponent = this.createComponent(PlayerComponent.class);
        playerComponent.speed.set(3,5);
        player.add(playerComponent);

        //box2d
        EwendLauncher.resetBodieAndFixtureDefinition();
        final B2DComponent b2DComponent = this.createComponent(B2DComponent.class);

        BODY_DEF.type = BodyDef.BodyType.DynamicBody;
        BODY_DEF.position.set(playerSpawnLocation.x,playerSpawnLocation.y);
        BODY_DEF.gravityScale=1;
        BODY_DEF.fixedRotation=true;

        b2DComponent.body = world.createBody(BODY_DEF);
        b2DComponent.body.setUserData("PLAYER");

        b2DComponent.width = width;
        b2DComponent.height = height;
        b2DComponent.renderPosition.set(b2DComponent.body.getPosition());


        FIXTURE_DEF.density=1;
        FIXTURE_DEF.isSensor=false;
        FIXTURE_DEF.restitution=0;

        FIXTURE_DEF.friction=0;
        FIXTURE_DEF.filter.categoryBits = BIT_PLAYER;
        FIXTURE_DEF.filter.maskBits = BIT_GROUND;
        final PolygonShape pShape = new PolygonShape();
        pShape.setAsBox(0.4f,0.4f);
        FIXTURE_DEF.shape = pShape;
        b2DComponent.body.createFixture(FIXTURE_DEF);
        pShape.dispose();
        player.add(b2DComponent);


        //animation component
        final AnimationComponent animationComponent = this.createComponent(AnimationComponent.class);
        animationComponent.aniType = AnimationType.PLAYER_IDLE;
        animationComponent.width = 80 * UNIT_SCALE;
        animationComponent.height = 80 * UNIT_SCALE;
        player.add(animationComponent);


        this.addEntity(player);
    }



}
