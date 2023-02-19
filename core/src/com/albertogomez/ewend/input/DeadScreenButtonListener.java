package com.albertogomez.ewend.input;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.events.PlayerDied;
import com.albertogomez.ewend.events.ResetLevel;
import com.albertogomez.ewend.screen.ScreenType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class DeadScreenButtonListener extends ClickListener {

    private final EwendLauncher context;

    public DeadScreenButtonListener(EwendLauncher context) {
        this.context = context;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if(event.getListenerActor() instanceof TextButton){
            context.getStage().getRoot().fire(new ResetLevel());
        }
        return super.touchDown(event, x, y, pointer, button);
    }
}
