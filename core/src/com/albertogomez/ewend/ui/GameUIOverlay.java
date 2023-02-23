package com.albertogomez.ewend.ui;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.events.PlayerManaAdded;
import com.albertogomez.ewend.input.ButtonListener;
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
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class GameUIOverlay extends Table implements EventListener {

    private final BitmapFont font;
    private final Stage stage;
    private final EwendLauncher context;
    private TextButton.TextButtonStyle style;
    private final TextureAtlas hudAtlas;
    private final TextButton furyBarLayout;
    private TextButton.TextButtonStyle furyBarLayoutStyle;
    private TextureRegionDrawable[] furyBarImages;
    private final AssetManager assetManager;

    public GameUIOverlay(EwendLauncher context) {
        super(context.getSkin());
        setFillParent(true);
        this.context = context;
        stage = context.getStage();
        stage.addListener(this);
        assetManager = context.getAssetManager();
        style = new TextButton.TextButtonStyle();
        font = new BitmapFont();
        hudAtlas = assetManager.get("ui/game_hud.atlas", TextureAtlas.class);
        furyBarImages = new TextureRegionDrawable[2];

        loadFuryBarImages();
        furyBarLayout = new TextButton("", furyBarLayoutStyle);

        this.add(furyBarLayout).expand().top().left().pad(30);
    }

    private void loadFuryBarImages(){
        for (int i = 0; i < furyBarImages.length; i++) {
            furyBarImages[i] = new TextureRegionDrawable(hudAtlas.findRegions("Furybar_layout").get(i).split(116, 480)[0][0]);
        }
        furyBarLayoutStyle = new TextButton.TextButtonStyle();
        furyBarLayoutStyle.font = font;
        furyBarLayoutStyle.up = furyBarImages[0];
    }

    @Override
    public boolean handle(Event event) {
        if (event instanceof PlayerManaAdded) {
            if (((PlayerManaAdded) event).mana >= 100) {
                furyBarLayoutStyle.up = furyBarImages[1];
            } else {
                furyBarLayoutStyle.up = furyBarImages[0];
            }
            furyBarLayout.setStyle(style);
        }
        return false;
    }
}
