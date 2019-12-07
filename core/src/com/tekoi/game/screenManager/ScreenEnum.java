package com.tekoi.game.screenManager;

import com.badlogic.gdx.Screen;
import com.strzal.gdx.screenManager.ScreenEnumIn;
import com.tekoi.game.TekoiGame;
import com.tekoi.game.screens.GameScreen;
import com.tekoi.game.screens.LoadingScreen;
import com.tekoi.game.screens.MenuScreen;
import com.tekoi.game.screens.MenuTestScreen;

/**
 * Based on http://www.pixnbgames.com/blog/libgdx/how-to-manage-screens-in-libgdx/
 */

public enum ScreenEnum implements ScreenEnumIn {
    GAME_SCREEN {
        public Screen getScreen(Object... params) {
            return new GameScreen((TekoiGame)params[0], (Integer) params[1]);
        }
    },
    LOADING_SCREEN {
        public Screen getScreen(Object... params) {
            return new LoadingScreen((TekoiGame)params[0]);
        }
    },
    MENU_SCREEN {
        public Screen getScreen(Object... params) {
            return new MenuTestScreen((TekoiGame)params[0]);
        }
    },
    MENU_SCREEN_OLD {
        public Screen getScreen(Object... params) {
            return new MenuScreen((TekoiGame)params[0]);
        }
    };

}
