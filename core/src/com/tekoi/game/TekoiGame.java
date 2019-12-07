package com.tekoi.game;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.strzal.gdx.BasicGame;
import com.tekoi.game.screenManager.ScreenEnum;
import com.tekoi.game.screenManager.ScreenManager;

public class TekoiGame extends BasicGame {

	@Override
	public void create () {
		batch = new SpriteBatch();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		ScreenManager.getInstance().initialize(this);
		ScreenManager.getInstance().showScreen(ScreenEnum.LOADING_SCREEN, this);
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
