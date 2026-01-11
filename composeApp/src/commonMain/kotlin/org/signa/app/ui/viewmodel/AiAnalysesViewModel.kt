package org.signa.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.signa.app.domain.usecase.UseCases

class AiAnalysesViewModel (
    private val useCases: UseCases
): ViewModel(){

    fun clearAnalyses() {
        viewModelScope.launch {
            useCases.clearAnalyses()
        }
    }
}