package com.albertogomez.ewend.map;

import com.badlogic.gdx.math.Vector2;

public class EnemyObject {
    public Vector2 position;
    public String name;
    public float width;
    public float height;

    public EnemyObject(String name,Vector2 position,float width,float height) {
        this.position = position;
        this.name = name;
        this.width = width;
        this.height = height;
    }
}
