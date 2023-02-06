package com.albertogomez.ewend.constants;

public class Constants {
    public static final float FIXED_TIME_STEP = 1/60f;

    //CAMBIAR SI CAMBIAS LA RESOLUCION DEL TILED MAP
    public static final float UNIT_SCALE = 1/32f;
    public static final short BIT_CIRCLE = 1 <<0;
    public static final short BIT_BOX = 1 <<1;
    public static final short BIT_GROUND = 1 <<2;
    public static final short BIT_PLAYER = 1 <<3;
    public static final String PLAYER_SPRITE_PATH = "character/character_effects.atlas";
}
