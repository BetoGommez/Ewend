package com.albertogomez.ewend.events;

import com.badlogic.gdx.scenes.scene2d.Event;

public class MessageEvent extends Event {
    String message;
    public MessageEvent(String message) {
        this.message = message;
    }
}
