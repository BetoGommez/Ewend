package com.albertogomez.ewend.constants;

public class Constants {
    /**
     * Value of fps
     */
    public static final float FIXED_TIME_STEP = 1/60f;
    /**
     *Map bit size
     */
    public static final float UNIT_SCALE = 1/16f;
    /**
     * Mask bit of the ground
     */
    public static final short BIT_GROUND = 1 <<0;
    /**
     * Mask bit of the game objects
     */
    public static final short BIT_GAME_OBJECT = 1 <<1;
    /**
     * Mask bit of the player
     */
    public static final short BIT_PLAYER = 1 <<2;
    /**
     * Mask bit of the enemies
     */
    public static final short BIT_ENEMY = 1 <<3;
    /**
     * Mask bit of the player attack
     */
    public static final short BIT_PLAYER_ATTACK = 1 <<4;
    /**
     * Mask bit of the enemy attack
     */
    public static final short BIT_ENEMY_ATTACK = 1 <<5;
    /**
     * Mask bit of the ground
     */
    public static final short BIT_BOUND = 1 <<6;

    /**
     * Dash delay time between dashes
     */
    public static final float DASH_DELAY = 3;

    /**
     * Path of the player atlas
     */
    public static final String PLAYER_SPRITE_PATH = "character/character.atlas";
    /**
     * Path of the sheep atlas
     */
    public static final String SHEEP_SPRITE_PATH = "enemy/sheep.atlas";
    /**
     * Path of the lamp atlas
     */
    public static final String LAMP_SPRITE_PATH = "object/lamp_effect.atlas";
    /**
     * Path of the firefly atlas
     */
    public static final String FIREFLY_EFFECT_PATH = "object/firefly_effect.atlas";
    /**
     * Path of the background image path
     */
    public static final String BACKGROUND_PATH = "maps/map1/materials/Background/background_0";
}
