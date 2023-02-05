package com.albertogomez.ewend.map;

import com.badlogic.gdx.assets.AssetManager;

public class MapManager {
    public static final String TAG =MapManager.class.getSimpleName();
    private final World world;
    private final Array<Body> boides;

    private final AssetManager assetManager;

    private MapType currentMapType;
    private Map currentMap;
    private EnumMap<MapTypre,Map> mapCache;
    private final Array<MapListener> listeners;

    public MapManager() {

    }
}
