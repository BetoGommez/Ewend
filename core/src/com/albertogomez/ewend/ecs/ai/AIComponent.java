package com.albertogomez.ewend.ecs.ai;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class AIComponent implements Component, Pool.Poolable {

    public AIState state = AIState.IDLE;
    public float milisecAccum=0f ;
    public float idleDelay=1f ;
    public float knockTime = 1.4f;
    public boolean attacked =false;
    public float maxDistanceFactor = 2f;
    public Vector2 initialPosition;

    public int direction = 1;

    @Override
    public void reset() {
        AIState state = AIState.IDLE;

    }
}
