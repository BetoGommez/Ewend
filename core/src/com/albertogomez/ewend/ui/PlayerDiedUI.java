package com.albertogomez.ewend.ui;


import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.events.PlayerDied;
import com.albertogomez.ewend.events.ResetLevel;
import com.albertogomez.ewend.input.ButtonListener;
import com.albertogomez.ewend.input.DeadScreenButtonListener;
import com.albertogomez.ewend.screen.GameScreen;
import com.albertogomez.ewend.screen.ScreenType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;

public class PlayerDiedUI extends Table implements EventListener {
    private final BitmapFont font;
    private final Stage stage;
    private final AssetManager assetManager;
    private  TextureAtlas hudAtlas;

    private Array<TextButton> buttons;
    private Skin skin;
    private TextButton.TextButtonStyle style;
    private TextButton button;
    private final DeadScreenButtonListener buttonListener;

    private float buttonSize;


    public PlayerDiedUI(EwendLauncher context) {
        super(context.getSkin());
        setFillParent(true);
        stage = context.getStage();
        skin = context.getSkin();
        assetManager = context.getAssetManager();
        font = new BitmapFont();
        buttons = new Array<TextButton>();
        buttonListener = new DeadScreenButtonListener(context);

        createButtons();
        this.setColor(1,1,1,0.1f);
    }

    private void createButtons() {
        style = new TextButton.TextButtonStyle();
        style.font = font;
        stage.addListener(this);
        buttonSize = EwendLauncher.HEIGHT / 4;

        buttons.add(createButton("right"));
        buttons.get(0).setName("JUMP");
        add(buttons.get(0)).size(buttonSize, buttonSize).bottom().right();

        buttons.add(createButton("left"));
        buttons.get(1).setName("ATTACK");
        add(buttons.get(1)).size(buttonSize, buttonSize).bottom().right();


    }


    private TextButton createButton(String drawableName) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();

        style.font = font;
        style.up = skin.getDrawable(drawableName);
        button = new TextButton("", style);
        button.addListener(buttonListener);
        return button;
    }



    @Override
    public boolean handle(Event event) {
        if(event instanceof ResetLevel){
            stage.removeListener(this);
            stage.getRoot().removeActor(this);
        }
        //TODO VOLVER A JUGAR O AL MENU

        return false;
    }

    @Override
    public boolean fire(Event event) {


        return true;
    }
}
