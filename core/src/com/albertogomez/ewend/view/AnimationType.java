package com.albertogomez.ewend.view;

import static com.albertogomez.ewend.constants.Constants.PLAYER_SPRITE_PATH;

public enum AnimationType {

    PLAYER_IDLE(PLAYER_SPRITE_PATH,"idle",0.05f),
    PLAYER_JUMP_START(PLAYER_SPRITE_PATH,"jump_mid",0.05f),
    PLAYER_LANDING(PLAYER_SPRITE_PATH,"jump_landing",0.05f),

    PLAYER_RUNNING(PLAYER_SPRITE_PATH,"run",0.05f);

    private final String atlasPath;
    private final String atlasKey;
    private final float frameTime;
    //private final int rowIndex;

    AnimationType(String atlasPath, String atlasKey, float frameTime) {
        this.atlasPath = atlasPath;
        this.atlasKey = atlasKey;
        this.frameTime = frameTime;
        //this.rowIndex = rowIndex;
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

  //  public int getRowIndex() {
   //     return rowIndex;
    //}
}
