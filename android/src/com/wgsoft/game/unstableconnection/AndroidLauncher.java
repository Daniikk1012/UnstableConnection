package com.wgsoft.game.unstableconnection;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.wgsoft.game.unstableconnection.MyGdxGame;

public final class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final AndroidApplicationConfiguration config =
            new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;
		initialize(new MyGdxGame(), config);
	}
}
