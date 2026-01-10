package org.signa.app.data.service

import org.signa.app.domain.model.Signal
import org.signa.app.domain.core.Result
import org.signa.app.domain.core.DataError

interface AiService {
    suspend fun analyzeSignals(signals: List<Signal>): Result<String, DataError>
}
