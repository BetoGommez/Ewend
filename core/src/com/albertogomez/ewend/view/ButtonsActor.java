package com.albertogomez.ewend.view;

import com.albertogomez.ewend.EwendLauncher;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;


public class ButtonsActor extends Actor  {

    private final BitmapFont font;
    private final AssetManager assetManager;
    private final TextureAtlas hudAtlas;
    private Skin skin;
    private TextButton.TextButtonStyle style;
    private TextButton button;

    public ButtonsActor(EwendLauncher context) {
        super();
        skin= context.getSkin();
        assetManager = context.getAssetManager();
        font = new BitmapFont();
        hudAtlas = assetManager.get("ui/game_ui/game_hud.atlas", TextureAtlas.class);
        style = new TextButton.TextButtonStyle();



        addButton("left",80,80);

        addButton("right",80,80);

    }

    private void addButton(String name,float height,float width){
        style = new TextButton.TextButtonStyle();
        style.font = font;
        style.up = skin.getDrawable(name) ;
        button = new TextButton("",style);
        skin.add("boton",button);

    }

    @Override
    public boolean remove() {
        return super.remove();
    }

    @Override
    public boolean addListener(EventListener listener) {
        return super.addListener(listener);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
    }
}
