package com.albertogomez.ewend.view;

import static com.albertogomez.ewend.constants.Constants.*;

public enum AnimationType {

    PLAYER_IDLE(PLAYER_SPRITE_PATH,"idle",0.1f,64,64),
    PLAYER_JUMP_START(PLAYER_SPRITE_PATH,"jump_mid",0.05f,64,64),
    PLAYER_LANDING(PLAYER_SPRITE_PATH,"jump_landing",0.05f,64,64),
    PLAYER_RUNNING(PLAYER_SPRITE_PATH,"run",0.05f,64,64),
    PLAYER_DAMAGED(PLAYER_SPRITE_PATH,"hit",0.05f,64,64),
    SHEEP_IDLE(SHEEP_SPRITE_PATH,"idle",0.05f,64,64),
    SHEEP_ATTACK(SHEEP_SPRITE_PATH,"attack",0.25f,64,64),
    SHEEP_RUN(SHEEP_SPRITE_PATH,"walk",0.05f,64,64),
    LAMP_EFFECT(LAMP_SPRITE_PATH,"lampon",0.3f,48,64),
    FIREFLY_EFFECT(FIREFLY_EFFECT_PATH,"firefly",0.3f,32,32);










    private final String atlasPath;
    private final String atlasKey;
    private final float frameTime;

    private final float width;
    private final float height;
    //private final int rowIndex;

    AnimationType(String atlasPath, String atlasKey, float frameTime,float width,float height) {
        this.atlasPath = atlasPath;
        this.atlasKey = atlasKey;
        this.frameTime = frameTime;
        this.width = width;
        this.height = height;
    }

    public String getAtlasKey() {
        return atlasKey;
    }

    public String getAtlasPath() {
        return atlasPath;
    }

    public float getFrameTime() {
        return frameTime;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    //  public int getRowIndex() {
   //     return rowIndex;
    //}
}
