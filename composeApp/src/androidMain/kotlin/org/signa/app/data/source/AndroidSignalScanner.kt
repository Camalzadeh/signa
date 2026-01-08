package org.signa.app.data.source

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import org.signa.app.domain.model.Signal
import org.signa.app.domain.model.SignalType

class AndroidSignalScanner(
    private val context: Context
) : SignalScanner {

    private val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    @SuppressLint("MissingPermission")
    override fun startScanning(): Flow<List<Signal>> = callbackFlow {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
                    val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
                    if (success) {
                        trySend(getWifiSignals())
                    } else {
                        // Keep sending old data or maybe just log?
                        trySend(getWifiSignals())
                    }
                }
            }
        }

        val intentFilter = IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        context.registerReceiver(receiver, intentFilter)

        // Initial scan
        wifiManager.startScan()
        
        // Periodic scan every 10 seconds to avoid heavy throttling but keep updates coming
        val scannerJob = launch {
            while(true) {
                delay(10000)
                wifiManager.startScan()
            }
        }

        awaitClose {
            scannerJob.cancel()
            context.unregisterReceiver(receiver)
        }
    }

    private fun getWifiSignals(): List<Signal> {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return emptyList()
        }

        return wifiManager.scanResults.map { scanResult ->
            val isSuspicious = scanResult.capabilities.contains("WEP") || scanResult.level > -40 
            Signal(
                id = scanResult.BSSID,
                type = SignalType.WIFI,
                name = scanResult.SSID.ifBlank { "Hidden Network" },
                strength = scanResult.level,
                macAddress = scanResult.BSSID,
                frequency = "${scanResult.frequency} MHz",
                timestamp = System.currentTimeMillis(), // Real-time timestamp
                isSuspicious = isSuspicious,
                // Generate dynamic graph data based on strength and timestamp to simulate wave
                graphData = generateGraphData(scanResult.BSSID, scanResult.level),
                rawData = mapOf(
                    "BSSID" to scanResult.BSSID,
                    "Capabilities" to scanResult.capabilities,
                    "Frequency" to scanResult.frequency.toString(),
                    "Channel Width" to (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) scanResult.channelWidth.toString() else "Unknown")
                )
            )
        }
    }
    
    private fun generateGraphData(seed: String, strength: Int): List<Float> {
        val hash = seed.hashCode()
        val timeShift = (System.currentTimeMillis() / 100.0).toFloat() // Animated phase shift
        val amplitude = (100 + strength) / 100f // Normalize strength (-100 to 0) to approx 0..1 scale (rough)
        
        return List(50) { index ->
             // Combine sine waves with time shift for animation
             val base = kotlin.math.sin((index * 0.2) + timeShift + hash) 
             // Modulate by amplitude and normalize to 0..1
             ((base * amplitude * 0.5) + 0.5).toFloat().coerceIn(0f, 1f)
        }
    }
}
