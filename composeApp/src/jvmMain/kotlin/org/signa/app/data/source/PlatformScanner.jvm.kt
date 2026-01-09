package org.signa.app.data.source

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun getPlatformScanner(): SignalScanner {
    return remember { JvmSignalScanner() }
}