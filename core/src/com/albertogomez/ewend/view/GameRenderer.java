package com.albertogomez.ewend.view;

import box2dLight.RayHandler;
import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.AnimationComponent;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.GameObjectComponent;
import com.albertogomez.ewend.map.Map;
import com.albertogomez.ewend.map.MapListener;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.profiling.GLProfiler;

import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.EnumMap;

import static com.albertogomez.ewend.constants.Constants.UNIT_SCALE;

public class GameRenderer implements Disposable, MapListener {

    private final AssetManager assetManager;
    private final FitViewport viewport;
    private final OrthographicCamera gameCamera;
    private final SpriteBatch spriteBatch;
    private final EnumMap<AnimationType, Animation<Sprite>> animationCache;

    private final OrthogonalTiledMapRenderer mapRenderer;
    private final GLProfiler profiler;
    private final World world;
    private final Box2DDebugRenderer box2DDebugRenderer;
    private final ImmutableArray<Entity> animatedEntities;
    private final ImmutableArray<Entity> gameObjectEntities;

    private final Array<TiledMapTileLayer> tiledMapLayers;
    private IntMap<Animation<Sprite>> mapAnimations;
    private final RayHandler rayHandler;

    public GameRenderer(final EwendLauncher context) {
        assetManager = context.getAssetManager();
        viewport = context.getScreenViewport();
        gameCamera = context.getGameCamera();
        spriteBatch = context.getSpriteBatch();

        animationCache = new EnumMap<AnimationType, Animation<Sprite>>(AnimationType.class);

        gameObjectEntities = context.getEcsEngine().getEntitiesFor(Family.all(GameObjectComponent.class,B2DComponent.class,AnimationComponent.class).get());
        animatedEntities = context.getEcsEngine().getEntitiesFor(Family.all(AnimationComponent.class, B2DComponent.class).get());
        mapAnimations = new IntMap<Animation<Sprite>>();

        mapRenderer = new OrthogonalTiledMapRenderer(null, UNIT_SCALE, context.getSpriteBatch());
        context.getMapManager().addMapListener(this);
        tiledMapLayers = new Array<TiledMapTileLayer>();

        profiler = new GLProfiler(Gdx.graphics);
        box2DDebugRenderer = new Box2DDebugRenderer();
        world = context.getWorld();



        if (profiler.isEnabled()) {
        } else {
            //box2DDebugRenderer = null;
            //world = null;
        }
        rayHandler = context.getRayHandler();




        //profiler.reset();
    }

    public void render(final float alpha) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        viewport.apply(false);
        spriteBatch.begin();

        if (mapRenderer.getMap() != null) {
            AnimatedTiledMapTile.updateAnimationBaseTime();
            mapRenderer.setView(gameCamera);
            for (final TiledMapTileLayer layer : tiledMapLayers) {
                mapRenderer.renderTileLayer(layer);
            }
        }
        for (final Entity entity : gameObjectEntities) {
            renderGameObject(entity, alpha);
        }
        for (final Entity entity : animatedEntities) {
            renderEntity(entity, alpha);
        }

        spriteBatch.end();
        rayHandler.setCombinedMatrix(gameCamera);
        rayHandler.updateAndRender();

        if (profiler.isEnabled()) {
            Gdx.app.debug("RenderInfo", "Bindings: " + profiler.getTextureBindings());
            Gdx.app.debug("RenderInfo", "Drawcalls: " + profiler.getDrawCalls());
            profiler.reset();
        }
        box2DDebugRenderer.render(world, viewport.getCamera().combined);


    }

    private void renderEntity(Entity entity, float alpha) {
        final B2DComponent b2DComponent = ECSEngine.b2dCmpMapper.get(entity);
        final AnimationComponent aniComponent = ECSEngine.aniCmpMapper.get(entity);

        if (aniComponent.aniType != null) {
            final Animation<Sprite> animation = getAnimation(aniComponent.aniType);
            final Sprite frame = animation.getKeyFrame(aniComponent.aniTime);
            b2DComponent.renderPosition.lerp(b2DComponent.body.getPosition(), alpha);
            frame.setBounds(b2DComponent.renderPosition.x - b2DComponent.width * b2DComponent.orientation, b2DComponent.renderPosition.y - b2DComponent.height, aniComponent.width * b2DComponent.orientation, aniComponent.height);
            frame.draw(spriteBatch);
        }
        gameCamera.position.set(ECSEngine.b2dCmpMapper.get(entity).renderPosition, 0);
        gameCamera.update();
    }

    private void renderGameObject(final Entity entity,final float alpha){
        final B2DComponent b2DComponent = ECSEngine.b2dCmpMapper.get(entity);
        final AnimationComponent aniComponent = ECSEngine.aniCmpMapper.get(entity);
        final GameObjectComponent gameObjectComponent = ECSEngine.gameObjCmpMapper.get(entity);

        if (gameObjectComponent.animationIndex != -1) {
            final Animation<Sprite> animation = mapAnimations.get(gameObjectComponent.animationIndex);

            final Sprite frame = animation.getKeyFrame(aniComponent.aniTime);
            frame.setBounds(b2DComponent.renderPosition.x, b2DComponent.renderPosition.y , aniComponent.width , aniComponent.height);
            frame.setOriginCenter();
            frame.setRotation(b2DComponent.body.getAngle()* MathUtils.radDeg);
            frame.draw(spriteBatch);
        }
    }

    private Animation<Sprite> getAnimation(final AnimationType aniType) {
        Animation<Sprite> animation = animationCache.get(aniType);
        if (animation == null) {
            //create animation
            Gdx.app.debug("GameRenderer", "Creating new animation of type " + aniType);
            final Array<TextureAtlas.AtlasRegion> atlasRegion = assetManager.get(aniType.getAtlasPath(), TextureAtlas.class).findRegions(aniType.getAtlasKey());
            animation = new Animation<Sprite>(aniType.getFrameTime(), getKeyFrame(atlasRegion), Animation.PlayMode.LOOP);
            animationCache.put(aniType, animation);
        }
        return animation;
    }


    private Array<? extends Sprite> getKeyFrame(Array<TextureAtlas.AtlasRegion> atlasRegion) {
        final Array<Sprite> keyFrames = new Array<Sprite>();
        TextureRegion[][] textureRegion = atlasRegion.get(0).split(64, 64);

        for (int i = 0; i < atlasRegion.size; i++) {
            if (atlasRegion.get(i).getTexture() != null) {
                textureRegion = atlasRegion.get(i).split(64, 64);
            }
            for (int j = 0; j < textureRegion.length; j++) {
                for (int k = 0; k < textureRegion[j].length; k++) {
                    final Sprite sprite = new Sprite(textureRegion[j][k]);
                    sprite.setOriginCenter();
                    keyFrames.add(sprite);
                }

            }
        }
        return keyFrames;
    }

    @Override
    public void dispose() {
        if (box2DDebugRenderer != null) {
            box2DDebugRenderer.dispose();
        }
    }

    @Override
    public void mapChange(Map map) {
        mapRenderer.setMap(map.getTiledMap());
        map.getTiledMap().getLayers().getByType(TiledMapTileLayer.class, tiledMapLayers);
        mapAnimations = map.getMapAnimations();
    }

       /* private Array<? extends Sprite> getKeyFrames(final TextureRegion[] textureRegion){
        final Array<Sprite> keyFrames = new Array<Sprite>();

        for(final TextureRegion region : textureRegion){
            final Sprite sprite = new Sprite(region);
            sprite.setOriginCenter();
            keyFrames.add(sprite);
        }
        return keyFrames;
    }
*/
}
