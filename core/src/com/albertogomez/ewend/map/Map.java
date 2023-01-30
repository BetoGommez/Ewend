package com.albertogomez.ewend.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import static com.albertogomez.ewend.constants.Constants.UNIT_SCALE;


public class Map {
    private static final String TAG = Map.class.getSimpleName();
    private final TiledMap tiledMap;
    private final Vector2 startLocation;

    private final Array<CollisionArea> collisionAreas;

    public Map(TiledMap tiledMap) {
        this.tiledMap = tiledMap;

        startLocation = new Vector2();
        collisionAreas = new Array<CollisionArea>();
        parseCollisionLayer();
        parsePlayerStartLocation();
    }

    private void parsePlayerStartLocation(){
        final MapLayer startLocationLayer = tiledMap.getLayers().get("playerLocation");
        if(startLocationLayer==null){
            Gdx.app.debug(TAG,"There wasnt foun any starter location");
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

    private void parseCollisionLayer(){
        //TODO CUANDO CAMBIES DE MAPA COJES DE AQUÍ LAS CAPAS DE COLISIONES
        final MapLayer objetos = tiledMap.getLayers().get("objetos");
        if(objetos==null){
            Gdx.app.debug(TAG,"The colision layer: "+objetos.getName()+" wasn't found");
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
            }else{
                Gdx.app.debug(TAG,"MapObject: "+mapObject+ " is not supported for the collision layer!");
                Gdx.app.debug(TAG,"MapObject: "+mapObject+ " AAAAAAAAAAAAAAAAAAAAAA");

            }
        }
    }

    public Array<CollisionArea> getCollisionAreas() {
        return collisionAreas;
    }

    public Vector2 getStartLocation() {
        return startLocation;
    }
}
