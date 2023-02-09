package com.albertogomez.ewend;

import box2dLight.Light;
import box2dLight.RayHandler;
import com.albertogomez.ewend.audio.AudioManager;
import com.albertogomez.ewend.audio.AudioType;
import com.albertogomez.ewend.ecs.ECSEngine;
import com.albertogomez.ewend.input.InputManager;
import com.albertogomez.ewend.map.MapManager;
import com.albertogomez.ewend.screen.AbstractScreen;
import com.albertogomez.ewend.screen.GameScreen;
import com.albertogomez.ewend.screen.LoadingScreen;
import com.albertogomez.ewend.screen.ScreenType;
import com.albertogomez.ewend.view.GameRenderer;
import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import java.nio.file.DirectoryStream;
import java.util.EnumMap;

import com.badlogic.gdx.utils.viewport.FitViewport;
import jdk.jfr.internal.LogLevel;

import static com.albertogomez.ewend.constants.Constants.*;

/**
 * The Game Principal Launcher
 * @author Alberto Gómez
 * @version 1.0.0
 */
public class EwendLauncher extends Game {
	/**
	 *Class Name
	 */
	private  static final String TAG = EwendLauncher.class.getSimpleName();

	public static final FixtureDef FIXTURE_DEF = new FixtureDef();
	public static final BodyDef BODY_DEF = new BodyDef();

	/**
	 * Contains the mapping of the screens and its type
	 */
	private EnumMap<ScreenType, Screen> screenCache;
	/**
	 * Viewport for all the game
	 */

	private World world;
	private WorldContactListener wcLstnr;
	private Box2DDebugRenderer box2DDebugRenderer;
	private ECSEngine ecsEngine;

	private OrthographicCamera gameCamera;
	private FitViewport viewport;
	private SpriteBatch spriteBatch;
	private float accumulator;


	private MapManager mapManager;
	private AssetManager assetManager;
	private I18NBundle i18NBundle;
	private Stage stage;
	private Skin skin;
	private InputManager inputManager;
	private AudioManager audioManager;
	private RayHandler rayHandler;
	private GameRenderer gameRenderer;

	private PreferenceManager preferenceManager;


	private static float HEIGHT;
	private static float WIDTH;

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);




		spriteBatch = new SpriteBatch();
		//BOX2D
		Box2D.init();
		accumulator = 0;
		world = new World(new Vector2(0,-9.81f),true);
		wcLstnr = new WorldContactListener();
		world.setContactListener(wcLstnr);
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0,0,0,0.2f);
		Light.setGlobalContactFilter(BIT_PLAYER, (short)1, BIT_GROUND);
		////

		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();

		//Initialize AssetManager
		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(assetManager.getFileHandleResolver()));

		initializeSkin();
		stage = new Stage(new FitViewport(WIDTH,HEIGHT),spriteBatch);
		////////


		//audio
		audioManager = new AudioManager(this);
		////////

		//input
		inputManager = new InputManager();
		Gdx.input.setInputProcessor(new InputMultiplexer(inputManager,stage));
		/////



		//Setup game viewport
		gameCamera = new OrthographicCamera();
		viewport = new FitViewport(WIDTH*UNIT_SCALE,HEIGHT*UNIT_SCALE,gameCamera);
		//

		//Ashley
		ecsEngine = new ECSEngine(this);
		//

		//MapManager
		mapManager = new MapManager(this);
		/////

		//Game Renderer
		gameRenderer = new GameRenderer(this);

		//preference manager
		preferenceManager = new PreferenceManager();

		//Set first screen
		screenCache = new EnumMap<ScreenType, Screen>(ScreenType.class);
		this.setScreen(ScreenType.LOADING);
		//
	}

	/**
	 * Sets the screen of the Game
	 * @Process If the screen type to be set doesn't exist, it creates one and saves it in cache
	 * @param screenType Wich screen to add and set
	 */
	public void setScreen(final ScreenType screenType){
		final Screen screen = screenCache.get(screenType);
		if(screen == null){
			try{
				Gdx.app.debug(TAG,"New screen: "+ screenType);
				final Screen newScreen = (Screen) ClassReflection.getConstructor(screenType.getScreenClass(),EwendLauncher.class).newInstance(this);
				screenCache.put(screenType, newScreen);

				setScreen(newScreen);
			}catch (ReflectionException e){
				throw new GdxRuntimeException("Screen: "+screenType +" could not be created",e);
			}
		}else {
			Gdx.app.debug(TAG,"Switching to screen: "+ screenType);
			setScreen(screen);
		}
	}

	@Override
	public void render() {
		super.render();
		final float deltaTime = Math.min(0.25f,Gdx.graphics.getRawDeltaTime());

		ecsEngine.update(deltaTime);
		accumulator += deltaTime;


		while(accumulator >= FIXED_TIME_STEP){
			//TODO save the previous postion of body
			world.step(FIXED_TIME_STEP,12,2);
			accumulator -= FIXED_TIME_STEP;
		}

		//TODO calculate renderPosition from previous position and real body position
		gameRenderer.render(accumulator/FIXED_TIME_STEP);
		//final float alpha = accumulator/FIXED_TIME_STEP;
		stage.getViewport().apply();
		stage.act(deltaTime);
		stage.draw();
	}

	@Override
	public void dispose() {
		super.dispose();
		gameRenderer.dispose();
		box2DDebugRenderer.dispose();
		rayHandler.dispose();
		world.dispose();
		assetManager.dispose();
		stage.dispose();
	}

	public Box2DDebugRenderer getBox2DDebugRenderer() {
		return box2DDebugRenderer;
	}

	public World getWorld() {
		return world;
	}

	/**
	 * Returns the viewport
	 * @return Viewport object
	 */
	public FitViewport getScreenViewport() {
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

	private void initializeSkin(){
		//Setup markup colors
		Colors.put("Red", Color.RED);
		Colors.put("Azul", Color.BLUE);


		//ttf bitmap
		final ObjectMap<String,Object> resources = new ObjectMap<String,Object>();
		final FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/customfont.ttf"));
		final FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameter.minFilter=Texture.TextureFilter.Linear;
		fontParameter.magFilter=Texture.TextureFilter.Linear;
		final int[] sizeToChange = {16,20,26,32};
		for(int size : sizeToChange){
			fontParameter.size = size;
			final BitmapFont bitmapFont = fontGenerator.generateFont(fontParameter);
			bitmapFont.getData().markupEnabled = true;
		    resources.put("font_"+size, bitmapFont);
		}
		fontGenerator.dispose();
		//load skin

		final SkinLoader.SkinParameter skinParameter = new SkinLoader.SkinParameter("ui/hud.atlas",resources);
		assetManager.load("ui/hud.json", Skin.class,skinParameter);
		assetManager.load("ui/strings", I18NBundle.class);
		assetManager.finishLoading();
		skin = assetManager.get("ui/hud.json", Skin.class);

		i18NBundle = assetManager.get("ui/strings");


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

	public static void resetBodyAndFixtureDefinition(){


		BODY_DEF.position.set(0,0);
		BODY_DEF.fixedRotation=false;
		BODY_DEF.gravityScale = 1;
		BODY_DEF.type = BodyDef.BodyType.StaticBody;
		FIXTURE_DEF.density=0;
		FIXTURE_DEF.isSensor=false;
		FIXTURE_DEF.restitution=0;
		FIXTURE_DEF.friction=0.2f;

		FIXTURE_DEF.filter.categoryBits = 0x0001;
		FIXTURE_DEF.filter.maskBits = -1;
		FIXTURE_DEF.shape = null;
	}
}
