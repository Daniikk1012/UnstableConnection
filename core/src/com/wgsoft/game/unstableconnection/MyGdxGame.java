package com.wgsoft.game.unstableconnection;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.wgsoft.game.unstableconnection.screen.GameOverScreen;
import com.wgsoft.game.unstableconnection.screen.GameScreen;
import com.wgsoft.game.unstableconnection.screen.MenuScreen;
import com.wgsoft.game.unstableconnection.screen.PauseScreen;
import com.wgsoft.game.unstableconnection.screen.SettingsScreen;

public final class MyGdxGame extends Game {
    private SpriteBatch spriteBatch;

    private Skin skin;

    private Preferences preferences;

    private Music music;

    private Sound leverOnSound;
    private Sound leverOffSound;
    private Sound wifiOnSound;
    private Sound shieldOpenSound;
    private Sound shieldCloseSound;

    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private GameOverScreen gameOverScreen;
    private SettingsScreen settingsScreen;
    private PauseScreen pauseScreen;

    @Override
    public void create () {
        spriteBatch = new SpriteBatch();

        skin = new Skin(Gdx.files.internal("img/skin.json"));

        preferences =
            Gdx.app.getPreferences("com.wgsoft.game.unstableconnection");

        for(final BitmapFont font: skin.getAll(BitmapFont.class).values()) {
            font.setUseIntegerPositions(false);
        }

        skin.getDrawable("battery-filler").setMinHeight(6f);
        skin.getDrawable("download-filler").setMinHeight(6f);
        skin.getDrawable("slider-filler").setMinHeight(6f);

        if(Gdx.app.getType() != ApplicationType.WebGL) {
            initSounds();
        }

        menuScreen = new MenuScreen(this);
        gameScreen = new GameScreen(this);
        gameOverScreen = new GameOverScreen(this);
        settingsScreen = new SettingsScreen(this);
        pauseScreen = new PauseScreen(this);

        setMenuScreen();
    }

    public void initSounds() {
        if(music == null) {
            music = Gdx.audio.newMusic(Gdx.files.internal("snd/music.mp3"));
            music.setLooping(true);
            updateMusic();
            music.play();
        }

        if(leverOnSound == null) {
            leverOnSound =
                Gdx.audio.newSound(Gdx.files.internal("snd/lever-on.wav"));
        }
        if(leverOffSound == null) {
            leverOffSound =
                Gdx.audio.newSound(Gdx.files.internal("snd/lever-off.wav"));
        }
        if(wifiOnSound == null) {
            wifiOnSound =
                Gdx.audio.newSound(Gdx.files.internal("snd/wifi-on.wav"));
        }
        if(shieldOpenSound == null) {
            shieldOpenSound =
                Gdx.audio.newSound(Gdx.files.internal("snd/shield-open.wav"));
        }
        if(shieldCloseSound == null) {
            shieldCloseSound =
                Gdx.audio.newSound(Gdx.files.internal("snd/shield-close.wav"));
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

        if(leverOnSound != null) {
            leverOnSound.dispose();
        }
        if(leverOffSound != null) {
            leverOffSound.dispose();
        }
        if(wifiOnSound != null) {
            wifiOnSound.dispose();
        }
        if(shieldOpenSound != null) {
            shieldOpenSound.dispose();
        }
        if(shieldCloseSound != null) {
            shieldCloseSound.dispose();
        }

        menuScreen.dispose();
        gameScreen.dispose();
        gameOverScreen.dispose();
        settingsScreen.dispose();
        pauseScreen.dispose();
    }

    public void setMenuScreen() {
        setScreen(menuScreen);
    }

    public void setGameScreen() {
        if(getScreen() == menuScreen) {
            gameScreen.reset();
        }
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

    public void setSettingsScreen() {
        settingsScreen.setFromScreen(getScreen());
        setScreen(settingsScreen);
    }

    public void setPauseScreen() {
        setScreen(pauseScreen);
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public Skin getSkin() {
        return skin;
    }

    public void setFloatPreference(final String name, final float value) {
        preferences.putFloat(name, value);
    }

    public float getFloatPreference(final String name, final float def) {
        return preferences.getFloat(name, def);
    }
    
    public void flushPreferences() {
        preferences.flush();
    }

    public void updateMusic() {
        music.setVolume(
            getFloatPreference("music", Constants.MUSIC_DEFAULT) / 100f
        );
    }

    public void playLeverOnSound() {
        leverOnSound.play(
            getFloatPreference("sound", Constants.SOUND_DEFAULT) / 100f
        );
    }

    public void playLeverOffSound() {
        leverOffSound.play(
            getFloatPreference("sound", Constants.SOUND_DEFAULT) / 100f
        );
    }

    public void playWifiOnSound() {
        wifiOnSound.play(
            getFloatPreference("sound", Constants.SOUND_DEFAULT) / 100f
        );
    }

    public void playShieldOpenSound() {
        shieldOpenSound.play(
            getFloatPreference("sound", Constants.SOUND_DEFAULT) / 100f
        );
    }

    public void playShieldCloseSound() {
        shieldCloseSound.play(
            getFloatPreference("sound", Constants.SOUND_DEFAULT) / 100f
        );
    }
}
