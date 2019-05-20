package com.tekoi.game.worldCreator;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.tekoi.game.constants.Map;

/**
 * Created by lelo on 31/01/18.
 */

public class WorldContactListener implements ContactListener {

    private int onGrounds = 0;
    private Array<Body> bodiesToRemove;
    private boolean levelFinished = false;
    private boolean gameOver = false;
    private boolean switchOn = false;
    private boolean warpA = false;
    private boolean warpB = false;
    private boolean justWarpedA = false;
    private boolean justWarpedB = false;
    private boolean showInfo = false;


    public WorldContactListener() {
        super();
        bodiesToRemove = new Array<Body>();
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        checkCharacterOnGrounds(contact);
        checkCharacterAttackEnemyContact(contact);
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        uncheckCharacterOnGrounds(contact);
        uncheckCharacterAttackEnemyContact(contact);
    }


    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }


    private void checkCharacterOnGrounds(Contact contact) {

        //Jump and Character change check
        if (isContact(contact, Map.PLAYER_BASE, Map.MAP_BLOCKS)
                || isContact(contact, Map.PLAYER_BASE, Map.MAP_PASS_BLOCKS)
                || isContact(contact, Map.PLAYER_BASE, Map.MAP_SLOPE)
                || isContact(contact, Map.PLAYER_BASE, Map.MAP_PUSH_BLOCKS)
                || isContact(contact, Map.PLAYER_BASE, Map.MAP_LEVEL_END_BLOCK)) {
            onGrounds++;
        }
    }

    private void uncheckCharacterOnGrounds(Contact contact) {
        if (isContact(contact, Map.PLAYER_BASE, Map.MAP_BLOCKS)
                || isContact(contact, Map.PLAYER_BASE, Map.MAP_PASS_BLOCKS)
                || isContact(contact, Map.PLAYER_BASE, Map.MAP_SLOPE)
                || isContact(contact, Map.PLAYER_BASE, Map.MAP_PUSH_BLOCKS)
                || isContact(contact, Map.PLAYER_BASE, Map.MAP_LEVEL_END_BLOCK)) {
            onGrounds--;
        }
    }


    private void checkCharacterAttackEnemyContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (isContact(contact, Map.PLAYER_ATTACK_SHAPE, Map.MAP_BASIC_ENEMY)) {
            Fixture basic_enemy = fixA.getUserData() == Map.MAP_BASIC_ENEMY ? fixA : fixB;
            bodiesToRemove.add(basic_enemy.getBody());
        }
    }

    private void uncheckCharacterAttackEnemyContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (isContact(contact, Map.PLAYER_ATTACK_SHAPE, Map.MAP_BASIC_ENEMY)) {
            Fixture basic_enemy = fixA.getUserData() == Map.MAP_BASIC_ENEMY ? fixA : fixB;
            bodiesToRemove.removeValue(basic_enemy.getBody(), true);
        }
    }


    private boolean isContact(Contact contact, String a, String b) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        if ((fixA.getUserData() == a && fixB.getUserData() == b) || (fixA.getUserData() == b && fixB.getUserData() == a)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isOnGrounds() {
        if (onGrounds == 1) {
            return true;
        } else {
            return false;
        }
    }


    public Array<Body> getBodiesToRemove() {
        return bodiesToRemove;
    }


    public boolean isLevelFinished() {
        return levelFinished;
    }

    public void setLevelFinished(boolean levelFinished) {
        this.levelFinished = levelFinished;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isSwitchOn() {
        return switchOn;
    }


    public void setWarpA(boolean warpA) {
        this.warpA = warpA;
    }

    public void setWarpB(boolean warpB) {
        this.warpB = warpB;
    }

    public boolean isWarpA() {
        return warpA;
    }

    public boolean isWarpB() {
        return warpB;
    }

    public boolean isJustWarpedA() {
        return justWarpedA;
    }

    public void setJustWarpedA(boolean justWarpedA) {
        this.justWarpedA = justWarpedA;
    }

    public boolean isJustWarpedB() {
        return justWarpedB;
    }

    public void setJustWarpedB(boolean justWarpedB) {
        this.justWarpedB = justWarpedB;
    }

    public boolean isShowInfo() {
        return showInfo;
    }

    public void setShowInfo(boolean showInfo) {
        this.showInfo = showInfo;
    }
}
