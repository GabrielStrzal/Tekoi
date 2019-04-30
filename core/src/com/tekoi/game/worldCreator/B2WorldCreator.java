package com.tekoi.game.worldCreator;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.tekoi.game.TekoiGame;
import com.tekoi.game.constants.Bits;
import com.tekoi.game.constants.Map;

/**
 * Created by Gabriel on 03/09/2017.
 */

public class B2WorldCreator {



    public B2WorldCreator(World world, TiledMap map, TekoiGame game){

        //Blocks
        FixtureDef blockFdef = new FixtureDef();
        PolygonShape blockShape = new PolygonShape();
        BodyDef blockBdef = new BodyDef();
        Body blockBody;
        if(map.getLayers().get(Map.MAP_BLOCKS) != null)
        for (MapObject object : map.getLayers().get(Map.MAP_BLOCKS).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            blockBdef.type = BodyDef.BodyType.StaticBody;
            blockBdef.position.set((rect.getX() + rect.getWidth() / 2) / TekoiGame.PPM, (rect.getY() + rect.getHeight() / 2) / TekoiGame.PPM);
            blockBody = world.createBody(blockBdef);
            blockShape.setAsBox((rect.getWidth() / 2) / TekoiGame.PPM, (rect.getHeight() / 2) / TekoiGame.PPM);
            blockFdef.shape = blockShape;
            blockFdef.filter.categoryBits = Bits.BRICK_BIT;
            blockFdef.filter.maskBits = Bits.PLAYER_BIT | Bits.BASE_BIT ;
            blockFdef.friction = 0;
            blockBody.createFixture(blockFdef).setUserData(Map.MAP_BLOCKS);
        }

    }
}
