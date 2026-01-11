package org.signa.app.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.signa.app.data.dao.SignalDao
import org.signa.app.data.mapper.toDomain
import org.signa.app.data.mapper.toEntity
import org.signa.app.data.source.SignalScanner
import org.signa.app.domain.model.Signal
import org.signa.app.domain.repository.SignalRepository

class SignalRepositoryImpl(
    private val signalDao: SignalDao,
    private val scanner: SignalScanner
) : SignalRepository {

    private val _signals = MutableStateFlow<List<Signal>>(emptyList())
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        scope.launch {
            val initialSignals = signalDao.getAllSignalsFlow().first().map { it.toDomain() }
            _signals.value = initialSignals
        }
    }

    override fun getAllSignals(): Flow<List<Signal>> = _signals.asStateFlow()

    override suspend fun scanAndSaveSignals() {
        scanner.startScanning().collect { incomingSignals ->
            val currentList = _signals.value.toMutableList()

            incomingSignals.forEach { newSignal ->
                val index = currentList.indexOfFirst { it.id == newSignal.id }

                val signalToSave = if (index != -1) {
                    val existing = currentList[index]
                    val updatedHistory = (existing.history + newSignal.history).takeLast(100)
                    newSignal.copy(
                        firstSeen = existing.firstSeen,
                        history = updatedHistory
                    )
                } else {
                    newSignal
                }

                if (index != -1) currentList[index] = signalToSave
                else currentList.add(signalToSave)

                scope.launch {
                    signalDao.upsertSignal(signalToSave.toEntity())
                }
            }

            _signals.value = currentList.sortedByDescending { it.strength }
        }
    }

    override fun getSignalById(id: String): Flow<Signal?> {
        return _signals.map { list -> list.find { it.id == id } }
    }

    override suspend fun clearAllSignals() {
        signalDao.deleteAllSignals()
        _signals.value = emptyList()
    }
}