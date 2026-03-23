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

public class GameOverScreen implements Screen {

    private static final String BG = "ui/game_over.png";

    private final MainGame game;
    private final int      score;
    private final int      extra;   // venue ID — passed for context, not displayed

    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Stage    stage;

    public GameOverScreen(MainGame game, int score, int extra) {
        this.game  = game;
        this.score = score;
        this.extra = extra;

        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);

        if (!game.manager.isLoaded(BG)) {
            game.manager.load(BG, Texture.class);
            game.manager.finishLoading();
        }

        // Persist score to leaderboard + personal best
        LeaderboardScreen.addScore(score);
        updateHighScore();

        buildUI();
        Gdx.input.setInputProcessor(stage);
    }

    private void updateHighScore() {
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        int best = prefs.getInteger(Constants.PREF_HIGH_SCORE, 0);
        if (score > best) {
            prefs.putInteger(Constants.PREF_HIGH_SCORE, score);
            prefs.flush();
        }
    }

    private void buildUI() {
        float cx = Constants.WORLD_WIDTH / 2f;

        // Personal best
        int best = Gdx.app.getPreferences(Constants.PREFS_NAME)
                         .getInteger(Constants.PREF_HIGH_SCORE, 0);

        // GAME OVER label — libgdxY = 854-120-72 = 662
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, null);
        Label gameOverLabel = new Label("GAME OVER", titleStyle);
        gameOverLabel.setPosition(cx - gameOverLabel.getPrefWidth() / 2f, 662f);
        stage.addActor(gameOverLabel);

        // SCORE label — libgdxY = 854-240-52 = 562
        Label.LabelStyle scoreStyle = new Label.LabelStyle(game.fontScore, null);
        Label scoreLabel = new Label("SCORE: " + score, scoreStyle);
        scoreLabel.setPosition(cx - scoreLabel.getPrefWidth() / 2f, 562f);
        stage.addActor(scoreLabel);

        // BEST label — libgdxY = 854-310-44 = 500
        Label.LabelStyle bodyStyle = new Label.LabelStyle(game.fontBody, null);
        Label bestLabel = new Label("BEST: " + best, bodyStyle);
        bestLabel.setPosition(cx - bestLabel.getPrefWidth() / 2f, 500f);
        stage.addActor(bestLabel);

        TextButton.TextButtonStyle rectStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);

        // RETRY — libgdxY = 854-460-64 = 330
        TextButton retryBtn = UiFactory.makeButton("RETRY", rectStyle, 280f, 64f);
        retryBtn.setPosition(cx - 140f, 330f);
        retryBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new GameScreen(game, Constants.VENUE_LOCAL_PUB));   // NEW instance every time
            }
        });
        stage.addActor(retryBtn);

        // MENU — libgdxY = 854-540-56 = 258
        TextButton menuBtn = UiFactory.makeButton("MENU", rectStyle, 280f, 56f);
        menuBtn.setPosition(cx - 140f, 258f);
        menuBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(menuBtn);

        // LEADERBOARD — libgdxY = 854-620-48 = 186
        TextButton lbBtn = UiFactory.makeButton("LEADERBOARD", rectStyle, 280f, 48f);
        lbBtn.setPosition(cx - 140f, 186f);
        lbBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new LeaderboardScreen(game));
            }
        });
        stage.addActor(lbBtn);

        // Android back key
        stage.addListener(new InputListener() {
            @Override public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.BACK) {
                    game.setScreen(new MainMenuScreen(game));
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
        game.playMusicOnce("sounds/music/music_game_over.ogg");
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_game_over.ogg", Sound.class).play(1.0f);
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
