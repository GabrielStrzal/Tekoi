package com.tekoi.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tekoi.game.TekoiGame;
import com.tekoi.game.config.GameConfig;
import com.tekoi.game.constants.ImagesPaths;
import com.tekoi.game.constants.LevelNames;
import com.tekoi.game.entity.BasicEnemy;
import com.tekoi.game.entity.Enemy;
import com.tekoi.game.entity.Player;
import com.tekoi.game.utils.GdxUtils;
import com.tekoi.game.worldCreator.B2WorldCreator;
import com.tekoi.game.worldCreator.WorldContactListener;


public class GameScreen implements Screen {

    private TekoiGame game;

    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Player player;

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private ShapeRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;
    private AssetManager assetManager;


    private float xVelocity = 2f;
    private float gravity = -10f;

    private float accumulator = 0;


    private WorldContactListener worldContactListener;
    private B2WorldCreator b2World;

    private boolean isDirectionRightPressed = false;
    private boolean isDirectionLeftPressed = false;


    private enum STATE {
        PLAYING, GAME_OVER, LEVEL_CLEARED, PAUSED
    }

    private STATE state;


    public GameScreen(TekoiGame game, int level) {
        this.game = game;
        this.assetManager = game.getAssetManager();
        state = STATE.PLAYING;

        gamecam = new OrthographicCamera();
        gamecam.setToOrtho(false, GameConfig.SCREEN_WIDTH / TekoiGame.PPM, GameConfig.SCREEN_HEIGHT / TekoiGame.PPM);
        gamePort = new FitViewport(GameConfig.SCREEN_WIDTH / TekoiGame.PPM, GameConfig.SCREEN_HEIGHT / TekoiGame.PPM, gamecam);

        world = new World(new Vector2(0, gravity), true);
        b2dr = new Box2DDebugRenderer();

        map = assetManager.get(LevelNames.LEVEL + level + LevelNames.TMX);
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / TekoiGame.PPM);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        player = new Player(world, (Texture) assetManager.get(ImagesPaths.CHAR_ATLAS));


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

        updateEnemies();
        removeDeadEnemiesFromWorld();
        handleInput(dt);

        player.update(dt);
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        gamecam.position.x = MathUtils.clamp(player.b2body.getPosition().x, gamePort.getWorldWidth() / 2, (layer.getTileWidth() * layer.getWidth()) / TekoiGame.PPM - gamePort.getWorldWidth() / 2);


        gamecam.update();
        mapRenderer.setView(gamecam);
    }


    private void updateEnemies() {

        if (!player.state.equals(Player.PLAYER_STATE.ATTACKING)) {
            for (Enemy enemy : b2World.enemies) {
                enemy.attacked = false;
            }
        }


        Array<Body> bodies = worldContactListener.getBodiesToRemove();
        for (Body b : bodies) {
            Enemy enemy = b2World.enemies.get(b2World.enemies.indexOf((BasicEnemy) b.getUserData(), true));
            if (player.state.equals(Player.PLAYER_STATE.ATTACKING)) {
                if (!enemy.attacked) {
                    enemy.HP = enemy.HP - 1;
                    enemy.attacked = true;
                }
            }
        }

    }

    private void removeDeadEnemiesFromWorld() {

        for (Enemy enemy : b2World.enemies) {
            if (enemy.HP <= 0) {
                b2World.enemies.removeValue(enemy, true);
                world.destroyBody(enemy.getBody());
            }
        }

    }

    private void handleInput(float dt) {


        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && worldContactListener.isOnGrounds()) {
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2) {
            isDirectionRightPressed = true;
        } else {
            isDirectionRightPressed = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2) {
            isDirectionLeftPressed = true;
        } else {
            isDirectionLeftPressed = false;
        }


        if (isDirectionRightPressed) {
            player.b2body.setLinearVelocity(xVelocity, player.b2body.getLinearVelocity().y);
            player.playerFacingRight = true;
        } else if (isDirectionLeftPressed) {
            player.b2body.setLinearVelocity(-xVelocity, player.b2body.getLinearVelocity().y);
            player.playerFacingRight = false;
        } else {
            player.b2body.applyForceToCenter(-(player.b2body.getLinearVelocity().x) * 2, player.b2body.getLinearVelocity().y, true);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.attack();
        }

    }


    @Override
    public void render(float delta) {
        switch (state) {
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
        if (GameConfig.debug) {
            b2dr.render(world, gamecam.combined);
        }
        draw(delta);


    }

    private void draw(float delta) {
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();

        drawEnemies(delta);
        player.draw(game.batch);
        game.batch.end();

    }

    private void drawEnemies(float delta) {
        for(Enemy enemy: b2World.enemies){
            enemy.render(game.batch, delta);
        }

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
