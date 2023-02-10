package com.albertogomez.ewend.events;

import jdk.internal.event.Event;

public class ButtonInputEvent extends Event {
    String buttonName;

    public ButtonInputEvent(String buttonName) {
        this.buttonName = buttonName;
    }
}
