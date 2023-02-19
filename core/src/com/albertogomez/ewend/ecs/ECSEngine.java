package com.albertogomez.ewend.ecs;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ai.AIComponent;
import com.albertogomez.ewend.ecs.ai.AISystem;
import com.albertogomez.ewend.ecs.components.*;
import com.albertogomez.ewend.ecs.components.enemy.EnemyComponent;
import com.albertogomez.ewend.ecs.components.enemy.EnemyType;
import com.albertogomez.ewend.ecs.system.*;
import com.albertogomez.ewend.events.EnemyDied;
import com.albertogomez.ewend.events.PlayerDied;
import com.albertogomez.ewend.map.GameObject;
import com.albertogomez.ewend.view.AnimationType;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

import static com.albertogomez.ewend.EwendLauncher.BODY_DEF;
import static com.albertogomez.ewend.EwendLauncher.FIXTURE_DEF;
import static com.albertogomez.ewend.constants.Constants.*;

public class ECSEngine extends PooledEngine implements EventListener {

    public static final ComponentMapper<PlayerComponent> playerCmpMapper = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<B2DComponent> b2dCmpMapper = ComponentMapper.getFor(B2DComponent.class);
    public static final ComponentMapper<AnimationComponent> aniCmpMapper = ComponentMapper.getFor(AnimationComponent.class);
    public static final ComponentMapper<GameObjectComponent> gameObjCmpMapper = ComponentMapper.getFor(GameObjectComponent.class);
    public static final ComponentMapper<EnemyComponent> eneObjCmpMapper = ComponentMapper.getFor(EnemyComponent.class);
    public static final ComponentMapper<AIComponent> aiCmoMapper = ComponentMapper.getFor(AIComponent.class);
    public static final ComponentMapper<AttackComponent> attCmpMapper = ComponentMapper.getFor(AttackComponent.class);
    public static final ComponentMapper<LifeComponent> lifeCmpMapper = ComponentMapper.getFor(LifeComponent.class);
    public static final ComponentMapper<DeadComponent> deadCmpMapper = ComponentMapper.getFor(DeadComponent.class);



    private final RayHandler rayHandler;
    private final Vector2 localPosition;
    private final Vector2 posBeforeRotation;
    private final Vector2 posAfterRotation;

    private final EwendLauncher context;
    private final World world;

    public ECSEngine(final EwendLauncher context) {
        super();
        world = context.getWorld();
        this.context = context;
        context.getStage().getRoot().addListener(this);
        rayHandler = context.getRayHandler();
        localPosition = new Vector2();
        posBeforeRotation = new Vector2();
        posAfterRotation = new Vector2();
        addSystems();

    }

    public void addSystems(){
        this.addSystem(new AnimationSystem(context));
        this.addSystem(new PlayerAnimationSystem(context));
        this.addSystem(new LightSystem());
        this.addSystem(new EnemyAnimationSystem(context));
        this.addSystem(new AISystem(context));
        this.addSystem(new AttackSystem(context));
        this.addSystem(new LifeSystem(context));
        this.addSystem(new DeadSystem(context));
        this.addSystem(new PlayerMovementSystem(context));
        this.addSystem(new PlayerCollisionSystem(context));
        this.addSystem(new ObjectAnimationSystem(context));
        this.addSystem(new DespawnObjectSystem());
    }



    public Entity createEnemy(final Vector2 spawnLocation, EnemyType type,final float height,final float width){
        final Entity enemy = this.createEntity();
        //enemy component
        final EnemyComponent enemyComponent = this.createComponent(EnemyComponent.class);
        enemyComponent.speed.set(0.5f,0);
        enemyComponent.enemyType = type;
        enemy.add(enemyComponent);

        //box2d
        EwendLauncher.resetBodyAndFixtureDefinition();
        final B2DComponent b2DComponent = this.createComponent(B2DComponent.class);

        BODY_DEF.type = BodyDef.BodyType.DynamicBody;
        BODY_DEF.position.set(spawnLocation.x,spawnLocation.y);
        BODY_DEF.gravityScale=1;
        BODY_DEF.fixedRotation=true;

        b2DComponent.body = world.createBody(BODY_DEF);
        b2DComponent.body.setUserData(enemy);

        b2DComponent.width = width*0.5f;
        b2DComponent.height = height*0.5f;
        b2DComponent.renderPosition.set(b2DComponent.body.getPosition());


        FIXTURE_DEF.density=1;
        FIXTURE_DEF.isSensor=false;
        FIXTURE_DEF.restitution=0;

        FIXTURE_DEF.friction=0;
        FIXTURE_DEF.filter.categoryBits = BIT_ENEMY;
        FIXTURE_DEF.filter.maskBits = (BIT_GROUND|BIT_PLAYER|BIT_PLAYER_ATTACK);
        final PolygonShape pShape = new PolygonShape();
        pShape.setAsBox(b2DComponent.height, b2DComponent.width);
        FIXTURE_DEF.shape = pShape;
        b2DComponent.body.createFixture(FIXTURE_DEF);
        pShape.dispose();
        enemy.add(b2DComponent);

        //create enemy light
        b2DComponent.lightDistance = 2;
        b2DComponent.lightFluctuationSpeed = 4;
        b2DComponent.light = new PointLight(rayHandler,32, new Color(1,1,1,0.73f),7,b2DComponent.body.getPosition().x,b2DComponent.body.getPosition().y);
        b2DComponent.lightFluctuationDistance = b2DComponent.light.getDistance()*0.02f;
        b2DComponent.light.attachToBody(b2DComponent.body);

        //animation component
        final AnimationComponent animationComponent = this.createComponent(AnimationComponent.class);
        animationComponent.aniType = type.defaultAnimation;
        animationComponent.width = width ;
        animationComponent.height = height ;
        enemy.add(animationComponent);

        //ai component
        final AIComponent aiComponent = this.createComponent(AIComponent.class);
        aiComponent.idleDelay =3;
        aiComponent.maxDistanceFactor = 0.9f;
        aiComponent.initialPosition = spawnLocation;
        enemy.add(aiComponent);

        //life component

        final LifeComponent lifeComponent = new LifeComponent(context.getStage());
        lifeComponent.health=1f;
        lifeComponent.mana=50f;
        lifeComponent.isEnemy=true;
        enemy.add(lifeComponent);

        //attack component
        final AttackComponent attackComponent = this.createComponent(AttackComponent.class);
        attackComponent.damage=5;
        attackComponent.attacking=false;
        attackComponent.attackHitboxHeight= b2DComponent.height;
        attackComponent.attackHitboxWidth=b2DComponent.width*1.5f;
        attackComponent.delay=0.5f;
        attackComponent.delayAccum=0;
        enemy.add(attackComponent);

        this.addEntity(enemy);
        return enemy;
    }


    public Entity createPlayer(final Vector2 playerSpawnLocation,final float height,final float width){
        final Entity player = this.createEntity();

        //player component
        final  PlayerComponent playerComponent = this.createComponent(PlayerComponent.class);
        playerComponent.speed.set(4,15);
        player.add(playerComponent);
        ///remove component
        final RemoveComponent removeComponent = this.createComponent(RemoveComponent.class);
        player.add(removeComponent);

        //box2d
        EwendLauncher.resetBodyAndFixtureDefinition();
        final B2DComponent b2DComponent = this.createComponent(B2DComponent.class);

        BODY_DEF.type = BodyDef.BodyType.DynamicBody;
        BODY_DEF.position.set(playerSpawnLocation.x,playerSpawnLocation.y);
        BODY_DEF.gravityScale=1;
        BODY_DEF.fixedRotation=true;

        b2DComponent.body = world.createBody(BODY_DEF);
        b2DComponent.body.setUserData(player);
        b2DComponent.width = width;
        b2DComponent.height = height;
        b2DComponent.renderPosition.set(b2DComponent.body.getPosition());


        FIXTURE_DEF.density=1;
        FIXTURE_DEF.isSensor=false;
        FIXTURE_DEF.restitution=0;

        FIXTURE_DEF.friction=0f;
        FIXTURE_DEF.filter.categoryBits = BIT_PLAYER;
        FIXTURE_DEF.filter.maskBits = (BIT_GROUND|BIT_GAME_OBJECT|BIT_ENEMY|BIT_ENEMY_ATTACK);
        final PolygonShape pShape = new PolygonShape();
        pShape.setAsBox(0.5f,0.9f);
        FIXTURE_DEF.shape = pShape;
        b2DComponent.body.createFixture(FIXTURE_DEF);
        pShape.dispose();
        player.add(b2DComponent);

        //create player light
        b2DComponent.lightDistance = 3;
        b2DComponent.lightFluctuationDistance = 0.3f;
        b2DComponent.lightFluctuationSpeed = 2;
        b2DComponent.light = new PointLight(rayHandler,64, new Color(1,1,1,0.7f),6,b2DComponent.body.getPosition().x,b2DComponent.body.getPosition().y);
        b2DComponent.light.attachToBody(b2DComponent.body);

        //animation component
        final AnimationComponent animationComponent = this.createComponent(AnimationComponent.class);
        animationComponent.aniType = AnimationType.PLAYER_IDLE;
        animationComponent.width = 55 * UNIT_SCALE;
        animationComponent.height = 55 * UNIT_SCALE;
        player.add(animationComponent);
        //life component
        final LifeComponent lifeComponent = new LifeComponent(context.getStage());
        lifeComponent.health=100f;
        lifeComponent.mana=0f;
        lifeComponent.isEnemy=false;
        player.add(lifeComponent);

        //attack component
        final AttackComponent attackComponent = this.createComponent(AttackComponent.class);
        attackComponent.damage=30f;
        attackComponent.attacking=false;
        attackComponent.delay=1f;
        attackComponent.delayAccum=1f;
        attackComponent.attackHitboxHeight= b2DComponent.height/2;
        attackComponent.attackHitboxWidth=b2DComponent.width*1.5f;
        player.add(attackComponent);



        this.addEntity(player);
        return player;
    }

    public void createGameObject(final GameObject gameObject){
        final Entity gameObjEntity = this.createEntity();

        //GameobjectComponent
        final GameObjectComponent gameObjectComponent  = this.createComponent(GameObjectComponent.class);
        gameObjectComponent.animationIndex = gameObject.getAnimationIndex();
        gameObjectComponent.type = gameObject.getType();
        if(gameObject.getIndexProperty()!=-1){
            gameObjectComponent.index = gameObject.getIndexProperty();
        }
        gameObjEntity.add(gameObjectComponent);

        //AnimationComponent
        final AnimationComponent animationComponent = this.createComponent(AnimationComponent.class);
        animationComponent.aniType = null;
        animationComponent.aniTime=0.0f;
        animationComponent.width = gameObject.getWidth();
        animationComponent.height= gameObject.getHeight();
        gameObjEntity.add(animationComponent);



        //box2d
        EwendLauncher.resetBodyAndFixtureDefinition();
        final float halfW = gameObject.getWidth() * 0.5f;
        final float halfH = gameObject.getHeight() * 0.5f;
        final float angleRed = gameObject.getRotDegree() * MathUtils.degreesToRadians;
        final B2DComponent b2DComponent = this.createComponent(B2DComponent.class);
        BODY_DEF.type = BodyDef.BodyType.StaticBody;
        BODY_DEF.position.set(gameObject.getPosition().x+halfW,gameObject.getPosition().y+halfH);
        b2DComponent.body = world.createBody(BODY_DEF);
        b2DComponent.body.setUserData(gameObjEntity);
        b2DComponent.width = gameObject.getWidth();
        b2DComponent.height = gameObject.getHeight();

        //save position before rotation
        localPosition.set(-halfW,-halfH);
        posBeforeRotation.set(b2DComponent.body.getWorldPoint(localPosition));
        //rotate body
        b2DComponent.body.setTransform(b2DComponent.body.getPosition(),angleRed);
        //get position after rotation
        posAfterRotation.set(b2DComponent.body.getWorldPoint(localPosition));
        //adjust position to original
        b2DComponent.body.setTransform(b2DComponent.body.getPosition().add(posBeforeRotation).sub(posAfterRotation),angleRed);
        b2DComponent.renderPosition.set(b2DComponent.body.getPosition().x-animationComponent.width*0.5f,b2DComponent.body.getPosition().y - animationComponent.height * 0.5f);

        FIXTURE_DEF.filter.categoryBits = BIT_GAME_OBJECT;
        FIXTURE_DEF.filter.maskBits = BIT_PLAYER;
        final PolygonShape pShape = new PolygonShape();
        pShape.setAsBox(halfW,halfH);
        FIXTURE_DEF.shape = pShape;
        b2DComponent.body.createFixture(FIXTURE_DEF);
        pShape.dispose();
        gameObjEntity.add(b2DComponent);

        this.addEntity(gameObjEntity);
    }

    @Override
    public boolean handle(Event event) {
        if(event instanceof PlayerDied){
            this.removeSystem(getSystem(PlayerMovementSystem.class));
        } else if (event instanceof EnemyDied) {

        }
        return false;
    }
}
