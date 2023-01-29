package com.albertogomez.ewend.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


public class Map {
    private static final String TAG = Map.class.getSimpleName();
    private final TiledMap tiledMap;

    private final Array<CollisionArea> collisionAreas;

    public Map(TiledMap tiledMap) {
        this.tiledMap = tiledMap;

        collisionAreas = new Array<CollisionArea>();
        parseCollisionLayer();
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
            } else if (mapObject instanceof PolylineMapObject) {
                final PolylineMapObject polylineMapObject = (PolylineMapObject) mapObject;
                final Polyline polyline = polylineMapObject.getPolyline();
                collisionAreas.add(new CollisionArea(polyline.getX(),polyline.getY(), polyline.getVertices()));
            }else{
                Gdx.app.debug(TAG,"MapObject: "+mapObject+ " is not supported for the collision layer!");
            }
        }
    }
}
