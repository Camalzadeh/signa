package org.signa.app.ui.utils

import org.signa.app.domain.model.AiAnalysis

sealed interface AiAnalysisState {
    data object Idle : AiAnalysisState

    data object Loading : AiAnalysisState

    data class Success(val analysis: AiAnalysis) : AiAnalysisState

    data class Error(val message: String) : AiAnalysisState
}