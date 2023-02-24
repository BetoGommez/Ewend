package com.albertogomez.ewend.input;

import com.albertogomez.ewend.input.GameKeys;
import com.albertogomez.ewend.input.InputManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.awt.*;

/**
 * Button input listener for game keys throw
 * @author Alberto Gómez
 */
public class ButtonListener extends ClickListener {
    /**
     * Input Manager that gets the key introduced
     */
    InputManager inputManager;

    /**
     * Constructor that set values
     * @param inputManager Input Manager that gets the key introduced
     */
    public ButtonListener(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    /**
     * Executed when the button ends to be touched
     * @param event Event with button info
     * @param x Position x
     * @param y Position y
     * @param pointer Pointer
     * @param toActor Event Actor.
     */
    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        Button buttonActor = null;
        if(event.getListenerActor() instanceof Button){
            buttonActor = (Button)event.getListenerActor();
        }
        if(buttonActor!=null){
            inputManager.keyUp(GameKeys.valueOf(buttonActor.getName()).getKeyCode()[0]);
        }

        super.exit(event, x, y, pointer, toActor);
    }

    /**
     * Executed when the button starts to be touched
     * @param event Event with button info
     * @param x Position x
     * @param y Position y
     * @param pointer Pointer
     * @param fromActor Event Actor.
     */
    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        if(Gdx.input.isTouched()){
            Button buttonActor = null;
            if(event.getListenerActor() instanceof Button){
                buttonActor = (Button)event.getListenerActor();
            }
            if(buttonActor!=null){
               inputManager.keyDown(GameKeys.valueOf(buttonActor.getName()).getKeyCode()[0]);
            }
        }

        super.enter(event, x, y, pointer, fromActor);
    }

}
