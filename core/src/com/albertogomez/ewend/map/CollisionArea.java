package com.albertogomez.ewend.map;


import static com.albertogomez.ewend.constants.Constants.UNIT_SCALE;

/**
 * Collision area values storing
 * @author Alberto Gómez
 */
public class CollisionArea {
    /**
     * Initial position x
     */
    private final float x;
    /**
     * Initial position y
     */
    private final float y;

    /**
     * All area vertices points
     */
    private final float[] vertices;
    /**
     * Collsion mask
     */
    private final short Mask;

    /**
     * Creates the collision area depending on values
     * @param x Initial position x
     * @param y Initial position y
     * @param vertices All area vertices points
     * @param Mask Collsion mask
     */
    public CollisionArea(float x,float y, float[] vertices,short Mask) {
        this.x = x*UNIT_SCALE;
        this.y = y*UNIT_SCALE;
        this.vertices = vertices;
        this.Mask = Mask;

        for (int i = 0; i < vertices.length; i+=2) {
            vertices[i] = vertices[i]*UNIT_SCALE;
            vertices[i+1] = vertices[i+1]*UNIT_SCALE;

        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public short getMask() {
        return Mask;
    }

    public float[] getVertices() {
        return vertices;
    }
}
