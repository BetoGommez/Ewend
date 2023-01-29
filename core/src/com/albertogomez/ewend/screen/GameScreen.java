package com.albertogomez.ewend.screen;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.map.CollisionArea;
import com.albertogomez.ewend.map.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ObjectFloatMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.albertogomez.ewend.EwendLauncher.*;
import static com.albertogomez.ewend.constants.Constants.*;

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

    private final AssetManager assetManager;

    private final OrthogonalTiledMapRenderer mapRenderer;
    private final OrthographicCamera gameCamera;
    private final GLProfiler profiler;

    private final Map map;

    public GameScreen(final EwendLauncher context) {
        super(context);

        //Lanza el renderizador de mapa
        mapRenderer = new OrthogonalTiledMapRenderer(null,UNIT_SCALE,context.getSpriteBatch());
        assetManager = context.getAssetManager();
        this.gameCamera = context.getGameCamera();
        //////////


        //Gestor de recursos
        profiler = new GLProfiler(Gdx.graphics);
        profiler.enable();
        //////////


        //crea el personaje
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();

        bodyDef.position.set(5f,5f);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.gravityScale=1;

        player= world.createBody(bodyDef);
        player.setUserData("PLAYER");

        fixtureDef.density=1;
        fixtureDef.isSensor=false;
        fixtureDef.restitution=0;
        fixtureDef.friction=0.5f;
        fixtureDef.filter.categoryBits = BIT_PLAYER;
        fixtureDef.filter.maskBits = BIT_GROUND;

        final PolygonShape pShape = new PolygonShape();
        pShape.setAsBox(0.5f,0.5f);

        fixtureDef.shape = pShape;

        player.createFixture(fixtureDef);
        pShape.dispose();



        ///////////

        //Coge el mapa, lo mete en renderizador y se crea la clase mapa para su procesado
        TiledMap tiledMap = assetManager.get("maps/mapa.tmx", TiledMap.class);
        mapRenderer.setMap(tiledMap);
        map = new Map(tiledMap);
        spawnCollisionAreas();
        /////////////////
    }

    private void resetBodieAndFixtureDefinition(){

    }
    private void spawnCollisionAreas(){
        for(final CollisionArea collisionArea : map.getCollisionAreas()){
            resetBodieAndFixtureDefinition();

            bodyDef.position.set(collisionArea.getX(),collisionArea.getY());
            bodyDef.fixedRotation=true;
            bodyDef.gravityScale = 1;
            bodyDef.type = BodyDef.BodyType.StaticBody;
            final Body body = world.createBody(bodyDef);

            body.setUserData("GROUND");
            fixtureDef.filter.categoryBits = BIT_GROUND;
            fixtureDef.filter.maskBits = -1;
            ChainShape cShape = new ChainShape();
            cShape.createChain(collisionArea.getVertices());
            fixtureDef.shape = cShape;
            body.createFixture(fixtureDef);
            cShape.dispose();
        }
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
            vely = player.getLinearVelocity().y;
        }

        player.applyLinearImpulse(
                ((velx-player.getLinearVelocity().x) * player.getMass()),
                ((vely-player.getLinearVelocity().y) * player.getMass()),
                player.getWorldCenter().x,player.getWorldCenter().y,true
        );

        viewport.apply(true);
        mapRenderer.setView(gameCamera);
        mapRenderer.render();
        box2DDebugRenderer.render(world, viewport.getCamera().combined);

        Gdx.app.debug("RenderInfo","Bindings: "+profiler.getTextureBindings());
        Gdx.app.debug("RenderInfo","Drawcalls: "+profiler.getDrawCalls());
        profiler.reset();


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
        mapRenderer.dispose();
    }
}
