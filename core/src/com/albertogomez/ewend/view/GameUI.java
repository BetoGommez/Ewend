package com.albertogomez.ewend.view;

import com.albertogomez.ewend.EwendLauncher;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class GameUI extends Table {

    public GameUI(EwendLauncher context) {
        super(context.getSkin());
        setFillParent(true);

        add(new TextButton(context.getI18NBundle().format("hola"),context.getSkin(),"huge"));
    }
}
