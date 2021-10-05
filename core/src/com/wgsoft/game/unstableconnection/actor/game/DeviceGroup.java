package com.wgsoft.game.unstableconnection.actor.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.wgsoft.game.unstableconnection.MyGdxGame;

public final class DeviceGroup extends WidgetGroup {
    private final MyGdxGame game;

    private final Image blackImage;

    private Image wifiImage;
    private Button refreshButton;
    private ProgressBar downloadBar;
    private Image pauseImage;

    public boolean power = true;
    private boolean mistaken;
    private boolean completed;

    public DeviceGroup(
        final MyGdxGame game,
        final String name,
        final Button button
    ) {
        this.game = game;

        setFillParent(true);
        setVisible(false);
        setTouchable(Touchable.childrenOnly);

        blackImage = new Image(game.getSkin(), "black");
        blackImage.setFillParent(true);

        final Button blurButton = new Button(game.getSkin(), "blur");
        blurButton.setFillParent(true);
        blurButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getStage().addActor(blackImage);
                blackImage.addAction(Actions.sequence(
                    Actions.alpha(0f),
                    Actions.alpha(1f, 0.25f),
                    Actions.run(() -> setVisible(false)),
                    Actions.alpha(0f, 0.25f),
                    Actions.removeActor()
                ));
                if(name.equals("shield")) {
                    game.playShieldCloseSound();
                }
            }
        });
        addActor(blurButton);

        final Image image = new Image(game.getSkin(), name);
        image.setFillParent(true);
        image.setTouchable(Touchable.disabled);
        addActor(image);

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getStage().addActor(blackImage);
                blackImage.addAction(Actions.sequence(
                    Actions.alpha(0f),
                    Actions.alpha(1f, 0.25f),
                    Actions.run(() -> setVisible(true)),
                    Actions.alpha(0f, 0.25f),
                    Actions.removeActor()
                ));
                if(name.equals("shield")) {
                    game.playShieldOpenSound();
                }
            }
        });
    }

    public DeviceGroup(
        final MyGdxGame game,
        final String name,
        final Button button,
        final float x,
        final float y,
        final float width,
        final float height
    ) {
        this(game, name, button);

        final Stack stack = new Stack();
        stack.setBounds(x, y, width, height);

        final Table table = new Table();
        table.top();

        wifiImage = new Image(game.getSkin(), "wifi");
        table.add(wifiImage).size(7f, 8f);
        refreshButton = new Button(game.getSkin(), "refresh");
        refreshButton.setVisible(false);
        table.add(refreshButton).size(10f);

        table.add().expandX();

        final ProgressBar batteryBar =
            new ProgressBar(0f, 10f, 1f, false, game.getSkin(), "battery");
        batteryBar.setValue(batteryBar.getMaxValue());
        table.add(batteryBar).size(13f, 8f);

        stack.add(table);

        pauseImage = new Image(game.getSkin(), "pause");
        refreshButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playWifiOnSound();
                wifiImage.setDrawable(game.getSkin(), "wifi");
                refreshButton.setVisible(false);
                pauseImage.setVisible(false);
            }
        });

        downloadBar =
            new ProgressBar(0f, 100f, 1f, false, game.getSkin(), "download");
        downloadBar.addAction(
            Actions.forever(Actions.delay(3f, Actions.run(() -> {
                if(!completed) {
                    if(!pauseImage.isVisible()) {
                        mistaken = false;
                        if(MathUtils.randomBoolean(0.01f)) {
                            wifiImage.setDrawable(game.getSkin(), "wifi-lost");
                            refreshButton.setVisible(true);
                            pauseImage.setVisible(true);
                        } else {
                            downloadBar.setValue(downloadBar.getValue() + 1f);
                            if(downloadBar.getValue()
                                == downloadBar.getMaxValue()
                            ) {
                                completed = true;
                            }
                        }
                    } else {
                        if(!mistaken) {
                            mistaken = true;
                        } else {
                            downloadBar.setValue(downloadBar.getMinValue());
                        }
                    }
                }
            })))
        );

        stack.add(new Container<>(downloadBar).size(30f, 10f));

        stack.add(new Container<>(pauseImage).size(30f, 20f));
        pauseImage.setVisible(false);

        final Image offImage = new Image(game.getSkin(), "black");
        offImage.setVisible(false);
        batteryBar.addAction(
            Actions.forever(Actions.delay(1f, Actions.run(() -> {
                if(power) {
                    if(MathUtils.randomBoolean(0.01f)) {
                        power = false;
                    } else {
                        batteryBar.setValue(batteryBar.getMaxValue());
                        offImage.setVisible(false);
                    }
                } else {
                    batteryBar.setValue(batteryBar.getValue() - 1f);
                    if(batteryBar.getValue() == batteryBar.getMinValue()) {
                        offImage.setVisible(true);
                    }
                }
            })))
        );
        stack.add(offImage);

        addActor(stack);
    }

    public void reset() {
        setVisible(false);

        if(wifiImage != null) {
            wifiImage.setDrawable(game.getSkin(), "wifi");
        }
        if(wifiImage != null) {
            refreshButton.setVisible(false);
        }
        if(wifiImage != null) {
            downloadBar.setValue(downloadBar.getMinValue());
        }
        if(wifiImage != null) {
            pauseImage.setVisible(false);
        }

        power = true;
        mistaken = false;
        completed = false;

        blackImage.clearActions();
        blackImage.remove();
    }

    public boolean isCompleted() {
        return completed;
    }
}
