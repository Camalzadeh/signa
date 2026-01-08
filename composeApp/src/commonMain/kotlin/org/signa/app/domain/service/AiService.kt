package org.signa.app.domain.service

import org.signa.app.domain.model.Signal
import org.signa.app.domain.util.Result
import org.signa.app.domain.util.DataError

interface AiService {
    suspend fun analyzeSignals(signals: List<Signal>): Result<String, DataError>
}
