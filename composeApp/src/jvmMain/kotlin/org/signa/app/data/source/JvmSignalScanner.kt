package org.signa.app.data.source

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.signa.app.domain.model.Signal
import org.signa.app.domain.model.SignalPoint
import org.signa.app.domain.model.SignalType
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.Clock
import java.time.ZoneId
class JvmSignalScanner : SignalScanner {
    override fun startScanning(): Flow<List<Signal>> = flow {
        while (true) {
            val signals = mutableListOf<Signal>()
            val os = System.getProperty("os.name").lowercase()

            if (os.contains("win")) {
                signals.addAll(scanWindowsWifi())
            }


            if (signals.isEmpty()) {
                signals.add(createSignal("Test_Mock_Network", "00:11:22:33:44:55", -45))
            }

            emit(signals)
            delay(5000)
        }
    }.flowOn(Dispatchers.IO)

    private fun scanWindowsWifi(): List<Signal> {
        val signals = mutableListOf<Signal>()
        try {
            val process = Runtime.getRuntime().exec("netsh wlan show networks mode=bssid")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?

            var currentSsid = ""
            var currentBssid = ""

            while (reader.readLine().also { line = it } != null) {
                val trimmed = line?.trim() ?: ""

                when {
                    trimmed.startsWith("SSID") -> {
                        currentSsid = trimmed.substringAfter(":").trim()
                        if (currentSsid.isEmpty()) currentSsid = "Hidden Network"
                    }

                    trimmed.startsWith("BSSID") -> {
                        currentBssid = trimmed.substringAfter(":").trim()
                    }

                    trimmed.startsWith("Signal") -> {
                        val percentage = trimmed.substringAfter(":").trim()
                            .removeSuffix("%").toIntOrNull() ?: 0
                        val dbm = (percentage / 2) - 100

                        if (currentBssid.isNotEmpty()) {
                            signals.add(createSignal(currentSsid, currentBssid, dbm))
                            currentBssid = ""
                        }
                    }
                }
            }
        } catch (e: Exception) {
            println("SCAN_ERROR: ${e.message}")
        }
        return signals
    }
    private fun createSignal(name: String, id: String, strength: Int): Signal {
        val now = System.currentTimeMillis()
        return Signal(
            id = id,
            name = name.ifBlank { "Unknown Network" },
            type = SignalType.WIFI,
            strength = strength,
            macAddress = id,
            timestamp = now,
            firstSeen = now,
            lastSeen = now,
            isSuspicious = strength > -25,
            history = listOf(SignalPoint(now, strength))
        )
    }
}