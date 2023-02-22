package com.albertogomez.ewend.ecs.components.player;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.awt.event.ComponentListener;

public class PlayerComponent implements Component, Pool.Poolable {

    public PlayerState playerState = PlayerState.IDLE;
    public Vector2 speed=new Vector2();
    public boolean touchingGround;
    public float knockedTime;
    public float milisecAccum;
    public boolean hasItem;
    public Array<Integer> takenFireflys = new Array<Integer>();
    @Override
    public void reset() {
        hasItem=false;
        speed.set(0,0);
        touchingGround=false;
    }
}
