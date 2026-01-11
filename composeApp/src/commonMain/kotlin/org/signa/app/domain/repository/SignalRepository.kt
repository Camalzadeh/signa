package org.signa.app.domain.repository

import kotlinx.coroutines.flow.Flow
import org.signa.app.domain.model.Signal

interface SignalRepository {
    fun getAllSignals(): Flow<List<Signal>>
    suspend fun scanAndSaveSignals()
    suspend fun clearAllSignals()
    fun getSignalById(id: String): Flow<Signal?>
}