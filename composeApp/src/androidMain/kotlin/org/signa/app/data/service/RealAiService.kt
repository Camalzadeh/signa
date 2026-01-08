package org.signa.app.data.service

import com.google.ai.client.generativeai.GenerativeModel
import org.signa.app.BuildConfig
import org.signa.app.domain.model.Signal
import org.signa.app.domain.service.AiService
import org.signa.app.domain.util.DataError
import org.signa.app.domain.util.Result

class RealAiService : AiService {
    // Requires GEMINI_API_KEY in local.properties
    private val apiKey = BuildConfig.GEMINI_API_KEY
    
    private val generativeModel by lazy {
        GenerativeModel(
            modelName = "gemini-pro",
            apiKey = apiKey
        )
    }

    override suspend fun analyzeSignals(signals: List<Signal>): Result<String, DataError> {
        if (apiKey.isBlank()) {
            return Result.Error(DataError.PERMISSION_DENIED)
        }

        return try {
            val signalDescription = signals.joinToString("\n") { signal ->
                """
                - Signal: ${signal.name} (${signal.type})
                  Strength: ${signal.strength} dBm
                  Frequency: ${signal.frequency ?: "Unknown"}
                  Suspicious: ${signal.isSuspicious}
                  Raw Data: ${signal.rawData}
                """.trimIndent()
            }

            val prompt = """
                Analyze the following radio signals detected in the environment. 
                Identify potential threats, security risks, or anomalies.
                
                Signals:
                $signalDescription
                
                Provide a concise security report.
            """.trimIndent()

            val response = generativeModel.generateContent(prompt)
            val text = response.text
            
            if (text != null) {
                Result.Success(text)
            } else {
                Result.Error(DataError.SERVER_ERROR)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(DataError.UNKNOWN)
        }
    }
}
