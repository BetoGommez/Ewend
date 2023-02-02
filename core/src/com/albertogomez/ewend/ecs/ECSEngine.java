package com.albertogomez.ewend.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;

public class ECSEngine extends PooledEngine {

    public ECSEngine() {
        super();
    }


    public void createPlayer(final Vector2 playerSpawnLocation){
        final Entity entity = this.createEntity();

        //add components
    }
}
