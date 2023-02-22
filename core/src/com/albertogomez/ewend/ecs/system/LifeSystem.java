package com.albertogomez.ewend.ecs.system;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.AttackComponent;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.DeadComponent;
import com.albertogomez.ewend.ecs.components.LifeComponent;
import com.albertogomez.ewend.events.EnemyDied;
import com.albertogomez.ewend.events.PlayerManaAdded;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public class LifeSystem extends IteratingSystem implements EventListener {



    private float manaAdd;

    public LifeSystem(EwendLauncher context) {
        super(Family.all(B2DComponent.class, LifeComponent.class).get());

        context.getStage().getRoot().addListener(this);

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Body entityBody = ECSEngine.b2dCmpMapper.get(entity).body;
        LifeComponent lifeComponent = ECSEngine.lifeCmpMapper.get(entity);
        if(ECSEngine.playerCmpMapper.get(entity)!=null&&manaAdd>1){
            if(lifeComponent.mana+manaAdd<=100){
                lifeComponent.addMana(manaAdd);
            }else {
                lifeComponent.addMana(100-lifeComponent.mana);
            }
                manaAdd=0;
            Gdx.app.debug("MANA: ", lifeComponent.mana+"");
        }

        if(lifeComponent.health<=0&&entity.getComponent(DeadComponent.class)==null){
            entity.add(new DeadComponent(5));
        }

    }

    @Override
    public boolean handle(Event event) {
        if(event instanceof EnemyDied){
            manaAdd=((EnemyDied)event).mana;
        }
        return false;
    }
}
