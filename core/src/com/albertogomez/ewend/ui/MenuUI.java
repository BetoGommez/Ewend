package com.albertogomez.ewend.ui;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.audio.AudioType;
import com.albertogomez.ewend.constants.Configs;
import com.albertogomez.ewend.events.ReturnToMenu;
import com.albertogomez.ewend.screen.ScreenType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
     * All settings buttons
     */
    private Array<TextButton> buttonsSettings;
    /**
     * All records buttons
     */
    private Array<TextButton> buttonsRecords;
    /**
     * All credits buttons
     */
    private Array<TextButton> buttonsCredits;

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
        buttonsSettings = new Array<TextButton>();
        buttonsRecords = new Array<TextButton>();
        buttonsCredits = new Array<TextButton>();

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
        buttonsOptions.add(createBasicButton("menu", "settings"));
        buttonsOptions.add(createBasicButton("menu", "records"));
        buttonsOptions.add(createBasicButton("menu", "credits"));
        buttonsOptions.add(createBasicButton("menu", "save"));

        buttonsOptions.get(3).addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menuUI.reset();
                showBaseMenu();
                return true;
            }
        });
        buttonsOptions.get(1).addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menuUI.reset();
                showRecords();
                return true;
            }
        });
        buttonsOptions.get(2).addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menuUI.reset();
                showCredits();
                return true;
            }
        });

        buttonsOptions.get(0).addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menuUI.reset();
                showSettings();
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
     * Shows settings
     */
    private void showSettings(){
        final Configs config = context.getConfig();
        buttonsSettings.clear();
        addBackground();

        buttonsSettings.add(createBasicButton("menu", "music"));
        changeOpacity(config.Music,buttonsSettings.get(0));

        buttonsSettings.add(createBasicButton("menu", "accelerometer"));
        changeOpacity(config.Acceloremeter,buttonsSettings.get(1));

        buttonsSettings.add(createBasicButton("menu", "vibration"));
        changeOpacity(config.Vibration,buttonsSettings.get(2));
        buttonsSettings.add(createBasicButton("menu", "back"));


        buttonsSettings.get(0).addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                if((config.Music=!config.Music)==false){
                    context.getAudioManager().stopCurrentMusic();
                }else{
                    context.getAudioManager().playAudio(AudioType.MENU_MUSIC);
                }
                changeOpacity(config.Music,buttonsSettings.get(0));
                return true;
            }
        });
        buttonsSettings.get(1).addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                config.Acceloremeter=!config.Acceloremeter;
                changeOpacity(config.Acceloremeter,buttonsSettings.get(1));
                return true;
            }
        });
        buttonsSettings.get(2).addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                config.Vibration=!config.Vibration;
                changeOpacity(config.Vibration,buttonsSettings.get(2));
                return true;
            }
        });

        buttonsSettings.get(3).addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                context.getPreferenceManager().saveConfigs(config);
                menuUI.reset();
                showOptions();
                return true;
            }
        });




        add(buttonsSettings.get(0)).center().padLeft(50).expand();
        add(buttonsSettings.get(1)).center().padLeft(50).expand();
        add(buttonsSettings.get(2)).center().padLeft(50).expand();
        this.row();
        add(buttonsSettings.get(3)).bottom().left().padLeft(50).padBottom(50f);
        background.setColor(1, 1, 1, 0.4f);
    }

    /**
     * Shows records
     */
    private void showRecords(){
        buttonsRecords.clear();
        addBackground();
        buttonsRecords.add(createBasicButton("menu","takenFireflys"));
        buttonsRecords.add(new TextButton(context.getPreferenceManager().getNumberTakenFireflys()+"/3",getSkin(),"menu"));
        buttonsRecords.add(createBasicButton("menu","killedEnemies"));
        buttonsRecords.add(new TextButton(""+context.getPreferenceManager().getKilledEnemies(),getSkin(),"menu"));
        buttonsRecords.add(createBasicButton("menu", "back"));

        buttonsRecords.get(4).addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menuUI.reset();
                showOptions();
                return true;
            }
        });


        add(buttonsRecords.get(0)).bottom().center().padLeft(50).expand();
        add(buttonsRecords.get(1)).bottom().center().padLeft(50).row();
        add(buttonsRecords.get(2)).center().padLeft(50);
        add(buttonsRecords.get(3)).center().padLeft(50).expand().row();
        add(buttonsRecords.get(4)).bottom().left().padLeft(50).expandX().padBottom(50f);
        background.setColor(1, 1, 1, 0.4f);
    }

    /**
     * Shows credits
     */
    private void showCredits(){
        TextButton newButton;
        String[] keys = new String[]{"sound","Álvaro Gómez","mainMusic","Forest Is Magic","levelMusic","Alexandr Zhelanov","soundEffects","Pixabay and Mixkit"
                ,"visual","Álvaro Gómez","mapTiles","The Favare","parallax","Ansimuz","landscape","EiskalterEngel18","enemies","Álvaro Gómez"
        ,"player","Álvaro Gómez","icon","Álvaro Gómez","special","David/Manuel Marín, Liion,\n Gabriel,Tommy,Myke,\nHugo, Piñeiro, Kapz "+i18NBundle.format("parents"),"back"};
        buttonsCredits.clear();
        addBackground();
        for (int i = 0; i < keys.length-3; i=i+2) {
            buttonsCredits.add(createBasicButton("huge",keys[i]));
            buttonsCredits.add(new TextButton(keys[i+1],getSkin(),"huge"));

        }
        buttonsCredits.add(createBasicButton("huge",keys[keys.length-3]));
        buttonsCredits.add(new TextButton(keys[keys.length-2],getSkin(),"huge"));
        buttonsCredits.add(createBasicButton("menu",keys[keys.length-1]));
        buttonsCredits.get(buttonsCredits.size-1).addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menuUI.reset();
                showOptions();
                return true;
            }
        });
        for (int i = 0; i < buttonsCredits.size-1; i=i+2) {
            add(buttonsCredits.get(i)).padTop(30f).center();
            add(buttonsCredits.get(i+1)).left().expandX().row();
        }
        add(buttonsCredits.get(buttonsCredits.size-1)).bottom().left().padBottom(50f).expandX().padLeft(50);
        background.setColor(1, 1, 1, 0.4f);
    }

    /**
     * Changes the text opacity
     * @param state True or false activated
     * @param button Button to set opcaity
     */
    private void changeOpacity(boolean state,TextButton button){
        button.getLabel().setColor(1,1,1,(state)?1:0.6f);
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
