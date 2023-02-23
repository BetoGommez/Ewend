package com.albertogomez.ewend.ui;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.audio.AudioType;
import com.albertogomez.ewend.input.GameKeys;
import com.albertogomez.ewend.screen.ScreenType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;

import static com.albertogomez.ewend.EwendLauncher.HEIGHT;
import static com.albertogomez.ewend.EwendLauncher.WIDTH;

public class MenuUI extends Table {

    private final Stage stage;
    private final EwendLauncher context;
    private final Skin skin;
    private final BitmapFont font;
    private final I18NBundle i18NBundle;
    private TextButton buttonTemplate;
    private TextButton background;
    private Array<TextButton> buttonsBaseMenu;
    private Array<TextButton> buttonsOptions;
    private TextButton.TextButtonStyle style;
    private final MenuUI menuUI = this;

    public MenuUI(final EwendLauncher context) {
        super(context.getSkin());
        this.context = context;
        i18NBundle = context.getI18NBundle();
        stage = context.getStage();
        skin = context.getSkin();
        font = new BitmapFont();
        setFillParent(true);
        buttonsBaseMenu = new Array<TextButton>();
        buttonsOptions = new Array<TextButton>();


        style = new TextButton.TextButtonStyle();
        style.font = font;
        style.up = new TextureRegionDrawable(new TextureRegion(context.getAssetManager().<Texture>get("ui/background.jpg")));
        background = new TextButton("", style);
        background.setSize(1000f * WIDTH / 900f, 700f * WIDTH / 900f);
        stage.addActor(background);

        showBaseMenu();
        this.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                context.getAudioManager().playAudio(AudioType.MENU_BUTTON);
                return true;
            }
        });
    }

    private void showBaseMenu() {
        buttonsBaseMenu.clear();
        style = new TextButton.TextButtonStyle();
        style.font = font;
        style.up = skin.getDrawable("title");
        TextButton logo = new TextButton("", style);
        buttonsBaseMenu.add(logo);
        buttonsBaseMenu.add(createBasicButton("menu", "start"));
        buttonsBaseMenu.get(1).addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                context.setScreen(ScreenType.LOADING);
                stage.getRoot().removeActor(background);
                return true;
            }
        });

        buttonsBaseMenu.add(createBasicButton("menu", "options"));
        buttonsBaseMenu.get(2).addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menuUI.reset();
                showOptions();
                return true;
            }
        });
        buttonsBaseMenu.add(createBasicButton("menu", "exit"));
        buttonsBaseMenu.get(3).addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return true;
            }
        });

        add(buttonsBaseMenu.get(0)).padTop(80).padRight(50).right().expand().height(buttonsBaseMenu.get(0).getHeight() * 2).width(buttonsBaseMenu.get(0).getWidth() * 2).top().row();
        add(buttonsBaseMenu.get(1)).padRight(50).right().top().row();
        add(buttonsBaseMenu.get(2)).padRight(50).right().top().row();
        add(buttonsBaseMenu.get(3)).expandY().padBottom(buttonsBaseMenu.get(0).getHeight() + 80).padRight(50).right().top().row();


        background.setColor(1, 1, 1, 1);

    }

    private void showOptions() {
        buttonsOptions.clear();
        buttonsOptions.add(createBasicButton("menu", "sound"));
        buttonsOptions.add(createBasicButton("menu", "records"));
        buttonsOptions.add(createBasicButton("menu", "mapping"));
        buttonsOptions.add(createBasicButton("menu", "save"));

        buttonsOptions.get(3).addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menuUI.reset();
                showBaseMenu();
                return true;
            }
        });
        add(buttonsOptions.get(0)).bottom().left().padLeft(50).expand().row();
        add(buttonsOptions.get(1)).bottom().left().padLeft(50).expandX().row();
        add(buttonsOptions.get(2)).bottom().left().padLeft(50).expandX().row();
        add(buttonsOptions.get(3)).bottom().left().padLeft(50).expandX().padBottom(50f).row();
        background.setColor(1, 1, 1, 0.4f);
    }


    private TextButton createBasicButton(String fontStyle, String key) {
        buttonTemplate = new TextButton(i18NBundle.format(key), getSkin(), fontStyle);
        return buttonTemplate;
    }


}
