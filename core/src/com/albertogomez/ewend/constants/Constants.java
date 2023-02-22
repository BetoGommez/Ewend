package com.albertogomez.ewend.constants;

public class Constants {
    public static final float FIXED_TIME_STEP = 1/60f;

    //CAMBIAR SI CAMBIAS LA RESOLUCION DEL TILED MAP
    public static final float UNIT_SCALE = 1/16f;
    public static final short BIT_GROUND = 1 <<0;
    public static final short BIT_GAME_OBJECT = 1 <<1;
    public static final short BIT_PLAYER = 1 <<2;
    public static final short BIT_ENEMY = 1 <<3;
    public static final short BIT_PLAYER_ATTACK = 1 <<4;
    public static final short BIT_ENEMY_ATTACK = 1 <<5;
    public static final short BIT_BOUND = 1 <<6;

    public static final float DASH_DELAY = 3;

    public static final String PLAYER_SPRITE_PATH = "character/character.atlas";
    public static final String SHEEP_SPRITE_PATH = "enemy/sheep.atlas";
    public static final String LAMP_SPRITE_PATH = "object/lamp_effect.atlas";
    public static final String FIREFLY_EFFECT_PATH = "object/firefly_effect.atlas";
    public static final String BACKGROUND_PATH = "maps/map1/materials/Background/background_0";
}
