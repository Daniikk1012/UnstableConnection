package com.wgsoft.game.unstableconnection.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.wgsoft.game.unstableconnection.Constants;
import com.wgsoft.game.unstableconnection.MyGdxGame;

public final class SettingsScreen extends ScreenAdapter {
    private final Stage stage;

    private Screen fromScreen;

    public SettingsScreen(final MyGdxGame game) {
        stage = new Stage(
            new FitViewport(Constants.WIDTH, Constants.HEIGHT),
            game.getSpriteBatch()
        );

        final Image backgroundImage =
            new Image(game.getSkin(), "main-background");
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        final Image blurImage = new Image(game.getSkin(), "highlight");
        blurImage.setFillParent(true);;
        stage.addActor(blurImage);

        final Table table = new Table();
        table.setFillParent(true);

        table.add(new Label("Sounds", game.getSkin(), "press-start-medium"));

        final Slider soundSlider =
            new Slider(0f, 100f, 5f, false, game.getSkin(), "slider");
        soundSlider.setValue(
            game.getFloatPreference("sound", Constants.SOUND_DEFAULT)
        );
        table.add(soundSlider).width(22f).pad(1f);

        table.row();

        table.add(new Label("Music", game.getSkin(), "press-start-medium"));

        final Slider musicSlider =
            new Slider(0f, 100f, 5f, false, game.getSkin(), "slider");
        musicSlider.setValue(
            game.getFloatPreference("music", Constants.MUSIC_DEFAULT)
        );
        table.add(musicSlider).width(22f).pad(1f);

        table.row();

        final TextButton applyButton =
            new TextButton("Apply", game.getSkin(), "menu");
        applyButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                game.setFloatPreference("sound", soundSlider.getValue());
                game.setFloatPreference("music", musicSlider.getValue());
                game.flushPreferences();
                game.updateMusic();
            }
        });
        table.add(applyButton).pad(1f);

        table.row();

        final TextButton backButton =
            new TextButton("Back", game.getSkin(), "menu");
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                game.setScreen(fromScreen);
            }
        });
        table.add(backButton).pad(1f);

        stage.addActor(table);
    }

    public void setFromScreen(final Screen fromScreen) {
        this.fromScreen = fromScreen;
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
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
