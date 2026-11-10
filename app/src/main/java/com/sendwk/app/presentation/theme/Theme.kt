package com.sendwk.app.presentation.theme

import android.app.Activity
import android.content.ContextWrapper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

private val DarkFuturisticScheme = darkColorScheme(
    primary = CyanNeon,
    secondary = MagentaNeon,
    tertiary = YellowNeon,
    background = DeepSpace,
    surface = DarkNebula,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = GhostWhite,
    onSurface = GhostWhite,
    surfaceVariant = RetroGray,
    onSurfaceVariant = CyanNeon.copy(alpha = 0.7f),
    error = ErrorNeon
)

private val LightFuturisticScheme = lightColorScheme(
    primary = PrimaryVariant,
    secondary = SecondaryVariant,
    tertiary = YellowNeon,
    background = SoftWhite,
    surface = LightSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = DeepSpace,
    onSurface = DeepSpace,
    surfaceVariant = SoftGray,
    onSurfaceVariant = PrimaryVariant.copy(alpha = 0.7f),
    error = ErrorNeon
)

val FuturisticTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 40.sp,
        letterSpacing = 1.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        color = CyanNeon
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.25.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        color = MagentaNeon
    )
)

@Composable
fun SendWkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkFuturisticScheme else LightFuturisticScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            var context = view.context
            while (context is ContextWrapper) {
                if (context is Activity) break
                context = context.baseContext
            }
            if (context is Activity) {
                val window = context.window
                window.statusBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = FuturisticTypography,
        content = content
    )
}
