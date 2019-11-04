package com.tekoi.game.worldCreator;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.tekoi.game.TekoiGame;

public class TextureMapObjectRenderer extends OrthogonalTiledMapRenderer {

    public TextureMapObjectRenderer(TiledMap map) {
        super(map);
    }

    public TextureMapObjectRenderer(TiledMap map, Batch batch) {
        super(map, batch);
    }

    public TextureMapObjectRenderer(TiledMap map, float unitScale) {
        super(map, unitScale);
    }

    public TextureMapObjectRenderer(TiledMap map, float unitScale, Batch batch) {
        super(map, unitScale, batch);
    }

    @Override
    public void renderObject(MapObject object) {
        if (object instanceof TextureMapObject) {
            TextureMapObject textureObject = (TextureMapObject) object;
            batch.draw(
                    textureObject.getTextureRegion(),
                    textureObject.getX() / TekoiGame.PPM,
                    textureObject.getY() / TekoiGame.PPM,
                    textureObject.getOriginX() / TekoiGame.PPM,
                    textureObject.getOriginY() / TekoiGame.PPM,
                    textureObject.getTextureRegion().getRegionWidth() / TekoiGame.PPM,
                    textureObject.getTextureRegion().getRegionHeight() / TekoiGame.PPM,
                    textureObject.getScaleX(),
                    textureObject.getScaleY(),
                    textureObject.getRotation() * -1
            );
        }
    }
}
