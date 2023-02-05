package com.albertogomez.ewend.screen;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.input.GameKeys;
import com.albertogomez.ewend.input.InputManager;
import com.albertogomez.ewend.map.CollisionArea;
import com.albertogomez.ewend.map.Map;
import com.albertogomez.ewend.ui.GameUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.*;

import static com.albertogomez.ewend.EwendLauncher.BODY_DEF;
import static com.albertogomez.ewend.EwendLauncher.FIXTURE_DEF;
import static com.albertogomez.ewend.constants.Constants.*;

/**
 * Screen of the gameplay
 */
public class GameScreen extends AbstractScreen<GameUI> {

    /**
     * Body object
     */


    /**
     * Defines the body behavior and its shape
     */


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
        //profiler.enable();
        //////////

        //Coge el mapa, lo mete en renderizador y se crea la clase mapa para su procesado
        TiledMap tiledMap = assetManager.get("maps/mapa.tmx", TiledMap.class);

        mapRenderer.setMap(tiledMap);
        map = new Map(tiledMap);
        /////////////////

        //Spawn personaje y objetos
        spawnCollisionAreas();
        context.getEcsEngine().createPlayer(map.getStartLocation(),0.75f,0.75f);
        ////////////
    }


    private void spawnCollisionAreas(){

        for(final CollisionArea collisionArea : map.getCollisionAreas()){

            BODY_DEF.position.set(collisionArea.getX(),collisionArea.getY());
            BODY_DEF.fixedRotation=true;
            final Body body = world.createBody(BODY_DEF);

            body.setUserData("GROUND");
            FIXTURE_DEF.filter.categoryBits = BIT_GROUND;
            FIXTURE_DEF.filter.maskBits = -1;
            ChainShape cShape = new ChainShape();
            cShape.createChain(collisionArea.getVertices());
            FIXTURE_DEF.shape = cShape;
            body.createFixture(FIXTURE_DEF);
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

        viewport.apply(false);
        mapRenderer.setView(gameCamera);
        mapRenderer.render();
        box2DDebugRenderer.render(world, viewport.getCamera().combined);
        //Gdx.app.debug("RenderInfo","Bindings: "+profiler.getTextureBindings());
        //Gdx.app.debug("RenderInfo","Drawcalls: "+profiler.getDrawCalls());
        //profiler.reset();
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

    @Override
    public boolean keyUp(InputManager inputManager, GameKeys key) {
        return false;
    }
}
