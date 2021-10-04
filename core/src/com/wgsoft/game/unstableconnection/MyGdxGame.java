package com.wgsoft.game.unstableconnection;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.wgsoft.game.unstableconnection.screen.GameOverScreen;
import com.wgsoft.game.unstableconnection.screen.GameScreen;
import com.wgsoft.game.unstableconnection.screen.MenuScreen;

public final class MyGdxGame extends Game {
    private SpriteBatch spriteBatch;

    private Skin skin;

    private Music music;

    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private GameOverScreen gameOverScreen;

    @Override
    public void create () {
        spriteBatch = new SpriteBatch();

        skin = new Skin(Gdx.files.internal("img/skin.json"));

        for(final BitmapFont font: skin.getAll(BitmapFont.class).values()) {
            font.setUseIntegerPositions(false);
        }

        skin.getDrawable("battery-filler").setMinHeight(6f);
        skin.getDrawable("download-filler").setMinHeight(6f);

        if(Gdx.app.getType() != ApplicationType.WebGL) {
            initSounds();
        }

        menuScreen = new MenuScreen(this);
        gameScreen = new GameScreen(this);
        gameOverScreen = new GameOverScreen(this);

        setMenuScreen();
    }

    public void initSounds() {
        if(music == null) {
            music = Gdx.audio.newMusic(Gdx.files.internal("snd/music.mp3"));
            music.setLooping(true);
            music.play();
        }
    }

    @Override
    public void render () {
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        super.render();
    }

    @Override
    public void dispose () {
        super.dispose();

        spriteBatch.dispose();

        skin.dispose();

        if(music != null) {
            music.dispose();
        }

        menuScreen.dispose();
        gameScreen.dispose();
        gameOverScreen.dispose();
    }

    public void setMenuScreen() {
        setScreen(menuScreen);
    }

    public void setGameScreen() {
        setScreen(gameScreen);
    }

    public void setGameOverScreen() {
        gameOverScreen.setGameOver();
        setScreen(gameOverScreen);
    }

    public void setVictoryScreen() {
        gameOverScreen.setVictory();
        setScreen(gameOverScreen);
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public Skin getSkin() {
        return skin;
    }
}
