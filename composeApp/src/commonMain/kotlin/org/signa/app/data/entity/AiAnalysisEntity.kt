package org.signa.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ai_analyses")
data class AiAnalysisEntity(
    @PrimaryKey val signalId: String,
    val analysisText: String,
    val timestamp: Long
)