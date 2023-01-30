package com.albertogomez.ewend.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class GameUI extends Table {

    public GameUI(Stage stage, Skin skin) {
        super(skin);
        setFillParent(true);
    }
}
