package com.wgsoft.game.unstableconnection.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.wgsoft.game.unstableconnection.Constants;
import com.wgsoft.game.unstableconnection.MyGdxGame;

public final class PauseScreen extends ScreenAdapter {
    private final Stage stage;

    public PauseScreen(final MyGdxGame game) {
        stage = new Stage(
            new FitViewport(Constants.WIDTH, Constants.HEIGHT),
            game.getSpriteBatch()
        );

        final Image backgroundImage =
            new Image(game.getSkin(), "main-background");
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        final Image blurImage = new Image(game.getSkin(), "highlight");
        blurImage.setFillParent(true);
        stage.addActor(blurImage);

        final Table table = new Table();
        table.setFillParent(true);

        final TextButton continueButton =
            new TextButton("Continue", game.getSkin(), "menu");
        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                game.setGameScreen();
            }
        });
        table.add(continueButton).pad(1f);

        table.row();

        final TextButton settingsButton =
            new TextButton("Settings", game.getSkin(), "menu");
        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                game.setSettingsScreen();
            }
        });
        table.add(settingsButton).pad(1f);

        table.row();

        final TextButton exitButton =
            new TextButton("Exit", game.getSkin(), "menu");
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                game.setMenuScreen();
            }
        });
        table.add(exitButton).pad(1f);

        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(final float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(final int width, final int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
