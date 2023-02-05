package com.albertogomez.ewend.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;

public class InputManager extends InputAdapter {
    private final GameKeys[] keyMapping ;
    private final boolean[] keyState;

    private final Array<GameKeyInputListener> listeners;




    public InputManager() {
        this.keyMapping = new GameKeys[256];
        for(final GameKeys gameKey : GameKeys.values()){
            for(final int code : gameKey.keyCode){
                keyMapping[code] = gameKey;

            }
        }
        keyState = new boolean[GameKeys.values().length];
        listeners = new Array<GameKeyInputListener>();

    }

@Override
    public boolean keyUp(int keycode) {

        final GameKeys gameKey = keyMapping[keycode];
        if(gameKey==null){
            //no mapping -> nothing to do
            return false;
        }

        notifyKeyUp(gameKey);

        return false;
    }




    @Override
    public boolean keyDown(int keycode) {
        final GameKeys gameKey = keyMapping[keycode];
        if(gameKey==null){
            //no mapping -> nothing to do
            return false;
        }


        notifyKeyDown(gameKey);
        Gdx.app.debug("SE ESTA PULSANDO","AAAAAAAAAAAA");


        return true;

    }





    public void notifyKeyDown(GameKeys gameKey){
        keyState[gameKey.ordinal()]=true;
        for( GameKeyInputListener listener:listeners){
            listener.keyPressed(this,gameKey);
        }
    }






    public void notifyKeyUp(GameKeys gameKey){
        keyState[gameKey.ordinal()]=false;

        for(final GameKeyInputListener listener:listeners){

            listener.keyUp(this,gameKey);
        }

    }



    public boolean isKeyPressed(final GameKeys gameKey){
        return keyState[gameKey.ordinal()];
    }

    public void addInputListener(final GameKeyInputListener listener){
        listeners.add(listener);
    }

    public void removeInputListener(final GameKeyInputListener listener){
        listeners.removeValue(listener,true);
    }






}
