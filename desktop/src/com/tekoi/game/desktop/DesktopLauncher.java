package com.tekoi.game.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.tekoi.game.TekoiGame;
import com.tekoi.game.config.GameConfig;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration ();
		config.setWindowedMode( GameConfig.SCREEN_DESKTOP_DISPLAY_WIDTH, GameConfig.SCREEN_DESKTOP_DISPLAY_HEIGHT);
		new Lwjgl3Application(new TekoiGame(), config);
	}
}
