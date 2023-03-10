package com.albertogomez.ewend;

import box2dLight.Light;
import box2dLight.RayHandler;
import com.albertogomez.ewend.audio.AudioManager;
import com.albertogomez.ewend.constants.Configs;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.input.InputManager;
import com.albertogomez.ewend.map.MapManager;
import com.albertogomez.ewend.screen.ScreenType;
import com.albertogomez.ewend.view.GameRenderer;
import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.EnumMap;

import static com.albertogomez.ewend.constants.Constants.*;

/**
 * The Game Principal Launcher
 * @author Alberto Gómez
 */
public class EwendLauncher extends Game {
    /**
     * Class Name
     */
    private static final String TAG = EwendLauncher.class.getSimpleName();
    /**
     * Fixture def assistant variable
     */
    public static final FixtureDef FIXTURE_DEF = new FixtureDef();
    /**
     * Body def assistant variable
     */
    public static final BodyDef BODY_DEF = new BodyDef();

    /**
     * Contains the mapping of the screens and its type
     */
    private EnumMap<ScreenType, Screen> screenCache;

    /**
     * Game World
     */
    private World world;
    /**
     * Collsion map handler
     */
    private WorldContactListener wcLstnr;
    /**
     * Entity Component System Engine
     */
    public ECSEngine ecsEngine;
    /**
     * Game Camera
     */
    private OrthographicCamera gameCamera;
    /**
     * Game Viewport
     */
    private ExtendViewport viewport;
    /**
     * Main spritebatch
     */
    private SpriteBatch spriteBatch;
    /**
     * Time accumulator
     */
    private float accumulator;
    /**
     * Main map manager
     */
    public MapManager mapManager;
    /**
     * Main assets manager
     */
    private AssetManager assetManager;
    /**
     * String bundle formatter
     */
    private I18NBundle i18NBundle;
    /**
     * Game stage for ui
     */
    private Stage stage;
    /**
     * Stage skin
     */
    private Skin skin;
    /**
     * Game buttons inputs handler
     */
    private InputManager inputManager;
    /**
     * Audio manager for playing sounds and musics
     */
    private AudioManager audioManager;
    /**
     * All game lights handler
     */
    private RayHandler rayHandler;
    /**
     * Render for the levels
     */
    public GameRenderer gameRenderer;
    /**
     * Storaged info manager
     */
    private PreferenceManager preferenceManager;
    /**
     * Screen HEIGHT
     */
    public static float SCREEN_HEIGHT;
    /**
     * Screen width
     */
    public static float SCREEN_WIDTH;

    /**
     * Creates the central game class
     */
    private Configs config;
    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);


        spriteBatch = new SpriteBatch();
        //BOX2D
        Box2D.init();
        accumulator = 0;
        world = new World(new Vector2(0, -9.81f * 1.4f), true);

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0, 0, 0, 0.5f);
        Light.setGlobalContactFilter(BIT_PLAYER, (short) 1, BIT_GROUND);
        ////

        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();


        //Initialize AssetManager
        assetManager = new AssetManager();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(assetManager.getFileHandleResolver()));

        initializeSkin();
        stage = new Stage(new ExtendViewport(SCREEN_WIDTH, SCREEN_HEIGHT), spriteBatch);
        ////////
        //audio
        audioManager = new AudioManager(assetManager);
        ////////

        //WorldContactListener
            wcLstnr = new WorldContactListener(this);
            world.setContactListener(wcLstnr);
        //

        //Setup game viewport
        gameCamera = new OrthographicCamera();
        viewport = new ExtendViewport(SCREEN_WIDTH * UNIT_SCALE*0.15f, SCREEN_HEIGHT * UNIT_SCALE*0.15f, gameCamera);
        //


        //input
        inputManager = new InputManager();
        Gdx.input.setInputProcessor(new InputMultiplexer(inputManager, stage));
        /////

        //preference manager
        preferenceManager = new PreferenceManager();
        //



        config = preferenceManager.loadConfig();

        screenCache = new EnumMap<ScreenType, Screen>(ScreenType.class);
        this.setScreen(ScreenType.MENU);
        //
    }


    /**
     * Tell the world to update, the stage to draw, the ECS to update and the gameRenderer to render
     */
    @Override
    public void render() {
        super.render();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        final float deltaTime = Math.min(0.25f, Gdx.graphics.getRawDeltaTime());

            accumulator += deltaTime;
            while (accumulator >= FIXED_TIME_STEP) {
                //TODO save the previous postion of body
                world.step(FIXED_TIME_STEP, 8, 8);
                accumulator -= FIXED_TIME_STEP;
            }
            if(ecsEngine!=null){
                ecsEngine.update(deltaTime);
            }
            if(gameRenderer!=null){
                gameRenderer.render(accumulator / FIXED_TIME_STEP);
            }
            stage.act(deltaTime);
            stage.draw();

        //TODO calculate renderPosition from previous position and real body position
    }

    /**
     * Sets a new screen
     * @param screenType Screen to be displayed
     */
    public void setScreen(final ScreenType screenType) {
        final Screen screen = screenCache.get(screenType);
        if (screen == null) {
            try {
                Gdx.app.debug(TAG, "New screen: " + screenType);
                final Screen newScreen = (Screen) ClassReflection.getConstructor(screenType.getScreenClass(), EwendLauncher.class).newInstance(this);
                screenCache.put(screenType, newScreen);

                setScreen(newScreen);
            } catch (ReflectionException e) {
                throw new GdxRuntimeException("Screen: " + screenType + " could not be created", e);
            }
        } else {
            Gdx.app.debug(TAG, "Switching to screen: " + screenType);
            setScreen(screen);
        }
    }

    /**
     * Initialices all the assets for the ui
     */
    private void initializeSkin() {
        //Setup markup colors
        Colors.put("Red", Color.RED);
        Colors.put("Azul", Color.BLUE);

        //ttf bitmap
        final ObjectMap<String, Object> resources = new ObjectMap<String, Object>();
        final FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/silkscreen_regular.ttf"));
        final FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.minFilter = Texture.TextureFilter.Linear;
        fontParameter.magFilter = Texture.TextureFilter.Linear;
        final int[] sizeToChange = {16, 20, 26, 32,48,64};
        for (int size : sizeToChange) {
            fontParameter.size = size;
            final BitmapFont bitmapFont = fontGenerator.generateFont(fontParameter);
            bitmapFont.getData().markupEnabled = true;
            resources.put("font_" + size, bitmapFont);
        }
        fontGenerator.dispose();
        //load skin

        final SkinLoader.SkinParameter skinParameter = new SkinLoader.SkinParameter("ui/game_hud.atlas", resources);
        assetManager.load("ui/game_hud.json", Skin.class, skinParameter);
        assetManager.load("ui/strings", I18NBundle.class);
        assetManager.load("ui/background.jpg", Texture.class);
        assetManager.load("ui/game_hud.atlas", TextureAtlas.class);

        assetManager.finishLoading();
        skin = assetManager.get("ui/game_hud.json", Skin.class);

        i18NBundle = assetManager.get("ui/strings");
    }


    /**
     * Disposes all game
     */
    @Override
    public void dispose() {
        super.dispose();
        gameRenderer.dispose();
        rayHandler.dispose();
        world.dispose();
        assetManager.dispose();
        stage.dispose();
    }


    public World getWorld() {
        return world;
    }
    public ExtendViewport getScreenViewport() {
        return viewport;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public OrthographicCamera getGameCamera() {
        return gameCamera;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public WorldContactListener getWcLstnr() {
        return wcLstnr;
    }

    public ECSEngine getEcsEngine() {
        return ecsEngine;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public Stage getStage() {
        return stage;
    }

    public Skin getSkin() {
        return skin;
    }

    public I18NBundle getI18NBundle() {
        return i18NBundle;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public RayHandler getRayHandler() {
        return rayHandler;
    }

    public PreferenceManager getPreferenceManager() {
        return preferenceManager;
    }

    public void setEcsEngine(ECSEngine ecsEngine) {
        this.ecsEngine = ecsEngine;
    }

    public void setMapManager(MapManager mapManager) {
        this.mapManager = mapManager;
    }

    public void setGameRenderer(GameRenderer gameRenderer) {
        this.gameRenderer = gameRenderer;
    }

    public Configs getConfig() {
        return config;
    }

    public void setConfig(Configs config) {
        this.config = config;
    }

    public static void resetBodyAndFixtureDefinition() {


        BODY_DEF.position.set(0, 0);
        BODY_DEF.fixedRotation = false;
        BODY_DEF.gravityScale = 1;
        BODY_DEF.type = BodyDef.BodyType.StaticBody;
        FIXTURE_DEF.density = 0;
        FIXTURE_DEF.isSensor = false;
        FIXTURE_DEF.restitution = 0;
        FIXTURE_DEF.friction = 0.2f;

        FIXTURE_DEF.filter.categoryBits = 0x0001;
        FIXTURE_DEF.filter.maskBits = -1;
        FIXTURE_DEF.shape = null;
    }
}
