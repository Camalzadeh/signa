package org.signa.app.data.repository

import kotlinx.coroutines.flow.Flow
import org.signa.app.data.source.SignalScanner
import org.signa.app.domain.model.Signal
import org.signa.app.domain.repository.SignalRepository
import org.signa.app.domain.util.Result
import org.signa.app.domain.util.DataError

class SignalRepositoryImpl(
    private val scanner: SignalScanner
) : SignalRepository {

    override fun getSignals(): Flow<List<Signal>> {
        return scanner.startScanning()
    }

    override suspend fun startScanning(): Result<Unit, DataError> {
        // In a real app, we might trigger a scan intent or check permissions here.
        // For now, we assume the scanner flow handles the active scanning loop.
        return Result.Success(Unit)
    }

    override suspend fun stopScanning() {
        // Stop scanning logic
    }
}
