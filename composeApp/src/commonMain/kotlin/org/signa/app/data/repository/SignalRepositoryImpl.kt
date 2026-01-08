package org.signa.app.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.signa.app.data.source.SignalScanner
import org.signa.app.domain.model.Signal
import org.signa.app.domain.repository.SignalRepository
import org.signa.app.domain.util.Result
import org.signa.app.domain.util.DataError

class SignalRepositoryImpl(
    private val scanner: SignalScanner
) : SignalRepository {

    private val _signals = kotlinx.coroutines.flow.MutableStateFlow<List<Signal>>(emptyList())
    private val scope = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default)

    init {
        scope.launch {
            scanner.startScanning().collect { newSignals ->
                val currentMap = _signals.value.associateBy { it.id }.toMutableMap()
                
                newSignals.forEach { newSignal ->
                    val existing = currentMap[newSignal.id]
                    if (existing != null) {
                        // Merge
                        val updatedHistory = (existing.history + newSignal.history).distinctBy { it.timestamp }
                        currentMap[newSignal.id] = newSignal.copy(
                            firstSeen = existing.firstSeen,
                            history = updatedHistory
                        )
                    } else {
                        currentMap[newSignal.id] = newSignal
                    }
                }
                
                // Sort by lastSeen descending (Active first)
                _signals.value = currentMap.values.sortedByDescending { it.lastSeen }
            }
        }
    }

    override fun getSignals(): Flow<List<Signal>> {
        return _signals
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
