package org.signa.app

import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.signa.app.presentation.screen.DashboardScreen
import org.signa.app.presentation.theme.SignaTheme

@Composable
@Preview
fun App() {
    SignaTheme {
        DashboardScreen()
    }
}
