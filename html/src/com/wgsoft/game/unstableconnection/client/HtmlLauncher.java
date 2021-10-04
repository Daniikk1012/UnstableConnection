package com.wgsoft.game.unstableconnection.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.wgsoft.game.unstableconnection.MyGdxGame;

public final class HtmlLauncher extends GwtApplication {
        @Override
        public GwtApplicationConfiguration getConfig () {
            return new GwtApplicationConfiguration(true);
        }

        @Override
        public ApplicationListener createApplicationListener () {
            return new MyGdxGame();
        }
}
