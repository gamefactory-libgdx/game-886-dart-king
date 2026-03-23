package com.asocity.dartking000886;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class UiFactory {

    /** Rectangle button style — standard UI buttons. */
    public static TextButton.TextButtonStyle makeRectStyle(AssetManager mgr, BitmapFont font) {
        TextButton.TextButtonStyle s = new TextButton.TextButtonStyle();
        s.font  = font;
        s.up    = new TextureRegionDrawable(new TextureRegion(
                      mgr.get("ui/buttons/button_rectangle_depth_gradient.png", Texture.class)));
        s.down  = new TextureRegionDrawable(new TextureRegion(
                      mgr.get("ui/buttons/button_rectangle_depth_flat.png", Texture.class)));
        return s;
    }

    /** Round button style — icon / small buttons. */
    public static TextButton.TextButtonStyle makeRoundStyle(AssetManager mgr, BitmapFont font) {
        TextButton.TextButtonStyle s = new TextButton.TextButtonStyle();
        s.font  = font;
        s.up    = new TextureRegionDrawable(new TextureRegion(
                      mgr.get("ui/buttons/button_round_depth_gradient.png", Texture.class)));
        s.down  = new TextureRegionDrawable(new TextureRegion(
                      mgr.get("ui/buttons/button_round_depth_flat.png", Texture.class)));
        return s;
    }

    /** Create and size a rectangle button in one call. */
    public static TextButton makeButton(String label, TextButton.TextButtonStyle style,
                                        float w, float h) {
        TextButton btn = new TextButton(label, style);
        btn.setSize(w, h);
        return btn;
    }

    /** Create a rectangle button centered horizontally at the given top-Y (FIGMA coordinates). */
    public static TextButton makeButtonCentered(String label, TextButton.TextButtonStyle style,
                                                float w, float h, float figmaTopY) {
        TextButton btn = new TextButton(label, style);
        btn.setSize(w, h);
        float libgdxY = Constants.WORLD_HEIGHT - figmaTopY - h;
        btn.setPosition((Constants.WORLD_WIDTH - w) / 2f, libgdxY);
        return btn;
    }
}
