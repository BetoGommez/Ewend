package com.albertogomez.ewend.ecs.components.player;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.awt.event.ComponentListener;

/**
 * Component of the player
 * @author Alberto Gómez
 */
public class PlayerComponent implements Component, Pool.Poolable {

    /**
     * Actual player state
     */
    public PlayerState playerState = PlayerState.IDLE;
    /**
     * Player speed values
     */
    public Vector2 speed=new Vector2();
    /**
     * The player has touched the ground true
     */
    public boolean touchingGround;
    /**
     * Time knocked after a hit
     */
    public float knockedTime;
    /**
     * Time accumulator
     */
    public float milisecAccum;
    public Array<Integer> takenFireflys = new Array<Integer>();
    @Override
    public void reset() {
        speed.set(0,0);
        touchingGround=false;
    }
}
