package com.albertogomez.ewend.ecs.components.enemy;

import com.albertogomez.ewend.view.AnimationType;

import static com.albertogomez.ewend.view.AnimationType.SHEEP_IDLE;

public enum EnemyType {
    SHEEP(SHEEP_IDLE,1.3f,1.6f),
    LAMA(null,0,0),
    RAM(null,0,0);

    public AnimationType defaultAnimation;
    public float fixedSizeX;
    public float fixedSizeY;

    EnemyType(AnimationType defaultAnimation, float fixedSizeX, float fixedSizeY) {
        this.defaultAnimation = defaultAnimation;
        this.fixedSizeX = fixedSizeX;
        this.fixedSizeY = fixedSizeY;
    }


}
