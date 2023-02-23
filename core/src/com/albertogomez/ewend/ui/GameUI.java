package com.albertogomez.ewend.ui;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.events.*;
import com.albertogomez.ewend.input.ButtonListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class GameUI extends Table implements EventListener{
    private final BitmapFont font;
    private final Stage stage;
    private final EwendLauncher context;
    private final AssetManager assetManager;
    private Array<TextButton> buttons;
    private final Skin skin;
    private TextButton.TextButtonStyle style;
    private TextButton button;

    private ProgressBar furyBar;

    private float animationAccum;
    private final ButtonListener buttonListener;

    private float buttonSize;
    private final ProgressBar.ProgressBarStyle furyBarStyle;

    private final TextureAtlas hudAtlas;
    private final Array<TextureRegionDrawable> animation;



    public GameUI(EwendLauncher context) {
        super(context.getSkin());
        setFillParent(true);
        this.context=context;
        stage= context.getStage();
        stage.addListener(this);

        skin= context.getSkin();
        assetManager = context.getAssetManager();
        animation = new Array<TextureRegionDrawable>();
        style = new TextButton.TextButtonStyle();
        font = new BitmapFont();
        hudAtlas = assetManager.get("ui/game_hud.atlas",TextureAtlas.class);
        buttons = new Array<TextButton>();
        buttonListener = new ButtonListener(context.getInputManager());
        buttonSize = EwendLauncher.HEIGHT/5;

        if(animation.size==0){
            TextureRegionDrawable drawableAux;
            Array<TextureAtlas.AtlasRegion> animationAtlas= (hudAtlas.findRegions("Fury"));
            for (TextureAtlas.AtlasRegion region : animationAtlas){
                drawableAux = new TextureRegionDrawable(region.split(116,384)[0][0]);
                animation.add(drawableAux);
            }
        }
        furyBarStyle = new ProgressBar.ProgressBarStyle();
        createButtons();
    }


    private void createButtons(){
        style = new TextButton.TextButtonStyle();
        style.font=font;

        furyBarStyle.disabledKnob = new TextureRegionDrawable(animation.get(0));
        furyBar = new ProgressBar(0,1,0.01f,true,furyBarStyle);

        furyBar.setValue(0);
        furyBar.setAnimateDuration(2f);

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

        buttons.add(createButton("Dash"));
        buttons.get(5).setName("DASH");
        //


        add(furyBar).height(384).top().left().padLeft(86).padTop(120).expandY().colspan(3);
        this.addActor(button);
        row();

        add(buttons.get(0)).size(buttonSize,buttonSize).left().bottom();
        add(buttons.get(1)).size(buttonSize,buttonSize).left().bottom();
        add(buttons.get(2)).size(buttonSize,buttonSize).bottom().right().expand();
        add(buttons.get(3)).size(buttonSize,buttonSize).bottom().right();
        add(buttons.get(4)).size(buttonSize,buttonSize).bottom().right();
        add(buttons.get(5)).size(buttonSize,buttonSize).bottom().right();
    }


    public void draw(float deltaTime){
        animationAccum+=deltaTime;
        if(animationAccum/0.1f>9){
            animationAccum=0;
        }
        furyBarStyle.knobBefore = animation.get((int)(animationAccum/0.1f));
        furyBar.setStyle(furyBarStyle);
    }

    private TextButton createButton(String imageKey){
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();

        style.font = font;
        style.up = skin.getDrawable(imageKey);
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
            //healthBar.setValue(1f);
            furyBar.setValue(0f);
        }else if(event instanceof PlayerManaAdded){
            furyBar.setValue(((PlayerManaAdded)event).mana/100f);
        }else if(event instanceof PlayerHealthChange){
            //healthBar.setValue(((PlayerHealthChange)event).health/100f);
        }
        return false;
    }

}
