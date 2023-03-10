package com.albertogomez.ewend.audio;


/**
 * Enum of audio files types
 */
public enum AudioType {

    //menu
    MENU_BUTTON("audio/keys.mp3",false,0.5f),
    MENU_MUSIC("audio/main_theme.mp3",true,1f),
    LEVEL("audio/level_music.wav",true,0.10f),

    //objects
    LAMP_TOUCH("audio/lamp_touch.ogg",false,0.5f),
    FIREFLY_TOUCH("audio/firefly_touch.wav",false,0.5f),

    //entities

    //ewend
    EWEND_CHARGE("audio/Ewend/Charge.wav",false,0.4f),
    EWEND_DAMAGED("audio/Ewend/Damaged.mp3",false,0.4f),
    EWEND_DASH("audio/Ewend/Dash.mp3",false,0.4f),
    EWEND_DEAD("audio/Ewend/Dead.wav",false,0.3f),
    EWEND_HIT("audio/Ewend/Hit.wav",false,0.4f),
    EWEND_JUMP("audio/Ewend/Jump.mp3",false,0.8f),
    EWEND_RUN("audio/Ewend/Run.mp3",false,1f),


    //sheep
    SHEEP_DAMAGED("audio/Sheep/Damaged.wav",false,0.4f),
    SHEEP_DEAD("audio/Sheep/Dead.mp3",false,0.4f),
    SHEEP_DETECTION("audio/Sheep/Detection.wav",false,0.4f);


    /**
     *Internal audio file path
     */
    private final String filePath;
    /**
     * When False is just a sound to play when time
     */
    private final boolean isMusic;
    /**
     * Volume value
     */
    private final float volume;

    /**
     * Creates a audio enum
     * @param filePath Internal audio file path
     * @param isMusic When False is just a sound to play when time
     * @param volume Volume value
     */
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
