package com.albertogomez.ewend.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

import static com.albertogomez.ewend.constants.Constants.UNIT_SCALE;


public class Map {
    private static final String TAG = Map.class.getSimpleName();
    private final TiledMap tiledMap;
    private final Vector2 startLocation;

    public static float MAP_WIDTH ;
    public static float MAP_HEIGHT ;

    private final Array<GameObject> gameObjects;
    private final IntMap<Animation<Sprite>> mapAnimations;

    private final Array<CollisionArea> collisionAreas;

    private final Array<EnemyObject> enemyObjects;

    public Map(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        collisionAreas = new Array<CollisionArea>();

        startLocation = new Vector2();

        mapAnimations = new IntMap<Animation<Sprite>>();
        gameObjects = new Array<GameObject>();
        enemyObjects = new Array<EnemyObject>();
        parseCollisionLayer();

        parsePlayerStartLocation();
        parseMapObjectLayer();
        parseEnemiesInfo();
        MAP_WIDTH =Float.parseFloat(tiledMap.getProperties().get("width").toString());
        MAP_HEIGHT =Float.parseFloat(tiledMap.getProperties().get("height").toString());
    }

    private void parsePlayerStartLocation(){
        final MapLayer startLocationLayer = tiledMap.getLayers().get("playerLocation");
        if(startLocationLayer==null){
            Gdx.app.debug(TAG,"There wasnt found any starter location");
            return;
        }
        final MapObjects objects = startLocationLayer.getObjects();
        for (final MapObject mapObj : objects){
            if(mapObj instanceof RectangleMapObject){
                final RectangleMapObject rectangleMapObject = (RectangleMapObject) mapObj;
                startLocation.set(rectangleMapObject.getRectangle().x*UNIT_SCALE,rectangleMapObject.getRectangle().y*UNIT_SCALE);

            }
        }
    }

    private void parseEnemiesInfo(){
        final MapLayer gameEnemiesLayer = tiledMap.getLayers().get("enemies");
        String name;
        if (gameEnemiesLayer==null){
            Gdx.app.debug("Map","There is no enemies in this layer");
            return;
        }

        final MapObjects objects = gameEnemiesLayer.getObjects();
        for(final MapObject object : objects){

            if(!(object instanceof TiledMapTileMapObject)){
                Gdx.app.debug("Map","Enemy of type: "+object+" is not supported!");
                continue;
            }
            final TiledMapTileMapObject tiledMapObj = (TiledMapTileMapObject) object;
            final MapProperties tiledMapObjProperties = tiledMapObj.getProperties();
            final MapProperties tileProperties = tiledMapObj.getTile().getProperties();
            if(tiledMapObj.getName()!=null) {
                name = tiledMapObj.getName();
            } else{
                Gdx.app.debug("Map","There is no name defined for tile: "+tiledMapObj.getProperties().get("id", Integer.class));
                continue;
            }

            final float width = tiledMapObjProperties.get("width",Float.class)*UNIT_SCALE;
            final float height = tiledMapObjProperties.get("height",Float.class)*UNIT_SCALE;
            enemyObjects.add(new EnemyObject(name, new Vector2(tiledMapObj.getX()*UNIT_SCALE,tiledMapObj.getY()*UNIT_SCALE),64*UNIT_SCALE/3,64*UNIT_SCALE/3));
        }

    }

    private void parseCollisionLayer(){
        //TODO CUANDO CAMBIES DE MAPA COJES DE AQUÍ LAS CAPAS DE COLISIONES
        final MapLayer objetos = tiledMap.getLayers().get("Ground");
        if(objetos==null){
            Gdx.app.debug(TAG,"The collision layer:  wasn't found");
            return;
        }

        final MapObjects mapObjects = objetos.getObjects();
        if(mapObjects==null){
            Gdx.app.debug(TAG,"There wasn't any collision on this layer");
            return;
        }


        //Aquí divide el tipo de objeto
        for(final MapObject mapObject: mapObjects){

            if(mapObject instanceof  RectangleMapObject){
                final RectangleMapObject rectangleMapObject = (RectangleMapObject) mapObject;
                final Rectangle rectangle = rectangleMapObject.getRectangle();
                final float[] rectVertices = new float[10];

                //left-bot
                    rectVertices[0] = 0;
                    rectVertices[1] = 0;
                //left-top
                    rectVertices[2] = 0;
                    rectVertices[3] = rectangle.getHeight();

                //right-top
                    rectVertices[4] = rectangle.getWidth();
                    rectVertices[5] = rectangle.getHeight();

                //right-bottom
                    rectVertices[6] = rectangle.getWidth();
                    rectVertices[7] = 0;

                //left-bottom
                    rectVertices[8] = 0;
                    rectVertices[9] = 0;


                    collisionAreas.add(new CollisionArea(rectangle.x,rectangle.y,rectVertices));

            } else if (mapObject instanceof PolylineMapObject) {

                final PolylineMapObject polylineMapObject = (PolylineMapObject) mapObject;
                final Polyline polyline = polylineMapObject.getPolyline();
                collisionAreas.add(new CollisionArea(polyline.getX(),polyline.getY(), polyline.getVertices()));
            }else if (mapObject instanceof PolygonMapObject){
                final PolygonMapObject polygonMapObject = (PolygonMapObject) mapObject;
                final Polygon polygon = polygonMapObject.getPolygon();
                collisionAreas.add(new CollisionArea(polygon.getX(),polygon.getY(),polygon.getVertices()));
            }
            else{
                Gdx.app.debug(TAG,"MapObject: "+mapObject+ " is not supported for the collision layer!");
                Gdx.app.debug(TAG,"MapObject: "+mapObject+ " AAAAAAAAAAAAAAAAAAAAAA");
            }
        }
    }

    private void parseMapObjectLayer(){
        final MapLayer gameObjectsLayer = tiledMap.getLayers().get("objects");
        if (gameObjectsLayer==null){
            Gdx.app.debug("Map","There is no gameobjects in this layer");
            return;
        }

        final MapObjects objects = gameObjectsLayer.getObjects();
        for(final MapObject object : objects){
            if(!(object instanceof TiledMapTileMapObject)){
                Gdx.app.debug("Map","GameObject of type: "+object+" is not supported!");
                continue;
            }

            final TiledMapTileMapObject tiledMapObj = (TiledMapTileMapObject) object;

            final MapProperties tiledMapObjProperties = tiledMapObj.getProperties();
            final MapProperties tileProperties = tiledMapObj.getTile().getProperties();
            final GameObjectType gameObjType;
            if(tiledMapObjProperties.containsKey("type")){
                gameObjType = GameObjectType.valueOf(tiledMapObjProperties.get("type",String.class));
            }else if(tileProperties.containsKey("type")){
                gameObjType = GameObjectType.valueOf(tileProperties.get("type",String.class));
            }else{
                Gdx.app.debug("Map","There is no gameobjecttype defined for tile: "+tiledMapObj.getProperties().get("id", Integer.class));
                continue;
            }

            final int animationIndex = tiledMapObj.getTile().getId();
            if(!createAnimation(animationIndex,tiledMapObj.getTile())){
                Gdx.app.debug("Map","Could not create animation for: "+tiledMapObj.getProperties().get("id", Integer.class));
                continue;

            }



            final float width = tiledMapObjProperties.get("width",Float.class)*UNIT_SCALE;
            final float height = tiledMapObjProperties.get("height",Float.class)*UNIT_SCALE;
            gameObjects.add(new GameObject(gameObjType, new Vector2(tiledMapObj.getX()*UNIT_SCALE,tiledMapObj.getY()*UNIT_SCALE),width,height,tiledMapObj.getRotation(),animationIndex));
        }


    }

    private boolean createAnimation(final int animationIndex, final TiledMapTile tile){
        Animation<Sprite> animation = mapAnimations.get(animationIndex);
        if(animation==null){
            Gdx.app.debug("Map","Creating new map animation for tile: "+tile.getId());

            if(tile instanceof AnimatedTiledMapTile){
                final AnimatedTiledMapTile aniTile = (AnimatedTiledMapTile) tile;
                final Sprite[] keyframes = new Sprite[aniTile.getFrameTiles().length];
                int i=0;
                for(final StaticTiledMapTile staticTile : aniTile.getFrameTiles()){
                    keyframes[i++] = new Sprite(staticTile.getTextureRegion());
                }
                animation = new Animation<Sprite>(aniTile.getAnimationIntervals()[0] * 0.001f,keyframes);
                animation.setPlayMode(Animation.PlayMode.LOOP);
                mapAnimations.put(animationIndex,animation);
            }else if(tile instanceof StaticTiledMapTile){
                animation = new Animation<Sprite>(0,new Sprite(tile.getTextureRegion()));
                mapAnimations.put(animationIndex,animation);
            }else{
                Gdx.app.log("Map","Tile of file "+tile+" is not supported for map animation");
                return false;
            }

        }
        return true;
    }

    public Array<CollisionArea> getCollisionAreas() {
        return collisionAreas;
    }

    public Vector2 getStartLocation() {
        return startLocation;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public Array<GameObject> getGameObjects() {
        return gameObjects;
    }

    public IntMap<Animation<Sprite>> getMapAnimations() {
        return mapAnimations;
    }

    public Array<EnemyObject> getEnemyObjects() {
        return enemyObjects;
    }
}
