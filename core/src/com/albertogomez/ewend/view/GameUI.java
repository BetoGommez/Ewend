package com.albertogomez.ewend.view;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.events.ButtonListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;

public class GameUI extends Table implements EventListener{
    private final BitmapFont font;
    private final Stage stage;
    private final AssetManager assetManager;
    private final TextureAtlas hudAtlas;

    private Array<TextButton> buttons;
    private Skin skin;
    private TextButton.TextButtonStyle style;
    private TextButton button;
    private ButtonListener buttonListener;

    private float buttonSize;

    @Override
    public boolean fire(Event event) {


        return true;
    }

    public GameUI(EwendLauncher context) {
        super(context.getSkin());
        setFillParent(true);
        stage= context.getStage();

        skin= context.getSkin();
        assetManager = context.getAssetManager();
        hudAtlas = assetManager.get("ui/game_ui/game_hud.atlas", TextureAtlas.class);
        font = new BitmapFont();

        buttons = new Array<TextButton>();
        buttonListener = new ButtonListener(context.getInputManager());

        style = new TextButton.TextButtonStyle();
        style.font=font;
        stage.addListener(this);

        buttonSize = EwendLauncher.HEIGHT/4;


        //button creation
        buttons.add(createButton("left"));
        buttons.get(0).setName("LEFT");
        add(buttons.get(0)).size(buttonSize,buttonSize).bottom().left();

        buttons.add(createButton("right"));
        buttons.get(1).setName("RIGHT");
        add(buttons.get(1)).size(buttonSize,buttonSize).bottom().left().expand();

        buttons.add(createButton("left"));
        buttons.get(2).setName("DASH");
        add(buttons.get(2)).size(buttonSize,buttonSize).bottom().right().expand();

        buttons.add(createButton("right"));
        buttons.get(3).setName("JUMP");
        add(buttons.get(3)).size(buttonSize,buttonSize).bottom().right();

        buttons.add(createButton("left"));
        buttons.get(4).setName("ATTACK");
        add(buttons.get(4)).size(buttonSize,buttonSize).bottom().right();



    }



    private TextButton createButton(String nombre){
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.up = skin.getDrawable(nombre);
        button= new TextButton("",style);
        button.addListener(buttonListener);
       return button;
    }


    @Override
    public boolean handle(Event event) {


        return false;
    }
}
