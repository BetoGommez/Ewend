package com.albertogomez.ewend.input;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.events.PlayerDied;
import com.albertogomez.ewend.events.ResetLevel;
import com.albertogomez.ewend.events.ReturnToMenu;
import com.albertogomez.ewend.screen.ScreenType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

/**
 * Button listener for the dead screen
 * @author Alberto Gómez
 */
public class DeadScreenButtonListener extends ClickListener {

    /**
     * Game main class
     */
    private final EwendLauncher context;

    /**
     * Constructor
     * @param context Game main class
     */
    public DeadScreenButtonListener(EwendLauncher context) {
        this.context = context;
    }

    /**
     * Executed when the button has been touched
     * @param event Event with button info
     * @param x Position x
     * @param y Position y
     * @param pointer Pointer
     */
    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if(event.getListenerActor() instanceof TextButton){
            TextButton aux = (TextButton) event.getListenerActor();
            switch (aux.getName()){
                case "Restart":
                    context.getStage().getRoot().fire(new ResetLevel());
                    break;
                case "Menu":
                    context.getStage().getRoot().fire(new ReturnToMenu());
                    break;
            }
        }
        return super.touchDown(event, x, y, pointer, button);
    }
}
