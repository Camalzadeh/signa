package org.signa.app.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.signa.app.ui.viewmodel.AiAnalysesViewModel
import org.signa.app.ui.viewmodel.AiAnalysisViewModel
import org.signa.app.ui.viewmodel.SignalViewModel

val uiModule = module {
    viewModel {
        SignalViewModel(get())
    }

    viewModel {
        AiAnalysesViewModel(get())
    }

    viewModel { (id: String) ->
        AiAnalysisViewModel(signalId = id, useCases = get())
    }


}
