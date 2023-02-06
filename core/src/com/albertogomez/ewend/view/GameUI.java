package com.albertogomez.ewend.view;

import com.albertogomez.ewend.EwendLauncher;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

public class GameUI extends Table {

    public GameUI(EwendLauncher context) {
        super(context.getSkin());
        setFillParent(true);
        TextButton button = new TextButton(context.getI18NBundle().format("hola"),context.getSkin(),"huge");
        button.getLabel().setWrap(true);
        button.align(Align.left);
        add(button).expand().bottom().fillX();
    }
}
