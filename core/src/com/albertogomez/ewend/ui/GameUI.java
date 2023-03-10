package com.albertogomez.ewend.ui;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.events.*;
import com.albertogomez.ewend.input.ButtonListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

import static com.albertogomez.ewend.EwendLauncher.SCREEN_HEIGHT;
import static com.albertogomez.ewend.EwendLauncher.SCREEN_WIDTH;

/**
 *
 * @author Alberto Gómez
 */
public class GameUI extends Table implements EventListener{
    /**
     * Basic font for style
     */
    private final BitmapFont font;
    /**
     * Game stage
     */
    private final Stage stage;
    /**
     * Main game class
     */
    private final EwendLauncher context;
    /**
     * Game asset manager
     */
    private final AssetManager assetManager;
    /**
     * Saves all ui buttons
     */
    private Array<TextButton> buttons;
    /**
     * Stage skin
     */
    private final Skin skin;
    /**
     * TextButton style
     */
    private TextButton.TextButtonStyle style;
    /**
     * Button
     */
    private TextButton button;
    /**
     * Button general size
     */
    private float buttonSize;
    /**
     * Animation time accumulator
     */
    private float animationAccum;
    /**
     * Style for the furybar
     */
    private final ProgressBar.ProgressBarStyle furyBarStyle;
    /**
     * FuryBar bar
     */
    private ProgressBar furyBar;
    /**
     * HealthBar bar
     */
    private final ProgressBar healthBar;
    /**
     * ButtonListener for the inputs
     */
    private final ButtonListener buttonListener;
    /**
     * Atlas that has all the hud images
     */
    private final TextureAtlas hudAtlas;
    /**
     * Animation of the furyBar knob
     */
    private final Array<TextureRegionDrawable> animation;

    private TutorialLoader tutorial ;


    /**
     * Creates the gameUI and instances all values
     * @param context Game main class
     */
    public GameUI(EwendLauncher context) {
        super(context.getSkin());
        setFillParent(true);
        this.context=context;
        stage= context.getStage();
        if(!context.getPreferenceManager().getTutorialDone()){
            tutorial = new TutorialLoader(this);
        }
        skin= context.getSkin();
        assetManager = context.getAssetManager();
        animation = new Array<TextureRegionDrawable>();
        style = new TextButton.TextButtonStyle();
        font = new BitmapFont();
        hudAtlas = assetManager.get("ui/game_hud.atlas",TextureAtlas.class);
        buttons = new Array<TextButton>();
        this.buttonListener = new ButtonListener(context.getInputManager());

        if(SCREEN_WIDTH / SCREEN_HEIGHT >2){
            buttonSize = SCREEN_HEIGHT /7;
        }else{
            buttonSize = SCREEN_HEIGHT /5;
        }

        if(animation.size==0){
            TextureRegionDrawable drawableAux;
            Array<TextureAtlas.AtlasRegion> animationAtlas= (hudAtlas.findRegions("Fury"));
            for (TextureAtlas.AtlasRegion region : animationAtlas){
                drawableAux = new TextureRegionDrawable(region.split(145,480)[0][0]);
                animation.add(drawableAux);
            }
        }
        furyBarStyle = new ProgressBar.ProgressBarStyle();

        //health bar
        ProgressBar.ProgressBarStyle healthBarStyle = new ProgressBar.ProgressBarStyle();
        healthBarStyle.background =new TextureRegionDrawable(hudAtlas.findRegions("Healthbar_layout").get(0).split(775,60)[0][0]);
        healthBarStyle.knobBefore =new TextureRegionDrawable(hudAtlas.findRegions("Healthbar").get(0).split(775,60)[0][0]);
        healthBar = new ProgressBar(0,1,0.1f,false,healthBarStyle);

        createButtons();


    }

    /**
     * Creates all buttons and adds the bars
     */
    private void createButtons(){
        style = new TextButton.TextButtonStyle();
        style.font=font;

        furyBarStyle.disabledKnob = new TextureRegionDrawable(animation.get(0));
        furyBar = new ProgressBar(0,1,0.01f,true,furyBarStyle);

        furyBar.setValue(0f);
        furyBar.setAnimateDuration(2f);

        healthBar.setValue(1f);
        healthBar.setAnimateDuration(2f);

        //button creation
        buttons.add(createButton("Left"));
        buttons.get(0).setName("LEFT");

        buttons.add(createButton("Right"));
        buttons.get(1).setName("RIGHT");

        buttons.add(createButton("Purify"));
        buttons.get(2).setName("PURIFY");

        buttons.add(createButton("Dash"));
        buttons.get(3).setName("DASH");

        buttons.add(createButton("Jump"));
        buttons.get(4).setName("JUMP");

        buttons.add(createButton("Attack"));
        buttons.get(5).setName("ATTACK");
        //

        add(furyBar).width(145).height(480).top().left().padLeft(20).padTop(175);
        add(healthBar).height(60).width(775).top().left().padTop(80).colspan(5).expand();

        this.add();
        row();
        this.add();
        this.add();
        this.add().expandX();

        add(buttons.get(2)).size(buttonSize,buttonSize).bottom().right().row();

        add(buttons.get(0)).size(buttonSize*1.5f,buttonSize*1.5f).left().bottom();
        add(buttons.get(1)).size(buttonSize*1.5f,buttonSize*1.5f).left().bottom().expandX();

        add(buttons.get(3)).size(buttonSize,buttonSize).bottom().right();

        buttons.get(4).setSize(buttonSize*1.5f,buttonSize*1.5f);
        buttons.get(4).addListener(buttonListener);
        add(buttons.get(4)).pad(5f).size(buttonSize*1.5f,buttonSize*1.5f).bottom().right();
        if(tutorial!=null){
            tutorial.hideButtons();
        }
    }

    /**
     * Changes the furyBar knob frame for the animation
     * @param deltaTime Time elapsed
     */
    public void draw(float deltaTime){
        animationAccum+=deltaTime;
        if(animationAccum/0.1f>9){
            animationAccum=0;
        }
        furyBarStyle.knobBefore = animation.get((int)(animationAccum/0.1f));
        furyBar.setStyle(furyBarStyle);
        if(tutorial!=null&&tutorial.writeText){
            tutorial.writeText(deltaTime);
        }
    }



    /**
     * Geneneral button creator
     * @param imageKey Key of the image on the atlas
     * @return Button created
     */
    private TextButton createButton(String imageKey){
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();

        style.font = font;
        style.up = skin.getDrawable(imageKey);
        button= new TextButton("",style);
        button.addListener(buttonListener);

        return button;
    }

    public Vector2 getJumpButtonPos(){
        return new Vector2(buttonSize*1.25f,buttonSize*1.25f);
    }

    /**
     * Handles the event input
     * @param event Event executed
     * @return Always false
     */
    @Override
    public boolean handle(Event event) {
        if(event instanceof ResetLevel){
            resetBars();

        }else if(event instanceof PlayerFuryChanged){
            furyBar.setValue(((PlayerFuryChanged)event).mana/100f);
        }else if(event instanceof PlayerHealthChanged){
            healthBar.setValue(((PlayerHealthChanged)event).health/100f);
        }
        return false;
    }

    /**
     * Puts health and fury bar to its default
     */
    public void resetBars(){
        healthBar.setValue(1f);
        furyBar.setValue(0f);
        context.getStage().getRoot().fire(new PlayerFuryChanged(0));
    }

    public ProgressBar getFuryBar() {
        return furyBar;
    }

    public ProgressBar getHealthBar() {
        return healthBar;
    }

    public Array<TextButton> getButtons() {
        return buttons;
    }

    public EwendLauncher getContext() {
        return context;
    }
}
