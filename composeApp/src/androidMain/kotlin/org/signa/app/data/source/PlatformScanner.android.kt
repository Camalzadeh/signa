package org.signa.app.data.source

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun getPlatformScanner(): SignalScanner {
    val context = LocalContext.current
    return AndroidSignalScanner(context)
}
