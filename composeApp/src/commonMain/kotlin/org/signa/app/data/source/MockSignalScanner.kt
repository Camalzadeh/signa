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
            val signals = List(5) {
                Signal(
                    id = it.toString(),
                    type = SignalType.WIFI,
                    name = "Mock Wifi $it",
                    strength = Random.nextInt(-100, -30),
                    timestamp = System.currentTimeMillis()
                )
            }
            emit(signals)
            delay(2000)
        }
    }
}
