package com.asocity.dartking000886.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.asocity.dartking000886.Constants;
import com.asocity.dartking000886.MainGame;
import com.asocity.dartking000886.UiFactory;

public class GameScreen implements Screen {

    private static final String[] GAME_BGS = {
        "backgrounds/game/game_background_1_game_background_1.png",
        "backgrounds/game/game_background_2_game_background_2.png",
        "backgrounds/game/game_background_3_game_background_3.png"
    };
    private static final String SPLASH = "sprites/object/paint_splat_a.png";

    private final MainGame game;
    private final int venueId;

    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Stage stage;
    private final ShapeRenderer shaper;

    private enum Phase { AIMING, FLYING }
    private Phase phase = Phase.AIMING;

    private float wobbleTime  = 0f;
    private float wobbleSpeed;
    private float aimX;
    private float power    = 0f;
    private boolean charging = false;

    private float flyTimer = 0f;
    private float dartStartX, dartStartY, dartEndX, dartEndY;

    private int dartsLeft  = Constants.DARTS_PER_ROUND;
    private int roundsLeft = Constants.ROUNDS_PER_GAME;
    private int score      = 0;
    private int coins      = 0;
    private int combo      = 0;

    private final Array<Vector2> impacts = new Array<>();

    private Label scoreLbl, dartsLbl, roundsLbl, comboLbl;

    private static final float PB_X = 20f;
    private static final float PB_Y = 100f;
    private static final float PB_W = 20f;
    private static final float PB_H = 200f;

    public GameScreen(MainGame game, int venueId) {
        this.game    = game;
        this.venueId = venueId;

        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        coins = prefs.getInteger(Constants.PREF_COINS, 0);

        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);
        shaper   = new ShapeRenderer();

        wobbleSpeed = venueId == Constants.VENUE_LOCAL_PUB    ? Constants.WOBBLE_SPEED_BASE
                    : venueId == Constants.VENUE_TOURNAMENT    ? Constants.WOBBLE_SPEED_VENUE2
                    :                                            Constants.WOBBLE_SPEED_VENUE3;
        aimX = Constants.BOARD_CENTER_X;

        String bg = GAME_BGS[Math.min(venueId, GAME_BGS.length - 1)];
        if (!game.manager.isLoaded(bg)) { game.manager.load(bg, Texture.class); }
        if (!game.manager.isLoaded(SPLASH)) { game.manager.load(SPLASH, Texture.class); }
        game.manager.finishLoading();

        buildHUD();
        registerInput();
    }

    private void buildHUD() {
        float cx = Constants.WORLD_WIDTH / 2f;

        scoreLbl  = new Label("0",  new Label.LabelStyle(game.fontScore, null));
        dartsLbl  = new Label("",   new Label.LabelStyle(game.fontBody,  null));
        roundsLbl = new Label("",   new Label.LabelStyle(game.fontBody,  null));
        comboLbl  = new Label("",   new Label.LabelStyle(game.fontBody,  null));

        scoreLbl.setPosition(cx - 60f, 760f);
        dartsLbl.setPosition(cx - 60f, 710f);
        roundsLbl.setPosition(cx + 60f, 710f);
        comboLbl.setPosition(cx - 40f,  680f);

        stage.addActor(scoreLbl);
        stage.addActor(dartsLbl);
        stage.addActor(roundsLbl);
        stage.addActor(comboLbl);

        TextButton.TextButtonStyle roundStyle = UiFactory.makeRoundStyle(game.manager, game.fontSmall);
        TextButton pauseBtn = UiFactory.makeButton("||", roundStyle,
                Constants.HUD_PAUSE_BTN_SIZE, Constants.HUD_PAUSE_BTN_SIZE);
        pauseBtn.setPosition(Constants.WORLD_WIDTH - Constants.HUD_PAUSE_BTN_SIZE - 10f,
                Constants.WORLD_HEIGHT - Constants.HUD_PAUSE_BTN_SIZE - 10f);
        pauseBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                game.setScreen(new PauseScreen(game, GameScreen.this, venueId));
            }
        });
        stage.addActor(pauseBtn);
    }

    private void registerInput() {
        Gdx.input.setInputProcessor(new InputMultiplexer(
            stage,
            new InputAdapter() {
                @Override public boolean keyDown(int keycode) {
                    if (keycode == Input.Keys.BACK) {
                        game.setScreen(new MainMenuScreen(game));
                        return true;
                    }
                    return false;
                }
            }
        ));
    }

    private void update(float delta) {
        wobbleTime += delta;

        if (phase == Phase.AIMING) {
            aimX = Constants.BOARD_CENTER_X
                 + MathUtils.sin(wobbleTime * wobbleSpeed) * Constants.WOBBLE_AMPLITUDE;

            if (Gdx.input.isTouched()) {
                charging = true;
                power = Math.min(power + delta * Constants.POWER_FILL_SPEED, Constants.MAX_POWER);
            } else if (charging) {
                throwDart();
                charging = false;
            }

        } else if (phase == Phase.FLYING) {
            flyTimer += delta;
            if (flyTimer >= Constants.DART_FLY_DURATION) {
                onDartLanded();
            }
        }
    }

    private void throwDart() {
        float aimError = (aimX - Constants.BOARD_CENTER_X) * (1f - power);
        float landX = Constants.BOARD_CENTER_X + aimError + MathUtils.random(-8f, 8f) * (1f - power);
        float landY = Constants.BOARD_CENTER_Y + MathUtils.random(-8f, 8f) * (1f - power);

        dartStartX = aimX;
        dartStartY = 200f;
        dartEndX   = landX;
        dartEndY   = landY;
        flyTimer   = 0f;
        phase      = Phase.FLYING;
        power      = 0f;

        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_shoot.ogg", Sound.class).play(1.0f);
    }

    private void onDartLanded() {
        float dx   = dartEndX - Constants.BOARD_CENTER_X;
        float dy   = dartEndY - Constants.BOARD_CENTER_Y;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        int pts;
        if (dist <= Constants.BULLSEYE_RADIUS) {
            pts = Constants.SCORE_BULLSEYE;
            coins += Constants.COINS_PER_BULLSEYE;
            if (game.sfxEnabled)
                game.manager.get("sounds/sfx/sfx_level_complete.ogg", Sound.class).play(1.0f);
        } else if (dist <= Constants.BULL_25_RADIUS) {
            pts = Constants.SCORE_BULL_25;
            coins += Constants.COINS_PER_HIT;
            if (game.sfxEnabled)
                game.manager.get("sounds/sfx/sfx_coin.ogg", Sound.class).play(1.0f);
        } else if (dist <= Constants.BOARD_RADIUS) {
            int sector = 1 + (int)((1f - dist / Constants.BOARD_RADIUS) * 19f);
            if (dist >= Constants.DOUBLE_RING_INNER && dist <= Constants.DOUBLE_RING_OUTER) {
                sector *= 2;
            } else if (dist >= Constants.TRIPLE_RING_INNER && dist <= Constants.TRIPLE_RING_OUTER) {
                sector *= 3;
            }
            pts = Math.min(sector, 60);
            coins += Constants.COINS_PER_HIT;
            if (game.sfxEnabled)
                game.manager.get("sounds/sfx/sfx_hit.ogg", Sound.class).play(1.0f);
        } else {
            pts = 0;
            combo = 0;
        }

        if (pts > 0) {
            combo++;
            if (combo >= Constants.COMBO_THRESHOLD) {
                pts = (int)(pts * Constants.COMBO_MULTIPLIER);
                coins += Constants.COINS_COMBO_BONUS;
            }
        }

        score     += pts;
        dartsLeft--;
        impacts.add(new Vector2(dartEndX, dartEndY));
        phase = Phase.AIMING;

        if (dartsLeft <= 0) {
            roundsLeft--;
            if (roundsLeft <= 0) {
                endGame();
            } else {
                dartsLeft = Constants.DARTS_PER_ROUND;
            }
        }
    }

    private void endGame() {
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        int saved = prefs.getInteger(Constants.PREF_COINS, 0);
        prefs.putInteger(Constants.PREF_COINS, saved + coins);
        prefs.flush();
        game.setScreen(new GameOverScreen(game, score, venueId));
    }

    @Override
    public void render(float delta) {
        update(delta);

        scoreLbl.setText(String.valueOf(score));
        dartsLbl.setText("D:" + dartsLeft);
        roundsLbl.setText("R:" + roundsLeft);
        comboLbl.setText(combo >= Constants.COMBO_THRESHOLD ? "COMBO x2!" : "");

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        String bg = GAME_BGS[Math.min(venueId, GAME_BGS.length - 1)];
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(game.manager.get(bg, Texture.class),
                0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        game.batch.end();

        drawDartboard();

        game.batch.begin();
        Texture splashTex = game.manager.get(SPLASH, Texture.class);
        for (Vector2 imp : impacts) {
            game.batch.draw(splashTex, imp.x - 12f, imp.y - 12f, 24f, 24f);
        }
        if (phase == Phase.FLYING) {
            float t  = flyTimer / Constants.DART_FLY_DURATION;
            float dx = dartStartX + (dartEndX - dartStartX) * t;
            float dy = dartStartY + (dartEndY - dartStartY) * t;
            game.batch.draw(splashTex, dx - 6f, dy - 24f, Constants.DART_WIDTH, Constants.DART_HEIGHT);
        }
        game.batch.end();

        shaper.setProjectionMatrix(camera.combined);
        shaper.begin(ShapeRenderer.ShapeType.Filled);
        shaper.setColor(0.3f, 0.3f, 0.3f, 1f);
        shaper.rect(PB_X, PB_Y, PB_W, PB_H);
        shaper.setColor(0.2f, 0.8f, 0.2f, 1f);
        shaper.rect(PB_X, PB_Y, PB_W, PB_H * power);
        shaper.end();

        if (phase == Phase.AIMING) {
            shaper.begin(ShapeRenderer.ShapeType.Line);
            shaper.setColor(Color.YELLOW);
            shaper.circle(aimX, Constants.BOARD_CENTER_Y, 8f, 12);
            shaper.end();
        }

        stage.act(delta);
        stage.draw();
    }

    private void drawDartboard() {
        float cx = Constants.BOARD_CENTER_X;
        float cy = Constants.BOARD_CENTER_Y;
        shaper.setProjectionMatrix(camera.combined);

        shaper.begin(ShapeRenderer.ShapeType.Filled);
        shaper.setColor(0.1f, 0.4f, 0.1f, 1f);
        shaper.circle(cx, cy, Constants.BOARD_RADIUS, 48);
        shaper.setColor(0.15f, 0.15f, 0.15f, 1f);
        shaper.circle(cx, cy, Constants.DOUBLE_RING_INNER, 48);
        shaper.setColor(0.8f, 0.1f, 0.1f, 1f);
        shaper.circle(cx, cy, Constants.TRIPLE_RING_OUTER, 48);
        shaper.setColor(0.15f, 0.15f, 0.15f, 1f);
        shaper.circle(cx, cy, Constants.TRIPLE_RING_INNER, 48);
        shaper.setColor(0.8f, 0.1f, 0.1f, 1f);
        shaper.circle(cx, cy, Constants.DOUBLE_RING_OUTER, 48);
        shaper.setColor(0.1f, 0.4f, 0.1f, 1f);
        shaper.circle(cx, cy, Constants.DOUBLE_RING_INNER, 48);
        shaper.setColor(0.1f, 0.5f, 0.1f, 1f);
        shaper.circle(cx, cy, Constants.BULL_25_RADIUS, 32);
        shaper.setColor(0.9f, 0.05f, 0.05f, 1f);
        shaper.circle(cx, cy, Constants.BULLSEYE_RADIUS, 24);
        shaper.end();

        shaper.begin(ShapeRenderer.ShapeType.Line);
        shaper.setColor(Color.WHITE);
        shaper.circle(cx, cy, Constants.BOARD_RADIUS, 48);
        shaper.circle(cx, cy, Constants.DOUBLE_RING_OUTER, 48);
        shaper.circle(cx, cy, Constants.DOUBLE_RING_INNER, 48);
        shaper.circle(cx, cy, Constants.TRIPLE_RING_OUTER, 48);
        shaper.circle(cx, cy, Constants.TRIPLE_RING_INNER, 48);
        shaper.circle(cx, cy, Constants.BULL_25_RADIUS, 32);
        shaper.circle(cx, cy, Constants.BULLSEYE_RADIUS, 24);
        shaper.end();
    }

    @Override
    public void show() {
        game.playMusic("sounds/music/music_gameplay.ogg");
        registerInput();
    }

    @Override public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        stage.dispose();
        shaper.dispose();
    }
}
