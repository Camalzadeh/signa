package org.signa.app.domain.usecase

import org.signa.app.domain.core.DataError
import org.signa.app.domain.model.AiAnalysis
import org.signa.app.domain.model.Signal
import org.signa.app.domain.repository.AiRepository
import org.signa.app.domain.core.Result

class GetAiAnalysisUseCase(
    private val repository: AiRepository
) {
    suspend operator fun invoke(
        signal: Signal,
        forceRefresh: Boolean = false
    ): Result<AiAnalysis, DataError> {

        if (signal.strength < -200) {
            return Result.Error(DataError.UNKNOWN)
        }

        return repository.getAnalysis(signal, forceRefresh)
    }
}