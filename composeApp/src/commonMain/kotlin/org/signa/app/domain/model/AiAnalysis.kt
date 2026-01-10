package org.signa.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AiAnalysis(
    val signalId: String,
    val analysisText: String,
    val timestamp: Long
)
