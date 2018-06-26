package com.tekoi.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tekoi.game.TekoiGame;
import com.tekoi.game.config.GameConfig;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int) GameConfig.DISPLAY_SCREEN_WIDTH_PX;
		config.height = (int)GameConfig.DISPLAY_SCREEN_HEIGHT_PX;
		new LwjglApplication(new TekoiGame(), config);
	}
}
