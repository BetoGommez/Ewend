package com.albertogomez.ewend.events;

import com.badlogic.gdx.scenes.scene2d.Event;

public class EnemyDied extends Event {

    public float mana = 0;

    public EnemyDied(float mana) {
        this.mana = mana;
    }
}
