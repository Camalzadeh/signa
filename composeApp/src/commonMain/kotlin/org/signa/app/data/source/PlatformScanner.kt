package org.signa.app.data.source

import androidx.compose.runtime.Composable

@Composable
expect fun getPlatformScanner(): SignalScanner
