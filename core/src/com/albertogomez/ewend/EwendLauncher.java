package com.albertogomez.ewend;

import com.albertogomez.ewend.screen.LoadingScreen;
import com.albertogomez.ewend.screen.ScreenType;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import java.util.EnumMap;

import jdk.jfr.internal.LogLevel;

public class EwendLauncher extends Game {
	private  static final String TAG = EwendLauncher.class.getSimpleName();

	//Mapa de tipo de escenas
	private EnumMap<ScreenType, Screen> screenCache;

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		screenCache = new EnumMap<ScreenType, Screen>(ScreenType.class);
		setScreen(ScreenType.LOADING);

	}

	/**
	 * Sets the screen of the Game
	 * @param screenType Wich screen to put
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


}
