package com.explorefaith.pixelkey.latin.uix

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.StateListDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.Log
import android.util.TypedValue
import androidx.annotation.ColorInt
import com.explorefaith.pixelkey.keyboard.Key
import com.explorefaith.pixelkey.latin.uix.theme.presets.PixelThemeHelper
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.android.material.color.DynamicColors
import com.explorefaith.pixelkey.keyboard.internal.KeyboardIconsSet
import com.explorefaith.pixelkey.latin.R
import com.explorefaith.pixelkey.latin.uix.actions.AllActions
import com.explorefaith.pixelkey.latin.uix.actions.AllActionsMap
import com.explorefaith.pixelkey.latin.uix.theme.DarkColorScheme
import kotlin.math.roundToInt

val KeyBordersSetting = SettingsKey(booleanPreferencesKey("keyBorders"), true)
val HiddenKeysSetting = SettingsKey(booleanPreferencesKey("hiddenKeys"), false)
val KeyHintsSetting   = SettingsKey(booleanPreferencesKey("keyHints"), true)
val KeyboardFontSetting = SettingsKey(stringPreferencesKey("keyboard_font"), "pixel")
val KeyHintColorSetting = SettingsKey(stringPreferencesKey("key_hint_color"), "subtle")
val SpacebarColorSetting = SettingsKey(stringPreferencesKey("spacebar_color"), "default")

// Sticker look (BUG/new-feature): keys drawn raised with shadows, gaps recessed over a
// background image so the keyboard feels like real stickers.
val StickerKeysSetting = SettingsKey(booleanPreferencesKey("stickerKeys"), false)
val KeyboardBgImageSetting = SettingsKey(stringPreferencesKey("keyboard_bg_image"), "none")

val KeyboardHeightMultiplierSetting = SettingsKey(floatPreferencesKey("keyboardHeightMultiplier"), 1.0f)
val KeyboardBottomOffsetSetting = SettingsKey(floatPreferencesKey("keyboardOffset"), 0.0f)

fun adjustColorBrightnessForContrast(bgColor: Int, fgColor: Int, desiredContrast: Float, adjustSaturation: Boolean = false): Int {
    // Convert RGB colors to HSL
    val bgHSL = FloatArray(3)
    ColorUtils.colorToHSL(bgColor, bgHSL)
    val fgHSL = FloatArray(3)
    ColorUtils.colorToHSL(fgColor, fgHSL)

    // Estimate the adjustment needed in lightness to achieve the desired contrast
    // This is a simplified approach and may not be perfectly accurate
    val lightnessAdjustment = (desiredContrast - 1) / 10.0f // Simplified and heuristic-based adjustment

    // Adjust the background color's lightness
    bgHSL[2] = bgHSL[2] + lightnessAdjustment
    bgHSL[2] = bgHSL[2].coerceIn(0f, 1f) // Ensure the lightness stays within valid range

    if(adjustSaturation) {
        bgHSL[1] = (bgHSL[1] + lightnessAdjustment).coerceIn(0f, 1f)
    }

    // Convert back to RGB and return the adjusted color
    return ColorUtils.HSLToColor(bgHSL)
}

fun<T> Preferences.get(key: SettingsKey<T>): T {
    return this[key.key] ?: key.default
}

class BasicThemeProvider(
    val context: Context,
    val overrideColorScheme: ColorScheme? = null,
    val themeKey: String? = null
) : DynamicThemeProvider {
    val currentThemeKey: String = themeKey ?: context.getSettingBlocking(THEME_KEY)
    val isPixelTheme: Boolean = currentThemeKey == "PixelTheme"
    override val primaryKeyboardColor: Int
    override val keyColor: Int

    override val keyboardBackground: Drawable
    override val keyBackground: Drawable
    override val spaceBarBackground: Drawable

    override val keyFeedback: Drawable

    override val moreKeysTextColor: Int
    override val moreKeysKeyboardBackground: Drawable
    override val popupKey: Drawable
    override val actionPopupKey: Drawable

    private val colors: HashMap<Int, Int> = HashMap()
    override fun getColor(i: Int): Int? {
        return colors[i]
    }


    private val drawables: HashMap<Int, Drawable> = HashMap()
    override fun getDrawable(i: Int): Drawable? {
        return drawables[i]
    }

    val icons: HashMap<String, Drawable?> = hashMapOf()
    override fun getIcon(iconName: String): Drawable? {
        if(iconName == KeyboardIconsSet.ICON_UNDEFINED) return null

        if(!icons.containsKey(iconName)) {
            Log.e("BasicThemeProvider", "Unknown icon $iconName")
        }

        return icons[iconName]
    }

    override fun getKeyboardHeightMultiplier(): Float {
        return keyboardHeight
    }

    private fun dp(dp: Dp): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.value,
            context.resources.displayMetrics
        );
    }

    private fun coloredRectangle(@ColorInt color: Int): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(color)
        }
    }

    private fun coloredRoundedRectangle(@ColorInt color: Int, radius: Float): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = radius
            setColor(color)
        }
    }

    /**
     * Sticker key visual (raised key over a recessed background): a dark shadow layer
     * peeking out at the bottom-right, with a gradient fill (light top -> dark bottom)
     * and a thin light outline on top of it. Pressed keys sink: the shadow inset shrinks.
     */
    private fun stickerKeyLayers(@ColorInt baseColor: Int, pressed: Boolean, radius: Float): Drawable {
        return try {
            val shadow = coloredRoundedRectangle(
                ColorUtils.blendARGB(baseColor, 0xFF000000.toInt(), 0.60f), radius)
            val lighten = if (pressed) 0.26f else 0.16f
            val darken = if (pressed) 0.26f else 0.12f
            val alpha = if (keyboardBgImage != "none") 0.88f else 1.0f
            val fill = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = radius
                orientation = GradientDrawable.Orientation.TL_BR
                colors = intArrayOf(
                    ColorUtils.setAlphaComponent(
                        ColorUtils.blendARGB(baseColor, 0xFFFFFFFF.toInt(), lighten),
                        (255 * alpha).roundToInt()),
                    ColorUtils.setAlphaComponent(baseColor, (255 * alpha).roundToInt()),
                    ColorUtils.setAlphaComponent(
                        ColorUtils.blendARGB(baseColor, 0xFF000000.toInt(), darken),
                        (255 * alpha).roundToInt())
                )
                setStroke(dp(1.dp).roundToInt(),
                    ColorUtils.setAlphaComponent(0xFFFFFFFF.toInt(), if (pressed) 110 else 60))
            }
            LayerDrawable(arrayOf(shadow, fill)).apply {
                val bottomInset = if (pressed) dp(1.5.dp).roundToInt() else dp(3.dp).roundToInt()
                setLayerInset(1,
                    dp(1.dp).roundToInt(), dp(1.dp).roundToInt(),
                    dp(2.dp).roundToInt(), bottomInset)
            }
        } catch (t: Throwable) {
            // The keyboard must never fail to draw: fall back to a plain key fill.
            Log.e("BasicThemeProvider", "Sticker key drawable failed; using plain fill", t)
            coloredRoundedRectangle(baseColor, radius)
        }
    }

    private fun keyVisual(baseColor: Int, radius: Float): Drawable =
        if (stickerKeys) stickerKeyLayers(baseColor, false, radius)
        else coloredRoundedRectangle(baseColor, radius)

    /** Background image + scrim so keys read as stickers on top of the artwork. */
    private fun buildKeyboardBackground(isPixelThemeFlag: Boolean, fallbackColor: Int): Drawable {
        val base = if (isPixelThemeFlag) coloredRectangle(0xFF000000.toInt())
                   else coloredRectangle(fallbackColor)
        if (keyboardBgImage == "none") return base
        val resId = when (keyboardBgImage) {
            "bride" -> R.drawable.kb_bg_bride
            "elf" -> R.drawable.kb_bg_elf
            "night" -> R.drawable.kb_bg_night
            "floral" -> R.drawable.kb_bg_floral
            "sunset" -> R.drawable.kb_bg_sunset
            else -> 0
        }
        if (resId == 0) return base
        return try {
            val image = android.graphics.drawable.BitmapDrawable(
                context.resources, android.graphics.BitmapFactory.decodeResource(context.resources, resId)
            ).apply { setGravity(android.view.Gravity.FILL) }
            val scrimColor = ColorUtils.blendARGB(fallbackColor, 0xFF000000.toInt(), 0.55f)
            LayerDrawable(arrayOf(image, coloredRectangle(scrimColor)))
        } catch (t: Throwable) {
            Log.e("BasicThemeProvider", "Failed to load keyboard background image", t)
            base
        }
    }

    private fun coloredOval(@ColorInt color: Int): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            cornerRadius = Float.MAX_VALUE
            setColor(color)
        }
    }

    private fun StateListDrawable.addStateWithHighlightLayerOnPressed(@ColorInt highlight: Int, stateSet: IntArray, drawable: Drawable, cornerRadius: Dp = 8.dp) {
        addState(intArrayOf(android.R.attr.state_pressed) + stateSet, LayerDrawable(
            arrayOf(
                drawable,
                coloredRoundedRectangle(highlight, dp(cornerRadius))
            )
        )
        )
        addState(stateSet, drawable)
    }

    val expertMode: Boolean
    val keyBorders: Boolean
    val showKeyHints: Boolean
    val keyHintColor: String
    val keyboardHeight: Float
    val keyboardFont: String
    val spacebarColorPref: String
    val stickerKeys: Boolean
    val keyboardBgImage: String
    override val keyboardTypeface: Typeface?

    fun hasUpdated(np: Preferences): Boolean {
        return np.get(HiddenKeysSetting) != expertMode
                || np.get(KeyBordersSetting) != keyBorders
                || np.get(KeyHintsSetting) != showKeyHints
                || np.get(KeyHintColorSetting) != keyHintColor
                || np.get(KeyboardHeightMultiplierSetting) != keyboardHeight
                || np.get(KeyboardFontSetting) != keyboardFont
                || np.get(SpacebarColorSetting) != spacebarColorPref
                || np.get(StickerKeysSetting) != stickerKeys
                || np.get(KeyboardBgImageSetting) != keyboardBgImage
    }


    private fun addIcon(iconName: String, drawableIntResId: Int, tint: Int) {
        addIcon(iconName, AppCompatResources.getDrawable(
            context,
            drawableIntResId
        ), tint)
    }

    private fun addIcon(iconName: String, drawable: Drawable?, tint: Int) {
        icons[iconName] = drawable?.apply {
            setTint(tint)
        }
    }

    init {
        val colorScheme = if(overrideColorScheme != null) {
            overrideColorScheme
        }else if(!DynamicColors.isDynamicColorAvailable()) {
            DarkColorScheme
        } else {
            val dCtx = DynamicColors.wrapContextIfAvailable(context)

            dynamicLightColorScheme(dCtx)
        }

        expertMode = context.getSettingBlocking(HiddenKeysSetting)
        keyBorders = context.getSettingBlocking(KeyBordersSetting)
        showKeyHints = context.getSettingBlocking(KeyHintsSetting)
        keyHintColor = context.getSettingBlocking(KeyHintColorSetting)
        keyboardHeight = context.getSettingBlocking(KeyboardHeightMultiplierSetting)
        keyboardFont = context.getSettingBlocking(KeyboardFontSetting)
        spacebarColorPref = context.getSettingBlocking(SpacebarColorSetting)
        stickerKeys = context.getSettingBlocking(StickerKeysSetting)
        keyboardBgImage = context.getSettingBlocking(KeyboardBgImageSetting)
        val customSpaceColor = when (spacebarColorPref) {
            "red" -> PixelThemeHelper.COLOR_RED
            "blue" -> PixelThemeHelper.COLOR_BLUE
            "yellow" -> PixelThemeHelper.COLOR_YELLOW
            "white" -> PixelThemeHelper.COLOR_WHITE
            "dark" -> PixelThemeHelper.COLOR_DARK
            else -> null
        }
        PixelThemeHelper.customSpacebarColor = customSpaceColor
        keyboardTypeface = when {
            keyboardFont == "normal" -> Typeface.DEFAULT
            keyboardFont == "pixel" ->
                ResourcesCompat.getFont(context, R.font.pixelify) ?: Typeface.DEFAULT
            else -> ResourcesCompat.getFont(context, R.font.space_grotesk) ?: Typeface.DEFAULT
        }

        val primary = colorScheme.primary.toArgb()
        val secondary = colorScheme.secondary.toArgb()
        val highlight = colorScheme.outline.copy(alpha = 0.33f).toArgb()

        val background = colorScheme.surface.toArgb()
        val surface = colorScheme.background.toArgb()
        val outline = colorScheme.outline.toArgb()

        val primaryContainer = colorScheme.primaryContainer.toArgb()
        val onPrimaryContainer = colorScheme.onPrimaryContainer.toArgb()

        val onPrimary = colorScheme.onPrimary.toArgb()
        val onPrimaryThird = colorScheme.onPrimary.copy(alpha = 0.33f).toArgb()
        val onSecondary = colorScheme.onSecondary.toArgb()
        val onBackground = colorScheme.onBackground.toArgb()
        val onBackgroundHalf = colorScheme.onBackground.copy(alpha = 0.5f).toArgb()
        val onBackgroundThird = colorScheme.onBackground.copy(alpha = 0.33f).toArgb()

        val transparent = Color.Transparent.toArgb()
        val yellowAccent = 0xFFFFCC00.toInt()
        primaryKeyboardColor = if(keyBorders) {
            colorScheme.background.toArgb()
        } else {
            colorScheme.surface.toArgb()
        }

        val ratio = 1.5f
        val keyColor = if(keyBorders) {
            var c = adjustColorBrightnessForContrast(primaryKeyboardColor, primaryKeyboardColor, ratio)
            if(c == primaryKeyboardColor) {
                // May happen if the color is already 100% white
                c = adjustColorBrightnessForContrast(primaryKeyboardColor, primaryKeyboardColor, 1.0f / (ratio / 2.0f + 0.5f))
            }
            c
        } else {
            transparent
        }

        this.keyColor = keyColor

        val functionalKeyColor = if(keyBorders) {
            adjustColorBrightnessForContrast(primaryKeyboardColor, primaryKeyboardColor, ratio / 2.0f + 0.5f, adjustSaturation = true)
        } else {
            transparent
        }

        val enterKeyBackground = if(expertMode) { functionalKeyColor } else { primary }
        val enterKeyForeground = if(expertMode) { onBackgroundThird } else { onPrimary }

        val hintColor = if (keyHintColor == "yellow") yellowAccent else onBackgroundHalf
        colors[R.styleable.Keyboard_Key_keyTextColor] = onBackground
        colors[R.styleable.Keyboard_Key_keyTextInactivatedColor] = onBackgroundHalf
        colors[R.styleable.Keyboard_Key_keyPressedTextColor] = onPrimary
        colors[R.styleable.Keyboard_Key_keyTextShadowColor] = 0
        colors[R.styleable.Keyboard_Key_actionKeyTextColor] = enterKeyForeground
        colors[R.styleable.Keyboard_Key_functionalTextColor] = onBackground
        colors[R.styleable.Keyboard_Key_keyHintLetterColor] = hintColor
        colors[R.styleable.Keyboard_Key_keyHintLabelColor] = hintColor
        colors[R.styleable.Keyboard_Key_keyShiftedLetterHintInactivatedColor] = hintColor
        colors[R.styleable.Keyboard_Key_keyShiftedLetterHintActivatedColor] = hintColor
        colors[R.styleable.Keyboard_Key_keyPreviewTextColor] = onSecondary
        colors[R.styleable.MainKeyboardView_languageOnSpacebarTextColor] = yellowAccent
        colors[R.styleable.MainKeyboardView_gestureTrailColor] = primary
        colors[R.styleable.MainKeyboardView_slidingKeyInputPreviewColor] = primary

        addIcon(KeyboardIconsSet.NAME_SHIFT_KEY, R.drawable.pixel_shift, onBackground)
        addIcon(KeyboardIconsSet.NAME_SHIFT_KEY_SHIFTED, R.drawable.pixel_shifted, onBackground)
        addIcon(KeyboardIconsSet.NAME_DELETE_KEY, R.drawable.pixel_delete, onBackground)
        addIcon(KeyboardIconsSet.NAME_SETTINGS_KEY, R.drawable.settings, onBackground)
        addIcon(KeyboardIconsSet.NAME_SPACE_KEY, null, onBackground)
        addIcon(KeyboardIconsSet.NAME_ENTER_KEY, R.drawable.pixel_enter, enterKeyForeground)
        addIcon(KeyboardIconsSet.NAME_GO_KEY, R.drawable.sym_keyboard_go_lxx_light, enterKeyForeground)
        addIcon(KeyboardIconsSet.NAME_SEARCH_KEY, R.drawable.pixel_search, enterKeyForeground)
        addIcon(KeyboardIconsSet.NAME_SEND_KEY, R.drawable.sym_keyboard_send_lxx_light, enterKeyForeground)
        addIcon(KeyboardIconsSet.NAME_NEXT_KEY, R.drawable.sym_keyboard_next_lxx_light, enterKeyForeground)
        addIcon(KeyboardIconsSet.NAME_DONE_KEY, R.drawable.sym_keyboard_done_lxx_light, enterKeyForeground)
        addIcon(KeyboardIconsSet.NAME_PREVIOUS_KEY, R.drawable.sym_keyboard_previous_lxx_light, enterKeyForeground)
        addIcon(KeyboardIconsSet.NAME_TAB_KEY, R.drawable.sym_keyboard_tab_holo_dark, onBackground) // TODO: Correct tint
        addIcon(KeyboardIconsSet.NAME_ZWNJ_KEY, R.drawable.sym_keyboard_zwnj_lxx_dark, onBackground)
        addIcon(KeyboardIconsSet.NAME_ZWJ_KEY, R.drawable.sym_keyboard_zwj_lxx_dark, onPrimary)

        addIcon(KeyboardIconsSet.NAME_EMOJI_ACTION_KEY, R.drawable.pixel_smile, onPrimary)
        addIcon(KeyboardIconsSet.NAME_EMOJI_NORMAL_KEY, R.drawable.pixel_smile, onBackground)

        // Add by name (action_emoji)
        AllActionsMap.forEach { (i, it) ->
            addIcon("action_${i}", it.icon, onBackground)
        }

        // Add by id (action_0)
        AllActions.forEachIndexed { i, it ->
            addIcon("action_${i}", it.icon, onBackground)
        }

        if(!showKeyHints) {
            colors[R.styleable.Keyboard_Key_keyHintLetterColor] = transparent
            colors[R.styleable.Keyboard_Key_keyHintLabelColor] = transparent
            colors[R.styleable.Keyboard_Key_keyShiftedLetterHintInactivatedColor] = transparent
            colors[R.styleable.Keyboard_Key_keyShiftedLetterHintActivatedColor] = transparent
        }

        if(expertMode) {
            colors[R.styleable.Keyboard_Key_keyTextColor] = transparent
            colors[R.styleable.Keyboard_Key_keyTextInactivatedColor] = transparent
            colors[R.styleable.Keyboard_Key_keyHintLetterColor] = transparent
            colors[R.styleable.Keyboard_Key_keyHintLabelColor] = transparent


            // Note: We don't fully hide some things, but fade them away as they may be important landmarks
            colors[R.styleable.Keyboard_Key_functionalTextColor] = onBackgroundThird
        }

        keyboardBackground = buildKeyboardBackground(isPixelTheme, primaryKeyboardColor)

        keyBackground = StateListDrawable().apply {
            addStateWithHighlightLayerOnPressed(highlight, intArrayOf(android.R.attr.state_active),
                if (stickerKeys) stickerKeyLayers(enterKeyBackground, false, dp(128.dp))
                else coloredRoundedRectangle(enterKeyBackground, dp(128.dp)),
                cornerRadius = 128.dp
            )

            addStateWithHighlightLayerOnPressed(highlight, intArrayOf(android.R.attr.state_checkable, android.R.attr.state_checked),
                if (stickerKeys) stickerKeyLayers(colorScheme.secondaryContainer.toArgb(), false, dp(8.dp))
                else coloredRoundedRectangle(colorScheme.secondaryContainer.toArgb(), dp(8.dp))
            )

            addStateWithHighlightLayerOnPressed(highlight, intArrayOf(android.R.attr.state_checkable),
                if(keyBorders) {
                    keyVisual(keyColor, dp(12.dp))
                } else {
                    coloredRectangle(transparent)
                }
            )

            addStateWithHighlightLayerOnPressed(highlight, intArrayOf(android.R.attr.state_first),
                if(keyBorders) {
                    keyVisual(functionalKeyColor, dp(8.dp))
                } else {
                    coloredRectangle(transparent)
                }
            )

            addStateWithHighlightLayerOnPressed(highlight, intArrayOf(android.R.attr.state_empty),
                if(keyBorders) {
                    keyVisual(keyColor, dp(12.dp))
                } else {
                    coloredRectangle(transparent)
                }
            )

            addStateWithHighlightLayerOnPressed(highlight, intArrayOf(),
                if(keyBorders) {
                    keyVisual(keyColor, dp(12.dp))
                } else {
                    coloredRectangle(transparent)
                }
            )
        }

        val spaceCornerRadius = if(keyBorders) {
            12.dp
        } else {
            48.dp
        }

        val effectiveSpaceColor = customSpaceColor ?: (if(keyBorders) keyColor else highlight)
        val spaceDrawable = if(customSpaceColor != null) {
            coloredRoundedRectangle(effectiveSpaceColor, dp(spaceCornerRadius)).apply {
                setStroke(dp(1.5.dp).roundToInt(), if (spacebarColorPref == "yellow") PixelThemeHelper.COLOR_DARK else yellowAccent)
            }
        } else if(keyBorders) {
            coloredRoundedRectangle(keyColor, dp(spaceCornerRadius)).apply {
                setStroke(dp(1.5.dp).roundToInt(), yellowAccent)
            }
        } else if(expertMode) {
            coloredRoundedRectangle(colorScheme.outline.copy(alpha = 0.1f).toArgb(), dp(spaceCornerRadius))
        } else {
            coloredRoundedRectangle(highlight, dp(spaceCornerRadius)).apply {
                setStroke(dp(1.5.dp).roundToInt(), yellowAccent)
            }
        }

        spaceBarBackground = StateListDrawable().apply {
            addState(intArrayOf(android.R.attr.state_pressed),
                LayerDrawable(
                    arrayOf(
                        if (stickerKeys) stickerKeyLayers(effectiveSpaceColor, true, dp(spaceCornerRadius)) else spaceDrawable,
                        coloredRoundedRectangle(highlight, dp(spaceCornerRadius))
                    )
                )
            )
            addState(intArrayOf(),
                if (stickerKeys) stickerKeyLayers(effectiveSpaceColor, false, dp(spaceCornerRadius)) else spaceDrawable
            )
        }

        keyFeedback = ShapeDrawable().apply {
            paint.color = secondary
            shape = RoundRectShape(
                floatArrayOf(
                    dp(8.dp), dp(8.dp), dp(8.dp), dp(8.dp),
                    dp(8.dp), dp(8.dp), dp(8.dp), dp(8.dp),
                ), null, null
            )

            intrinsicWidth = dp(48.dp).roundToInt()
            intrinsicHeight = dp(24.dp).roundToInt()

            setPadding(0, 0, 0, dp(50.dp).roundToInt())
        }

        moreKeysTextColor = onPrimaryContainer
        moreKeysKeyboardBackground = coloredRoundedRectangle(primaryContainer, dp(8.dp))
        popupKey = StateListDrawable().apply {
            addStateWithHighlightLayerOnPressed(primary, intArrayOf(),
                coloredRoundedRectangle(primaryContainer, dp(8.dp))
            )
        }
        actionPopupKey = StateListDrawable().apply {
            addStateWithHighlightLayerOnPressed(primary, intArrayOf(),
                coloredRoundedRectangle(primaryContainer, dp(128.dp)),
                128.dp
            )
        }

    }


    private var cachedCornerRadius = -1f
    private var cachedPreviewWidth = 0
    private var cachedPreviewHeight = 0
    private var cachedPreviewPadding = 0

    private fun ensureDimensions() {
        if (cachedCornerRadius < 0f) {
            cachedCornerRadius = dp(12.dp)
            cachedPreviewWidth = dp(48.dp).roundToInt()
            cachedPreviewHeight = dp(24.dp).roundToInt()
            cachedPreviewPadding = dp(50.dp).roundToInt()
        }
    }

    override fun getKeyBackground(key: Key, isPressed: Boolean): Drawable? {
        if (!isPixelTheme) return null
        ensureDimensions()
        return PixelThemeHelper.getKeyBackground(key, isPressed, cachedCornerRadius)
    }

    override fun getKeyTextColor(key: Key): Int? {
        if (!isPixelTheme) return null
        return PixelThemeHelper.getKeyTextColor(key)
    }

    override fun getKeyHintColor(key: Key): Int? {
        if (!isPixelTheme) return null
        return PixelThemeHelper.getKeyHintColor(key)
    }

    override fun getKeyPreviewBackground(key: Key): Drawable? {
        if (!isPixelTheme) return null
        ensureDimensions()
        return PixelThemeHelper.getKeyPreviewBackground(
            key,
            cachedCornerRadius,
            cachedPreviewWidth,
            cachedPreviewHeight,
            cachedPreviewPadding
        )
    }
}
