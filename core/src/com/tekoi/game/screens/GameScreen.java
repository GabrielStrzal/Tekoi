package com.tekoi.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tekoi.game.TekoiGame;
import com.tekoi.game.config.GameConfig;
import com.tekoi.game.constants.ImagesPaths;
import com.tekoi.game.constants.LevelNames;
import com.tekoi.game.entity.Player;
import com.tekoi.game.utils.GdxUtils;
import com.tekoi.game.worldCreator.B2WorldCreator;
import com.tekoi.game.worldCreator.WorldContactListener;

/**
 * Created by lelo on 24/06/18.
 */

public class GameScreen implements Screen {

    private TekoiGame game;

    private OrthographicCamera gamecam;
    private OrthographicCamera b2dcam;
    private Viewport gamePort;
    private Player player;

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Texture background;

    private ShapeRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;
    private AssetManager assetManager;


    private boolean characterChange;

    private float xVelocity = 1f;
    private float gravity = -10f;
    private float jumpSpeed = 250f;

    private float accumulator = 0;


    private WorldContactListener worldContactListener;
    private B2WorldCreator b2World;
    private float numberOfFlowerInLevel;
    private boolean waitRestartComplete;

    //character movement
    private boolean isDirectionUp;
    private boolean isDirectionRight = true;


    private enum STATE {
        PLAYING, GAME_OVER, LEVEL_CLEARED, PAUSED
    }
    private STATE state;

    private BitmapFont bitmapFont;
    private GlyphLayout layout = new GlyphLayout();



    public GameScreen(TekoiGame game, int level) {
        this.game = game;
        this.assetManager = game.getAssetManager();
        state = STATE.PLAYING;

        gamecam = new OrthographicCamera();
        b2dcam = new OrthographicCamera();
        b2dcam.setToOrtho(false,  GameConfig.SCREEN_WIDTH / TekoiGame.PPM, GameConfig.SCREEN_HEIGHT/ TekoiGame.PPM);
        gamePort = new FitViewport(GameConfig.SCREEN_WIDTH / TekoiGame.PPM, GameConfig.SCREEN_HEIGHT/ TekoiGame.PPM, gamecam);

        world = new World(new Vector2(0, gravity), true);
        b2dr = new Box2DDebugRenderer();

        map = assetManager.get(LevelNames.LEVEL+ level + LevelNames.TMX);
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / TekoiGame.PPM);
        gamecam.position.set(gamePort.getWorldWidth()/2,gamePort.getWorldHeight()/2, 0);
        player = new Player(world, (Texture) assetManager.get(ImagesPaths.CHAR_ATLAS));
        //background = assetManager.get(ImagesPaths.GAME_BACKGROUND);


        b2World = new B2WorldCreator(world, map, game);
        worldContactListener = new WorldContactListener();
        world.setContactListener(worldContactListener);

    }

    @Override
    public void show() {
        renderer = new ShapeRenderer();
    }

    public void update(float dt) {

        final float frameTime = Math.min(dt, 0.25f);
        accumulator += frameTime;
        while (accumulator >= 1f / 60f) {
            world.step(1f / 60f, 6, 2);
            accumulator -= 1f / 60f;
        }

        handleInput(dt);

        player.update(dt);
        gamecam.update();
        b2dcam.update();
        mapRenderer.setView(b2dcam);
    }

    private void handleInput(float dt) {

        if(isDirectionRight){
            player.b2body.setLinearVelocity(xVelocity, player.b2body.getLinearVelocity().y);
        }else{
            player.b2body.setLinearVelocity(-xVelocity, player.b2body.getLinearVelocity().y);
        }

        if(isDirectionUp && worldContactListener.isOnGrounds()){
            player.b2body.applyForceToCenter(0,jumpSpeed,true);
            isDirectionUp = false;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)|| Gdx.input.isKeyJustPressed(Input.Keys.W)){
            if(worldContactListener.isOnGrounds()){
                isDirectionUp = true;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)){
            isDirectionRight = true;
            game.directionRight = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)|| Gdx.input.isKeyPressed(Input.Keys.A)){
            isDirectionRight = false;
            game.directionRight = false;
        }


    }


    @Override
    public void render(float delta) {
        switch(state) {
            case PLAYING: {
                update(delta);
//                checkLevelCompleted();
//                checkGameOver();
//                checkSwitchOn();
//                checkWarp();
//                checkShowInfo();
            }
            break;
            case GAME_OVER: {
//                checkUserConfirmation();
            }
            break;
            case LEVEL_CLEARED: {
//                checkUserConfirmation();
            }
            break;
            case PAUSED: {
//                checkUnPauseGame();
            }
            break;
        }


        GdxUtils.clearScreen();
//        game.batch.begin();
//        game.batch.draw(background, 0, 0, (GameConfig.SCREEN_WIDTH / TekoiGame.PPM), (GameConfig.SCREEN_HEIGHT / TekoiGame.PPM));
//        game.batch.end();

        mapRenderer.render();
        if(GameConfig.debug) {
            b2dr.render(world, b2dcam.combined);
        }
        draw(delta);


    }

    private void draw(float delta) {
        game.batch.setProjectionMatrix(b2dcam.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        world.dispose();
        b2dr.dispose();
    }
}
