package com.albertogomez.ewend.constants;

public class Constants {
    public static final float FIXED_TIME_STEP = 1/60f;

    //CAMBIAR SI CAMBIAS LA RESOLUCION DEL TILED MAP
    public static final float UNIT_SCALE = 1/32f;
    public static final short BIT_GROUND = 1 <<0;
    public static final short BIT_GAME_OBJECT = 1 <<1;
    public static final short BIT_PLAYER = 1 <<2;
    public static final short BIT_ENEMY = 1 <<3;
    public static final short BIT_PLAYER_ATTACK = 1 <<4;
    public static final short BIT_ENEMY_ATTACK = 1 <<5;
    public static final String PLAYER_SPRITE_PATH = "character/character_effects.atlas";
    public static final String SHEEP_SPRITE_PATH = "enemy/sheep.atlas";
}
