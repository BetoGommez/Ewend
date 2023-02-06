package com.albertogomez.ewend.view;

import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.AnimationComponent;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.map.Map;
import com.albertogomez.ewend.map.MapListener;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.profiling.GLProfiler;

import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.albertogomez.ewend.constants.Constants.UNIT_SCALE;

public class GameRenderer implements Disposable, MapListener {

    private final AssetManager assetManager;
    private final FitViewport viewport;
    private final OrthographicCamera gameCamera;
    private final SpriteBatch spriteBatch;

    private final OrthogonalTiledMapRenderer mapRenderer;
    private final GLProfiler profiler;
    private final World world;
    private final Box2DDebugRenderer box2DDebugRenderer;

    private Sprite dummySprite;
    private final ImmutableArray<Entity> animatedEntities;

    private final Array<TiledMapTileLayer> tiledMapLayers;




    public GameRenderer(final EwendLauncher context) {
        assetManager = context.getAssetManager();
        viewport = context.getScreenViewport();
        gameCamera = context.getGameCamera();
        spriteBatch = context.getSpriteBatch();

        animatedEntities = context.getEcsEngine().getEntitiesFor(Family.all(AnimationComponent.class,B2DComponent.class).get());

        mapRenderer = new OrthogonalTiledMapRenderer(null,UNIT_SCALE,context.getSpriteBatch());
        context.getMapManager().addMapListener(this);
        tiledMapLayers = new Array<TiledMapTileLayer>();

        profiler = new GLProfiler(Gdx.graphics);
        box2DDebugRenderer = new Box2DDebugRenderer();
        world = context.getWorld();

        if(profiler.isEnabled()){
        }else{
            //box2DDebugRenderer = null;
            //world = null;
        }

        //TODO aqui puedes probar los cambios de mapa con un input

        //profiler.reset();
    }

    public void render(final float alpha){
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();

        viewport.apply(false);
        if(mapRenderer.getMap()!=null){
            mapRenderer.setView(gameCamera);
            for(final TiledMapTileLayer layer : tiledMapLayers){
                mapRenderer.renderTileLayer(layer);
            }
        }
        for (final Entity entity : animatedEntities){
            renderEntity(entity,alpha);
        }
        spriteBatch.end();


        if(profiler.isEnabled()){
            Gdx.app.debug("RenderInfo","Bindings: "+profiler.getTextureBindings());
            Gdx.app.debug("RenderInfo","Drawcalls: "+profiler.getDrawCalls());
            profiler.reset();
        }
        box2DDebugRenderer.render(world, viewport.getCamera().combined);


    }

    private void renderEntity(Entity entity, float alpha) {
        final B2DComponent b2DComponent = ECSEngine.b2dCmpMapper.get(entity);

        b2DComponent.renderPosition.lerp(b2DComponent.body.getPosition(), alpha);
        dummySprite.setBounds(b2DComponent.renderPosition.x,b2DComponent.renderPosition.y, b2DComponent.width, b2DComponent.height);
        dummySprite.draw(spriteBatch);
    }

    @Override
    public void dispose() {
        if(box2DDebugRenderer!=null){
            box2DDebugRenderer.dispose();
        }
    }

    @Override
    public void mapChange(Map map) {
        mapRenderer.setMap(map.getTiledMap());
        map.getTiledMap().getLayers().getByType(TiledMapTileLayer.class, tiledMapLayers);
        if(dummySprite==null){
           dummySprite = assetManager.get("character/character_effects", TextureAtlas.class).createSprite("play");
           dummySprite.setOriginCenter();
        }
    }
}
