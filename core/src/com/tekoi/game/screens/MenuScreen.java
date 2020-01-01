package com.tekoi.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.strzal.gdxUtilLib.screenManager.ScreenManager;
import com.strzal.gdxUtilLib.utils.GdxUtils;
import com.tekoi.game.TekoiGame;
import com.tekoi.game.config.GameConfig;
import com.tekoi.game.constants.GameAssets;
import com.tekoi.game.constants.ImagesPaths;
import com.tekoi.game.screenManager.ScreenEnum;

@Deprecated
public class MenuScreen extends ScreenAdapter {

    private static final int PLAY_BUTTON_Y = (int)GameConfig.SCREEN_HEIGHT /8;

    private Texture backgroundTexture;
    private Texture playTexture;
    private Texture playPressTexture;
    private BitmapFont font;

    private Stage stage;
    private final TekoiGame game;
    private final AssetManager assetManager;


    public MenuScreen(TekoiGame game) {
        this.game = game;
        this.assetManager = game.getAssetManager();
    }

    public void show() {
        stage = new Stage(new FitViewport(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        backgroundTexture = assetManager.get(ImagesPaths.MENU_BACKGROUND);
        Image background = new Image(backgroundTexture);
        stage.addActor(background);
        font = new BitmapFont(Gdx.files.internal(GameAssets.GAME_FONT),false);
        font.setColor(Color.DARK_GRAY);
        font.getData().setScale(.6f);


        //Play Button
        playTexture = assetManager.get(ImagesPaths.MENU_PLAY_BUTTON);
        playPressTexture = assetManager.get(ImagesPaths.MENU_PLAY_BUTTON_PRESSED);
        ImageButton play = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(playTexture)),
                new TextureRegionDrawable(new TextureRegion(playPressTexture)));
        play.setPosition(GameConfig.SCREEN_WIDTH /2 - playTexture.getWidth()/2, PLAY_BUTTON_Y);

        play.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count,
                            int button) {
                super.tap(event, x, y, count, button);
                    ScreenManager.getInstance().showScreen(ScreenEnum.GAME_SCREEN, game, 1);
            }
        });
        stage.addActor(play);

    }
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    public void render(float delta) {
        GdxUtils.clearScreen();
        stage.act(delta);
        stage.draw();
        stage.getBatch().begin();
        font.draw(stage.getBatch(), GameConfig.GAME_VERSION,
                (GameConfig.SCREEN_WIDTH * .87f), (GameConfig.SCREEN_HEIGHT * .95f));
        stage.getBatch().end();

    }

    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
    }

}
