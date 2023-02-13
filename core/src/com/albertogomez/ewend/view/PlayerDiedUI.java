package com.albertogomez.ewend.view;


import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.events.PlayerDied;
import com.albertogomez.ewend.input.ButtonListener;
import com.albertogomez.ewend.input.DeadScreenButtonListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;

public class PlayerDiedUI extends Table implements EventListener {
    private final BitmapFont font;
    private final Stage stage;
    private final EwendLauncher context;
    private final AssetManager assetManager;
    private final TextureAtlas hudAtlas;

    private Array<TextButton> buttons;
    private Skin skin;
    private TextButton.TextButtonStyle style;
    private TextButton button;
    private final DeadScreenButtonListener buttonListener;

    private float buttonSize;


    public PlayerDiedUI(EwendLauncher context) {
        super(context.getSkin());
        setFillParent(true);
        this.context = context;
        stage = context.getStage();
        skin = context.getSkin();
        assetManager = context.getAssetManager();
        hudAtlas = assetManager.get("ui/game_ui/game_hud.atlas", TextureAtlas.class);
        font = new BitmapFont();

        buttons = new Array<TextButton>();
        buttonListener = new DeadScreenButtonListener();
        createButtons();


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


    private TextButton createButton(String nombre) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();

        style.font = font;
        style.up = skin.getDrawable(nombre);
        button = new TextButton("", style);
        button.addListener(buttonListener);
        return button;
    }


    @Override
    public boolean handle(Event event) {
        /*if(event instanceof PlayerDied){
            stage.getRoot().removeActor(this);
            TextButton boton = createButton("left");
            boton.setName("hola");
            add(boton);
            context.stopGame();

        }*/
        //TODO VOLVER A JUGAR O AL MENU

        return false;
    }

    @Override
    public boolean fire(Event event) {


        return true;
    }
}
