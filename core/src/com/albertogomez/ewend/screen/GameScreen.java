package com.albertogomez.ewend.screen;

import com.albertogomez.ewend.EwendLauncher;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ObjectFloatMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.albertogomez.ewend.EwendLauncher.*;

/**
 * Screen of the gameplay
 */
public class GameScreen extends AbstractScreen {

    /**
     * Body object
     */
    private final BodyDef bodyDef;

    /**
     * Defines the body behavior and its shape
     */
    private final FixtureDef fixtureDef;

    private final Body player;

    public GameScreen(final EwendLauncher context) {
        super(context);

        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
        Body body = world.createBody(bodyDef);

        //create a box


        bodyDef.position.set(4.5f,3);
        bodyDef.gravityScale = 0;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        player = world.createBody(bodyDef);
        player.setUserData("PLAYER");

        fixtureDef.density=1;
        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0.5f;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = BIT_PLAYER;
        fixtureDef.filter.maskBits = BIT_GROUND ;
        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(0.5f,0.5f);
        fixtureDef.shape = boxShape;
        player.createFixture(fixtureDef);

        boxShape.dispose();



        //create a room

        bodyDef.position.set(0,0);
        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bodyDef);

        body.setUserData("GROUND");
        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0.5f;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = BIT_GROUND;
        fixtureDef.filter.maskBits = -1;
        ChainShape cShape = new ChainShape();
        cShape.createChain(new float[]{1,1,1,15,8,15,8,1});
        fixtureDef.shape = cShape;
        body.createFixture(fixtureDef);
        boxShape.dispose();



    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isTouched()){
          //  context.setScreen(ScreenType.LOADING);
        }
        viewport.apply(true);


        box2DDebugRenderer.render(world, viewport.getCamera().combined);



        final float velx;
        final float vely ;
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            velx=3;
        }else if(Gdx.input.isKeyPressed(Input.Keys.A)){
            velx=-3;
        }else {
            velx=0;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            vely=3;
        }else if(Gdx.input.isKeyPressed(Input.Keys.S)){
            vely=-3;
        }else{
            vely=0;
        }

        player.applyLinearImpulse(
                ((velx-player.getLinearVelocity().x) * player.getMass()),
                ((vely-player.getLinearVelocity().y) * player.getMass()),
                player.getWorldCenter().x,player.getWorldCenter().y,true
        );



    }



    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
