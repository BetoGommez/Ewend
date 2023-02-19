package com.albertogomez.ewend.view;

import com.badlogic.gdx.graphics.g2d.Animation;

import static com.albertogomez.ewend.constants.Constants.*;

public enum AnimationType {

    PLAYER_IDLE(PLAYER_SPRITE_PATH,"idle",0.1f, Animation.PlayMode.LOOP,64,64),
    PLAYER_JUMP_START(PLAYER_SPRITE_PATH,"jump_mid",0.05f,Animation.PlayMode.LOOP,64,64),
    PLAYER_LANDING(PLAYER_SPRITE_PATH,"jump_landing",0.05f,Animation.PlayMode.LOOP,64,64),
    PLAYER_RUNNING(PLAYER_SPRITE_PATH,"run",0.05f,Animation.PlayMode.LOOP,64,64),
    PLAYER_DAMAGED(PLAYER_SPRITE_PATH,"hit",0.05f,Animation.PlayMode.LOOP,64,64),
    PLAYER_DEAD(PLAYER_SPRITE_PATH,"death",1f,Animation.PlayMode.NORMAL,64,64),
    PLAYER_ATTACK(PLAYER_SPRITE_PATH,"AttackA",0.10f,Animation.PlayMode.NORMAL,64,64),

    SHEEP_IDLE(SHEEP_SPRITE_PATH,"Idle",0.15f,Animation.PlayMode.LOOP,60,65),
    SHEEP_ATTACK(SHEEP_SPRITE_PATH,"Attack",0.15f,Animation.PlayMode.NORMAL,65,65),
    SHEEP_WALK(SHEEP_SPRITE_PATH,"Walk",0.10f,Animation.PlayMode.LOOP,60,65),
    SHEEP_RUN(SHEEP_SPRITE_PATH,"Run",0.15f,Animation.PlayMode.LOOP,60,65),

    LAMP_EFFECT(LAMP_SPRITE_PATH,"lampon",0.3f, Animation.PlayMode.NORMAL,48,64),
    FIREFLY_EFFECT(FIREFLY_EFFECT_PATH,"firefly",0.3f, Animation.PlayMode.LOOP,32,32);

    private final String atlasPath;
    private final String atlasKey;
    private final float frameTime;
    private final Animation.PlayMode playMode;

    private final float width;
    private final float height;
    //private final int rowIndex;

    AnimationType(String atlasPath, String atlasKey, float frameTime, Animation.PlayMode playMode, float width, float height) {
        this.atlasPath = atlasPath;
        this.atlasKey = atlasKey;
        this.frameTime = frameTime;
        this.width = width;
        this.height = height;
        this.playMode = playMode;
    }

    public Animation.PlayMode getPlayMode() {
        return playMode;
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
