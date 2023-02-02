package com.albertogomez.ewend.audio;

public enum AudioType {

    SELECT("audio/select.mp3",false,0.5f),
    LEVEL("audio/level_music.mp3",true,0.4f);

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
