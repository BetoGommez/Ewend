package com.albertogomez.ewend.ui;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.input.GameKeys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.StringBuilder;

public class LoadingUI extends Table {
    private final ProgressBar progressBar;
    private final TextButton txtButton;
    private final TextButton pressAnyKeyButton;

    private String loadingString ;
    public LoadingUI(final EwendLauncher context) {
        super(context.getSkin());
        setFillParent(true);

        final I18NBundle i18NBundle = context.getI18NBundle();
        loadingString = i18NBundle.format("loading");

        progressBar = new ProgressBar(0,1,0.1f,false,getSkin(), "default");
        progressBar.setAnimateDuration(1);
        txtButton = new TextButton(loadingString,getSkin(), "huge");
        txtButton.getLabel().setWrap(true);

        pressAnyKeyButton = new TextButton(i18NBundle.format("pressAnyKey"),getSkin(),"normal");
        pressAnyKeyButton.getLabel().setWrap(true);
        pressAnyKeyButton.setVisible(false);


        pressAnyKeyButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                context.getInputManager().notifyKeyDown(GameKeys.INTERACT);
                return true;
            }
        } );

        add(pressAnyKeyButton).expand().fill().center().row();
        add(txtButton).expand().fillX().bottom().row();
        add(progressBar).expandX().fillX().bottom().pad(20,25,20,25);
        bottom();
    }
    public void setProgress(final float progress){
        progressBar.setValue(progress);

        final StringBuilder stringBuilder = txtButton.getLabel().getText();
        stringBuilder.setLength(0);
        stringBuilder.append(loadingString);
        stringBuilder.append("\n{ ");
        stringBuilder.append(progress*100);
        stringBuilder.append("% }");
        //...
        txtButton.getLabel().invalidateHierarchy();


        if(progress>=1&& !pressAnyKeyButton.isVisible()){
            pressAnyKeyButton.setVisible(true);
            pressAnyKeyButton.setColor(1,1,1,0);
            pressAnyKeyButton.addAction(Actions.forever(Actions.sequence(Actions.alpha(1,1),Actions.alpha(0,1))));
        }
    }
}
