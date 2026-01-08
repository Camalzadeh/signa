package org.signa.app.data.source

import androidx.compose.runtime.Composable

@Composable
actual fun getPlatformScanner(): SignalScanner {
    return MockSignalScanner() // Fallback to mock on Desktop for now
}
