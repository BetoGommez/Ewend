package com.albertogomez.ewend.events;


import com.badlogic.gdx.scenes.scene2d.Event;

public class PlayerDied extends Event {
    private String takenFireflys; //TODO CAMBIALO A SU UNIDAD CORRECTA


    public PlayerDied(String takenFireflys) {
        this.takenFireflys = takenFireflys;
    }
}
