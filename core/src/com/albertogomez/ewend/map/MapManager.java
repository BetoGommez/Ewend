package com.albertogomez.ewend.map;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.physics.box2d.World;

import java.util.EnumMap;

import static com.albertogomez.ewend.constants.Constants.BIT_GROUND;

public class MapManager {
    public static final String TAG = MapManager.class.getSimpleName();
    private final World world;
    private final Array<Body> bodies;

    private final AssetManager assetManager;

    private MapType currentMapType;
    private Map currentMap;
    private EnumMap<MapType, Map> mapCache;
    private final Array<MapListener> listeners;
    private final EwendLauncher context;

    public MapManager(EwendLauncher context) {
        currentMapType = null;
        currentMap = null;
        world = context.getWorld();
        assetManager = context.getAssetManager();
        bodies = new Array<Body>();
        mapCache = new EnumMap<MapType, Map>(MapType.class);
        listeners = new Array<MapListener>();
        this.context = context;
    }

    public void addMapListener(final MapListener listener){
        listeners.add(listener);
    }

    public void setMap(final MapType type){
        if(currentMapType==type){
            return;
        }

        if(currentMap!=null){
            world.getBodies(bodies);
            destroyCollisionAreas();
        }

        //set new map
        Gdx.app.debug(TAG,"Changing to map "+type);
        currentMap = mapCache.get(type);
        if(currentMap==null){
            Gdx.app.debug(TAG,"Creating new map of type "+type);
            final TiledMap tiledMap = assetManager.get(type.getFilePath(),TiledMap.class);
            currentMap = new Map(tiledMap);
            mapCache.put(type,currentMap);
        }

        //create map entities/bodies
        spawnCollisionAreas();

        for(final MapListener listener: listeners){
            listener.mapChange(currentMap);
        }

        context.getEcsEngine().removeAllEntities();
    }

    private void destroyCollisionAreas(){
        for(final Body body : bodies){
            if("GROUND".equals(body.getUserData())){
                world.destroyBody(body);
            }
        }
    }

    private void spawnCollisionAreas(){
        EwendLauncher.resetBodieAndFixtureDefinition();
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
