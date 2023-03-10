package com.albertogomez.ewend.audio;

import com.albertogomez.ewend.EwendLauncher;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 *Plays the app sounds
 * @author Alberto Gómez
 */
public class AudioManager {
    /**
     * Current music type
     */
    private AudioType currentMusicType;
    /**
     * Current music
     */
    private Music currentMusic;
    /**
     * Asset game manager
     */
    private final AssetManager assetManager;


    /**
     * Creates the AudioManager
     * @param assetManager
     */
    public AudioManager(AssetManager assetManager){
        this.assetManager =assetManager;
        currentMusic = null;
        currentMusicType=null;
    }

    /**
     * Plays an audio or music
     * @param type Enum with the audio path
     */
    public void playAudio(final AudioType type){
        if(type.isMusic()){
            //play music
            if(currentMusicType==type){
                //is already playing
                return;
            }else if(currentMusic!=null){
                currentMusic.stop();
            }

            currentMusicType=type;
            currentMusic = assetManager.get(type.getFilePath(),Music.class);
            currentMusic.setLooping(true);
            currentMusic.setVolume(type.getVolume());
            currentMusic.play();
        }else{
            //play sound
            assetManager.get(type.getFilePath(), Sound.class).play(type.getVolume());
        }
    }

    /**
     * Stops the current playing music
     */
    public void stopCurrentMusic(){
        if(currentMusic!=null){
            currentMusic.stop();
            currentMusic=null;
            currentMusicType=null;
        }
    }
}
