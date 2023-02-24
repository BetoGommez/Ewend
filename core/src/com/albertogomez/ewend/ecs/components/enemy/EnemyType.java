package com.albertogomez.ewend.ecs.components.enemy;

import com.albertogomez.ewend.view.AnimationType;

import static com.albertogomez.ewend.view.AnimationType.SHEEP_IDLE;

/**
 * Different enemies that exist
 * @author Alberto Gómez
 */
public enum EnemyType {
    SHEEP(SHEEP_IDLE);

    /**
     * The entity default animation
     */
    public AnimationType defaultAnimation;


    /**
     * Creates the type
     * @param defaultAnimation The entity default animation
     */
    EnemyType(AnimationType defaultAnimation) {
        this.defaultAnimation = defaultAnimation;

    }


}
