package org.signa.app.data.source

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.signa.app.domain.model.Signal
import org.signa.app.domain.model.SignalType
import kotlin.random.Random

class MockSignalScanner : SignalScanner {
    override fun startScanning(): Flow<List<Signal>> = flow {
        while (true) {
            val signals = List(15) { index ->
                val type = SignalType.values().random()
                val isSuspicious = type == SignalType.OTHER && Random.nextBoolean()
                
                Signal(
                    id = index.toString(),
                    type = type,
                    name = if (isSuspicious) "Unknown signal source ${Random.nextInt(1000, 9999)}" else "Mock ${type.name} $index",
                    strength = Random.nextInt(-100, -30),
                    isSuspicious = isSuspicious,
                    graphData = generateMockGraphData(),
                    timestamp = System.currentTimeMillis()
                )
            }.sortedByDescending { it.isSuspicious } // Dangerous on top

            emit(signals)
            delay(3000)
        }
    }

    private fun generateMockGraphData(): List<Float> {
        return List(50) {
            (kotlin.math.sin(it * 0.2) + kotlin.math.cos(it * 0.5) + Random.nextDouble(0.0, 0.5)).toFloat().coerceIn(0f, 1f)
        }
    }
}
