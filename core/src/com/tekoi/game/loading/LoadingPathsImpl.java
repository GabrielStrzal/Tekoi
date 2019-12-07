package com.tekoi.game.loading;

import com.strzal.gdx.loading.LoadingPaths;
import com.tekoi.game.constants.GameAssets;
import com.tekoi.game.constants.ImagesPaths;
import com.tekoi.game.constants.LevelNames;

import java.util.ArrayList;
import java.util.List;

public class LoadingPathsImpl implements LoadingPaths {
    @Override
    public List<String> getTexturePaths() {
        List<String> list = new ArrayList<>();

        //Menu
        list.add(ImagesPaths.MENU_BACKGROUND);
        list.add(ImagesPaths.MENU_PLAY_BUTTON);
        list.add(ImagesPaths.MENU_PLAY_BUTTON_PRESSED);
        list.add(ImagesPaths.MENU_RETURN_BUTTON);
        list.add(ImagesPaths.MENU_RETURN_BUTTON_PRESSED);
        list.add(ImagesPaths.MENU_RESTART_BUTTON);
        list.add(ImagesPaths.MENU_RESTART_BUTTON_PRESSED);

        //Character
        list.add(ImagesPaths.CHAR_ATLAS);

        //Enemies
        list.add(ImagesPaths.BASIC_ENEMY);
        list.add(ImagesPaths.WALKING_ENEMY);

        return list;
    }

    @Override
    public List<String> getBitmapPaths() {
        List<String> list = new ArrayList<>();
        list.add(GameAssets.GAME_FONT);
        return list;
    }

    @Override
    public List<String> getTileMapPaths() {
        List<String> list = new ArrayList<>();

        //Levels
        list.add(LevelNames.LEVEL_1);
        list.add(LevelNames.LEVEL_2);

        return list;
    }

    @Override
    public List<String> getMusicPaths() {
        return null;
    }

    @Override
    public List<String> getSoundPaths() {
        return null;
    }

}
