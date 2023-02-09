package com.albertogomez.ewend.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

public class AttackComponent implements Component, Pool.Poolable {

    public float damage;
    public boolean attacking;

    public float delay=1f;
    public float delayAccum=0f;
    public boolean isPlayer;

    public Body attackBox;
    public float attackHitboxWidth;
    public float attackHitboxHeight;

    @Override
    public void reset() {
        damage=0;
        attacking=false;
        isPlayer=false;
        attackHitboxHeight=0;
        attackHitboxWidth=0;
    }

    public boolean canAttack(){
        if(delayAccum>delay){
            return true;
        }
        return false;
    }
}
