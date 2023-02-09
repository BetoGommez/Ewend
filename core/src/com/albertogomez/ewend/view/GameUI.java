package com.albertogomez.ewend.view;

import com.albertogomez.ewend.EwendLauncher;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.Align;

public class GameUI extends Table {
    private final BitmapFont font;
    private final AssetManager assetManager;
    private final TextureAtlas hudAtlas;
    private Skin skin;
    private TextButton.TextButtonStyle style;
    private TextButton button;

    public GameUI(EwendLauncher context) {
        super(context.getSkin());
        setFillParent(true);
        skin= context.getSkin();
        assetManager = context.getAssetManager();
        font = new BitmapFont();
        hudAtlas = assetManager.get("ui/game_ui/game_hud.atlas", TextureAtlas.class);
        style = new TextButton.TextButtonStyle();
        style.font=font;




        add(createButton("left")).size(80,80).bottom().left();

        add(createButton("right")).size(80,80).bottom().left().expand();

        add(createButton("left")).size(80,80).bottom().right().expand();

        add(createButton("right")).size(80,80).bottom().right();

        add(createButton("right")).size(80,80).bottom().right();



    }

    private TextButton createButton(String nombre){
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.up = skin.getDrawable(nombre);
        button= new TextButton("",style);
       return button;
    }



}
