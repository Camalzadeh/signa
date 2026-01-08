package org.signa.app.domain.repository

import kotlinx.coroutines.flow.Flow
import org.signa.app.domain.model.Signal
import org.signa.app.domain.util.Result
import org.signa.app.domain.util.DataError

interface SignalRepository {
    fun getSignals(): Flow<List<Signal>>
    suspend fun startScanning(): Result<Unit, DataError>
    suspend fun stopScanning()
}
