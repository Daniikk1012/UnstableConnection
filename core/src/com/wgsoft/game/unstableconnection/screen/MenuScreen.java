package com.wgsoft.game.unstableconnection.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.wgsoft.game.unstableconnection.Constants;
import com.wgsoft.game.unstableconnection.MyGdxGame;

public final class MenuScreen extends ScreenAdapter {
    private final Stage stage;

    public MenuScreen(final MyGdxGame game) {
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

        final Label titleLabel = new Label(
            "UNSTABLE\nCONNECTION",
            game.getSkin(),
            "press-start-large"
        );
        titleLabel.setAlignment(Align.center);
        table.add(titleLabel).pad(1f);

        table.row();

        final TextButton startButton =
            new TextButton("Start", game.getSkin(), "menu");
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.initSounds();
                game.setGameScreen();
            }
        });
        table.add(startButton).pad(1f);

        table.row();

        final TextButton exitButton =
            new TextButton("Exit", game.getSkin(), "menu");
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
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
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
