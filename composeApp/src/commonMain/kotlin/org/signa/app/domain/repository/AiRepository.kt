package org.signa.app.domain.repository

import org.signa.app.domain.model.AiAnalysis
import org.signa.app.domain.model.Signal
import org.signa.app.domain.core.DataError
import org.signa.app.domain.core.Result

interface AiRepository {
    suspend fun getAnalysis(signal: Signal, forceRefresh: Boolean = false): Result<AiAnalysis, DataError>
    suspend fun clearOldAnalyses()
}