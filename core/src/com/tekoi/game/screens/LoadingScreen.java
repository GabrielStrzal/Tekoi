package com.tekoi.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tekoi.game.TekoiGame;
import com.tekoi.game.config.GameConfig;
import com.tekoi.game.constants.GameAssets;
import com.tekoi.game.constants.ImagesPaths;
import com.tekoi.game.constants.LevelNames;
import com.tekoi.game.utils.GdxUtils;

/**
 * based on "LibGDX Game Development By Example"
 */

public class LoadingScreen extends ScreenAdapter{

    private static final float PROGRESS_BAR_WIDTH = 400;
    private static final float PROGRESS_BAR_HEIGHT = 100;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private float progress = 0;
    private TekoiGame game;
    protected final AssetManager assetManager;

    public LoadingScreen(TekoiGame game) {
        this.game = game;
        this.assetManager = game.getAssetManager();
    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(GameConfig.SCREEN_WIDTH /2, GameConfig.SCREEN_HEIGHT, 0);
        camera.update();
        viewport = new FitViewport(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();

        //Menu
        assetManager.load(ImagesPaths.MENU_BACKGROUND, Texture.class);
        assetManager.load(ImagesPaths.MENU_PLAY_BUTTON, Texture.class);
        assetManager.load(ImagesPaths.MENU_PLAY_BUTTON_PRESSED, Texture.class);
        assetManager.load(GameAssets.GAME_FONT, BitmapFont.class);

        //Character
        assetManager.load(ImagesPaths.CHAR_ATLAS, Texture.class);
        assetManager.load(ImagesPaths.CHAR_ATTACK, Texture.class);

        //Levels
        assetManager.load(LevelNames.LEVEL_1, TiledMap.class);

    }
    @Override
    public void render(float delta) {
        update();
        GdxUtils.clearScreen();
        draw();
    }
    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
    private void update() {
        if (assetManager.update()) {
            game.setScreen(new TestScreen(game));
        } else {
            progress = assetManager.getProgress();
        }
    }
    private void draw() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(
                (GameConfig.SCREEN_WIDTH - PROGRESS_BAR_WIDTH) / 2,
                (GameConfig.SCREEN_HEIGHT - PROGRESS_BAR_HEIGHT / 2),
                progress * PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
        shapeRenderer.end();

    }
}
