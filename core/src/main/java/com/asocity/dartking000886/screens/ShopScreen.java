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

public class ShopScreen implements Screen {

    private static final String BG = "ui/shop.png";

    private final MainGame game;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Stage    stage;

    public ShopScreen(MainGame game) {
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

        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, null);
        Label.LabelStyle bodyStyle  = new Label.LabelStyle(game.fontBody,  null);
        Label.LabelStyle smallStyle = new Label.LabelStyle(game.fontSmall, null);

        Label titleLabel = new Label("SHOP", titleStyle);
        titleLabel.setPosition(cx - titleLabel.getPrefWidth() / 2f, 748f);
        stage.addActor(titleLabel);

        int coins = prefs.getInteger(Constants.PREF_COINS, 0);
        Label coinsLabel = new Label("COINS: " + coins, bodyStyle);
        coinsLabel.setPosition(cx - coinsLabel.getPrefWidth() / 2f, 692f);
        stage.addActor(coinsLabel);

        Label powerLabel = new Label("POWER-UPS", smallStyle);
        powerLabel.setPosition(cx - powerLabel.getPrefWidth() / 2f, 642f);
        stage.addActor(powerLabel);

        TextButton.TextButtonStyle rectStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);

        // Shield
        boolean shieldOwned = prefs.getBoolean(Constants.PREF_SHIELD_OWN, false);
        TextButton shieldBtn = UiFactory.makeButton(
                shieldOwned ? "SHIELD (owned)" : "SHIELD " + Constants.PRICE_SHIELD + "c",
                rectStyle, 280f, 56f);
        shieldBtn.setPosition(cx - 140f, 576f);
        shieldBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                tryBuy(Constants.PRICE_SHIELD, Constants.PREF_SHIELD_OWN);
            }
        });
        stage.addActor(shieldBtn);

        // Coin Magnet
        boolean magnetOwned = prefs.getBoolean(Constants.PREF_MAGNET_OWN, false);
        TextButton magnetBtn = UiFactory.makeButton(
                magnetOwned ? "MAGNET (owned)" : "MAGNET " + Constants.PRICE_COIN_MAGNET + "c",
                rectStyle, 280f, 56f);
        magnetBtn.setPosition(cx - 140f, 506f);
        magnetBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                tryBuy(Constants.PRICE_COIN_MAGNET, Constants.PREF_MAGNET_OWN);
            }
        });
        stage.addActor(magnetBtn);

        // 2x Score
        boolean doubleOwned = prefs.getBoolean(Constants.PREF_DOUBLE_OWN, false);
        TextButton doubleBtn = UiFactory.makeButton(
                doubleOwned ? "2x SCORE (owned)" : "2x SCORE " + Constants.PRICE_DOUBLE_SCORE + "c",
                rectStyle, 280f, 56f);
        doubleBtn.setPosition(cx - 140f, 436f);
        doubleBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                tryBuy(Constants.PRICE_DOUBLE_SCORE, Constants.PREF_DOUBLE_OWN);
            }
        });
        stage.addActor(doubleBtn);

        // Skins section
        Label skinsLabel = new Label("DART SKINS", smallStyle);
        skinsLabel.setPosition(cx - skinsLabel.getPrefWidth() / 2f, 380f);
        stage.addActor(skinsLabel);

        final int[] skinPrices = {0, Constants.PRICE_SKIN_2, Constants.PRICE_SKIN_3};
        String[] skinNames = {"DEFAULT (free)",
                              "GOLD " + skinPrices[1] + "c",
                              "DIAMOND " + skinPrices[2] + "c"};
        float[] skinYs = {314f, 248f, 182f};
        int savedSkin  = prefs.getInteger(Constants.PREF_SKIN, 0);

        for (int i = 0; i < 3; i++) {
            final int skinIdx = i;
            String lbl = savedSkin == i ? skinNames[i] + " [sel]" : skinNames[i];
            TextButton skinBtn = UiFactory.makeButton(lbl, rectStyle, 280f, 48f);
            skinBtn.setPosition(cx - 140f, skinYs[i]);
            skinBtn.addListener(new ChangeListener() {
                @Override public void changed(ChangeEvent e, Actor a) {
                    Preferences p = Gdx.app.getPreferences(Constants.PREFS_NAME);
                    int c = p.getInteger(Constants.PREF_COINS, 0);
                    if (skinPrices[skinIdx] == 0 || c >= skinPrices[skinIdx]) {
                        if (skinPrices[skinIdx] > 0) {
                            p.putInteger(Constants.PREF_COINS, c - skinPrices[skinIdx]);
                        }
                        p.putInteger(Constants.PREF_SKIN, skinIdx);
                        p.flush();
                        playClick();
                        game.setScreen(new ShopScreen(game));
                    }
                }
            });
            stage.addActor(skinBtn);
        }

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

    private void tryBuy(int price, String prefKey) {
        Preferences p = Gdx.app.getPreferences(Constants.PREFS_NAME);
        if (p.getBoolean(prefKey, false)) return;
        int coins = p.getInteger(Constants.PREF_COINS, 0);
        if (coins >= price) {
            p.putInteger(Constants.PREF_COINS, coins - price);
            p.putBoolean(prefKey, true);
            p.flush();
            playClick();
            game.setScreen(new ShopScreen(game));
        }
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
