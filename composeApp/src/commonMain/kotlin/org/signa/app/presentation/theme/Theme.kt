package org.signa.app.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = GraphColors.CyberNeon,
    onPrimary = GraphColors.VoidBlack,
    primaryContainer = GraphColors.NebulaPurple.copy(alpha = 0.2f),
    onPrimaryContainer = GraphColors.CyberNeon,
    secondary = GraphColors.SignalGreen,
    onSecondary = GraphColors.VoidBlack,
    background = GraphColors.DeepSpaceBlack,
    onBackground = GraphColors.StarlightWhite,
    surface = GraphColors.VoidBlack,
    onSurface = GraphColors.StarlightWhite,
    error = GraphColors.AlertRed
)

@Composable
fun SignaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = GraphTypography,
        content = content
    )
}
