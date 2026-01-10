package org.signa.app.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.signa.app.data.entity.SignalEntity

@Dao
interface SignalDao {
    @Upsert
    suspend fun upsertSignal(signal: SignalEntity)

    @Query("SELECT * FROM signals ORDER BY lastSeen DESC")
    fun getAllSignalsFlow(): Flow<List<SignalEntity>>

    @Query("SELECT * FROM signals WHERE id = :id")
    suspend fun getSignalById(id: String): SignalEntity?

    @Query("DELETE FROM signals")
    suspend fun deleteAllSignals()

    @Query("DELETE FROM signals WHERE id = :id")
    suspend fun deleteSignalById(id: String)

    @Query("SELECT * FROM signals WHERE id = :id")
    fun getSignalByIdFlow(id: String): Flow<SignalEntity?>
}