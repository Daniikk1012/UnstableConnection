package com.wgsoft.game.unstableconnection.actor.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public final class DeviceGroup extends WidgetGroup {
    private final Skin skin;

    private final Image blackImage;

    private Image wifiImage;
    private Button refreshButton;
    private ProgressBar downloadBar;
    private Image pauseImage;

    public boolean power = true;
    private boolean mistaken;
    private boolean completed;

    public DeviceGroup(final Skin skin, final String name, final Button button)
    {
        this.skin = skin;

        setFillParent(true);
        setVisible(false);
        setTouchable(Touchable.childrenOnly);

        blackImage = new Image(skin, "black");
        blackImage.setFillParent(true);

        final Button blurButton = new Button(skin, "blur");
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
            }
        });
        addActor(blurButton);

        final Image image = new Image(skin, name);
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
            }
        });
    }

    public DeviceGroup(
        final Skin skin,
        final String name,
        final Button button,
        final float x,
        final float y,
        final float width,
        final float height
    ) {
        this(skin, name, button);

        final Stack stack = new Stack();
        stack.setBounds(x, y, width, height);

        final Table table = new Table();
        table.top();

        wifiImage = new Image(skin, "wifi");
        table.add(wifiImage).size(7f, 8f);
refreshButton = new Button(skin, "refresh");
        refreshButton.setVisible(false);
        table.add(refreshButton).size(10f);

        table.add().expandX();

        final ProgressBar batteryBar =
            new ProgressBar(0f, 10f, 1f, false, skin, "battery");
        batteryBar.setValue(batteryBar.getMaxValue());
        table.add(batteryBar).size(13f, 8f);

        stack.add(table);

        pauseImage = new Image(skin, "pause");
        refreshButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                wifiImage.setDrawable(skin, "wifi");
                refreshButton.setVisible(false);
                pauseImage.setVisible(false);
            }
        });

        downloadBar = new ProgressBar(0f, 100f, 1f, false, skin, "download");
        downloadBar.addAction(
            Actions.forever(Actions.delay(3f, Actions.run(() -> {
                if(!completed) {
                    if(!pauseImage.isVisible()) {
                        mistaken = false;
                        if(MathUtils.randomBoolean(0.01f)) {
                            wifiImage.setDrawable(skin, "wifi-lost");
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

        final Image offImage = new Image(skin, "black");
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
            wifiImage.setDrawable(skin, "wifi");
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
