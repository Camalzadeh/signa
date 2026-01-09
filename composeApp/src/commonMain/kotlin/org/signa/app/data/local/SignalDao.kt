package org.signa.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SignalDao {
    @Upsert
    suspend fun upsertSignal(signal: SignalEntity)

    @Query("SELECT * FROM signals ORDER BY lastSeen DESC")
    fun getAllSignalsFlow(): Flow<List<SignalEntity>>

    @Query("SELECT * FROM signals WHERE id = :id")
    suspend fun getSignalById(id: String): SignalEntity?
}