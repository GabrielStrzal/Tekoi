package com.tekoi.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tekoi.game.TekoiGame;
import com.tekoi.game.config.GameConfig;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int) GameConfig.SCREEN_WIDTH;
		config.height = (int)GameConfig.SCREEN_HEIGHT;
		new LwjglApplication(new TekoiGame(), config);
	}
}
