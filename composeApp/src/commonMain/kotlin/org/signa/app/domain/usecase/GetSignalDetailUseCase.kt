package org.signa.app.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.signa.app.domain.model.Signal
import org.signa.app.domain.repository.SignalRepository

class GetSignalDetailUseCase(
    private val repository: SignalRepository
) {
    operator fun invoke(id: String): Flow<Signal?> {
        return repository.getSignalById(id)
    }
}