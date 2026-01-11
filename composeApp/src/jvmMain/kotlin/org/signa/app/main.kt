package org.signa.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.compose.resources.painterResource
import org.signa.app.di.initKoin
import signa.composeapp.generated.resources.Res
import signa.composeapp.generated.resources.logo

fun main() = application {

    initKoin()

    Window(
        onCloseRequest = ::exitApplication,
        title = "Signa",
        icon = painterResource(Res.drawable.logo)
    ) {
        App()
    }
}
