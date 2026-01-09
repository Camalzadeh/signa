package org.signa.app.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.signa.app.data.local.SignalDao
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
    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        scope.launch {
            signalDao.getAllSignalsFlow().firstOrNull()?.let { entities ->
                _signals.value = entities.map { it.toDomain() }
            }
        }
    }

    override fun getAllSignals(): Flow<List<Signal>> = _signals.asStateFlow()

    override suspend fun scanAndSaveSignals() {
        scanner.startScanning()
            .onEach { incomingSignals ->
                val currentList = _signals.value.toMutableList()

                incomingSignals.forEach { newSignal ->
                    val index = currentList.indexOfFirst { it.id == newSignal.id }
                    if (index != -1) {
                        val existing = currentList[index]
                        val updatedHistory = (existing.history + newSignal.history).takeLast(100)
                        currentList[index] = newSignal.copy(
                            firstSeen = existing.firstSeen,
                            history = updatedHistory
                        )
                    } else {
                        currentList.add(newSignal)
                    }

                    scope.launch(Dispatchers.IO) {
                        try {
                            val signalToSave = currentList.find { it.id == newSignal.id }
                            signalToSave?.let {
                                signalDao.upsertSignal(it.toEntity())
                            }
                        } catch (e: Exception) {
                            println("DB_SAVE_ERROR: ${e.message}")
                        }
                    }
                }

                _signals.value = currentList.sortedByDescending { it.timestamp }
            }
            .collect()
    }

    override fun getSignal(id: String): Flow<Signal?> {
        return _signals.map { list -> list.find { it.id == id } }
    }

    override suspend fun clearAllSignals() {
        signalDao.deleteAllSignals()
        _signals.value = emptyList()
    }
}