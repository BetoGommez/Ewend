package com.albertogomez.ewend.events;

import com.albertogomez.ewend.ecs.components.LifeComponent;
import com.badlogic.gdx.scenes.scene2d.Event;

public class PlayerHealthChange extends Event {
    public float health;

    public PlayerHealthChange(float health) {
        this.health = health;
    }
}
