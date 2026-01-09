package org.signa.app.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.signa.app.data.local.SignalDao
import org.signa.app.data.local.SignalEntity
import org.signa.app.data.source.SignalScanner
import org.signa.app.domain.model.Signal
import org.signa.app.domain.model.SignalPoint
import org.signa.app.domain.model.SignalType
import org.signa.app.domain.repository.SignalRepository
import org.signa.app.domain.util.Result
import org.signa.app.domain.util.DataError

class SignalRepositoryImpl(
    private val scanner: SignalScanner,
    private val dao: SignalDao
) : SignalRepository {

    private val _signals = MutableStateFlow<List<Signal>>(emptyList())
    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        scope.launch {
            dao.getAllSignals().first().let { entities ->
                _signals.value = entities.map { it.toDomain() }
            }

            scanner.startScanning().collect { newSignals ->
                val currentMap = _signals.value.associateBy { it.id }.toMutableMap()

                newSignals.forEach { newSignal ->
                    dao.insertSignal(newSignal.toEntity())

                    val existing = currentMap[newSignal.id]
                    if (existing != null) {
                        val updatedHistory = (existing.history + newSignal.history).takeLast(100)
                        currentMap[newSignal.id] = newSignal.copy(
                            firstSeen = existing.firstSeen,
                            history = updatedHistory
                        )
                    } else {
                        currentMap[newSignal.id] = newSignal
                    }
                }

                _signals.value = currentMap.values.sortedByDescending { it.timestamp }
            }
        }
    }

    override fun getSignals(): Flow<List<Signal>> = _signals

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

fun Signal.toEntity(): SignalEntity = SignalEntity(
    id = this.id,
    name = this.name,
    type = this.type.name,
    strength = this.strength,
    timestamp = this.timestamp,
    isSuspicious = this.isSuspicious
)

fun SignalEntity.toDomain(): Signal = Signal(
    id = this.id,
    name = this.name,
    type = try { SignalType.valueOf(this.type) } catch (e: Exception) { SignalType.OTHER },
    strength = this.strength,
    timestamp = this.timestamp,
    isSuspicious = this.isSuspicious,
    macAddress = this.id,
    history = listOf(SignalPoint(this.timestamp, this.strength))
)