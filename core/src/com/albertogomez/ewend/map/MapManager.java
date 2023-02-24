package com.albertogomez.ewend.map;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.enemy.EnemyType;
import com.albertogomez.ewend.view.AnimationType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.physics.box2d.World;

import java.util.EnumMap;

import static com.albertogomez.ewend.constants.Constants.*;

/**
 * Map info handler
 * @author Alberto Gómez
 */
public class MapManager {
    /**
     * Game World
     */
    private final World world;
    /**
     * Bodies to be created on map
     */
    private final Array<Body> bodies;
    /**
     * Game Asset Manager
     */
    private final AssetManager assetManager;
    /**
     * Entity Component System handler
     */
    private final ECSEngine ecsEngine;
    /**
     * Objects that can be removed on map dispose
     */
    private final Array<Entity> gameObjectsToRemove;

    /**
     * Actual Map Type
     */
    private MapType currentMapType;
    /**
     * Current map info
     */
    private Map currentMap;
    /**
     * Different maps storage
     */
    private EnumMap<MapType, Map> mapCache;
    /**
     * All map listeners
     */
    private final Array<MapListener> listeners;
    /**
     * Main game class
     */
    private final EwendLauncher context;

    /**
     * Constructor tha sets all values
     * @param context Game main class
     */
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


    /**
     * Adds a map listener to the array
     * @param listener Listener
     */
    public void addMapListener(final MapListener listener){
        listeners.add(listener);
    }

    /**
     * Sets the map to be rendered, if it already exists does all the removes and creates all the entities, collisions and objects
     * @param type
     */
    public void setMap(final MapType type){

        if(currentMap!=null){
            world.getBodies(bodies);
            mapDispose();
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

    /**
     * Destroys the objects and collision from the world
     */
    public void mapDispose(){
        destroyCollisionAreas();
        destroyGameObjects();
    }

    /**
     * Creates the player
     */
    private void spawnPlayer(){
        context.getEcsEngine().createPlayer(currentMap.getStartLocation(), AnimationType.PLAYER_IDLE.getHeight()*UNIT_SCALE/4.5f,AnimationType.PLAYER_IDLE.getWidth()*UNIT_SCALE/4.5f);
    }

    /**
     * Spawn all enemies on map
     */
    private void spawnEnemies(){
        for(final EnemyObject enemyObject : currentMap.getEnemyObjects()){
            ecsEngine.createEnemy(enemyObject.position, EnemyType.valueOf(enemyObject.name), enemyObject.height, enemyObject.width);
        }
    }

    /**
     * Spawn all game objects , excepting the already taken firefly
     * @param fireflyIndexes Firefly that has been already taken
     */
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

    /**
     * Destroy all game object from the world
     */
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

    /**
     * Destroy all collision areas from world
     */
    private void destroyCollisionAreas(){
        for(final Body body : bodies){
            if("Destroyable".equals(body.getUserData())){
                world.destroyBody(body);
            }

        }
    }

    /**
     * Spawn all collision areas
     */
    private void spawnCollisionAreas(){
        EwendLauncher.resetBodyAndFixtureDefinition();
        for(final CollisionArea collisionArea : currentMap.getCollisionAreas()) {
            EwendLauncher.BODY_DEF.position.set(collisionArea.getX(), collisionArea.getY());
            EwendLauncher.BODY_DEF.fixedRotation = true;
            final Body body = world.createBody(EwendLauncher.BODY_DEF);
            body.setUserData("Destroyable");
            EwendLauncher.FIXTURE_DEF.filter.categoryBits = collisionArea.getMask();
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
