package org.signa.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.signa.app.domain.model.Signal
import org.signa.app.domain.repository.SignalRepository

class SignalViewModel(
    private val repository: SignalRepository
) : ViewModel() {

    val signals: StateFlow<List<Signal>> = repository.getAllSignals()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            println("DEBUG: ViewModel skan prosesini tetiklədi")
            repository.scanAndSaveSignals()
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearAllSignals()
        }
    }
}