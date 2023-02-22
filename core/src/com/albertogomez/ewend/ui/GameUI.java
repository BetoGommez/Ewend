package com.albertogomez.ewend.ui;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.events.*;
import com.albertogomez.ewend.input.ButtonListener;
import com.albertogomez.ewend.ui.PlayerDiedUI;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;

public class GameUI extends Table implements EventListener{
    private final BitmapFont font;
    private final Stage stage;
    private final EwendLauncher context;



    private Array<TextButton> buttons;
    private final Skin skin;
    private TextButton.TextButtonStyle style;
    private TextButton button;

    private ProgressBar manaBar;
    private ProgressBar healthBar;
    private final ButtonListener buttonListener;

    private float buttonSize;



    public GameUI(EwendLauncher context) {
        super(context.getSkin());
        setFillParent(true);
        this.context=context;
        stage= context.getStage();
        skin= context.getSkin();

        font = new BitmapFont();

        buttons = new Array<TextButton>();
        buttonListener = new ButtonListener(context.getInputManager());
        createButtons();


    }

    private void createButtons(){
        style = new TextButton.TextButtonStyle();
        style.font=font;
        stage.addListener(this);

        buttonSize = EwendLauncher.HEIGHT/5;
        manaBar = new ProgressBar(0,1,0.01f,false,skin,"mana");
        healthBar = new ProgressBar(0,1,0.01f,false,skin,"health");
        healthBar.setValue(100);
        manaBar.setValue(0);

        //button creation
        buttons.add(createButton("Left"));
        buttons.get(0).setName("LEFT");

        buttons.add(createButton("Right"));
        buttons.get(1).setName("RIGHT");

        buttons.add(createButton("Purify"));
        buttons.get(2).setName("DASH");

        buttons.add(createButton("Jump"));
        buttons.get(3).setName("JUMP");

        buttons.add(createButton("Attack"));
        buttons.get(4).setName("ATTACK");
        //

        add(healthBar).top().left().pad(5).colspan(3);
        row();
        add(manaBar).top().left().pad(5).expandY().colspan(3);
        row();
        add(buttons.get(0)).size(buttonSize,buttonSize).left().bottom();
        add(buttons.get(1)).size(buttonSize,buttonSize).left().bottom();//.bottom().left().expand();
        add(buttons.get(2)).size(buttonSize,buttonSize).bottom().right().expand();//.bottom().right().expand();
        add(buttons.get(3)).size(buttonSize,buttonSize).bottom().right();//.bottom().right();
        add(buttons.get(4)).size(buttonSize,buttonSize).bottom().right();//.bottom().right();
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
        if(event instanceof PlayerDied){
            context.getPreferenceManager().saveTakenFireflys(((PlayerDied)event).playerComponent.takenFireflys);
            stage.addActor(new PlayerDiedUI(context));
            stage.getRoot().removeActor(this);
        }else if(event instanceof ResetLevel){
            stage.getRoot().addActor(this);
            healthBar.setValue(1f);
            manaBar.setValue(0f);
        }else if(event instanceof PlayerManaAdded){
            manaBar.setValue(((PlayerManaAdded)event).mana/100f);
        }else if(event instanceof PlayerHealthChange){
            healthBar.setValue(((PlayerHealthChange)event).health/100f);
        }
        return false;
    }

}
