package com.albertogomez.ewend.ui;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.input.GameKeys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;

/**
 * Loding overlay from Loading Screen
 * @author Alberto Gómez
 */
public class LoadingUI extends Table {

    /**
     * Button of press to start
     */
    private final TextButton pressAnyKeyButton;
    /**
     * Animation accumulator
     */
    private float aniAccum;
    /**
     * Style for the button
     */
    private TextButton.TextButtonStyle style;
    /**
     * Loading button
     */
    private final TextButton loading;
    /**
     * Lamp
     */
    private TextButton lamp;
    /**
     * Regions of the animation
     */
    private final Array<TextureRegion> animation;

    /**
     * Creates the Loading UI and all his elements
     * @param context Game main class
     */
    public LoadingUI(final EwendLauncher context) {
        super(context.getSkin());
        setFillParent(true);
        final I18NBundle i18NBundle = context.getI18NBundle();
        animation = new Array<TextureRegion>();
        lamp = null;
        style=null;

        pressAnyKeyButton = new TextButton(i18NBundle.format("pressTheScreen"),getSkin(),"huge");
        pressAnyKeyButton.setVisible(false);
        pressAnyKeyButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                context.getInputManager().notifyKeyDown(GameKeys.INTERACT);
                return true;
            }
        } );

        loading = new TextButton(i18NBundle.format("loading"),getSkin(),"mid_huge");

        if(animation.size==0){
            Array<TextureAtlas.AtlasRegion> animationAtlas= (context.getAssetManager().get("ui/game_hud.atlas", TextureAtlas.class).findRegions("lamp"));
            for (TextureAtlas.AtlasRegion region : animationAtlas){
                animation.add(region.split(48,64)[0][0]);
            }
        }

        style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(animation.get(0));
        style.font = new BitmapFont();
        lamp = new TextButton("",style);

        add(pressAnyKeyButton).padLeft(200f).expand().center().row();
        add(loading).pad(60).bottom().left();
        add(lamp).right().size(48*5,64*5).padRight(50f);
    }

    /**
     * Gets the progress from the assetManager and if it's done shows the press the screen to start
     * @param progress Progress done 0-1
     * @param deltaTime Time elapsed
     */
    public void setProgress(final float progress,float deltaTime){
        aniAccum+=deltaTime;
        if(aniAccum/0.3f>4){
            aniAccum=0;
        }
        style.up = new TextureRegionDrawable(animation.get((int)(aniAccum/0.3f)));
        lamp.setStyle(style);
        if(progress>=1&& !pressAnyKeyButton.isVisible()){
            pressAnyKeyButton.setVisible(true);
            pressAnyKeyButton.setColor(1,1,1,0);
            pressAnyKeyButton.addAction(Actions.forever(Actions.sequence(Actions.alpha(1,1),Actions.alpha(0,1))));
        }
    }
}
