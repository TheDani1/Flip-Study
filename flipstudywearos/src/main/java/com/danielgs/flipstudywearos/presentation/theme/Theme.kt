package com.danielgs.flipstudywearos.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme
import com.example.flipstudy.theme.md_theme_dark_background
import com.example.flipstudy.theme.md_theme_dark_error
import com.example.flipstudy.theme.md_theme_dark_errorContainer
import com.example.flipstudy.theme.md_theme_dark_inverseOnSurface
import com.example.flipstudy.theme.md_theme_dark_inversePrimary
import com.example.flipstudy.theme.md_theme_dark_inverseSurface
import com.example.flipstudy.theme.md_theme_dark_onBackground
import com.example.flipstudy.theme.md_theme_dark_onError
import com.example.flipstudy.theme.md_theme_dark_onErrorContainer
import com.example.flipstudy.theme.md_theme_dark_onPrimary
import com.example.flipstudy.theme.md_theme_dark_onPrimaryContainer
import com.example.flipstudy.theme.md_theme_dark_onSecondary
import com.example.flipstudy.theme.md_theme_dark_onSecondaryContainer
import com.example.flipstudy.theme.md_theme_dark_onSurface
import com.example.flipstudy.theme.md_theme_dark_onSurfaceVariant
import com.example.flipstudy.theme.md_theme_dark_onTertiary
import com.example.flipstudy.theme.md_theme_dark_onTertiaryContainer
import com.example.flipstudy.theme.md_theme_dark_outline
import com.example.flipstudy.theme.md_theme_dark_outlineVariant
import com.example.flipstudy.theme.md_theme_dark_primary
import com.example.flipstudy.theme.md_theme_dark_primaryContainer
import com.example.flipstudy.theme.md_theme_dark_scrim
import com.example.flipstudy.theme.md_theme_dark_secondary
import com.example.flipstudy.theme.md_theme_dark_secondaryContainer
import com.example.flipstudy.theme.md_theme_dark_surface
import com.example.flipstudy.theme.md_theme_dark_surfaceTint
import com.example.flipstudy.theme.md_theme_dark_surfaceVariant
import com.example.flipstudy.theme.md_theme_dark_tertiary
import com.example.flipstudy.theme.md_theme_dark_tertiaryContainer
import com.example.flipstudy.theme.md_theme_light_background
import com.example.flipstudy.theme.md_theme_light_error
import com.example.flipstudy.theme.md_theme_light_errorContainer
import com.example.flipstudy.theme.md_theme_light_inverseOnSurface
import com.example.flipstudy.theme.md_theme_light_inversePrimary
import com.example.flipstudy.theme.md_theme_light_inverseSurface
import com.example.flipstudy.theme.md_theme_light_onBackground
import com.example.flipstudy.theme.md_theme_light_onError
import com.example.flipstudy.theme.md_theme_light_onErrorContainer
import com.example.flipstudy.theme.md_theme_light_onPrimary
import com.example.flipstudy.theme.md_theme_light_onPrimaryContainer
import com.example.flipstudy.theme.md_theme_light_onSecondary
import com.example.flipstudy.theme.md_theme_light_onSecondaryContainer
import com.example.flipstudy.theme.md_theme_light_onSurface
import com.example.flipstudy.theme.md_theme_light_onSurfaceVariant
import com.example.flipstudy.theme.md_theme_light_onTertiary
import com.example.flipstudy.theme.md_theme_light_onTertiaryContainer
import com.example.flipstudy.theme.md_theme_light_outline
import com.example.flipstudy.theme.md_theme_light_outlineVariant
import com.example.flipstudy.theme.md_theme_light_primary
import com.example.flipstudy.theme.md_theme_light_primaryContainer
import com.example.flipstudy.theme.md_theme_light_scrim
import com.example.flipstudy.theme.md_theme_light_secondary
import com.example.flipstudy.theme.md_theme_light_secondaryContainer
import com.example.flipstudy.theme.md_theme_light_surface
import com.example.flipstudy.theme.md_theme_light_surfaceTint
import com.example.flipstudy.theme.md_theme_light_surfaceVariant
import com.example.flipstudy.theme.md_theme_light_tertiary
import com.example.flipstudy.theme.md_theme_light_tertiaryContainer
import com.example.flipstudy.theme.wearColorPalette

private val LightColors = Colors(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    error = md_theme_light_error,
    onError = md_theme_light_onError,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
)

private val DarkColors = Colors(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
)

@Composable
fun FlipStudyTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (!useDarkTheme) {
        LightColors
    } else {
        DarkColors
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
}