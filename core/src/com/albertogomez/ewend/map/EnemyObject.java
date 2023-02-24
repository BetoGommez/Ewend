package com.albertogomez.ewend.map;

import com.badlogic.gdx.math.Vector2;

/**
 * Enemy Object info on the tiled map
 * @author Alberto Gómez
 */
public class EnemyObject {
    /**
     * Initial position on map
     */
    public Vector2 position;
    /**
     * Enemy name
     */
    public String name;
    /**
     * Enemy width
     */
    public float width;
    /**
     * Enemy height
     */
    public float height;

    /**
     * Constructor that sets all the values
     * @param name Enemy name
     * @param position Initial position on map
     * @param width Enemy width
     * @param height Enemy height
     */
    public EnemyObject(String name,Vector2 position,float width,float height) {
        this.position = position;
        this.name = name;
        this.width = width;
        this.height = height;
    }
}
