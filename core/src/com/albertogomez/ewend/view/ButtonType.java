package com.albertogomez.ewend.view;

public enum ButtonType {
    LEFT_BUTTON("left",70,70),
    RIGHT_BUTTON("right",70,70),
    JUMP_BUTTON("left",70,70),
    ATTACK_BUTTON("left",70,70),
    CHARGE_BUTTON("left",70,70),
    DASH_BUTTON("left",70,70);

    public String atlasName;

    public float width;
    public float heigth;

    ButtonType(String atlasName, float width, float heigth) {
        this.atlasName = atlasName;
        this.width = width;
        this.heigth = heigth;
    }
}
