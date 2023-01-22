package com.albertogomez.ewend.screen;

import com.albertogomez.ewend.EwendLauncher;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.physics.box2d.*;
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

    public GameScreen(final EwendLauncher context) {
        super(context);

        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();


        //create a circle

        bodyDef.position.set(4.5f,15);
        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bodyDef);

        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0.5f;
        fixtureDef.friction = 0.1f;
        fixtureDef.filter.categoryBits = BIT_CIRCLE;
        fixtureDef.filter.maskBits = BIT_GROUND | BIT_BOX;
        CircleShape shape = new CircleShape();
        shape.setRadius(0.5f);
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
        shape.dispose();



        //create a box

        bodyDef.position.set(5.2f,6);
        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0.5f;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = BIT_BOX;
        fixtureDef.filter.maskBits = BIT_GROUND | BIT_CIRCLE;
        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(0.5f,0.5f);
        fixtureDef.shape = boxShape;
        body.createFixture(fixtureDef);
        boxShape.dispose();

        //create a platform

        bodyDef.position.set(4.5f,2);
        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bodyDef);

        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0.5f;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = BIT_GROUND;
        fixtureDef.filter.maskBits = -1;
        boxShape = new PolygonShape();
        boxShape.setAsBox(4,0.5f);
        fixtureDef.shape = boxShape;
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
            context.setScreen(ScreenType.LOADING);
        }

        viewport.apply(true);
        box2DDebugRenderer.render(world, viewport.getCamera().combined);
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
