package org.signa.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.signa.app.di.initKoin

fun main() = application {

    initKoin()

    Window(
        onCloseRequest = ::exitApplication,
        title = "KotlinProject",
    ) {
        App()
    }
}
