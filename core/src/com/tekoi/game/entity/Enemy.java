package com.tekoi.game.entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.tekoi.game.TekoiGame;

public abstract class Enemy {

    protected Body body;
    protected TekoiGame game;
    protected AssetManager assetManager;
    protected float width;
    protected float height;
    protected Texture texture;

    public int HP = 1;
    public boolean attacked = false;

    public Enemy(Body body, TekoiGame game) {
        this.body = body;
        this.game = game;
        this.assetManager = game.getAssetManager();
    }


    public void render(SpriteBatch batch, float delta) {
        batch.draw(texture, (body.getPosition().x - width / 2), (body.getPosition().y - height / 2), height, width);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Body getBody() {
        return body;
    }
}
