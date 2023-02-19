package com.albertogomez.ewend.events;

import com.albertogomez.ewend.ecs.components.PlayerComponent;
import com.badlogic.gdx.scenes.scene2d.Event;

public class FireflyTaken extends Event {
    public PlayerComponent playerComponent;

    public FireflyTaken(PlayerComponent playerComponent) {
        this.playerComponent = playerComponent;
    }
}
