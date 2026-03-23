package com.asocity.dartking000886.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
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

public class VenueSelectScreen implements Screen {

    private static final String BG = "ui/venue_select.png";

    private final MainGame game;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Stage stage;

    public VenueSelectScreen(MainGame game) {
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
        int venueUnlock = prefs.getInteger(Constants.PREF_VENUE_UNLOCK, 0);
        int coins       = prefs.getInteger(Constants.PREF_COINS, 0);

        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, null);
        Label.LabelStyle bodyStyle  = new Label.LabelStyle(game.fontBody,  null);
        Label.LabelStyle smallStyle = new Label.LabelStyle(game.fontSmall, null);

        Label titleLabel = new Label("CHOOSE VENUE", titleStyle);
        titleLabel.setPosition(cx - titleLabel.getPrefWidth() / 2f, 748f);
        stage.addActor(titleLabel);

        TextButton.TextButtonStyle rectStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);

        // --- LOCAL PUB (always unlocked) ---
        TextButton pubBtn = UiFactory.makeButton("LOCAL PUB", rectStyle, 280f, 64f);
        pubBtn.setPosition(cx - 140f, 570f);
        pubBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new GameScreen(game, Constants.VENUE_LOCAL_PUB));
            }
        });
        stage.addActor(pubBtn);

        Label pubDesc = new Label("Normal speed", smallStyle);
        pubDesc.setPosition(cx - pubDesc.getPrefWidth() / 2f, 548f);
        stage.addActor(pubDesc);

        // --- TOURNAMENT ---
        if (venueUnlock >= 1) {
            TextButton tourBtn = UiFactory.makeButton("TOURNAMENT", rectStyle, 280f, 64f);
            tourBtn.setPosition(cx - 140f, 440f);
            tourBtn.addListener(new ChangeListener() {
                @Override public void changed(ChangeEvent e, Actor a) {
                    playClick();
                    game.setScreen(new GameScreen(game, Constants.VENUE_TOURNAMENT));
                }
            });
            stage.addActor(tourBtn);

            Label tourDesc = new Label("Faster sway", smallStyle);
            tourDesc.setPosition(cx - tourDesc.getPrefWidth() / 2f, 418f);
            stage.addActor(tourDesc);
        } else {
            // Show locked with cost
            TextButton lockedBtn = UiFactory.makeButton(
                    "TOURNAMENT [" + Constants.VENUE_TOURNAMENT_COST + " coins]", rectStyle, 280f, 64f);
            lockedBtn.setPosition(cx - 140f, 440f);
            lockedBtn.addListener(new ChangeListener() {
                @Override public void changed(ChangeEvent e, Actor a) {
                    Preferences p = Gdx.app.getPreferences(Constants.PREFS_NAME);
                    int c = p.getInteger(Constants.PREF_COINS, 0);
                    if (c >= Constants.VENUE_TOURNAMENT_COST) {
                        p.putInteger(Constants.PREF_COINS, c - Constants.VENUE_TOURNAMENT_COST);
                        p.putInteger(Constants.PREF_VENUE_UNLOCK, 1);
                        p.flush();
                        playClick();
                        game.setScreen(new VenueSelectScreen(game));
                    }
                }
            });
            stage.addActor(lockedBtn);
        }

        // --- CHAMPIONS ---
        if (venueUnlock >= 2) {
            TextButton champBtn = UiFactory.makeButton("CHAMPIONS", rectStyle, 280f, 64f);
            champBtn.setPosition(cx - 140f, 310f);
            champBtn.addListener(new ChangeListener() {
                @Override public void changed(ChangeEvent e, Actor a) {
                    playClick();
                    game.setScreen(new GameScreen(game, Constants.VENUE_CHAMPIONS));
                }
            });
            stage.addActor(champBtn);

            Label champDesc = new Label("Max difficulty", smallStyle);
            champDesc.setPosition(cx - champDesc.getPrefWidth() / 2f, 288f);
            stage.addActor(champDesc);
        } else {
            TextButton lockedBtn2 = UiFactory.makeButton(
                    "CHAMPIONS [" + Constants.VENUE_CHAMPIONS_COST + " coins]", rectStyle, 280f, 64f);
            lockedBtn2.setPosition(cx - 140f, 310f);
            lockedBtn2.addListener(new ChangeListener() {
                @Override public void changed(ChangeEvent e, Actor a) {
                    Preferences p = Gdx.app.getPreferences(Constants.PREFS_NAME);
                    int c = p.getInteger(Constants.PREF_COINS, 0);
                    if (c >= Constants.VENUE_CHAMPIONS_COST) {
                        p.putInteger(Constants.PREF_COINS, c - Constants.VENUE_CHAMPIONS_COST);
                        p.putInteger(Constants.PREF_VENUE_UNLOCK, 2);
                        p.flush();
                        playClick();
                        game.setScreen(new VenueSelectScreen(game));
                    }
                }
            });
            stage.addActor(lockedBtn2);
        }

        // Coin display
        Label coinsLabel = new Label("COINS: " + coins, bodyStyle);
        coinsLabel.setPosition(cx - coinsLabel.getPrefWidth() / 2f, 200f);
        stage.addActor(coinsLabel);

        // BACK button
        TextButton backBtn = UiFactory.makeButton("BACK", rectStyle, 120f, 48f);
        backBtn.setPosition(20f, 26f);
        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playBack();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(backBtn);

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
        game.playMusic("sounds/music/music_menu.ogg");
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
