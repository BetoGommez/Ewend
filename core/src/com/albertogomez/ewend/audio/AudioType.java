package com.albertogomez.ewend.audio;

public enum AudioType {

    //menu
    MENU_BUTTON("audio/keys.mp3",false,0.5f),
    MENU_MUSIC("audio/main_theme.mp3",true,0.5f),
    LEVEL("audio/level_music.wav",true,0.2f),

    //objects
    LAMP_TOUCH("audio/lamp_touch.ogg",false,0.5f),
    FIREFLY_TOUCH("audio/firefly_touch.wav",false,0.5f),

    //entities

    //ewend
    EWEND_CHARGE("audio/Ewend/Charge.wav",false,0.5f),
    EWEND_DAMAGED("audio/Ewend/Damaged.mp3",false,0.5f),
    EWEND_DASH("audio/Ewend/Dash.mp3",false,0.5f),
    EWEND_DEAD("audio/Ewend/Dead.wav",false,0.5f),
    EWEND_HIT("audio/Ewend/Hit.wav",false,0.5f),
    EWEND_JUMP("audio/Ewend/Jump.mp3",false,0.5f),
    EWEND_RUN("audio/Ewend/Run.mp3",false,0.5f),


    //sheep
    SHEEP_DAMAGED("audio/Sheep/Damaged.wav",false,0.5f),
    SHEEP_DEAD("audio/Sheep/Dead.mp3",false,0.5f),
    SHEEP_DETECTION("audio/Sheep/Detection.wav",false,0.5f);



    private final String filePath;
    private final boolean isMusic;
    private final float volume;

    AudioType(String filePath, boolean isMusic, float volume) {
        this.filePath = filePath;
        this.isMusic = isMusic;
        this.volume = volume;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isMusic() {
        return isMusic;
    }

    public float getVolume() {
        return volume;
    }
}
