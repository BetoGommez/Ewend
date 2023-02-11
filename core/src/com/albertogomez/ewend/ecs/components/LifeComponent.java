package com.albertogomez.ewend.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class LifeComponent implements Component, Pool.Poolable {

    public float health;
    public boolean isEnemy;
    public float charge;

    @Override
    public void reset() {
        health=100f;
        charge=0f;
    }

    public void removeHealth(AttackComponent attackComponent){
        health -= attackComponent.damage;
    }
}
