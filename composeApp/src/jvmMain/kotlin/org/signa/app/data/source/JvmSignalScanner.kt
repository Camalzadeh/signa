package org.signa.app.data.source

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.signa.app.domain.model.Signal
import org.signa.app.domain.model.SignalPoint
import org.signa.app.domain.model.SignalType
import java.io.BufferedReader
import java.io.InputStreamReader

class JvmSignalScanner : SignalScanner {
    override fun startScanning(): Flow<List<Signal>> = flow {
        while (true) {
            val signals = mutableListOf<Signal>()
            val os = System.getProperty("os.name").lowercase()

            if (os.contains("win")) {
                signals.addAll(scanWindowsWifi())
            }
            // Qeyd: macOS və Linux üçün əlavə əmrlər (airport, nmcli) bura əlavə oluna bilər

            emit(signals)
            delay(10000) // 10 saniyədən bir skan et
        }
    }

    private fun scanWindowsWifi(): List<Signal> {
        val signals = mutableListOf<Signal>()
        try {
            val process = Runtime.getRuntime().exec("netsh wlan show networks mode=bssid")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?

            var currentSsid = ""
            var currentBssid = ""
            var currentSignalStrength = 0
            var currentType = ""

            while (reader.readLine().also { line = it } != null) {
                val trimmed = line?.trim() ?: ""
                when {
                    trimmed.startsWith("SSID") -> {
                        currentSsid = trimmed.substringAfter(":").trim()
                    }
                    trimmed.startsWith("BSSID") -> {
                        currentBssid = trimmed.substringAfter(":").trim()
                    }
                    trimmed.startsWith("org.signa.app.domain.model.Signal") -> {
                        val percentage = trimmed.substringAfter(":").trim().removeSuffix("%").toIntOrNull() ?: 0
                        // Faizdən dBm-ə çevirmə: dBm = (percentage / 2) - 100
                        currentSignalStrength = (percentage / 2) - 100

                        if (currentBssid.isNotEmpty()) {
                            signals.add(createSignal(currentSsid, currentBssid, currentSignalStrength))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return signals
    }

    private fun createSignal(name: String, id: String, strength: Int): Signal {
        return Signal(
            id = id,
            name = name.ifBlank { "Unknown Network" },
            type = SignalType.WIFI,
            strength = strength,
            macAddress = id,
            timestamp = System.currentTimeMillis(),
            isSuspicious = strength > -30,
            history = listOf(SignalPoint(System.currentTimeMillis(), strength))
        )
    }
}