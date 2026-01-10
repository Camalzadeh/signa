package org.signa.app.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import org.signa.app.data.entity.AiAnalysisEntity

@Dao
interface AiDao {
    @Query("SELECT * FROM ai_analyses WHERE signalId = :id")
    suspend fun getAnalysisForSignal(id: String): AiAnalysisEntity?

    @Upsert
    suspend fun saveAnalysis(analysis: AiAnalysisEntity)

    @Query("DELETE FROM ai_analyses")
    suspend fun deleteAllAnalyses()
}