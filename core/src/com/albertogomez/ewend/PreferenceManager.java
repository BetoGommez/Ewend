package com.albertogomez.ewend;

import com.albertogomez.ewend.constants.Configs;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;

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

    /**
     * Load the config preferences
     * @return Configuration object
     */
    public Configs loadConfig(){
        boolean music=true;
        boolean accelerometer=true;
        boolean vibration=true;
        if(preferences.contains("music")){
            music = preferences.getBoolean("music");
            accelerometer = preferences.getBoolean("accelerometer");
            vibration = preferences.getBoolean("vibration");
        }

        return new Configs(vibration,accelerometer,music);
    }

    /**
     * Saves the config internally
     * @param configs Config object
     */
    public void saveConfigs(Configs configs){

        preferences.putBoolean("music",configs.Music);
        preferences.putBoolean("accelerometer",configs.Acceloremeter);
        preferences.putBoolean("vibration",configs.Vibration);
        preferences.flush();
    }

    /**
     * Gets the number of taken fireflys
     * @return Taken fireflys
     */
    public int getNumberTakenFireflys(){
        return preferences.getString("TAKEN_FIREFLYS").split(",").length;
    }

    /**
     * Adds 1 to killed enemies
     */
    public void updateKilledEnemies(){
        preferences.putInteger("killedEnemies",preferences.getInteger("killedEnemies")+1);
        preferences.flush();
    }

    /**
     * Gets if the tutorial has been done before
     * @return True if the tutorial its done
     */
    public boolean getTutorialDone(){
        return preferences.getBoolean("tutorialDone");
    }

    /**
     * Sets the tutorial into state done saved
     */
    public void setTutorialDone(){
        preferences.putBoolean("tutorialDone",true);
        preferences.flush();
    }

    /**
     * Get the number of killed enemies
     * @return Killed enemies
     */
    public int getKilledEnemies(){
        return preferences.getInteger("killedEnemies");
    }


}
