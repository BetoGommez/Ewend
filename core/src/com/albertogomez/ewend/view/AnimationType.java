package com.albertogomez.ewend.view;

import com.badlogic.gdx.graphics.g2d.Animation;

import static com.albertogomez.ewend.constants.Constants.*;

public enum AnimationType {


    //PLAYER
    PLAYER_IDLE(PLAYER_SPRITE_PATH,"Idle",0.1f, Animation.PlayMode.LOOP,74,71),
    PLAYER_RUNNING(PLAYER_SPRITE_PATH,"Run",0.05f,Animation.PlayMode.LOOP,76,60),
    PLAYER_JUMP_START(PLAYER_SPRITE_PATH,"Jump",0.05f,Animation.PlayMode.LOOP,54,82),
    PLAYER_DOUBLE_JUMP(PLAYER_SPRITE_PATH,"DoubleJump",0.05f,Animation.PlayMode.LOOP,36,36),
    PLAYER_FALL(PLAYER_SPRITE_PATH,"Fall",0.05f,Animation.PlayMode.LOOP,54,71),
    PLAYER_DASH(PLAYER_SPRITE_PATH,"Dash",0.05f,Animation.PlayMode.LOOP,63,32),
    PLAYER_DAMAGED(PLAYER_SPRITE_PATH,"Damaged",0.1f,Animation.PlayMode.NORMAL,68,64),
    PLAYER_DEAD(PLAYER_SPRITE_PATH,"Die",0.3f,Animation.PlayMode.NORMAL,80,64),
    PLAYER_ATTACK(PLAYER_SPRITE_PATH,"Attack",0.09f,Animation.PlayMode.NORMAL,103,66),

    //ENEMIES
    SHEEP_IDLE(SHEEP_SPRITE_PATH,"Idle",0.15f,Animation.PlayMode.LOOP,60,65),
    SHEEP_ATTACK(SHEEP_SPRITE_PATH,"Attack",0.15f,Animation.PlayMode.NORMAL,65,65),
    SHEEP_WALK(SHEEP_SPRITE_PATH,"Walk",0.10f,Animation.PlayMode.LOOP,60,65),
    SHEEP_RUN(SHEEP_SPRITE_PATH,"Run",0.15f,Animation.PlayMode.LOOP,60,65),
    SHEEP_DAMAGED(SHEEP_SPRITE_PATH,"Damaged",1,Animation.PlayMode.NORMAL,60,65),
    SHEEP_TRANSFORM(SHEEP_SPRITE_PATH,"Transform",0.2f,Animation.PlayMode.NORMAL,60,65),

    //OBJECTS
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
