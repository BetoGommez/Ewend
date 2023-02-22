package com.albertogomez.ewend.view;

import box2dLight.RayHandler;
import com.albertogomez.ewend.EwendLauncher;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.ecs.components.AnimationComponent;
import com.albertogomez.ewend.ecs.components.B2DComponent;
import com.albertogomez.ewend.ecs.components.GameObjectComponent;
import com.albertogomez.ewend.ecs.components.LifeComponent;
import com.albertogomez.ewend.map.Map;
import com.albertogomez.ewend.map.MapListener;
import com.albertogomez.ewend.screen.GameScreen;
import com.albertogomez.ewend.utils.CameraStyles;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.profiling.GLProfiler;

import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.EnumMap;

import static com.albertogomez.ewend.constants.Constants.BACKGROUND_PATH;
import static com.albertogomez.ewend.constants.Constants.UNIT_SCALE;
import static com.albertogomez.ewend.map.Map.MAP_HEIGHT;
import static com.albertogomez.ewend.map.Map.MAP_WIDTH;

public class GameRenderer implements Disposable, MapListener {

    private final AssetManager assetManager;
    private final ExtendViewport viewport;
    private final OrthographicCamera gameCamera;
    private final SpriteBatch spriteBatch;
    private final EnumMap<AnimationType, Animation<Sprite>> animationCache;

    private final OrthogonalTiledMapRenderer mapRenderer;
    private final GLProfiler profiler;
    private final World world;

    private B2DComponent playerB2dComp;
    private final Box2DDebugRenderer box2DDebugRenderer;
    private final ImmutableArray<Entity> animatedEntities;
    private final ImmutableArray<Entity> gameObjectEntities;

    private final Array<TiledMapTileLayer> tiledMapLayers;
    private IntMap<Animation<Sprite>> mapAnimations;

    private Array<Texture> backgroundImages;
    private int[] backgroundOffsets = {0,0,0,0};
    private final RayHandler rayHandler;
    private final Array<TiledMapTileLayer> overlappingLayers;

    public GameRenderer(final EwendLauncher context) {
        assetManager = context.getAssetManager();
        viewport = context.getScreenViewport();
        gameCamera = context.getGameCamera();
        gameCamera.zoom=0.5f;

        spriteBatch = context.getSpriteBatch();

        animationCache = new EnumMap<AnimationType, Animation<Sprite>>(AnimationType.class);

        gameObjectEntities = context.getEcsEngine().getEntitiesFor(Family.all(GameObjectComponent.class,B2DComponent.class,AnimationComponent.class).get());
        animatedEntities = context.getEcsEngine().getEntitiesFor(Family.all(AnimationComponent.class, B2DComponent.class, LifeComponent.class).get());
        mapAnimations = new IntMap<Animation<Sprite>>();

        mapRenderer = new OrthogonalTiledMapRenderer(null, UNIT_SCALE, context.getSpriteBatch());

        tiledMapLayers = new Array<TiledMapTileLayer>();
        overlappingLayers = new Array<TiledMapTileLayer>();

        world = context.getWorld();
        rayHandler = context.getRayHandler();

        profiler = new GLProfiler(Gdx.graphics);
        box2DDebugRenderer = new Box2DDebugRenderer();

        if (profiler.isEnabled()) {
        } else {
            //box2DDebugRenderer = null;
            //world = null;
        }
        context.getMapManager().addMapListener(this);
        //profiler.reset();
    }

    public void render(final float alpha) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply(false);
        spriteBatch.begin();

        if (mapRenderer.getMap() != null) {
            renderBackground();
            AnimatedTiledMapTile.updateAnimationBaseTime();
            mapRenderer.setView(gameCamera);
            for (final TiledMapTileLayer layer : tiledMapLayers) {
                if(layer.getName().contains("Overlapping")||layer.getName().contains("Platform")){
                    overlappingLayers.add(layer);
                }else{
                    mapRenderer.renderTileLayer(layer);
                }
            }
        }

        AnimatedTiledMapTile.updateAnimationBaseTime();
        for (final Entity entity : animatedEntities) {
            renderEntity(entity, alpha);
        }
        for (final Entity entity : gameObjectEntities) {
            renderGameObject(entity, alpha);
        }
        spriteBatch.end();

        rayHandler.updateAndRender();
        rayHandler.setCombinedMatrix(gameCamera);

        spriteBatch.begin();
        if(mapRenderer.getMap()!=null){
            for (TiledMapTileLayer overlapping : overlappingLayers){
                mapRenderer.renderTileLayer(overlapping);
            }
            overlappingLayers.clear();
        }
        spriteBatch.end();


        //camera center
        float startX = gameCamera.viewportWidth/4;
        float startY = gameCamera.viewportHeight/4;
        if(playerB2dComp!=null){
            CameraStyles.lerpToTarget(gameCamera,playerB2dComp.renderPosition);
            CameraStyles.boundary(gameCamera,startX,startY,MAP_WIDTH-startX*2,MAP_HEIGHT);
        }
        gameCamera.update();

        if (profiler.isEnabled()) {
            Gdx.app.debug("RenderInfo", "Bindings: " + profiler.getTextureBindings());
            Gdx.app.debug("RenderInfo", "Drawcalls: " + profiler.getDrawCalls());
            profiler.reset();
        }
     box2DDebugRenderer.render(world, viewport.getCamera().combined);


    }

    private void renderBackground(){

        if(playerB2dComp!=null){
            float conversor = backgroundImages.get(0).getWidth()/2;
            backgroundOffsets[0]= (int) ((playerB2dComp.renderPosition.x)%conversor);
            backgroundOffsets[1]= (int) ((playerB2dComp.renderPosition.x*3)%conversor);
            backgroundOffsets[2]= (int) ((playerB2dComp.renderPosition.x*4)%conversor);
            backgroundOffsets[3]= (int) ((playerB2dComp.renderPosition.x*10)%conversor);
        }

        for (int i = 0; i < backgroundOffsets.length; i++) {
            if(backgroundOffsets[i]>backgroundImages.get(i).getWidth()/2){
                backgroundOffsets[i]= -backgroundOffsets[i];
            }
           spriteBatch.draw(backgroundImages.get(i),-backgroundOffsets[i],0,backgroundImages.get(i).getWidth()/2,backgroundImages.get(i).getHeight());
           spriteBatch.draw(backgroundImages.get(i),-backgroundOffsets[i]+backgroundImages.get(i).getWidth()/2,0,backgroundImages.get(i).getWidth()/2,backgroundImages.get(i).getHeight());
           spriteBatch.draw(backgroundImages.get(i),-backgroundOffsets[i]+(backgroundImages.get(i).getWidth()/2)*2,0,backgroundImages.get(i).getWidth()/2,backgroundImages.get(i).getHeight());
        }

    }

    private void renderEntity(Entity entity, float alpha) {
        final B2DComponent b2DComponent = ECSEngine.b2dCmpMapper.get(entity);
        final AnimationComponent aniComponent = ECSEngine.aniCmpMapper.get(entity);

        if (aniComponent.aniType != null) {
            final Animation<Sprite> animation = getAnimation(aniComponent.aniType);
            final Sprite frame = animation.getKeyFrame(aniComponent.aniTime);
            b2DComponent.renderPosition.lerp(b2DComponent.body.getPosition(), alpha);
            if(ECSEngine.playerCmpMapper.get(entity)!=null)
            {
                    playerB2dComp = b2DComponent;
                frame.setBounds(b2DComponent.renderPosition.x - aniComponent.width/2 * b2DComponent.orientation, b2DComponent.renderPosition.y - aniComponent.height/2, aniComponent.width * b2DComponent.orientation, aniComponent.height);
            }else{
                frame.setBounds(b2DComponent.renderPosition.x - b2DComponent.width * -b2DComponent.orientation, b2DComponent.renderPosition.y - b2DComponent.height, aniComponent.width * -b2DComponent.orientation, aniComponent.height);
            }
            frame.draw(spriteBatch);
        }
    }

    private void renderGameObject(final Entity entity,final float alpha){
        final B2DComponent b2DComponent = ECSEngine.b2dCmpMapper.get(entity);
        final AnimationComponent aniComponent = ECSEngine.aniCmpMapper.get(entity);
        final GameObjectComponent gameObjectComponent = ECSEngine.gameObjCmpMapper.get(entity);
        Animation<Sprite> animation = null;
        if (gameObjectComponent.animationIndex != -1) {
            if(aniComponent.aniType!=null){
                animation = getAnimation(aniComponent.aniType);
            }else{
                animation = mapAnimations.get(gameObjectComponent.animationIndex);
            }
            final Sprite frame = animation.getKeyFrame(aniComponent.aniTime);
            frame.setBounds(b2DComponent.renderPosition.x, b2DComponent.renderPosition.y , aniComponent.width , aniComponent.height);

            frame.draw(spriteBatch);
        }
    }

    private Animation<Sprite> getAnimation(final AnimationType aniType) {
        Animation<Sprite> animation = animationCache.get(aniType);
        if (animation == null) {
            //create animation
            Animation.PlayMode animationMode = aniType.getPlayMode();
            Gdx.app.debug("GameRenderer", "Creating new animation of type " + aniType);
            final Array<TextureAtlas.AtlasRegion> atlasRegion = assetManager.get(aniType.getAtlasPath(), TextureAtlas.class).findRegions(aniType.getAtlasKey());
            animation = new Animation<Sprite>(aniType.getFrameTime(), getKeyFrame(atlasRegion,aniType.getWidth(), aniType.getHeight()), animationMode);
            animationCache.put(aniType, animation);
        }
        return animation;
    }


    private Array<? extends Sprite> getKeyFrame(Array<TextureAtlas.AtlasRegion> atlasRegion,float frameWidth,float frameHeight) {
        final Array<Sprite> keyFrames = new Array<Sprite>();
        TextureRegion[][] textureRegion = atlasRegion.get(0).split((int)frameWidth, (int)frameHeight);

        for (int i = 0; i < atlasRegion.size; i++) {
            if (atlasRegion.get(i).getTexture() != null) {
                textureRegion = atlasRegion.get(i).split((int)frameWidth, (int)frameHeight);
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
        backgroundImages =  map.getBackgroundImages();
        map.getTiledMap().getLayers().getByType(TiledMapTileLayer.class, tiledMapLayers);
        mapAnimations = map.getMapAnimations();
    }

}
