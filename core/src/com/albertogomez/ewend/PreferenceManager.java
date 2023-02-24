package com.albertogomez.ewend;

import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Storaged prefrences manager
 * @author Alberto Gómez
 */
public class PreferenceManager{
    /**
     * Game preferences
     */
    private final Preferences preferences;

    /**
     * Constructor that gets the preferences
     */
    public PreferenceManager() {
        this.preferences = Gdx.app.getPreferences("ewendLauncher");

    }

    /**
     * Saves the taken fireflys
     * @param fireflyIndexes Taken fireflys during game
     */
    public void saveTakenFireflys(Array<Integer> fireflyIndexes){
        String key = "TAKEN_FIREFLYS";
        String newSave = "";
        int parsedIndex = 0;
        if(preferences.getString(key)!=null){
            String[] alreadySaved = preferences.getString(key).split(",");
            for (int i = 0; i < alreadySaved.length; i++) {
                if(!alreadySaved[i].equals("")){
                    parsedIndex = Integer.parseInt(alreadySaved[i]);
                    if(fireflyIndexes.contains(parsedIndex,true)){
                        fireflyIndexes.removeValue(parsedIndex,true);
                    }
                }
            }
            if(fireflyIndexes.size!=0){
                newSave = fireflyIndexes.toString(",");
            }
            for (int i = 0; i < alreadySaved.length; i++) {
                newSave+= alreadySaved[i]+",";
            }
            preferences.putString(key,newSave);

        }else{
            preferences.putString(key,"");
        }
        preferences.flush();
    }
}
