package org.signa.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.signa.app.domain.model.Signal
import org.signa.app.domain.usecase.UseCases


class SignalViewModel(
    private val useCases: UseCases
) : ViewModel() {

    val signals: StateFlow<List<Signal>> = useCases.getSignals()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // ViewModel yaradılan kimi skaneri işə salırıq
        viewModelScope.launch {
            println("DEBUG: Skaner prosesi başladıldı...")
            useCases.scanSignals()
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            useCases.clearSignals()
        }
    }
}