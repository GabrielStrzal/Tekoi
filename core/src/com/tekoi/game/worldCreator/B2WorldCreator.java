package com.tekoi.game.worldCreator;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.tekoi.game.TekoiGame;
import com.tekoi.game.constants.Bits;
import com.tekoi.game.constants.Map;
import com.tekoi.game.entity.enemies.Enemy;
import com.tekoi.game.entity.enemies.EnemyFactory;


public class B2WorldCreator {

    public Array<Enemy> enemies;
    private EnemyFactory factory;


    public B2WorldCreator(World world, TiledMap map, TekoiGame game) {

        factory = new EnemyFactory();
        enemies = new Array<Enemy>();

        createBlocks(world, map);
        createBasicEnemy(world, map, game);
        createWalkingEnemy(world, map, game);

    }

    private void createBlocks(World world, TiledMap map) {
        FixtureDef blockFdef = new FixtureDef();
        PolygonShape blockShape = new PolygonShape();
        BodyDef blockBdef = new BodyDef();
        Body blockBody;
        if (map.getLayers().get(Map.MAP_BLOCKS) != null)
            for (MapObject object : map.getLayers().get(Map.MAP_BLOCKS).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                blockBdef.type = BodyDef.BodyType.StaticBody;
                blockBdef.position.set((rect.getX() + rect.getWidth() / 2) / TekoiGame.PPM, (rect.getY() + rect.getHeight() / 2) / TekoiGame.PPM);
                blockBody = world.createBody(blockBdef);
                blockShape.setAsBox((rect.getWidth() / 2) / TekoiGame.PPM, (rect.getHeight() / 2) / TekoiGame.PPM);
                blockFdef.shape = blockShape;
                blockFdef.filter.categoryBits = Bits.BRICK_BIT;
                blockFdef.filter.maskBits = Bits.PLAYER_BIT | Bits.BASE_BIT | Bits.ENEMY_BIT;
                blockFdef.friction = 0;
                blockBody.createFixture(blockFdef).setUserData(Map.MAP_BLOCKS);
            }
    }

    private void createWalkingEnemy(World world, TiledMap map, TekoiGame game) {

        FixtureDef walkingEnemyFdef = new FixtureDef();
        PolygonShape walkingEnemyShape = new PolygonShape();
        BodyDef walkingEnemyBdef = new BodyDef();
        if (map.getLayers().get(Map.MAP_WALKING_ENEMY) != null)
            for (MapObject object : map.getLayers().get(Map.MAP_WALKING_ENEMY).getObjects()) {
                Ellipse tiledMapEllipse = ((EllipseMapObject) object).getEllipse();

                walkingEnemyBdef.type = BodyDef.BodyType.DynamicBody;
                walkingEnemyBdef.position.set((tiledMapEllipse.x + tiledMapEllipse.width / 2) / TekoiGame.PPM,
                        (tiledMapEllipse.y + tiledMapEllipse.height / 2) / TekoiGame.PPM);
                walkingEnemyShape.setAsBox((32 / 2) / TekoiGame.PPM, (64 / 2) / TekoiGame.PPM);
                walkingEnemyFdef.shape = walkingEnemyShape;
                walkingEnemyFdef.filter.categoryBits = Bits.ENEMY_BIT;
                walkingEnemyFdef.filter.maskBits = Bits.PLAYER_BIT | Bits.BASE_BIT | Bits.BRICK_BIT;
                Body walkingEnemyBody = world.createBody(walkingEnemyBdef);
                MassData massData = new MassData();
                massData.mass = 1000000;
                walkingEnemyBody.setMassData(massData);
                walkingEnemyBody.createFixture(walkingEnemyFdef).setUserData(Map.MAP_WALKING_ENEMY);
                Enemy f = factory.createEnemy(walkingEnemyBody, game, EnemyFactory.EnemyType.WALKING);
                walkingEnemyBody.setUserData(f);
                enemies.add(f);

            }

    }

    private void createBasicEnemy(World world, TiledMap map, TekoiGame game) {
        BodyDef basicEnemyBdef = new BodyDef();
        FixtureDef basicEnemyFdef = new FixtureDef();
        PolygonShape basicEnemyShape = new PolygonShape();
        Body basicEnemyBody;
        if (map.getLayers().get(Map.MAP_BASIC_ENEMY) != null)
            for (MapObject object : map.getLayers().get(Map.MAP_BASIC_ENEMY).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                basicEnemyBdef.type = BodyDef.BodyType.DynamicBody;
                basicEnemyBdef.position.set((rect.getX() + rect.getWidth() / 2) / TekoiGame.PPM, (rect.getY() + rect.getHeight() / 2) / TekoiGame.PPM);
                basicEnemyBody = world.createBody(basicEnemyBdef);
                basicEnemyShape.setAsBox((rect.getWidth() / 2) / TekoiGame.PPM, (rect.getHeight() / 2) / TekoiGame.PPM);
                basicEnemyFdef.shape = basicEnemyShape;
                basicEnemyFdef.filter.categoryBits = Bits.ENEMY_BIT;
                basicEnemyFdef.filter.maskBits = Bits.PLAYER_BIT | Bits.BASE_BIT | Bits.BRICK_BIT;
                MassData massData = new MassData();
                massData.mass = 1000000;
                basicEnemyBody.setMassData(massData);
                basicEnemyBody.createFixture(basicEnemyFdef).setUserData(Map.MAP_BASIC_ENEMY);
                Enemy basicEnemy = factory.createEnemy(basicEnemyBody, game, EnemyFactory.EnemyType.BASIC);
                basicEnemyBody.setUserData(basicEnemy);
                enemies.add(basicEnemy);
            }

    }
}
