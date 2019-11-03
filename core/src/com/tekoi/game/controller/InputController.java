package com.tekoi.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.tekoi.game.entity.Player;
import com.tekoi.game.worldCreator.WorldContactListener;

public class InputController {

    private Player player;
    private WorldContactListener worldContactListener;

    private final float X_VELOCITY = 2f;
    private final float JUMP_SPEED = 6f;
    private final int TOP_LINEAR_VELOCITY = 70;


    public InputController(Player player, WorldContactListener worldContactListener) {
        this.player = player;
        this.worldContactListener = worldContactListener;
    }

    public void handleInput(float dt) {

        //Jump
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && worldContactListener.isOnGrounds() && player.b2body.getLinearVelocity().y == 0) {
            player.b2body.applyLinearImpulse(new Vector2(0, JUMP_SPEED), player.b2body.getWorldCenter(), true);
        }
        //Move Right
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= TOP_LINEAR_VELOCITY) {
            player.b2body.setLinearVelocity(X_VELOCITY, player.b2body.getLinearVelocity().y);
            player.playerFacingRight = true;
        }
        //Move left
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -TOP_LINEAR_VELOCITY) {
            player.b2body.setLinearVelocity(-X_VELOCITY, player.b2body.getLinearVelocity().y);
            player.playerFacingRight = false;
        }
        //apply only to X some force to reduce speed if player not moving
        else {
            player.b2body.applyForceToCenter(-(player.b2body.getLinearVelocity().x) * 2, 0, true);
        }
        //remove x speed if player is slower (maybe remove this code later)
        if(player.b2body.getLinearVelocity().x < .2 && player.b2body.getLinearVelocity().x > -.2)
            player.b2body.setLinearVelocity(0, player.b2body.getLinearVelocity().y);

        //Attack
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.attack();
        }

    }
}
