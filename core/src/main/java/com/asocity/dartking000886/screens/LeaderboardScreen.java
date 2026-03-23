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

public class LeaderboardScreen implements Screen {

    private static final String BG = "ui/leaderboard.png";

    // Row top-Y values (Figma) → libgdxY = 854 - topY - 52
    private static final float[] ROW_Y = {
        582f, 524f, 466f, 408f, 350f, 292f, 234f, 176f
    };

    private final MainGame game;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Stage    stage;

    // -----------------------------------------------------------------------
    // Static helper — call from GameOverScreen to persist a score
    // -----------------------------------------------------------------------

    /** Insert {@code score} into the persistent top-{@link Constants#LEADERBOARD_SIZE} list. */
    public static void addScore(int score) {
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);

        // Load current scores into a sorted array (descending)
        int size = Constants.LEADERBOARD_SIZE;
        int[] scores = new int[size];
        for (int i = 0; i < size; i++) {
            scores[i] = prefs.getInteger(Constants.PREF_SCORE_PREFIX + i, 0);
        }

        // Find insertion position
        int insertAt = size; // default: no insertion if score is too low
        for (int i = 0; i < size; i++) {
            if (score > scores[i]) {
                insertAt = i;
                break;
            }
        }

        if (insertAt < size) {
            // Shift scores down to make room
            for (int i = size - 1; i > insertAt; i--) {
                prefs.putInteger(Constants.PREF_SCORE_PREFIX + i,
                                 prefs.getInteger(Constants.PREF_SCORE_PREFIX + (i - 1), 0));
            }
            prefs.putInteger(Constants.PREF_SCORE_PREFIX + insertAt, score);
            prefs.flush();
        }
    }

    // -----------------------------------------------------------------------

    public LeaderboardScreen(MainGame game) {
        this.game = game;

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
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);

        // Title — libgdxY = 854-50-56 = 748
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, null);
        Label titleLabel = new Label("LEADERBOARD", titleStyle);
        titleLabel.setPosition(cx - titleLabel.getPrefWidth() / 2f, 748f);
        stage.addActor(titleLabel);

        // Header row — libgdxY = 854-170-40 = 644
        Label.LabelStyle bodyStyle  = new Label.LabelStyle(game.fontBody, null);
        Label.LabelStyle smallStyle = new Label.LabelStyle(game.fontSmall, null);

        Label headerLabel = new Label("RANK        SCORE", bodyStyle);
        headerLabel.setPosition(cx - headerLabel.getPrefWidth() / 2f, 644f);
        stage.addActor(headerLabel);

        // Score rows
        int size = Constants.LEADERBOARD_SIZE;
        for (int i = 0; i < size; i++) {
            int rowScore = prefs.getInteger(Constants.PREF_SCORE_PREFIX + i, 0);
            String rankText = (i + 1) + ".";
            String scoreText = rowScore > 0 ? String.valueOf(rowScore) : "---";

            // Rank — left-aligned at x=40
            Label rankLabel = new Label(rankText, smallStyle);
            rankLabel.setPosition(40f, ROW_Y[i]);
            stage.addActor(rankLabel);

            // Score — centered
            Label scoreLabel = new Label(scoreText, smallStyle);
            scoreLabel.setPosition(cx - scoreLabel.getPrefWidth() / 2f, ROW_Y[i]);
            stage.addActor(scoreLabel);
        }

        // BACK button — libgdxY = 854-780-48 = 26
        TextButton.TextButtonStyle rectStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);
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
