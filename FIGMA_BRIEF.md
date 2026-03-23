# Figma AI Design Brief — Dart King

---

## 1. Art Style & Color Palette

Dart King uses a **warm pub realism** aesthetic — rich mahogany wood textures, brass fixtures, and amber candlelight blended with a slightly stylized cartoon edge to keep the tone playful rather than gritty. Think classic British pub meets sports arcade: leather upholstery, chalk scoreboards, and neon beer signs casting colored halos. The progression through three venues shifts the palette from cozy amber (Local Pub) through cool tournament blue-white (Tournament Hall) to electric gold-crimson prestige (Champions Arena).

**Primary palette:**
- `#1A0E06` — Deep espresso black (backgrounds, dark panels)
- `#5C2A0E` — Mahogany brown (wood grain, frames)
- `#D4820A` — Burnished amber (warm light, accents)
- `#F5E6C8` — Parchment cream (text panels, score areas)

**Accent:**
- `#E8003A` — Bullseye crimson (dartboard red, danger highlights)
- `#4ECDC4` — Mint teal (neon glow on higher venues, score pops)

**Font mood:** Bold compressed sans-serif with slight retro slab undertone — sturdy, pub-sign energy. Think condensed wood-block lettering aged with character.

---

## 2. App Icon — icon_512.png (512×512px)

**Background:** Deep radial gradient from `#1A0E06` at the edges to `#3D1A06` at center, suggesting a dim spotlight on a pub wall. Subtle wood-plank texture overlaid at low opacity in the outer ring.

**Central symbol:** A front-facing dartboard fills ~70% of the canvas — classic alternating black/cream/red/green sectors with a glowing bullseye center in `#E8003A` radiating a soft crimson halo. Three brass-tipped darts are lodged in the board at dramatic angles, creating a dynamic composition: one centered in the bullseye, two fanned to upper-left and right sectors.

**Glow/shadow:** The bullseye emits a pulsing warm red glow (`#E8003A` at 60% opacity radial). Each dart casts a sharp directional shadow downward-right. A thin gold ring border (`#D4820A`) traces the icon's square edge, suggesting a framed pub trophy.

**Mood:** Confident, precise, slightly dramatic — a split-second freeze-frame of a perfect throw.

---

## 3. UI Screens (480×854 portrait)

---

### MainMenuScreen

**A) BACKGROUND IMAGE**
A richly textured pub interior at dusk, viewed head-on. The back wall is dark mahogany paneling with faint horizontal wood grain. Center background shows a large ornate dartboard mounted on the wall, slightly out of focus, softly lit by two warm brass wall sconces flanking it — casting amber cones upward and downward. A thick empty rectangular banner frame hangs across the upper third, crafted from dark wood with brass corner rivets and a carved rope border — blank inside for title text. Below it, two empty rounded-rectangle card outlines in aged parchment `#F5E6C8` at 20% opacity sit side by side suggesting a lower panel area. Amber volumetric light dust particles drift upward throughout. Overall tone: `#1A0E06` to `#2E1508` gradient floor-to-ceiling.

**B) BUTTON LAYOUT**
```
DART KING (title label)  | top-Y=90px  | x=centered          | size=360x80
PLAY                     | top-Y=340px | x=centered          | size=280x64
VENUE SELECT             | top-Y=420px | x=centered          | size=280x56
LEADERBOARD              | top-Y=490px | x=centered          | size=280x56
SHOP                     | top-Y=560px | x=centered          | size=280x56
SETTINGS                 | top-Y=630px | x=centered          | size=280x56
HOW TO PLAY              | top-Y=700px | x=centered          | size=280x48
```

---

### VenueSelectScreen

**A) BACKGROUND IMAGE**
Three venue preview panels arranged vertically, each a distinct environment thumbnail framed in carved mahogany with brass nails. Top panel glows warm amber (Local Pub glimpse — brick walls, dim lights). Middle panel emits cool blue-white fluorescent light (Tournament Hall glimpse — polished floor, banners). Bottom panel radiates gold-crimson prestige light (Champions Arena glimpse — red velvet curtains, trophy shelf). The background between panels is deep `#1A0E06` with a subtle spotlight from above illuminating each frame. Each panel frame is ornate but the interiors are fully visible scenic art. A blank horizontal banner shape in parchment outline spans the very top for a title area.

**B) BUTTON LAYOUT**
```
SELECT VENUE (label)     | top-Y=40px  | x=centered          | size=300x52
LOCAL PUB                | top-Y=160px | x=centered          | size=320x120
TOURNAMENT HALL          | top-Y=310px | x=centered          | size=320x120
CHAMPIONS ARENA          | top-Y=460px | x=centered          | size=320x120
BACK                     | top-Y=780px | x=left@20px         | size=120x48
```

---

### LocalPubScreen (Gameplay Background)

**A) BACKGROUND IMAGE**
A cozy brick-walled pub interior at night. The dartboard mounting wall is the focal point — worn red bricks, a dartboard silhouette mount circle on the wall (empty center, the actual board is drawn in code). Warm Edison bulb string lights run along the top edge casting golden bokeh halos. A chalkboard scorecard hangs upper-left, blank. The floor is dark stained hardwood with faint grain. A blurred bar counter with amber glasses sits in the far background. Ambient smoke haze near the ceiling. Overall palette heavy on `#5C2A0E`, `#D4820A`, `#1A0E06`. Two empty decorative side panels — left and right thin vertical strips — frame the play field in dark wood trim.

**B) BUTTON LAYOUT**
```
SCORE (label)            | top-Y=20px  | x=right@20px        | size=140x44
VENUE NAME (label)       | top-Y=20px  | x=left@20px         | size=160x44
PAUSE                    | top-Y=20px  | x=centered          | size=80x44
```

---

### TournamentScreen (Gameplay Background)

**A) BACKGROUND IMAGE**
A polished tournament hall — clean white walls with blue-grey wainscoting panels. Two tall tournament banners hang on either side of the dartboard wall in royal blue and silver, blank of text. Recessed ceiling spotlights cast sharp white cones downward onto the play area, creating a high-contrast clean stage feeling. The floor is reflective light grey hardwood. Score monitor frames (blank screens) sit in upper corners. Subtle crowd silhouettes are visible in extreme background blur. Palette shifts cool: `#1A2035` background, `#3B5998` banner blue, `#C0C8D8` silver-white accents, `#4ECDC4` teal neon trim.

**B) BUTTON LAYOUT**
```
SCORE (label)            | top-Y=20px  | x=right@20px        | size=140x44
VENUE NAME (label)       | top-Y=20px  | x=left@20px         | size=160x44
PAUSE                    | top-Y=20px  | x=centered          | size=80x44
```

---

### ChampionsScreen (Gameplay Background)

**A) BACKGROUND IMAGE**
A grand champions arena — deep crimson velvet curtains frame both sides of a dramatically lit stage. The back wall is rich burgundy `#4A0010` with gold leaf ornamental molding. Two large trophy silhouettes flank the dartboard mount position. Overhead, a large circular spotlight ring in gold (`#D4820A`) radiates downward. The floor is glossy black marble with gold vein lines. Ambient particle glow in gold and crimson drifts upward like embers. A blank ornate championship shield outline hangs in the upper center — empty inside. Overall mood: prestige, high stakes, royal ceremony.

**B) BUTTON LAYOUT**
```
SCORE (label)            | top-Y=20px  | x=right@20px        | size=140x44
VENUE NAME (label)       | top-Y=20px  | x=left@20px         | size=160x44
PAUSE                    | top-Y=20px  | x=centered          | size=80x44
```

---

### GameOverScreen

**A) BACKGROUND IMAGE**
Dark moody version of whichever venue the player was in — desaturated 60%, with a vignette darkening all edges to near-black. Center holds a large empty hexagonal frame in tarnished brass, suggesting a plaque or trophy that went unclaimed — blank inside. Broken dart shards are scattered artistically at the bottom of the frame as decorative detail. Red emergency-style lighting bleeds in from the corners with a subtle `#E8003A` glow. A cracked chalkboard texture overlays the entire screen at 15% opacity. Atmosphere: somber but not harsh — a pub chalkboard wiped clean.

**B) BUTTON LAYOUT**
```
GAME OVER (label)        | top-Y=120px | x=centered          | size=320x72
SCORE (label)            | top-Y=240px | x=centered          | size=280x52
BEST (label)             | top-Y=310px | x=centered          | size=280x44
RETRY                    | top-Y=460px | x=centered          | size=280x64
MENU                     | top-Y=540px | x=centered          | size=280x56
LEADERBOARD              | top-Y=620px | x=centered          | size=280x48
```

---

### LeaderboardScreen

**A) BACKGROUND IMAGE**
A classic pub trophy wall — dark mahogany shelves recede into a blurred background holding faint brass trophy silhouettes. The foreground features a large ornate leaderboard frame: dark wood outer border with brass nail accents and a hanging chain at top, the interior is a smooth parchment `#F5E6C8` panel — completely blank inside (rows drawn in code). A small crown motif is carved into the top-center of the frame, inlaid with `#D4820A` gold. Warm candlelight from below illuminates the board. The overall screen background is `#1A0E06` with subtle wood grain texture.

**B) BUTTON LAYOUT**
```
LEADERBOARD (label)      | top-Y=50px  | x=centered          | size=300x56
RANK / NAME / SCORE (header row label) | top-Y=170px | x=centered | size=440x40
[Row 1]                  | top-Y=220px | x=centered          | size=440x52
[Row 2]                  | top-Y=278px | x=centered          | size=440x52
[Row 3]                  | top-Y=336px | x=centered          | size=440x52
[Row 4]                  | top-Y=394px | x=centered          | size=440x52
[Row 5]                  | top-Y=452px | x=centered          | size=440x52
[Row 6]                  | top-Y=510px | x=centered          | size=440x52
[Row 7]                  | top-Y=568px | x=centered          | size=440x52
[Row 8]                  | top-Y=626px | x=centered          | size=440x52
BACK                     | top-Y=780px | x=left@20px         | size=120x48
```

---

### ShopScreen

**A) BACKGROUND IMAGE**
A pub back-room shop aesthetic — rough stone walls with wooden shelves displaying dart sets in shadow boxes, framed as decorative art. The shelves have subtle wood grain and brass bracket supports. Center panel is an empty ornate wooden display case frame — glass-front style with brass hinges — blank inside for item grid. Upper region has a large blank carved-wood sign shape (for SHOP title). Warm amber uplighting from shelf underlights. The overall color is `#2E1508` mid-tone with `#D4820A` warm accent light hitting the shelf edges.

**B) BUTTON LAYOUT**
```
SHOP (label)             | top-Y=50px  | x=centered          | size=260x60
COINS (label)            | top-Y=50px  | x=right@20px        | size=160x44
[Item Grid 3x2]          | top-Y=160px | x=centered          | size=440x420
BUY (per item)           | top-Y=varies| x=centered          | size=130x44
BACK                     | top-Y=780px | x=left@20px         | size=120x48
```

---

### SettingsScreen

**A) BACKGROUND IMAGE**
A cozy pub corner nook — dark wood booth seating suggested in the background blur, a small round table surface in the foreground lower area. The focal element is a large blank clipboard or chalkboard frame centered on screen — wooden frame with chalk dust at the base, interior entirely blank (settings drawn in code). A single hanging pendant lamp above the frame casts warm circular light. Faint dartboard ring patterns are subtly embossed into the wood-panel wall texture at low opacity. Palette: `#1A0E06`, `#5C2A0E`, `#D4820A` lamp glow.

**B) BUTTON LAYOUT**
```
SETTINGS (label)         | top-Y=50px  | x=centered          | size=280x56
SOUND (toggle label)     | top-Y=200px | x=left@60px         | size=200x48
SOUND TOGGLE             | top-Y=200px | x=right@60px        | size=100x48
MUSIC (toggle label)     | top-Y=270px | x=left@60px         | size=200x48
MUSIC TOGGLE             | top-Y=270px | x=right@60px        | size=100x48
VIBRATION (toggle label) | top-Y=340px | x=left@60px         | size=200x48
VIBRATION TOGGLE         | top-Y=340px | x=right@60px        | size=100x48
BACK                     | top-Y=780px | x=left@20px         | size=120x48
```

---

### HowToPlayScreen

**A) BACKGROUND IMAGE**
A chalk-illustrated tutorial feel — a matte dark chalkboard green-black (`#1A2418`) wall texture as the full background. Decorative chalk-drawn border lines trace the edges in white at 40% opacity — slightly rough, hand-drawn feel. Center contains a large blank chalkboard panel (framed by a thin chalk-line border), interior entirely empty for instructions text drawn in code. Upper area has a decorative chalk dartboard sketch — simple rings in white chalk, purely ornamental at 30% opacity. Bottom corners have small chalk dart sketches pointing inward. Overall mood: informal pub game lesson, welcoming and clear.

**B) BUTTON LAYOUT**
```
HOW TO PLAY (label)      | top-Y=50px  | x=centered          | size=300x56
[Instruction text area]  | top-Y=150px | x=centered          | size=420x500
BACK                     | top-Y=780px | x=left@20px         | size=120x48
```

---

## 4. Export Checklist

```
- icon_512.png (512x512)
- ui/main_menu.png (480x854)
- ui/venue_select.png (480x854)
- ui/local_pub.png (480x854)
- ui/tournament.png (480x854)
- ui/champions.png (480x854)
- ui/game_over.png (480x854)
- ui/leaderboard.png (480x854)
- ui/shop.png (480x854)
- ui/settings.png (480x854)
- ui/how_to_play.png (480x854)
```