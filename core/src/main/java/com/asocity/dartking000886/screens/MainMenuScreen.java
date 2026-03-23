package com.asocity.dartking000886.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
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

public class MainMenuScreen implements Screen {

    private static final String BG = "ui/main_menu.png";

    private final MainGame    game;
    private final OrthographicCamera camera;
    private final Viewport    viewport;
    private final Stage       stage;

    public MainMenuScreen(MainGame game) {
        this.game = game;

        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);

        // Ensure background is loaded
        if (!game.manager.isLoaded(BG)) {
            game.manager.load(BG, Texture.class);
            game.manager.finishLoading();
        }

        buildUI();
        Gdx.input.setInputProcessor(stage);
    }

    private void buildUI() {
        float cx = Constants.WORLD_WIDTH / 2f;

        // Title label
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, null);
        Label titleLabel = new Label("DART KING", titleStyle);
        titleLabel.setPosition(cx - titleLabel.getPrefWidth() / 2f, 684f);
        stage.addActor(titleLabel);

        TextButton.TextButtonStyle rectStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);

        // PLAY  — libgdxY = 854-340-64 = 450
        TextButton playBtn = UiFactory.makeButton("PLAY", rectStyle, 280f, 64f);
        playBtn.setPosition(cx - 140f, 450f);
        playBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new VenueSelectScreen(game));
            }
        });
        stage.addActor(playBtn);

        // VENUE SELECT  — libgdxY = 378
        TextButton venueBtn = UiFactory.makeButton("VENUE SELECT", rectStyle, 280f, 56f);
        venueBtn.setPosition(cx - 140f, 378f);
        venueBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new VenueSelectScreen(game));
            }
        });
        stage.addActor(venueBtn);

        // LEADERBOARD  — libgdxY = 308
        TextButton lbBtn = UiFactory.makeButton("LEADERBOARD", rectStyle, 280f, 56f);
        lbBtn.setPosition(cx - 140f, 308f);
        lbBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new LeaderboardScreen(game));
            }
        });
        stage.addActor(lbBtn);

        // SHOP  — libgdxY = 238
        TextButton shopBtn = UiFactory.makeButton("SHOP", rectStyle, 280f, 56f);
        shopBtn.setPosition(cx - 140f, 238f);
        shopBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new ShopScreen(game));
            }
        });
        stage.addActor(shopBtn);

        // SETTINGS  — libgdxY = 168
        TextButton settingsBtn = UiFactory.makeButton("SETTINGS", rectStyle, 280f, 56f);
        settingsBtn.setPosition(cx - 140f, 168f);
        settingsBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new SettingsScreen(game));
            }
        });
        stage.addActor(settingsBtn);

        // HOW TO PLAY  — libgdxY = 106
        TextButton howBtn = UiFactory.makeButton("HOW TO PLAY", rectStyle, 280f, 48f);
        howBtn.setPosition(cx - 140f, 106f);
        howBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new HowToPlayScreen(game));
            }
        });
        stage.addActor(howBtn);

        // Back key listener
        stage.addListener(new InputListener() {
            @Override public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.BACK) {
                    Gdx.app.exit();
                    return true;
                }
                return false;
            }
        });
    }

    private void playClick() {
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_button_click.ogg", Sound.class).play(1.0f);
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
