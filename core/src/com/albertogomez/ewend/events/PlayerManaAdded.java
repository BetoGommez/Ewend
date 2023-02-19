package com.albertogomez.ewend.events;

import com.albertogomez.ewend.ecs.components.LifeComponent;
import com.badlogic.gdx.scenes.scene2d.Event;


public class PlayerManaAdded extends Event {
    public float mana;

    public PlayerManaAdded(float mana) {
        this.mana = mana;
    }
}
