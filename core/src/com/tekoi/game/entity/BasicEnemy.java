package com.tekoi.game.entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.tekoi.game.TekoiGame;
import com.tekoi.game.constants.ImagesPaths;

public class BasicEnemy extends Enemy {

    public BasicEnemy(Body body, TekoiGame game) {
        super(body, game);
        texture = assetManager.get(ImagesPaths.BASIC_ENEMY);
        height = texture.getHeight() / TekoiGame.PPM;
        width = texture.getWidth() / TekoiGame.PPM;
        HP = 3;
    }
}
