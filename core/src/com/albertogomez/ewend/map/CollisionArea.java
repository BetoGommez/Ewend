package com.albertogomez.ewend.map;


import static com.albertogomez.ewend.constants.Constants.UNIT_SCALE;

public class CollisionArea {
    //esta es la posicion inicial x
    private final float x;
    //esta es la posicion inicial y
    private final float y;

    //determina el numnero de puntas que tinen la colision sobre toodo si es mano alzada
    private final float[] vertices;


    public CollisionArea(float x,float y, float[] vertices) {
        this.x = x*UNIT_SCALE;
        this.y = y*UNIT_SCALE;
        this.vertices = vertices;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float[] getVertices() {
        return vertices;
    }
}