package com.albertogomez.ewend.ecs.components.enemy;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class EnemyComponent implements Component, Pool.Poolable {

    public Vector2 speed=new Vector2();
    public EnemyType enemyType;
    @Override
    public void reset() {
        speed.set(0,0);
        enemyType=null;
    }
}
