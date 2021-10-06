package com.wgsoft.game.unstableconnection.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.wgsoft.game.unstableconnection.Constants;
import com.wgsoft.game.unstableconnection.MyGdxGame;
import com.wgsoft.game.unstableconnection.actor.game.DeviceGroup;

public final class GameScreen extends ScreenAdapter {
    private final MyGdxGame game;

    private final Stage stage;

    private final DeviceGroup phoneGroup;
    private final DeviceGroup tabletGroup;
    private final DeviceGroup tvGroup;
    private final DeviceGroup pcGroup;
    private final DeviceGroup shieldGroup;

    private final CheckBox tvShieldCheckBox;
    private final CheckBox pcShieldCheckBox;
    private final CheckBox phoneShieldCheckBox;
    private final CheckBox tabletShieldCheckBox;

    private float time;

    public GameScreen(final MyGdxGame game) {
        this.game = game;

        stage = new Stage(
            new FitViewport(Constants.WIDTH, Constants.HEIGHT),
            game.getSpriteBatch()
        );

        final Image backgroundImage =
            new Image(game.getSkin(), "main-background");
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        final Button phoneButton = new Button(game.getSkin(), "highlight");
        phoneButton.setBounds(7f, 54f, 12f, 15f);
        stage.addActor(phoneButton);

        final Button tabletButton = new Button(game.getSkin(), "highlight");
        tabletButton.setBounds(2f, 34f, 23f, 17f);
        stage.addActor(tabletButton);

        final Button tvButton = new Button(game.getSkin(), "highlight");
        tvButton.setBounds(29f, 56f, 42f, 14f);
        stage.addActor(tvButton);

        final Button pcButton = new Button(game.getSkin(), "highlight");
        pcButton.setBounds(30f, 35f, 39f, 18f);
        stage.addActor(pcButton);

        final Button shieldButton =
            new Button(game.getSkin(), "highlight");
        shieldButton.setBounds(82f, 42f, 18f, 28f);
        stage.addActor(shieldButton);

        final Label timeLabel =
            new Label("", game.getSkin(), "press-start-medium");
        timeLabel.setFillParent(true);
        timeLabel.setTouchable(Touchable.disabled);
        timeLabel.setAlignment(Align.bottom | Align.center);
        timeLabel.addAction(Actions.forever(new Action() {
            @Override
            public boolean act(final float delta) {
                time -= delta;
                if(time >= 0f) {
                    int minutes = (int)time / 60;
                    int seconds = (int)time % 60;
                    timeLabel.setText(fixTwo(minutes) + ":" + fixTwo(seconds));
                } else {
                    game.setGameOverScreen();
                }
                return true;
            }
        }));
        stage.addActor(timeLabel);

        final Button pauseButton = new Button(game.getSkin(), "pause");
        pauseButton.setBounds(0f, 0f, 8f, 8f);
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                game.setPauseScreen();
            }
        });
        stage.addActor(pauseButton);

        phoneGroup = new DeviceGroup(
            game, "phone", phoneButton, 35f, 10f, 30f, 53f);
        stage.addActor(phoneGroup);

        tabletGroup = new DeviceGroup(
            game, "tablet", tabletButton, 20f, 14f, 58f, 42f);
        stage.addActor(tabletGroup);

        tvGroup = new DeviceGroup(game, "tv", tvButton, 6f, 10f, 88f, 55f);
        stage.addActor(tvGroup);

        pcGroup = new DeviceGroup(game, "pc", pcButton, 6f, 14f, 88f, 41f);
        stage.addActor(pcGroup);

        shieldGroup = new DeviceGroup(game, "shield", shieldButton);

        tvShieldCheckBox = createShieldCheckBox(5f, tvGroup);
        shieldGroup.addActor(tvShieldCheckBox);

        pcShieldCheckBox = createShieldCheckBox(21f, pcGroup);
        shieldGroup.addActor(pcShieldCheckBox);

        phoneShieldCheckBox =
            createShieldCheckBox(43f, phoneGroup);
        shieldGroup.addActor(phoneShieldCheckBox);

        tabletShieldCheckBox =
            createShieldCheckBox(75f, tabletGroup);
        shieldGroup.addActor(tabletShieldCheckBox);

        stage.addActor(shieldGroup);
    }

    private CheckBox createShieldCheckBox(
        final float x,
        final DeviceGroup group
    ) {
        final CheckBox checkBox = new CheckBox(null, game.getSkin(), "lever");
        checkBox.setBounds(x, 25f, 12f, 18f);
        checkBox.setChecked(true);
        checkBox.addAction(Actions.forever(Actions.run(() -> {
            if(!group.power) {
                checkBox.setChecked(false);
            }
        })));
        checkBox.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                group.power = checkBox.isChecked();
                if(checkBox.isChecked()) {
                    game.playLeverOnSound();
                } else {
                    game.playLeverOffSound();
                }
            }
        });
        return checkBox;
    }

    private String fixTwo(final int number) {
        if(number == 0) {
            return "00";
        } else if(number < 10) {
            return "0" + number;
        } else {
            return String.valueOf(number);
        }
    }

    public void reset() {
        time = 7f * 60f;

        phoneGroup.reset();
        tabletGroup.reset();
        tvGroup.reset();
        pcGroup.reset();
        shieldGroup.reset();

        tvShieldCheckBox.setChecked(true);
        pcShieldCheckBox.setChecked(true);
        phoneShieldCheckBox.setChecked(true);
        tabletShieldCheckBox.setChecked(true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(final float delta) {
        stage.act(delta);
        stage.draw();

        if(phoneGroup.isCompleted()
            && tabletGroup.isCompleted()
            && tvGroup.isCompleted()
            && pcGroup.isCompleted()
        ) {
            game.setVictoryScreen();
        }
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
