package com.tekoi.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.strzal.gdx.BasicGame;
import com.strzal.gdx.screenManager.ScreenManager;
import com.tekoi.game.config.GameConfig;
import com.tekoi.game.loading.LoadingPathsImpl;
import com.tekoi.game.screenManager.ScreenEnum;
import com.tekoi.game.screens.MenuTestScreen;

public class TekoiGame extends BasicGame {

	@Override
	public void create () {
		screenWidth = GameConfig.SCREEN_WIDTH;
		screenHeight = GameConfig.SCREEN_HEIGHT;

		batch = new SpriteBatch();
		loadingPaths = new LoadingPathsImpl();

		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		ScreenManager.getInstance().initialize(this);
		ScreenManager.getInstance()
				.showScreen(ScreenEnum.LOADING_SCREEN, this, loadingPaths, new MenuTestScreen(this));
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
