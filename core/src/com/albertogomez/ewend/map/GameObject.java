package com.albertogomez.ewend.map;

import com.badlogic.gdx.math.Vector2;

public class GameObject {
    private final GameObjectType type;
    private final Vector2 position;
    private final float width;
    private final float height;
    private final float rotDegree;
    private final int animationIndex;


    public GameObject(GameObjectType type, Vector2 position, float width, float height, float rotDegree, int animationIndex) {
        this.type = type;
        this.position = position;
        this.width = width;
        this.height = height;
        this.rotDegree = rotDegree;
        this.animationIndex = animationIndex;
    }

    public GameObjectType getType() {
        return type;
    }

    public float getHeight() {
        return height;
    }

    public float getRotDegree() {
        return rotDegree;
    }

    public float getWidth() {
        return width;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public Vector2 getPosition() {
        return position;
    }
}
