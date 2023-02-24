package com.albertogomez.ewend.map;

import com.badlogic.gdx.math.Vector2;

/**
 * Game Object existing on map values storing
 * @author Alberto Gómez
 */
public class GameObject {
    /**
     * Game Object type
     */
    private final GameObjectType type;
    /**
     * Map position
     */
    private final Vector2 position;
    /**
     * Map object width
     */
    private final float width;
    /**
     * Map object height
     */
    private final float height;
    /**
     * Game object rotation
     */
    private final float rotDegree;
    /**
     * Game object animation index value
     */
    private final int animationIndex;
    /**
     * Game object index got from the map
     */
    private final int indexProperty;


    /**
     * Constructor that set all the values
     * @param type Game Object type
     * @param position Map position
     * @param width Map object width
     * @param height Map object height
     * @param rotDegree Game object rotation
     * @param animationIndex Game object animation index value
     * @param indexProperty Game object index got from the map
     */
    public GameObject(GameObjectType type, Vector2 position, float width, float height, float rotDegree, int animationIndex, int indexProperty) {
        this.type = type;
        this.position = position;
        this.width = width;
        this.height = height;
        this.rotDegree = rotDegree;
        this.animationIndex = animationIndex;
        this.indexProperty = indexProperty;
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

    public int getIndexProperty() {
        return indexProperty;
    }
}
