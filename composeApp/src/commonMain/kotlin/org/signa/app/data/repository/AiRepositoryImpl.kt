package org.signa.app.data.repository

import org.signa.app.data.dao.AiDao
import org.signa.app.data.mapper.toDomain
import org.signa.app.data.mapper.toEntity
import org.signa.app.data.service.AiService
import org.signa.app.domain.model.AiAnalysis
import org.signa.app.domain.model.Signal
import org.signa.app.domain.repository.AiRepository
import org.signa.app.domain.core.Result
import org.signa.app.domain.core.DataError

class AiRepositoryImpl(
    private val aiDao: AiDao,
    private val aiService: AiService
) : AiRepository {

    override suspend fun getAnalysis(signal: Signal, forceRefresh: Boolean): Result<AiAnalysis, DataError> {
        if (!forceRefresh) {
            val cachedEntity = aiDao.getAnalysisForSignal(signal.id)
            if (cachedEntity != null) {
                return Result.Success(cachedEntity.toDomain())
            }
        }

        return when (val apiResult = aiService.analyzeSignals(listOf(signal))) {
            is Result.Success -> {
                val newAnalysis = AiAnalysis(
                    signalId = signal.id,
                    analysisText = apiResult.data,
                    timestamp = System.currentTimeMillis()
                )
                aiDao.saveAnalysis(newAnalysis.toEntity())

                Result.Success(newAnalysis)
            }
            is Result.Error -> Result.Error(apiResult.error)
        }
    }

    override suspend fun clearOldAnalyses() {
        aiDao.deleteAllAnalyses()
    }
}