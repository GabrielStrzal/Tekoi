package com.tekoi.game.entity.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.tekoi.game.TekoiGame;

public abstract class Enemy extends Sprite {

    protected Body body;
    protected TekoiGame game;
    protected AssetManager assetManager;
    protected float width;
    protected float height;

    public int HP = 1;
    public boolean attacked = false;

    public Enemy(Body body, TekoiGame game, Texture texture) {
        super(texture);
        setSize(texture.getWidth()/ TekoiGame.PPM,texture.getHeight()/ TekoiGame.PPM);
        this.body = body;
        this.game = game;
        this.assetManager = game.getAssetManager();
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

    public void update() {
        setPosition(body.getPosition().x - width / 2, body.getPosition().y - height / 2);
    }
}
