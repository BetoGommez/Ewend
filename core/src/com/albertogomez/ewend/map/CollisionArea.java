package com.albertogomez.ewend.map;


import static com.albertogomez.ewend.constants.Constants.UNIT_SCALE;

public class CollisionArea {
    //esta es la posicion inicial x
    private final float x;
    //esta es la posicion inicial y
    private final float y;

    //determina el numnero de puntas que tinen la colision sobre toodo si es mano alzada
    private final float[] vertices;
    private final short Mask;


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
