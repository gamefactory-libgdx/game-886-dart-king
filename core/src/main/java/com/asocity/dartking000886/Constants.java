package com.asocity.dartking000886;

public class Constants {

    // World dimensions
    public static final float WORLD_WIDTH  = 480f;
    public static final float WORLD_HEIGHT = 854f;

    // Dartboard layout
    public static final float BOARD_CENTER_X     = WORLD_WIDTH / 2f;
    public static final float BOARD_CENTER_Y     = 480f;
    public static final float BOARD_RADIUS       = 160f;
    public static final float BULLSEYE_RADIUS    = 16f;
    public static final float BULL_25_RADIUS     = 32f;
    public static final float DOUBLE_RING_INNER  = 140f;
    public static final float DOUBLE_RING_OUTER  = 160f;
    public static final float TRIPLE_RING_INNER  = 90f;
    public static final float TRIPLE_RING_OUTER  = 110f;

    // Score values
    public static final int SCORE_BULLSEYE   = 50;
    public static final int SCORE_BULL_25    = 25;
    public static final int SCORE_MISS       = 0;

    // Dart throw physics
    public static final float AIM_SENSITIVITY    = 1.8f;
    public static final float MIN_POWER          = 0.3f;
    public static final float MAX_POWER          = 1.0f;
    public static final float POWER_FILL_SPEED   = 0.9f;  // fills 0→1 per second
    public static final float DART_FLY_DURATION  = 0.35f; // seconds dart travels to board
    public static final float DART_SIZE          = 48f;
    public static final float DART_WIDTH         = 12f;
    public static final float DART_HEIGHT        = 48f;

    // Sway / aim wobble
    public static final float WOBBLE_AMPLITUDE   = 18f;   // world units
    public static final float WOBBLE_SPEED_BASE  = 1.4f;  // radians per second at venue 1
    public static final float WOBBLE_SPEED_VENUE2= 2.0f;
    public static final float WOBBLE_SPEED_VENUE3= 2.8f;

    // Darts per round
    public static final int DARTS_PER_ROUND    = 6;
    public static final int ROUNDS_PER_GAME    = 3;

    // Combo multipliers
    public static final int COMBO_THRESHOLD    = 3;       // hits in a row to activate
    public static final float COMBO_MULTIPLIER = 2.0f;

    // Coin rewards
    public static final int COINS_PER_BULLSEYE = 5;
    public static final int COINS_PER_HIT      = 1;
    public static final int COINS_COMBO_BONUS  = 3;

    // Venue unlock costs
    public static final int VENUE_TOURNAMENT_COST = 200;
    public static final int VENUE_CHAMPIONS_COST  = 500;

    // Shop prices
    public static final int PRICE_SHIELD        = 20;
    public static final int PRICE_COIN_MAGNET   = 30;
    public static final int PRICE_DOUBLE_SCORE  = 50;
    public static final int PRICE_SKIN_2        = 100;
    public static final int PRICE_SKIN_3        = 150;

    // Power-up durations (seconds)
    public static final float DURATION_SHIELD      = 5f;
    public static final float DURATION_COIN_MAGNET = 10f;

    // HUD layout
    public static final float HUD_PADDING        = 20f;
    public static final float HUD_LABEL_H        = 44f;
    public static final float HUD_PAUSE_BTN_SIZE = 80f;

    // UI button sizes
    public static final float BTN_W_MAIN   = 280f;
    public static final float BTN_H_MAIN   = 64f;
    public static final float BTN_W_STD    = 280f;
    public static final float BTN_H_STD    = 56f;
    public static final float BTN_W_SMALL  = 120f;
    public static final float BTN_H_SMALL  = 48f;
    public static final float BTN_ROUND    = 56f;

    // Venue IDs
    public static final int VENUE_LOCAL_PUB     = 0;
    public static final int VENUE_TOURNAMENT    = 1;
    public static final int VENUE_CHAMPIONS     = 2;

    // SharedPreferences keys
    public static final String PREFS_NAME       = "DartKingPrefs";
    public static final String PREF_HIGH_SCORE  = "highScore";
    public static final String PREF_COINS       = "coins";
    public static final String PREF_MUSIC       = "musicEnabled";
    public static final String PREF_SFX         = "sfxEnabled";
    public static final String PREF_SKIN        = "selectedSkin";
    public static final String PREF_VENUE_UNLOCK= "venueUnlock";
    public static final String PREF_SHIELD_OWN  = "shieldOwned";
    public static final String PREF_MAGNET_OWN  = "magnetOwned";
    public static final String PREF_DOUBLE_OWN  = "doubleScoreOwned";

    // Leaderboard
    public static final int LEADERBOARD_SIZE    = 8;
    public static final String PREF_SCORE_PREFIX= "score_";
    public static final String PREF_NAME_PREFIX = "name_";

    // Font sizes
    public static final int FONT_SIZE_TITLE  = 48;
    public static final int FONT_SIZE_BODY   = 28;
    public static final int FONT_SIZE_SMALL  = 20;
    public static final int FONT_SIZE_SCORE  = 64;
    public static final int FONT_SIZE_HUD    = 24;

    // Colors (RGBA hex components for reference)
    public static final float COLOR_ESPRESSO_R   = 0.102f;
    public static final float COLOR_ESPRESSO_G   = 0.055f;
    public static final float COLOR_ESPRESSO_B   = 0.024f;

    public static final float COLOR_AMBER_R      = 0.831f;
    public static final float COLOR_AMBER_G      = 0.510f;
    public static final float COLOR_AMBER_B      = 0.039f;

    public static final float COLOR_CRIMSON_R    = 0.910f;
    public static final float COLOR_CRIMSON_G    = 0.000f;
    public static final float COLOR_CRIMSON_B    = 0.231f;
}
