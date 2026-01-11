package org.signa.app.domain.usecase

import org.signa.app.domain.repository.SignalRepository

class ScanSignalsUseCase(private val repository: SignalRepository) {
    suspend operator fun invoke() {
        repository.scanAndSaveSignals()
    }
}