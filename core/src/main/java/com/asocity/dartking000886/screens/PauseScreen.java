package com.asocity.dartking000886.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.asocity.dartking000886.Constants;
import com.asocity.dartking000886.MainGame;
import com.asocity.dartking000886.UiFactory;

public class PauseScreen implements Screen {

    private static final String BG = "backgrounds/menu/Bright_City1.png";

    private final MainGame game;
    private final Screen   previousScreen;
    private final int      venueId;

    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Stage    stage;

    public PauseScreen(MainGame game, Screen previousScreen, int venueId) {
        this.game           = game;
        this.previousScreen = previousScreen;
        this.venueId        = venueId;

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

        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, null);
        Label pauseLabel = new Label("PAUSED", titleStyle);
        pauseLabel.setPosition(cx - pauseLabel.getPrefWidth() / 2f, 650f);
        stage.addActor(pauseLabel);

        TextButton.TextButtonStyle rectStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);

        // RESUME
        TextButton resumeBtn = UiFactory.makeButton("RESUME", rectStyle, 280f, 64f);
        resumeBtn.setPosition(cx - 140f, 490f);
        resumeBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(previousScreen);
            }
        });
        stage.addActor(resumeBtn);

        // RESTART
        TextButton restartBtn = UiFactory.makeButton("RESTART", rectStyle, 280f, 56f);
        restartBtn.setPosition(cx - 140f, 414f);
        restartBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new GameScreen(game, venueId));
            }
        });
        stage.addActor(restartBtn);

        // MAIN MENU
        TextButton menuBtn = UiFactory.makeButton("MAIN MENU", rectStyle, 280f, 56f);
        menuBtn.setPosition(cx - 140f, 340f);
        menuBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playBack();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(menuBtn);

        stage.addListener(new InputListener() {
            @Override public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.BACK) {
                    game.setScreen(previousScreen);
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

    private void playBack() {
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_button_back.ogg", Sound.class).play(1.0f);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(game.manager.get(BG, Texture.class),
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
