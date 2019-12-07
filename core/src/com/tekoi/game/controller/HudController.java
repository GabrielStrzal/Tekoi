package com.tekoi.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.strzal.gdx.screenManager.ScreenManager;
import com.tekoi.game.TekoiGame;
import com.tekoi.game.config.GameConfig;
import com.tekoi.game.constants.ImagesPaths;
import com.tekoi.game.screenManager.ScreenEnum;
import com.tekoi.game.screens.GameScreen;

public class HudController {
    Viewport viewport;
    Stage stage;
    Stage stageGameControls;
    Stage actionButtonScreenStage;


    OrthographicCamera camera;

    private Texture backButtonTexture;
    private Texture backButtonPressedTexture;
    private Texture levelRestartButtonTexture;
    private Texture levelRestartButtonPressedTexture;
    private AssetManager assetManager;


    private static final float BACK_BUTTON_X = GameConfig.SCREEN_WIDTH - 250;
    private static final float BACK_BUTTON_Y = GameConfig.SCREEN_HEIGHT - 140;
    private static final float BUTTON_SIZE = 180;
    private static final float BUTTON_PADDING = 220;

    private TekoiGame tekoiGame;
    GameScreen gameScreen;


    public HudController(final TekoiGame tekoiGame, GameScreen gameScreen) {
        this.tekoiGame = tekoiGame;
        this.gameScreen = gameScreen;
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
        stage = new Stage(viewport);
        stageGameControls = new Stage(viewport);
        actionButtonScreenStage = new Stage(viewport);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(stageGameControls);
        inputMultiplexer.addProcessor(actionButtonScreenStage);
        Gdx.input.setInputProcessor(inputMultiplexer);
        assetManager = tekoiGame.getAssetManager();


        //Back Button
        backButtonTexture = assetManager.get(ImagesPaths.MENU_RETURN_BUTTON);
        backButtonPressedTexture = assetManager.get(ImagesPaths.MENU_RETURN_BUTTON_PRESSED);
        ImageButton backBtn = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(backButtonTexture)),
                new TextureRegionDrawable(new TextureRegion(backButtonPressedTexture)));
        backBtn.setPosition(BACK_BUTTON_X, BACK_BUTTON_Y);
        backBtn.setSize(BUTTON_SIZE, BUTTON_SIZE);

        backBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count,
                                  int button) {
                super.tap(event, x, y, count, button);
                ScreenManager.getInstance().showScreen(ScreenEnum.MENU_SCREEN, tekoiGame);

            }
        });
        stage.addActor(backBtn);

        //Level Restart Button
        levelRestartButtonTexture = assetManager.get(ImagesPaths.MENU_RESTART_BUTTON);
        levelRestartButtonPressedTexture = assetManager.get(ImagesPaths.MENU_RESTART_BUTTON_PRESSED);

        ImageButton levelRestartBtn = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(levelRestartButtonTexture)),
                new TextureRegionDrawable(new TextureRegion(levelRestartButtonPressedTexture)));
        levelRestartBtn.setPosition(BACK_BUTTON_X - (BUTTON_PADDING * 1), BACK_BUTTON_Y);
        levelRestartBtn.setSize(BUTTON_SIZE, BUTTON_SIZE);
        levelRestartBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                ScreenManager.getInstance().showScreen(ScreenEnum.GAME_SCREEN, tekoiGame, tekoiGame.currentLevel);
            }
        });
        stage.addActor(levelRestartBtn);
    }


    public void resize(int width, int height) {
        stage.getViewport().update(width, height);

    }

    public void draw() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        //if (tekoiGame.getGameStatsHandler().isDisplayControllers()) {
        stageGameControls.draw();
        //}

    }
}

