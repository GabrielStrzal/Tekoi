package com.tekoi.game.entity.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.tekoi.game.TekoiGame;


public class BasicEnemy extends Enemy {

    public BasicEnemy(Body body, TekoiGame game, Texture texture) {
        super(body, game, texture);
        height = texture.getHeight() / TekoiGame.PPM;
        width = texture.getWidth() / TekoiGame.PPM;
//        HP = 3;
    }
}
