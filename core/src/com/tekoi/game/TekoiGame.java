package com.tekoi.game;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.strzal.gdxUtilLib.BasicGame;
import com.strzal.gdxUtilLib.screenManager.ScreenManager;
import com.tekoi.game.audio.AudioHandler;
import com.tekoi.game.config.GameConfig;
import com.tekoi.game.loading.LoadingPathsImpl;
import com.tekoi.game.screenManager.ScreenEnum;
import com.tekoi.game.screens.MenuScreen;

public class TekoiGame extends BasicGame {

	private AudioHandler audioHandler;

	@Override
	public void create () {
		screenWidth = GameConfig.SCREEN_WIDTH;
		screenHeight = GameConfig.SCREEN_HEIGHT;

		batch = new SpriteBatch();
		loadingPaths = new LoadingPathsImpl();
		audioHandler = new AudioHandler(this);

		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		ScreenManager.getInstance().initialize(this);
		ScreenManager.getInstance()
				.showScreen(ScreenEnum.LOADING_SCREEN, this, loadingPaths, new MenuScreen(this));
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

	public AudioHandler getAudioHandler() {
		return audioHandler;
	}
}
