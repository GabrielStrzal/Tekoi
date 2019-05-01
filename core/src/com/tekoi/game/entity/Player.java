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
import com.tekoi.game.TekoiGame;
import com.tekoi.game.constants.Bits;
import com.tekoi.game.constants.Map;

public class Player extends Sprite {

    public World world;
    public Body b2body;

    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;

    private float animationTimer = 0;
    private final Animation walking;
    private final TextureRegion standing;


    private enum PLAYER_DIRECTION {
        RIGHT, LEFT
    }

    private enum PLAYER_STATE {
        MOVING, ATTACKING, CROUCK, DEAD
    }

    public Player(World world, Texture texture){
        this(world, texture, 200/ TekoiGame.PPM, 380/ TekoiGame.PPM);
        setSize(WIDTH/TekoiGame.PPM,HEIGHT/TekoiGame.PPM);
    }

    public Player(World world, Texture texture, float x, float y) {
        super(texture);
        this.world = world;

        TextureRegion[] regions = TextureRegion.split(texture, WIDTH, HEIGHT)[0];
        walking = new Animation(0.1F, regions[0], regions[1], regions[2],regions[3],regions[4],regions[5], regions[6]);
        walking.setPlayMode(Animation.PlayMode.LOOP);
        standing = regions[7];

        definePlayer(x, y);

    }

    public void update(float dt){
        animationTimer += dt;

        float xSpeed = b2body.getLinearVelocity().x;

        setPosition(b2body.getPosition().x - getWidth() /2, b2body.getPosition().y - getHeight()/2);

        //set Texture
        TextureRegion regionToDraw = standing;
        if (xSpeed != 0) {
            regionToDraw = (TextureRegion) walking.getKeyFrame(animationTimer);
        }
        if (xSpeed < 0) {
            if (!regionToDraw.isFlipX()) regionToDraw.flip(true,false);
        } else if (xSpeed > 0) {
            if (regionToDraw.isFlipX()) regionToDraw.flip(true,false);
        }
        setRegion(regionToDraw);
    }

    public void definePlayer(float x, float y){

        BodyDef bdef = new BodyDef();
        bdef.position.set(x , y); //initial position
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body  = world.createBody(bdef);


        //Body Fixture
        FixtureDef bodyFdef = new FixtureDef();
        PolygonShape bodyShape = new PolygonShape();
        bodyShape.setAsBox(15/ TekoiGame.PPM ,(26)/ TekoiGame.PPM , new Vector2(0, 0/ TekoiGame.PPM),0);
        bodyFdef.filter.categoryBits = Bits.PLAYER_BIT;
        bodyFdef.filter.maskBits = Bits.BRICK_BIT | Bits.LEVEL_END_BIT
                | Bits.DAMAGE_BIT | Bits.PASS_BLOCK_BIT ;
        bodyFdef.shape = bodyShape;
        b2body.createFixture(bodyFdef).setUserData(Map.PLAYER_BODY);


        //Base Circle
        FixtureDef fdefCircle = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(15/ TekoiGame.PPM);
        circleShape.setPosition(new Vector2(0,-15/ TekoiGame.PPM));
        fdefCircle.filter.categoryBits = Bits.PLAYER_BIT;
        fdefCircle.filter.maskBits = Bits.BRICK_BIT ;
        fdefCircle.shape = circleShape;
        fdefCircle.friction = 0;
        b2body.createFixture(fdefCircle).setUserData(Map.PLAYER_BODY);


        //Base Sensor
        FixtureDef baseSensorFdef = new FixtureDef();
        PolygonShape baseShape = new PolygonShape();
        baseShape.setAsBox(15/ TekoiGame.PPM ,16/ TekoiGame.PPM , new Vector2(0,-22/ TekoiGame.PPM),0);
        baseSensorFdef.shape = baseShape;
        baseSensorFdef.isSensor = true;
        baseSensorFdef.filter.categoryBits = Bits.BASE_BIT;
        baseSensorFdef.filter.maskBits = Bits.BRICK_BIT | Bits.PASS_BLOCK_BIT;
        b2body.createFixture(baseSensorFdef).setUserData(Map.PLAYER_BASE);
    }

    public void dispose(){
        world.destroyBody(b2body);
    }
}
