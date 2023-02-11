package com.albertogomez.ewend.input;

import com.albertogomez.ewend.input.GameKeys;
import com.albertogomez.ewend.input.InputManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.awt.*;

public class ButtonListener extends ClickListener {
    InputManager inputManager;
    public ButtonListener(InputManager inputManager) {
        this.inputManager = inputManager;
    }

   /* @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        Button buttonActor = null;
        if(event.getListenerActor() instanceof Button){
            buttonActor = (Button)event.getListenerActor();
        }
        if(buttonActor!=null){
            inputManager.keyDown(GameKeys.valueOf(buttonActor.getName()).getKeyCode()[0]);
        }
        super.touchUp(event, x, y, pointer, button);
        return super.touchDown(event, x, y, pointer, button);
    }
*/


    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        //SALIR DEL BOTON

        Button buttonActor = null;
        if(event.getListenerActor() instanceof Button){
            buttonActor = (Button)event.getListenerActor();
        }
        if(buttonActor!=null){
            inputManager.keyUp(GameKeys.valueOf(buttonActor.getName()).getKeyCode()[0]);
        }

        super.exit(event, x, y, pointer, toActor);
    }

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



/*    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        Button buttonActor = null;
        if(event.getListenerActor() instanceof Button){
            buttonActor = (Button)event.getListenerActor();
        }
        if(buttonActor!=null){
            inputManager.keyUp(GameKeys.valueOf(buttonActor.getName()).getKeyCode()[0]);
        }
        super.touchUp(event, x, y, pointer, button);
    }*/


}
