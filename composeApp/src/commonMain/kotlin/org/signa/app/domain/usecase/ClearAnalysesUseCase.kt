package org.signa.app.domain.usecase

import org.signa.app.domain.repository.AiRepository

class ClearAnalysesUseCase(
    private val repository: AiRepository
) {
    suspend operator fun invoke() {
        repository.clearOldAnalyses()
    }
}