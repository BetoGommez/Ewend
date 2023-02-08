package com.albertogomez.ewend.ecs.components;

import com.albertogomez.ewend.view.AnimationType;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class AnimationComponent implements Component, Pool.Poolable{

    public AnimationType aniType;
    public float aniTime;
    public float width;
    public float height;
    @Override
    public void reset() {
        aniTime=0;
        aniType=null;
        width = height = 0;
    }


}
