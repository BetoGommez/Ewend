package com.albertogomez.ewend.ui;


import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.events.ResetLevel;
import com.albertogomez.ewend.input.DeadScreenButtonListener;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;

/**
 * Layout for when the player is dead
 * @author Alberto Gómez
 */
public class LevelEnd extends Table implements EventListener {

    /**
     * Game stage
     */
    private final Stage stage;
    /**
     * All buttons
     */
    private Array<TextButton> buttons;
    /**
     * String bungle formatter
     */
    private final I18NBundle i18NBundle;
    /**
     * Stage skin
     */
    private Skin skin;
    /**
     * Listener for the buttons
     */
    private final DeadScreenButtonListener buttonListener;
    /**
     * Button general size
     */
    private float buttonSize;


    public LevelEnd(EwendLauncher context,boolean won) {
        super(context.getSkin());
        setFillParent(true);
        stage = context.getStage();
        skin = context.getSkin();
        buttons = new Array<TextButton>();
        i18NBundle = context.getI18NBundle();
        buttonListener = new DeadScreenButtonListener(context);

        this.row().colspan(3);
        if(won){
            congratsMessage();
        }

        createButtons();
        this.setColor(1,1,1,0.8f);
    }

    /**
     * Creates a congratulations message if has won
     */
    private void congratsMessage(){
        TextButton congrats = createButton("menu","congratulations");

        this.add(congrats).center().size(EwendLauncher.SCREEN_HEIGHT /4,EwendLauncher.SCREEN_HEIGHT /8).row();
    }

    /**
     * Creates restart and menu button
     */
    private void createButtons() {
        stage.addListener(this);
        buttonSize = EwendLauncher.SCREEN_HEIGHT / 4;


        buttons.add(createButton("menu","restart"));
        buttons.get(0).addListener(buttonListener);
        buttons.get(0).setName("Restart");
        add(buttons.get(0)).size(buttonSize, buttonSize).top().right().padRight(30f);
        this.add();
        buttons.add(createButton("menu","menu"));
        buttons.get(1).setName("Menu");
        buttons.get(1).addListener(buttonListener);
        add(buttons.get(1)).size(buttonSize, buttonSize).top().left().padLeft(30f);


    }


    /**
     * Genreal button creator
     * @param fontStyle Font size
     * @param key Key of the string to get from bundle
     * @return Button done
     */
    private TextButton createButton(String fontStyle,String key) {
        return new TextButton(i18NBundle.format(key), skin, fontStyle);
    }


    /**
     * Handle event input
     * @param event Event executed
     * @return Always false
     */
    @Override
    public boolean handle(Event event) {
        if(event instanceof ResetLevel){
            stage.removeListener(this);
            stage.getRoot().removeActor(this);
        }

        return false;
    }

    @Override
    public boolean fire(Event event) {


        return true;
    }
}
