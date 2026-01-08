package org.signa.app.data.service

import kotlinx.coroutines.delay
import org.signa.app.domain.model.Signal
import org.signa.app.domain.service.AiService
import org.signa.app.domain.util.Result
import org.signa.app.domain.util.DataError

class MockAiService : AiService {
    override suspend fun analyzeSignals(signals: List<Signal>): Result<String, DataError> {
        delay(1500) // Simulate network delay
        val wifiCount = signals.count { it.type == org.signa.app.domain.model.SignalType.WIFI }
        val strongSignals = signals.count { it.strength > -50 }
        
        return Result.Success(
            """
            Analysis Result:
            - Detected $wifiCount WiFi networks.
            - $strongSignals signals are strong and reliable.
            - Environment appears to be a typical residential or office area.
            - Recommendation: Connect to 'Mock Wifi 0' for best performance.
            """.trimIndent()
        )
    }
}
