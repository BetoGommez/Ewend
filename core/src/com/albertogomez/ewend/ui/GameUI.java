package com.albertogomez.ewend.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class GameUI extends Table {

    public GameUI(Stage stage, Skin skin) {
        super(skin);
        setFillParent(true);

        add(new TextButton("hooooola",skin,"huge"));
    }
}
