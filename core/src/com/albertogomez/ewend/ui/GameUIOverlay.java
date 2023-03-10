package com.albertogomez.ewend.ui;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.events.PlayerFuryChanged;
import com.albertogomez.ewend.input.ButtonListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import static com.albertogomez.ewend.EwendLauncher.SCREEN_HEIGHT;
import static com.albertogomez.ewend.EwendLauncher.SCREEN_WIDTH;

/**
 * Overlay for the GameUI
 * @author Alberto Gómez
 */
public class GameUIOverlay extends Table implements EventListener {

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
     * Atlas with all menu images
     */
    private final TextureAtlas hudAtlas;
    /**
     * ButtonListener for the button input
     */
    private final ButtonListener buttonListener;
    /**
     * Fury bar layout button
     */
    private final TextButton furyBarLayout;
    /**
     * Furybar layout style
     */
    private TextButton.TextButtonStyle furyBarLayoutStyle;
    /**
     * Fury bar two states
     */
    private TextureRegionDrawable[] furyBarImages;
    /**
     * Game asset manager
     */
    private final AssetManager assetManager;

    /**
     * Creates the GameUIOverlay
     * @param context Game Main class
     * @param principalUI Principal Game UI
     */
    public GameUIOverlay(EwendLauncher context, GameUI principalUI) {
        super(context.getSkin());
        setFillParent(true);
        this.context = context;
        stage = context.getStage();
        stage.addListener(this);
        this.buttonListener = new ButtonListener(context.getInputManager());
        assetManager = context.getAssetManager();
        font = new BitmapFont();
        hudAtlas = assetManager.get("ui/game_hud.atlas", TextureAtlas.class);
        furyBarImages = new TextureRegionDrawable[2];

        loadFuryBarImages();
        furyBarLayout = new TextButton("", furyBarLayoutStyle);

        float buttonSize;
        if(SCREEN_WIDTH / SCREEN_HEIGHT >2){
            buttonSize= SCREEN_HEIGHT /7;
        }else{
            buttonSize= SCREEN_HEIGHT /5;
        }

        this.add(furyBarLayout).width(145).height(600).expand().top().left().padLeft(20).padTop(55).row();
        this.add(principalUI.getButtons().get(5)).width(buttonSize).height(buttonSize).padBottom(principalUI.getJumpButtonPos().y).padRight(principalUI.getJumpButtonPos().x).bottom().right();
    }

    /**
     * Load fury bar images from atlas
     */
    private void loadFuryBarImages(){
        for (int i = 0; i < furyBarImages.length; i++) {
            furyBarImages[i] = new TextureRegionDrawable(hudAtlas.findRegions("Furybar_layout").get(i).split(145, 600)[0][0]);
        }
        furyBarLayoutStyle = new TextButton.TextButtonStyle();
        furyBarLayoutStyle.font = font;
        furyBarLayoutStyle.up = furyBarImages[0];
    }

    /**
     * Creates the attack button
     * @return Button created
     */


    /**
     * Handle event inputs
     * @param event Event executed
     * @return Always false
     */
    @Override
    public boolean handle(Event event) {
        if (event instanceof PlayerFuryChanged) {
            if (((PlayerFuryChanged) event).mana >= 100) {
                furyBarLayoutStyle.up = furyBarImages[1];
            } else {
                furyBarLayoutStyle.up = furyBarImages[0];
            }
            furyBarLayout.setStyle(furyBarLayoutStyle);
        }


        return false;
    }
}
