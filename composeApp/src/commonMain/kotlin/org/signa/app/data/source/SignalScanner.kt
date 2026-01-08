package org.signa.app.data.source

import kotlinx.coroutines.flow.Flow
import org.signa.app.domain.model.Signal

interface SignalScanner {
    fun startScanning(): Flow<List<Signal>>
}
