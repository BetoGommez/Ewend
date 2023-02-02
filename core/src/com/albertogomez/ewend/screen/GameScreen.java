package com.albertogomez.ewend.screen;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.map.CollisionArea;
import com.albertogomez.ewend.map.Map;
import com.albertogomez.ewend.ui.GameUI;
import com.albertogomez.ewend.ui.LoadingUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import static com.albertogomez.ewend.constants.Constants.*;

/**
 * Screen of the gameplay
 */
public class GameScreen extends AbstractScreen<GameUI> {

    /**
     * Body object
     */
    private final BodyDef bodyDef;

    /**
     * Defines the body behavior and its shape
     */
    private final FixtureDef fixtureDef;

    private Body player;

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

        ///////////

        //Coge el mapa, lo mete en renderizador y se crea la clase mapa para su procesado
        TiledMap tiledMap = assetManager.get("maps/mapa.tmx", TiledMap.class);

        mapRenderer.setMap(tiledMap);
        map = new Map(tiledMap);

        /////////////////

        //Gestor de formas y fisicas
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
        ////////////

        //Spawn personaje y objetos
        spawnPlayer();
        spawnCollisionAreas();
        ////////////
    }

    private void spawnPlayer(){
        resetBodieAndFixtureDefinition();

        bodyDef.position.set(map.getStartLocation());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.gravityScale=1;
        bodyDef.fixedRotation=true;

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
    }

    private void resetBodieAndFixtureDefinition(){

        bodyDef.position.set(0,0);
        bodyDef.fixedRotation=false;
        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.StaticBody;
        fixtureDef.density=0;
        fixtureDef.isSensor=false;
        fixtureDef.restitution=0;
        fixtureDef.friction=0.2f;

        fixtureDef.filter.categoryBits = 0x0001;
        fixtureDef.filter.maskBits = -1;
        fixtureDef.shape = null;
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
    public void show() {
        super.show();

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }



    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        final float velx;
        final float vely ;
        if(Gdx.input.isKeyPressed(Input.Keys.D)||Gdx.input.isTouched()){
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
    protected GameUI getScreenUI(EwendLauncher context) {
        return new GameUI(context);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }


    @Override
    public void dispose() {
        mapRenderer.dispose();
    }
}
