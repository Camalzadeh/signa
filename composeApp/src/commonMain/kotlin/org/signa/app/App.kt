package org.signa.app

import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.signa.app.ui.MainScreen
import org.signa.app.ui.theme.SignaTheme

@Composable
@Preview
fun App() {
    KoinContext{
        SignaTheme {
            MainScreen()
        }
    }
}
