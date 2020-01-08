package com.tekoi.game.screenManager;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.strzal.gdxUtilLib.loading.LoadingPaths;
import com.strzal.gdxUtilLib.screenManager.ScreenEnumInterface;
import com.strzal.gdxUtilLib.screens.LoadingScreen;
import com.tekoi.game.TekoiGame;
import com.tekoi.game.screens.GameScreen;
import com.tekoi.game.screens.MenuScreen;

/**
 * Based on http://www.pixnbgames.com/blog/libgdx/how-to-manage-screens-in-libgdx/
 */

public enum ScreenEnum implements ScreenEnumInterface {
    GAME_SCREEN {
        public Screen getScreen(Object... params) {
            return new GameScreen((TekoiGame)params[0], (Integer) params[1]);
        }
    },
    LOADING_SCREEN {
        public Screen getScreen(Object... params) {
            return new LoadingScreen((TekoiGame)params[0], (LoadingPaths)params[1], (ScreenAdapter)params[2]);
        }
    },
    MENU_SCREEN {
        public Screen getScreen(Object... params) {
            return new MenuScreen((TekoiGame)params[0]);
        }
    }

}
