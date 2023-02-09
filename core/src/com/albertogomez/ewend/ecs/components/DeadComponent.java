package com.albertogomez.ewend.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class DeadComponent implements Component, Pool.Poolable {

    public boolean isDead;



    @Override
    public void reset() {

        isDead = false;
    }
}
