package com.tekoi.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.tekoi.game.TekoiGame;
import com.tekoi.game.constants.Bits;
import com.tekoi.game.constants.ImagesPaths;
import com.tekoi.game.constants.Map;

public class Player extends Sprite {

    public World world;
    public Body b2body;

    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;

    private float animationTimer = 0;
    private final Animation walking;
    private final TextureRegion standing;
    private final TextureRegion action;

    private TextureRegion regionToDraw;
    public boolean playerFacingRight = true;

    long attackStartTime;


    public enum PLAYER_STATE {
        IDLE, MOVING, ATTACKING, CROUCHING, DEAD
    }

    public PLAYER_STATE state = PLAYER_STATE.IDLE;

    public Player(World world, Texture texture) {
        this(world, texture, 200 / TekoiGame.PPM, 380 / TekoiGame.PPM);
        setSize(WIDTH / TekoiGame.PPM, HEIGHT / TekoiGame.PPM);
    }

    public Player(World world, Texture texture, float x, float y) {
        super(texture);
        this.world = world;

        TextureRegion[] regions = TextureRegion.split(texture, WIDTH, HEIGHT)[0];
        walking = new Animation(0.1F, regions[0], regions[1], regions[2], regions[3], regions[4], regions[5], regions[6]);
        walking.setPlayMode(Animation.PlayMode.LOOP);
        standing = regions[7];
        action = new TextureRegion(new Texture(Gdx.files.internal(ImagesPaths.CHAR_ATTACK)));

        regionToDraw = standing;
        definePlayer(x, y);

    }

    public void attack() {
        if (state != PLAYER_STATE.ATTACKING) {// para nao permitir que continue iniciando o ataque sempre
            attackStartTime = TimeUtils.millis();
        }
        state = PLAYER_STATE.ATTACKING;
    }

    public void update(float dt) {
        animationTimer += dt;

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        switch (state) {
            case IDLE: {
                checkPlayerVelocity();
                regionToDraw = standing;
            }
            break;
            case MOVING: {
                checkPlayerVelocity();
                regionToDraw = (TextureRegion) walking.getKeyFrame(animationTimer);
            }
            break;
            case ATTACKING: {
                checkAttackOver();
                regionToDraw = action;

            }
            break;
            case CROUCHING: {

            }
            break;
            case DEAD: {

            }
            break;
        }

        if (!playerFacingRight) {
            if (!regionToDraw.isFlipX()) regionToDraw.flip(true, false);
        } else {
            if (regionToDraw.isFlipX()) regionToDraw.flip(true, false);
        }
        setRegion(regionToDraw);


    }

    private void checkPlayerVelocity() {
        float xSpeed = b2body.getLinearVelocity().x;

        if (xSpeed < 0) {
            playerFacingRight = false;
        } else if (xSpeed > 0) {
            playerFacingRight = true;
        }

        if (xSpeed == 0) {
            state = PLAYER_STATE.IDLE;
        } else if (xSpeed != 0) {
            state = PLAYER_STATE.MOVING;
        }
    }


    private void checkAttackOver() {
        long attackElapsedTime = TimeUtils.timeSinceMillis(attackStartTime);
        if (attackElapsedTime > 500) {
            state = PLAYER_STATE.IDLE;
        }


    }

    public void definePlayer(float x, float y) {

        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y); //initial position
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);


        //Body Fixture
        FixtureDef bodyFdef = new FixtureDef();
        PolygonShape bodyShape = new PolygonShape();
        bodyShape.setAsBox(15 / TekoiGame.PPM, (26) / TekoiGame.PPM, new Vector2(0, 0 / TekoiGame.PPM), 0);
        bodyFdef.filter.categoryBits = Bits.PLAYER_BIT;
        bodyFdef.filter.maskBits = Bits.BRICK_BIT | Bits.LEVEL_END_BIT
                | Bits.DAMAGE_BIT | Bits.PASS_BLOCK_BIT;
        bodyFdef.shape = bodyShape;
        b2body.createFixture(bodyFdef).setUserData(Map.PLAYER_BODY);


        //Base Circle
        FixtureDef fdefCircle = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(15 / TekoiGame.PPM);
        circleShape.setPosition(new Vector2(0, -15 / TekoiGame.PPM));
        fdefCircle.filter.categoryBits = Bits.PLAYER_BIT;
        fdefCircle.filter.maskBits = Bits.BRICK_BIT;
        fdefCircle.shape = circleShape;
        fdefCircle.friction = 0;
        b2body.createFixture(fdefCircle).setUserData(Map.PLAYER_BODY);


        //Base Sensor
        FixtureDef baseSensorFdef = new FixtureDef();
        PolygonShape baseShape = new PolygonShape();
        baseShape.setAsBox(15 / TekoiGame.PPM, 16 / TekoiGame.PPM, new Vector2(0, -22 / TekoiGame.PPM), 0);
        baseSensorFdef.shape = baseShape;
        baseSensorFdef.isSensor = true;
        baseSensorFdef.filter.categoryBits = Bits.BASE_BIT;
        baseSensorFdef.filter.maskBits = Bits.BRICK_BIT | Bits.PASS_BLOCK_BIT;
        b2body.createFixture(baseSensorFdef).setUserData(Map.PLAYER_BASE);

        //Attack right
        FixtureDef attackRightSensorFdef = new FixtureDef();

        PolygonShape rightAttackShape = new PolygonShape();
        rightAttackShape.setAsBox(15 / TekoiGame.PPM, 16 / TekoiGame.PPM,
                new Vector2(10 / TekoiGame.PPM, -6 / TekoiGame.PPM), 0);
        attackRightSensorFdef.shape = rightAttackShape;
        attackRightSensorFdef.isSensor = true;
        attackRightSensorFdef.filter.categoryBits = Bits.BASE_BIT;
        attackRightSensorFdef.filter.maskBits = Bits.BRICK_BIT | Bits.PASS_BLOCK_BIT;
        b2body.createFixture(attackRightSensorFdef).setUserData(Map.PLAYER_ATTACK_RIGHT);


        //Attack left
        FixtureDef attackLeftSensorFdef = new FixtureDef();
        PolygonShape leftAttackShape = new PolygonShape();
        leftAttackShape.setAsBox(15 / TekoiGame.PPM, 16 / TekoiGame.PPM,
                new Vector2(-10 / TekoiGame.PPM, -6 / TekoiGame.PPM), 0);
        attackLeftSensorFdef.shape = leftAttackShape;
        attackLeftSensorFdef.isSensor = true;
        attackLeftSensorFdef.filter.categoryBits = Bits.BASE_BIT;
        attackLeftSensorFdef.filter.maskBits = Bits.BRICK_BIT | Bits.PASS_BLOCK_BIT;
        b2body.createFixture(attackLeftSensorFdef).setUserData(Map.PLAYER_ATTACK_LEFT);

    }


    public void dispose() {
        world.destroyBody(b2body);
    }
}
