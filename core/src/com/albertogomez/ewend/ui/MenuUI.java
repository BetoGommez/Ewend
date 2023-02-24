package com.albertogomez.ewend.ui;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.audio.AudioType;
import com.albertogomez.ewend.events.ReturnToMenu;
import com.albertogomez.ewend.screen.ScreenType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;

import static com.albertogomez.ewend.EwendLauncher.SCREEN_HEIGHT;
import static com.albertogomez.ewend.EwendLauncher.SCREEN_WIDTH;

/**
 *
 * @author Alberto Gómez
 */
public class MenuUI extends Table implements EventListener  {

    /**
     * Game stage
     */
    private final Stage stage;
    /**
     * Main game class
     */
    private final EwendLauncher context;
    /**
     * Game skin
     */
    private final Skin skin;
    /**
     * basic font
     */
    private final BitmapFont font;
    /**
     * String bundle formatter
     */
    private final I18NBundle i18NBundle;
    /**
     * Button template
     */
    private TextButton buttonTemplate;
    /**
     * Background image
     */
    private TextButton background;
    /**
     * All basic menu buttons
     */
    private Array<TextButton> buttonsBaseMenu;
    /**
     * All options menu buttons
     */
    private Array<TextButton> buttonsOptions;
    /**
     * Style for the buttons
     */
    private TextButton.TextButtonStyle style;
    /**
     * This class
     */
    private final MenuUI menuUI = this;

    /**
     * Creates the MenuUI and executes the create buttons and background
     * @param context Game main class
     */
    public MenuUI(final EwendLauncher context) {
        super(context.getSkin());
        this.context = context;
        i18NBundle = context.getI18NBundle();
        stage = context.getStage();
        stage.getRoot().addListener(this);
        skin = context.getSkin();
        font = new BitmapFont();
        setFillParent(true);
        buttonsBaseMenu = new Array<TextButton>();
        buttonsOptions = new Array<TextButton>();

        createBackground();
        addBackground();
        showBaseMenu();
        this.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                context.getAudioManager().playAudio(AudioType.MENU_BUTTON);
                return true;
            }
        });
    }

    /**
     * Shows the basic menu
     */
    private void showBaseMenu() {
        buttonsBaseMenu.clear();
        addBackground();
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

    /**
     * Shows the option menu
     */
    private void showOptions() {
        buttonsOptions.clear();
        addBackground();
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


    /**
     * Creates a basic button
     * @param fontStyle Style used
     * @param key Key of the string in bundle
     * @return Button created
     */
    private TextButton createBasicButton(String fontStyle, String key) {
        buttonTemplate = new TextButton(i18NBundle.format(key), getSkin(), fontStyle);
        return buttonTemplate;
    }

    /**
     * Creates the background image
     */
    private void createBackground(){
        TextButton.TextButtonStyle styleBackground = new TextButton.TextButtonStyle();
        styleBackground.font = font;
        styleBackground.up = new TextureRegionDrawable(new TextureRegion(context.getAssetManager().<Texture>get("ui/background.jpg")));
        background = new TextButton("", styleBackground);
        if(SCREEN_HEIGHT > SCREEN_WIDTH){
            background.setSize(1000f * SCREEN_HEIGHT / 900f, 700f * SCREEN_HEIGHT / 900f);
        }else{
            background.setSize(1000f * SCREEN_WIDTH / 900f, 700f * SCREEN_WIDTH / 900f);
        }
    }

    /**
     * Adds the background to the screen
     */
    private void addBackground(){
        this.addActor(background);
    }

    /**
     * Handle event input
     * @param event Event executed
     * @return Always false
     */
    @Override
    public boolean handle(Event event) {
        if(event instanceof ReturnToMenu){
            addBackground();
        }
        return false;
    }
}
