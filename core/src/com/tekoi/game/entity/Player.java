package com.tekoi.game.entity;

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
import com.tekoi.game.constants.Map;

public class Player extends Sprite {

    public World world;
    public Body b2body;
    public Body b2body_attack;

    public static final int WIDTH = 190;
    public static final int HEIGHT = 128;
    public static final double SIZE_MULTIPLIER = 1;//.7;

    public static final double MULTIPLIER = 2;//1.4;

    public static final int CHAR_COLLISION_WIDTH = (int)(15 * MULTIPLIER);
    public static final int CHAR_COLLISION_HEIGHT = (int)(26 * MULTIPLIER);
    public static final int BASE_SENSOR_HEIGHT = (int)(16 * MULTIPLIER);
    public static final int BASE_SENSOR_POSITION_Y = (int)(-22 * MULTIPLIER);
    public static final int ATTACK_BOX_WIDTH = (int)(15 * MULTIPLIER);
    public static final int ATTACK_BOX_HEIGHT = (int)(16 * MULTIPLIER);
    public static final int ATTACK_BOX_POSITION_Y = (int)(-6 * MULTIPLIER);
    public static final int ATTACK_BOX_SIZE_DISPLACEMENT = (int)(26 * MULTIPLIER);

    public static final int ATTACK_TIME = 150;

    private float animationTimer = 0;
    private final Animation walking;
    private final TextureRegion standing;
    private final TextureRegion attack;
    private final Animation action;

    private TextureRegion regionToDraw;
    public boolean playerFacingRight = true;

    long attackStartTime;

    public enum PLAYER_STATE {
        IDLE, MOVING, ATTACKING, CROUCHING, DEAD
    }

    public PLAYER_STATE state = PLAYER_STATE.IDLE;

    public Player(World world, Texture texture) {
        this(world, texture, 200 / TekoiGame.PPM, 380 / TekoiGame.PPM);
        //this sets the size of the character in the screen - it's being changed to get a better proportion
        setSize((float) (WIDTH * SIZE_MULTIPLIER / TekoiGame.PPM), (float) (HEIGHT * SIZE_MULTIPLIER / TekoiGame.PPM));
    }

    public Player(World world, Texture texture, float x, float y) {
        super(texture);
        this.world = world;

        TextureRegion[][] regions = TextureRegion.split(texture, WIDTH, HEIGHT);
        walking = new Animation(0.1F, regions[1][0], regions[1][1], regions[1][2], regions[1][3], regions[1][4], regions[1][5], regions[1][6]);
        walking.setPlayMode(Animation.PlayMode.LOOP);
        standing = regions[0][0];
        action = new Animation(0.9F, regions[2][1], regions[2][2], regions[2][3]);
        action.setPlayMode(Animation.PlayMode.LOOP);
        attack = regions[2][2];
        regionToDraw = standing;
        definePlayer(x, y);

    }

    public void attack() {
        if (state != PLAYER_STATE.ATTACKING) {// para nao permitir que continue iniciando o ataque sempre
            attackStartTime = TimeUtils.millis();
            createAttackShape(b2body);
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
                //regionToDraw = (TextureRegion) action.getKeyFrame(animationTimer);
                regionToDraw = attack;
                updateAttackShape();


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

        if (xSpeed < -.5) {
            playerFacingRight = false;
            state = PLAYER_STATE.MOVING;
        } else if (xSpeed > .5) {
            playerFacingRight = true;
            state = PLAYER_STATE.MOVING;
        } else {
            state = PLAYER_STATE.IDLE;
        }
    }


    private void checkAttackOver() {
        long attackElapsedTime = TimeUtils.timeSinceMillis(attackStartTime);
        if (attackElapsedTime > ATTACK_TIME) {
            state = PLAYER_STATE.IDLE;
            removeAttackShape();
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
        bodyShape.setAsBox(CHAR_COLLISION_WIDTH / TekoiGame.PPM, CHAR_COLLISION_HEIGHT / TekoiGame.PPM,
                new Vector2(0 / TekoiGame.PPM, 0 / TekoiGame.PPM), 0);
        bodyFdef.filter.categoryBits = Bits.PLAYER_BIT;
        bodyFdef.filter.maskBits = Bits.BRICK_BIT | Bits.LEVEL_END_BIT
                | Bits.DAMAGE_BIT | Bits.PASS_BLOCK_BIT | Bits.ENEMY_BIT;
        bodyFdef.shape = bodyShape;
        b2body.createFixture(bodyFdef).setUserData(Map.PLAYER_BODY);


        //Base Circle
        FixtureDef fdefCircle = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(CHAR_COLLISION_WIDTH / TekoiGame.PPM);
        circleShape.setPosition(new Vector2(0 / TekoiGame.PPM, -CHAR_COLLISION_WIDTH / TekoiGame.PPM));
        fdefCircle.filter.categoryBits = Bits.PLAYER_BIT;
        fdefCircle.filter.maskBits = Bits.BRICK_BIT;
        fdefCircle.shape = circleShape;
        fdefCircle.friction = 0;
        b2body.createFixture(fdefCircle).setUserData(Map.PLAYER_BODY);

        //Base Sensor
        FixtureDef baseSensorFdef = new FixtureDef();
        PolygonShape baseShape = new PolygonShape();
        baseShape.setAsBox(CHAR_COLLISION_WIDTH / TekoiGame.PPM, BASE_SENSOR_HEIGHT / TekoiGame.PPM, new Vector2(0 / TekoiGame.PPM, BASE_SENSOR_POSITION_Y / TekoiGame.PPM), 0);
        baseSensorFdef.shape = baseShape;
        baseSensorFdef.isSensor = true;
        baseSensorFdef.filter.categoryBits = Bits.BASE_BIT;
        baseSensorFdef.filter.maskBits = Bits.BRICK_BIT | Bits.PASS_BLOCK_BIT;
        b2body.createFixture(baseSensorFdef).setUserData(Map.PLAYER_BASE);



    }

    private void createAttackShape(Body b2body){

        BodyDef bdef = new BodyDef();
        bdef.position.set(b2body.getPosition().x, b2body.getPosition().y); //initial position
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body_attack = world.createBody(bdef);
        FixtureDef attackSensorFdef = new FixtureDef();
        PolygonShape attackShape = new PolygonShape();

        int direction = playerFacingRight ?  ATTACK_BOX_SIZE_DISPLACEMENT :  -ATTACK_BOX_SIZE_DISPLACEMENT;

        attackShape.setAsBox(ATTACK_BOX_WIDTH / TekoiGame.PPM, ATTACK_BOX_HEIGHT / TekoiGame.PPM,
                new Vector2(direction / TekoiGame.PPM, ATTACK_BOX_POSITION_Y / TekoiGame.PPM), 0);
        attackSensorFdef.shape = attackShape;
        attackSensorFdef.isSensor = true;

        attackSensorFdef.filter.categoryBits = Bits.BASE_BIT;
        attackSensorFdef.filter.maskBits = Bits.BRICK_BIT | Bits.PASS_BLOCK_BIT | Bits.ENEMY_BIT;
        b2body_attack.createFixture(attackSensorFdef).setUserData(Map.PLAYER_ATTACK_SHAPE);

    }

    private void updateAttackShape() {
        b2body_attack.setTransform(b2body.getPosition().x, b2body.getPosition().y, 0);
    }

    private void removeAttackShape(){
        world.destroyBody(b2body_attack);
    }


    public void dispose() {
        world.destroyBody(b2body);
    }
}
