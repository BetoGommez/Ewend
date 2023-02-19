package com.albertogomez.ewend.ecs.components;

import com.albertogomez.ewend.map.GameObjectType;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class GameObjectComponent implements Component, Pool.Poolable {
    public GameObjectType type;
    public int animationIndex;

    public int index=-1;
    public boolean touched = false;
    @Override
    public void reset() {
        type = null;
        animationIndex = -1;
    }


}
