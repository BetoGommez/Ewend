package com.albertogomez.ewend.constants;

/**
 * Saves the three basic configuration
 * @author Alberto Gómez
 */
public class Configs {
    /**
     * If true vibration activated
     */
    public boolean Vibration;
    /**
     * If true accelerometer activated
     */
    public boolean Acceloremeter;
    /**
     * If true music activated
     */
    public boolean Music;

    /**
     * Creates a configuration with values
     * @param vibration Vibration
     * @param acceloremeter Accelerometer
     * @param music Music
     */
    public Configs(boolean vibration, boolean acceloremeter, boolean music) {
        Vibration = vibration;
        Acceloremeter = acceloremeter;
        this.Music = music;
    }
}
