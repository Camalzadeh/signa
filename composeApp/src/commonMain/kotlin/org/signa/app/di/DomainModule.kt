package org.signa.app.di

import org.koin.dsl.module
import org.signa.app.domain.usecase.ClearAnalysesUseCase
import org.signa.app.domain.usecase.ClearSignalsUseCase
import org.signa.app.domain.usecase.GetAiAnalysisUseCase
import org.signa.app.domain.usecase.GetSignalDetailUseCase
import org.signa.app.domain.usecase.GetSignalsUseCase
import org.signa.app.domain.usecase.ScanSignalsUseCase
import org.signa.app.domain.usecase.UseCases


val domainModule = module {

    factory { ScanSignalsUseCase(get()) }
    factory { GetSignalsUseCase(get()) }
    factory { GetSignalDetailUseCase(get()) }
    factory { GetAiAnalysisUseCase(get()) }
    factory { ClearSignalsUseCase(get()) }
    factory { ClearAnalysesUseCase(get()) }

    factory {
        UseCases(
            scanSignals = get(),
            getSignals = get(),
            getSignalDetail = get(),
            getAiAnalysis = get(),
            clearSignals = get(),
            clearAnalyses = get()
        )
    }
}