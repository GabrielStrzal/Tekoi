package com.tekoi.game.entity.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.tekoi.game.TekoiGame;

public class WalkingEnemy extends Enemy {

    public WalkingEnemy(Body body, TekoiGame game, Texture texture) {
        super(body, game, texture);
        height = texture.getHeight() / TekoiGame.PPM;
        width = texture.getWidth() / TekoiGame.PPM;
        HP = 3;
    }

    @Override
    public void update() {
        super.update();
        //Força para andar
        //body.applyForceToCenter(-100000, 0 , false);
    }
}
