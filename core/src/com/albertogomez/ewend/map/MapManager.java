package com.albertogomez.ewend.map;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.enemy.EnemyType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.physics.box2d.World;

import java.util.EnumMap;

import static com.albertogomez.ewend.constants.Constants.BACKGROUND_PATH;
import static com.albertogomez.ewend.constants.Constants.BIT_GROUND;

public class MapManager {
    private final World world;
    private final Array<Body> bodies;

    private final AssetManager assetManager;

    private final ECSEngine ecsEngine;
    private final Array<Entity> gameObjectsToRemove;

    private MapType currentMapType;
    private Map currentMap;
    private EnumMap<MapType, Map> mapCache;
    private final Array<MapListener> listeners;
    private final EwendLauncher context;

    public MapManager(EwendLauncher context) {
        currentMapType = null;
        currentMap = null;
        world = context.getWorld();
        ecsEngine = context.getEcsEngine();
        assetManager = context.getAssetManager();
        bodies = new Array<Body>();
        mapCache = new EnumMap<MapType, Map>(MapType.class);
        listeners = new Array<MapListener>();
        gameObjectsToRemove = new Array<Entity>();
        this.context = context;
    }



    public void addMapListener(final MapListener listener){
        listeners.add(listener);
    }

    public void setMap(final MapType type){

        if(currentMap!=null){
            world.getBodies(bodies);
            destroyCollisionAreas();
            destroyGameObjects();
        }

        //set new map
        currentMap = mapCache.get(type);
        if(currentMap==null){
            final TiledMap tiledMap = assetManager.get(type.getFilePath(),TiledMap.class);
            currentMap = new Map(tiledMap,assetManager);
            mapCache.put(type,currentMap);
        }

        //create map entities/bodies

        spawnCollisionAreas();
        spawnGameObjects(currentMap.getFireflyIndexes());
        spawnEnemies();
        spawnPlayer();

        for(final MapListener listener: listeners){
            listener.mapChange(currentMap);
        }
        currentMapType = type;

    }
    private void spawnPlayer(){
        context.getEcsEngine().createPlayer(currentMap.getStartLocation(),0.75f,0.75f);
    }

    private void spawnEnemies(){
        for(final EnemyObject enemyObject : currentMap.getEnemyObjects()){
            ecsEngine.createEnemy(enemyObject.position, EnemyType.valueOf(enemyObject.name), enemyObject.height, enemyObject.width);
        }
    }

    private void spawnGameObjects(Array<Integer> fireflyIndexes){
        for(final GameObject gameObject : currentMap.getGameObjects()){
            if(gameObject.getType()==GameObjectType.FIREFLY){
                if (!fireflyIndexes.contains(gameObject.getIndexProperty(),true)){
                    ecsEngine.createGameObject(gameObject);
                }
            }else{
                ecsEngine.createGameObject(gameObject);
            }
        }
    }

    private void destroyGameObjects(){
        for(final Entity entity : ecsEngine.getEntities()){
            if(ECSEngine.gameObjCmpMapper.get(entity)!=null){
                gameObjectsToRemove.add(entity);
            }
        }
        for(final Entity entity : gameObjectsToRemove){
            ecsEngine.removeEntity(entity);
        }
        gameObjectsToRemove.clear();
    }

    private void destroyCollisionAreas(){
        for(final Body body : bodies){
            if("GROUND".equals(body.getUserData())){
                world.destroyBody(body);
            }

        }
    }

    private void spawnCollisionAreas(){
        EwendLauncher.resetBodyAndFixtureDefinition();
        for(final CollisionArea collisionArea : currentMap.getCollisionAreas()) {
            EwendLauncher.BODY_DEF.position.set(collisionArea.getX(), collisionArea.getY());
            EwendLauncher.BODY_DEF.fixedRotation = true;
            final Body body = world.createBody(EwendLauncher.BODY_DEF);

            body.setUserData("GROUND");
            EwendLauncher.FIXTURE_DEF.filter.categoryBits = BIT_GROUND;
            EwendLauncher.FIXTURE_DEF.filter.maskBits = -1;
            final ChainShape chainShape = new ChainShape();
            chainShape.createChain(collisionArea.getVertices());
            EwendLauncher.FIXTURE_DEF.shape = chainShape;
            body.createFixture(EwendLauncher.FIXTURE_DEF);
            chainShape.dispose();
        }
    }

    public Map getCurrentMap(){ return currentMap;}


    public MapType getCurrentMapType() {
        return currentMapType;
    }
}
