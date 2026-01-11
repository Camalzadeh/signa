package org.signa.app.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.signa.app.domain.model.Signal
import org.signa.app.domain.repository.SignalRepository

class GetSignalsUseCase(
    private val repository: SignalRepository
) {
    operator fun invoke(): Flow<List<Signal>> {
        return repository.getAllSignals()
    }
}