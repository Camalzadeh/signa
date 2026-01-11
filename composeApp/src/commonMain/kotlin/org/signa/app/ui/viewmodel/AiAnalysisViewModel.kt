package org.signa.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.signa.app.domain.usecase.UseCases
import org.signa.app.domain.core.Result
import org.signa.app.domain.model.Signal
import org.signa.app.ui.utils.AiAnalysisState

class AiAnalysisViewModel(
    private val signalId: String,
    private val useCases: UseCases
) : ViewModel() {

    private val _signal = MutableStateFlow<Signal?>(null)
    val signal = _signal.asStateFlow()

    private val _analysisState = MutableStateFlow<AiAnalysisState>(AiAnalysisState.Idle)
    val analysisState = _analysisState.asStateFlow()

    init {
        // Ekran açılan kimi yalnız siqnalın detallarını (ad, güc və s.) gətiririk
        fetchSignalOnly()
    }

    private fun fetchSignalOnly() {
        viewModelScope.launch {
            val currentSignal = useCases.getSignalDetail(signalId).firstOrNull()
            _signal.value = currentSignal

            // Əgər əvvəllər bu siqnal üçün analiz edilibsə, onu gətirək
            if (currentSignal != null) {
                val cached = useCases.getAiAnalysis(currentSignal, forceRefresh = false)
                if (cached is Result.Success) {
                    _analysisState.value = AiAnalysisState.Success(cached.data)
                }
            }
        }
    }

    fun startNewAnalysis() {
        val currentSignal = _signal.value ?: return

        viewModelScope.launch {
            _analysisState.value = AiAnalysisState.Loading
            val result = useCases.getAiAnalysis(currentSignal, forceRefresh = true)

            _analysisState.value = when (result) {
                is Result.Success -> AiAnalysisState.Success(result.data)
                is Result.Error -> AiAnalysisState.Error(result.error.toString())
            }
        }
    }
}

