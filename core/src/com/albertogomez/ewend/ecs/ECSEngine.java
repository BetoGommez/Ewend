package com.albertogomez.ewend.ecs;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.PlayerComponent;
import com.albertogomez.ewend.ecs.system.PlayerMovementSystem;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.albertogomez.ewend.constants.Constants.BIT_GROUND;
import static com.albertogomez.ewend.constants.Constants.BIT_PLAYER;

public class ECSEngine extends PooledEngine {

    public static final ComponentMapper<PlayerComponent> playerCmpMapper = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<B2DComponent> b2dCmpMapper = ComponentMapper.getFor(B2DComponent.class);
    private final World world;
    private final FixtureDef fixtureDef;
    private final BodyDef bodyDef;

    public ECSEngine(final EwendLauncher context) {
        super();
        world = context.getWorld();
        fixtureDef = new FixtureDef();
        bodyDef = new BodyDef();

        this.addSystem(new PlayerMovementSystem(context));
    }


    public void createPlayer(final Vector2 playerSpawnLocation,final float height,final float width){
        final Entity player = this.createEntity();

        //player component
        player.add(this.createComponent(PlayerComponent.class));

        //box2d
        resetBodieAndFixtureDefinition();
        final B2DComponent b2DComponent = this.createComponent(B2DComponent.class);

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(playerSpawnLocation.x,playerSpawnLocation.y);
        bodyDef.gravityScale=1;
        bodyDef.fixedRotation=true;

        b2DComponent.body = world.createBody(bodyDef);
        b2DComponent.body.setUserData("PLAYER");
        b2DComponent.width = width;
        b2DComponent.height = height;

        fixtureDef.density=1;
        fixtureDef.isSensor=false;
        fixtureDef.restitution=0;
        fixtureDef.friction=1;
        fixtureDef.filter.categoryBits = BIT_PLAYER;
        fixtureDef.filter.maskBits = BIT_GROUND;
        final PolygonShape pShape = new PolygonShape();
        pShape.setAsBox(0.5f,0.5f);
        fixtureDef.shape = pShape;
        b2DComponent.body.createFixture(fixtureDef);
        pShape.dispose();
        player.add(b2DComponent);

        this.addEntity(player);
    }


    private void resetBodieAndFixtureDefinition(){

        bodyDef.position.set(0,0);
        bodyDef.fixedRotation=false;
        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.StaticBody;
        fixtureDef.density=0;
        fixtureDef.isSensor=false;
        fixtureDef.restitution=0;
        fixtureDef.friction=0.2f;

        fixtureDef.filter.categoryBits = 0x0001;
        fixtureDef.filter.maskBits = -1;
        fixtureDef.shape = null;
    }
}
