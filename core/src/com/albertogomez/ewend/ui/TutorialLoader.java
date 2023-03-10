package com.albertogomez.ewend.ui;


import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.LifeComponent;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.SnapshotArray;

import java.awt.Color;

public class TutorialLoader {

    /**
     * Game UI
     */
    private final GameUI gameUI;
    /**
     * Game UI buttons
     */
    private Array<TextButton> buttons;
    /**
     * Button with text in middle of screen
     */
    private TextButton screenText;
    /**
     * Actual button selected index
     */
    private int buttonIndex;
    /**
     * String resource manager
     */
    private I18NBundle i18NBundle;
    /**
     * Actual printed char index
     */
    private int charIndex;
    /**
     * Story text to tell index
     */
    private int storyIndex;
    /**
     * If true orders to write text o screen
     */
    public boolean writeText;
    /**
     * Text being printed
     */
    private String actualText;

    /**
     * Contrstructor for tutorial
     * @param gameUI Game UI
     */
    public TutorialLoader(final GameUI gameUI) {
        this.gameUI = gameUI;

        this.i18NBundle = gameUI.getContext().getI18NBundle();
        buttonIndex = 0;
        storyIndex=0;
        charIndex=-1;
        writeText = true;
        actualText = "";
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();



        screenText = new TextButton("", gameUI.getSkin(), "menu");
        screenText.setColor(0,0,0,1f);
        screenText.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                if (storyIndex == 4) {

                    screenText.removeListener(this);
                    startTutorial();
                } else {
                    if(actualText.length()-1==charIndex){

                        writeText = true;
                        charIndex=-1;
                        storyIndex++;
                        actualText = i18NBundle.format("story"+storyIndex);
                        screenText.setText("");

                    }
                }

                return true;
            }
        });
        screenText.align(Align.center);
        screenText.setZIndex(5);
        screenText.setFillParent(true);
        screenText.getLabel().setWrap(true);
        screenText.pad(400f);

        screenText.center();
        screenText.setVisible(true);
        gameUI.addActor(screenText);


    }

    /**
     * Hides all game buttons
     */
    public void hideButtons(){
        this.buttons = gameUI.getButtons();
        for (int i = 0; i < buttons.size; i++) {
            buttons.get(i).setVisible(false);
        }
    }

    /**
     * Writes one character of the text each call
     * @param delta Time elapsed
     */
    public void writeText(float delta) {
        if(charIndex<actualText.length()-1){
            charIndex++;
            screenText.setText(screenText.getText()+""+actualText.charAt(charIndex));

        }else{
            writeText=false;

        }
    }


    /**
     * All button tutorial step by step
     */
    public void startTutorial() {
        final TextButton actualTextButton;
        if (buttonIndex < buttons.size) {
            actualTextButton = buttons.get(buttonIndex);
            actualTextButton.setVisible(true);
            charIndex=-1;
            actualText=i18NBundle.format( actualTextButton.getName().toLowerCase()+"Instruction");
            writeText=true;
            switch (actualTextButton.getName()) {

                case "PURIFY":
                    screenText.setText("");

                    LifeComponent lifeComponent = ECSEngine.lifeCmpMapper.get(gameUI.getContext().getEcsEngine().getPlayer());
                    lifeComponent.health = 80;
                    lifeComponent.fury = 40;
                    gameUI.getFuryBar().setValue(lifeComponent.fury / 100);
                    gameUI.getHealthBar().setValue(lifeComponent.health / 100);
                    actualTextButton.addListener(new ClickListener() {
                        @Override
                        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                                actualTextButton.setVisible(false);
                                actualTextButton.removeListener(this);
                                buttonIndex++;
                                actualText="";
                                charIndex=-1;
                                startTutorial();

                        }
                    });
                    break;

                case "DASH":


                    ECSEngine.playerCmpMapper.get(gameUI.getContext().getEcsEngine().getPlayer()).touchingGround = false;
                default:
                    screenText.setText("");
                    writeText=true;
                    actualTextButton.addListener(new ClickListener() {
                        @Override
                        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                            actualTextButton.setVisible(false);
                            actualTextButton.removeListener(this);
                            buttonIndex++;

                            ECSEngine.playerCmpMapper.get(gameUI.getContext().getEcsEngine().getPlayer()).touchingGround = true;
                            startTutorial();

                        }
                    });
                    break;
            }





        } else {
            gameUI.removeActor(screenText);
            gameUI.getContext().getPreferenceManager().setTutorialDone();
            for (int i = 0; i < buttons.size; i++) {
                buttons.get(i).setVisible(true);
            }
        }

    }


}
