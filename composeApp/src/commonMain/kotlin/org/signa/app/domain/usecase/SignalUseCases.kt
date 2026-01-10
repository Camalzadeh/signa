package org.signa.app.domain.usecase

data class SignalUseCases(
    val getSignals: GetSignalsUseCase,
    val getAiAnalysis: GetAiAnalysisUseCase,
    val clearSignals: ClearSignalsUseCase,
    val clearAnalyses: ClearAnalysesUseCase
)