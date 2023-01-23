package com.albertogomez.ewend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        final Fixture fixtureA = contact.getFixtureA();
        final Fixture fixtureB = contact.getFixtureB();

        Gdx.app.debug("COLISION", "BEGIN: "+fixtureA.getBody().getUserData() +" "+fixtureA.isSensor());
        Gdx.app.debug("COLISION", "BEGIN: "+fixtureB.getBody().getUserData() +" "+fixtureB.isSensor());

    }

    @Override
    public void endContact(Contact contact) {
        final Fixture fixtureA = contact.getFixtureA();
        final Fixture fixtureB = contact.getFixtureB();
        Gdx.app.debug("COLISION", "END: "+fixtureA.getBody().getUserData() +" "+fixtureA.isSensor());
        Gdx.app.debug("COLISION", "END: "+fixtureB.getBody().getUserData() +" "+fixtureB.isSensor());

    }



    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
