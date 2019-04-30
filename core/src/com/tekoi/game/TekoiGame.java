package com.tekoi.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.tekoi.game.screenManager.ScreenEnum;
import com.tekoi.game.screenManager.ScreenManager;

public class TekoiGame extends Game {

	public static final float PPM = 100;
	public SpriteBatch batch;
	private final AssetManager assetManager = new AssetManager();

	public boolean directionRight = true;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		ScreenManager.getInstance().initialize(this);
		ScreenManager.getInstance().showScreen(ScreenEnum.LOADING_SCREEN, this);

	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}
}
