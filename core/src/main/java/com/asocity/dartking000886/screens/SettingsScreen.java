package com.asocity.dartking000886.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.asocity.dartking000886.Constants;
import com.asocity.dartking000886.MainGame;
import com.asocity.dartking000886.UiFactory;

public class SettingsScreen implements Screen {

    private static final String BG = "ui/settings.png";

    private final MainGame game;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Stage    stage;

    private final Preferences prefs;

    // Toggle button references so we can update their labels
    private TextButton sfxToggleBtn;
    private TextButton musicToggleBtn;

    public SettingsScreen(MainGame game) {
        this.game  = game;
        this.prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);

        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);

        if (!game.manager.isLoaded(BG)) {
            game.manager.load(BG, Texture.class);
            game.manager.finishLoading();
        }

        buildUI();
        Gdx.input.setInputProcessor(stage);
    }

    private void buildUI() {
        float cx = Constants.WORLD_WIDTH / 2f;

        // Title label — libgdxY = 854-50-56 = 748
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, null);
        Label titleLabel = new Label("SETTINGS", titleStyle);
        titleLabel.setPosition(cx - titleLabel.getPrefWidth() / 2f, 748f);
        stage.addActor(titleLabel);

        TextButton.TextButtonStyle rectStyle  = UiFactory.makeRectStyle(game.manager, game.fontBody);
        Label.LabelStyle           bodyStyle  = new Label.LabelStyle(game.fontBody, null);

        // ----------------------------------------------------------------
        // SFX ROW — libgdxY = 854-200-48 = 606
        // ----------------------------------------------------------------
        Label sfxLabel = new Label("SFX", bodyStyle);
        sfxLabel.setPosition(60f, 606f);
        sfxLabel.setSize(200f, 48f);
        stage.addActor(sfxLabel);

        sfxToggleBtn = UiFactory.makeButton(
                game.sfxEnabled ? "ON" : "OFF", rectStyle, 100f, 48f);
        // right@60 → x = 480 - 60 - 100 = 320
        sfxToggleBtn.setPosition(320f, 606f);
        sfxToggleBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                game.sfxEnabled = !game.sfxEnabled;
                prefs.putBoolean(Constants.PREF_SFX, game.sfxEnabled);
                prefs.flush();
                sfxToggleBtn.setText(game.sfxEnabled ? "ON" : "OFF");
                if (game.sfxEnabled)
                    game.manager.get("sounds/sfx/sfx_toggle.ogg", Sound.class).play(1.0f);
            }
        });
        stage.addActor(sfxToggleBtn);

        // ----------------------------------------------------------------
        // MUSIC ROW — libgdxY = 854-270-48 = 536
        // ----------------------------------------------------------------
        Label musicLabel = new Label("MUSIC", bodyStyle);
        musicLabel.setPosition(60f, 536f);
        musicLabel.setSize(200f, 48f);
        stage.addActor(musicLabel);

        musicToggleBtn = UiFactory.makeButton(
                game.musicEnabled ? "ON" : "OFF", rectStyle, 100f, 48f);
        musicToggleBtn.setPosition(320f, 536f);
        musicToggleBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                game.musicEnabled = !game.musicEnabled;
                prefs.putBoolean(Constants.PREF_MUSIC, game.musicEnabled);
                prefs.flush();
                musicToggleBtn.setText(game.musicEnabled ? "ON" : "OFF");
                if (game.currentMusic != null) {
                    if (game.musicEnabled) game.currentMusic.play();
                    else                   game.currentMusic.pause();
                }
                if (game.sfxEnabled)
                    game.manager.get("sounds/sfx/sfx_toggle.ogg", Sound.class).play(1.0f);
            }
        });
        stage.addActor(musicToggleBtn);

        // ----------------------------------------------------------------
        // BACK button — libgdxY = 854-780-48 = 26
        // ----------------------------------------------------------------
        TextButton backBtn = UiFactory.makeButton("BACK", rectStyle, 120f, 48f);
        backBtn.setPosition(20f, 26f);
        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playBack();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(backBtn);

        // Android back key
        stage.addListener(new InputListener() {
            @Override public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.BACK) {
                    playBack();
                    game.setScreen(new MainMenuScreen(game));
                    return true;
                }
                return false;
            }
        });
    }

    private void playBack() {
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_button_back.ogg", Sound.class).play(1.0f);
    }

    // -----------------------------------------------------------------------

    @Override
    public void show() {
        game.playMusic("sounds/music/music_menu.ogg");
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(
            game.manager.get(BG, Texture.class),
            0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
