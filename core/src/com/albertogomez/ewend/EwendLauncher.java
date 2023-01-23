package com.albertogomez.ewend;

import com.albertogomez.ewend.screen.AbstractScreen;
import com.albertogomez.ewend.screen.LoadingScreen;
import com.albertogomez.ewend.screen.ScreenType;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import java.nio.file.DirectoryStream;
import java.util.EnumMap;

import com.badlogic.gdx.utils.viewport.FitViewport;
import jdk.jfr.internal.LogLevel;

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

	/**
	 * Contains the mapping of the screens and its type
	 */
	private EnumMap<ScreenType, Screen> screenCache;
	/**
	 * Viewport for all the game
	 */
	private FitViewport viewport;


	public static final short BIT_CIRCLE = 1 <<0;
	public static final short BIT_BOX = 1 <<1;
	public static final short BIT_GROUND = 1 <<2;
	public static final short BIT_PLAYER = 1 <<3;

	private Box2DDebugRenderer box2DDebugRenderer;
	private World world;

	private static final float FIXED_TIME_STEP = 1/60f;
	private float accumulator;

	private WorldContactListener wcLstnr;

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Box2D.init();
		accumulator = 0;
		world = new World(new Vector2(0,-9.81f),true);
		wcLstnr = new WorldContactListener();
		world.setContactListener(wcLstnr);

		box2DDebugRenderer = new Box2DDebugRenderer();
		viewport = new FitViewport(9,16);

		screenCache = new EnumMap<ScreenType, Screen>(ScreenType.class);

		this.setScreen(ScreenType.GAME);
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

	//	Gdx.app.debug(TAG,""+ Gdx.graphics.getRawDeltaTime());
		accumulator += Math.min(0.25f,Gdx.graphics.getRawDeltaTime());
		while(accumulator >= FIXED_TIME_STEP){
			world.step(FIXED_TIME_STEP,6,2);
			accumulator -= FIXED_TIME_STEP;
		}



		//final float alpha = accumulator/FIXED_TIME_STEP;

	}

	@Override
	public void dispose() {
		super.dispose();
		box2DDebugRenderer.dispose();;
		world.dispose();
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
}
