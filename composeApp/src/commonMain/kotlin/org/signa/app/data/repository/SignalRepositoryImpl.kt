package org.signa.app.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override fun getSignal(id: String): Flow<Signal?> {
        return getSignals().map { signals ->
            signals.find { it.id == id }
        }
    }

    override suspend fun startScanning(): Result<Unit, DataError> {

        return Result.Success(Unit)
    }

    override suspend fun stopScanning() {

    }
}
