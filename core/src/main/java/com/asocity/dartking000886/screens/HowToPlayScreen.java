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

public class HowToPlayScreen implements Screen {

    private static final String BG = "ui/how_to_play.png";

    private final MainGame game;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Stage    stage;

    public HowToPlayScreen(MainGame game) {
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

        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, null);
        Label.LabelStyle bodyStyle  = new Label.LabelStyle(game.fontBody,  null);
        Label.LabelStyle smallStyle = new Label.LabelStyle(game.fontSmall, null);

        Label titleLabel = new Label("HOW TO PLAY", titleStyle);
        titleLabel.setPosition(cx - titleLabel.getPrefWidth() / 2f, 748f);
        stage.addActor(titleLabel);

        String[] lines = {
            "1. Watch the crosshair sway.",
            "2. HOLD screen to charge power.",
            "3. RELEASE to throw the dart.",
            "4. Aim for the bullseye for 50 pts!",
            "5. Hit 3 in a row for a COMBO x2.",
            "6. Collect coins to unlock venues.",
            "   " + Constants.DARTS_PER_ROUND + " darts per round, " + Constants.ROUNDS_PER_GAME + " rounds total."
        };

        float y = 648f;
        for (String line : lines) {
            Label lbl = new Label(line, smallStyle);
            lbl.setPosition(cx - lbl.getPrefWidth() / 2f, y);
            stage.addActor(lbl);
            y -= 76f;
        }

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
