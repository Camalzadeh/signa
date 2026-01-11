package org.signa.app.domain.usecase

data class UseCases(
    val scanSignals: ScanSignalsUseCase,
    val getSignals: GetSignalsUseCase,
    val getSignalDetail: GetSignalDetailUseCase,
    val getAiAnalysis: GetAiAnalysisUseCase,
    val clearSignals: ClearSignalsUseCase,
    val clearAnalyses: ClearAnalysesUseCase
)