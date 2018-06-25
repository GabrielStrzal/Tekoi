package com.tekoi.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tekoi.game.TekoiGame;
import com.tekoi.game.utils.GdxUtils;

/**
 * Created by lelo on 24/06/18.
 */

public class GameScreen implements Screen {

    private TekoiGame game;
    private Texture img;
    private SpriteBatch batch;

    public GameScreen(TekoiGame game) {
        this.game = game;
        batch = game.batch;
        img = new Texture("badlogic.jpg");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        GdxUtils.clearScreen();
        draw();
    }

    private void draw() {
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

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
        img.dispose();
    }
}
