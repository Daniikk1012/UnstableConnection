package com.wgsoft.game.unstableconnection.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.wgsoft.game.unstableconnection.MyGdxGame;

public final class DesktopLauncher {
	public static void main (String[] arg) {
		final Lwjgl3ApplicationConfiguration config =
            new Lwjgl3ApplicationConfiguration();
        config.setTitle("Unstable Connection");
		new Lwjgl3Application(new MyGdxGame(), config);
	}
}
