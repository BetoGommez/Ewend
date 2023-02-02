package com.albertogomez.ewend.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import java.awt.event.ComponentListener;

public class PlayerComponent implements Component, Pool.Poolable {

    //te sirve de ejemplo de condicion que cumple el personaje
    public boolean hasItem;


    @Override
    public void reset() {
        hasItem=false;
    }
}
