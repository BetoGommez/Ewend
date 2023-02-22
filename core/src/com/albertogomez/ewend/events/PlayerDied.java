package com.albertogomez.ewend.events;


import com.albertogomez.ewend.ecs.components.player.PlayerComponent;
import com.badlogic.gdx.scenes.scene2d.Event;

public class PlayerDied extends Event {
    public PlayerComponent playerComponent;
    public PlayerDied(PlayerComponent playerComponent) {
        this.playerComponent=playerComponent;
    }
}
