package com.albertogomez.ewend;

import com.albertogomez.ewend.ecs.ECSEngine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;


public class PreferenceManager implements Json.Serializable{
    private final Preferences preferences;
    private final Json json;
    private Vector2 playerPos;

    public PreferenceManager() {
        this.preferences = Gdx.app.getPreferences("ewendLauncher");
        json = new Json();
        playerPos = new Vector2();

    }

    public boolean containsKey(final String key){
        return preferences.contains(key);
    }

    public void setFloatValue(final String key, final float value){
        preferences.putFloat(key,value);
        preferences.flush();
    }

    public float getFloatValue(final String key){
        return preferences.getFloat(key,0.0f);
    }

    public void saveGameState(final Entity player){
        playerPos.set(ECSEngine.b2dCmpMapper.get(player).body.getPosition());
        preferences.putString("GAME_STATE",new Json().toJson(this));
    }

    @Override
    public void write(Json json) {
        json.writeValue("PLAYER_X",playerPos.x);
        json.writeValue("PLAYER_Y",playerPos.y);


    }

    @Override
    public void read(Json json, JsonValue jsonData) {

    }
}
