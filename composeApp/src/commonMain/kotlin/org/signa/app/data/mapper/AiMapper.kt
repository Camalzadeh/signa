package org.signa.app.data.mapper

import org.signa.app.data.entity.AiAnalysisEntity
import org.signa.app.domain.model.AiAnalysis

fun AiAnalysisEntity.toDomain() = AiAnalysis(
    signalId = signalId,
    analysisText = analysisText,
    timestamp = timestamp
)

fun AiAnalysis.toEntity() = AiAnalysisEntity(
    signalId = signalId,
    analysisText = analysisText,
    timestamp = timestamp
)