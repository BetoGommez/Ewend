package com.albertogomez.ewend.ecs.components;

import com.albertogomez.ewend.events.PlayerHealthChange;
import com.albertogomez.ewend.events.PlayerManaAdded;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Pool;

public class LifeComponent implements Component, Pool.Poolable {

    public float health;

    public boolean isEnemy;
    private final Stage stage;
    public float mana;

    public LifeComponent(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void reset() {
        health=100f;
        mana=0f;
    }

    public void removeHealth(AttackComponent attackComponent){
        health -= attackComponent.damage;//TODO AQUI PILLA EL DE ENEMIGOS Y TUYO BOBOLON

    }

    public void addMana(float manaAdd){
        this.mana+=manaAdd;
        stage.getRoot().fire(new PlayerManaAdded(this.mana));
    }
}
