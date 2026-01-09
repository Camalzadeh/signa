package org.signa.app.data.source

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.*
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.telephony.*
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.signa.app.domain.model.*

class AndroidSignalScanner(
    private val context: Context
) : SignalScanner {

    private val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private val bluetoothAdapter: BluetoothAdapter? = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    private val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    @SuppressLint("MissingPermission")
    override fun startScanning(): Flow<List<Signal>> = callbackFlow {
        val signalsMap = mutableMapOf<String, Signal>()

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    WifiManager.SCAN_RESULTS_AVAILABLE_ACTION -> {
                        getWifiSignals().forEach { signalsMap[it.id] = it }
                    }
                    BluetoothDevice.ACTION_FOUND -> {
                        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                        val rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, -100).toInt()
                        device?.let {
                            val s = createBluetoothSignal(it, rssi)
                            signalsMap[s.id] = s
                        }
                    }
                }
                trySend(signalsMap.values.toList().sortedByDescending { it.strength })
            }
        }

        val intentFilter = IntentFilter().apply {
            addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
            addAction(BluetoothDevice.ACTION_FOUND)
        }
        context.registerReceiver(receiver, intentFilter)

        val scannerJob = launch {
            while (isActive) {
                wifiManager.startScan()

                if (bluetoothAdapter?.isDiscovering == true) bluetoothAdapter.cancelDiscovery()
                bluetoothAdapter?.startDiscovery()

                getCellSignals().forEach { signalsMap[it.id] = it }

                trySend(signalsMap.values.toList().sortedByDescending { it.strength })

                delay(12000)
            }
        }

        awaitClose {
            scannerJob.cancel()
            context.unregisterReceiver(receiver)
            bluetoothAdapter?.cancelDiscovery()
        }
    }

    private fun getWifiSignals(): List<Signal> {
        if (!hasPermissions()) return emptyList()
        if (ActivityCompat.checkSelfPermission(
                this.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return emptyList()
        return wifiManager.scanResults.map { res ->
            Signal(
                id = res.BSSID,
                type = SignalType.WIFI,
                name = res.SSID.ifBlank { "Unknown WiFi" },
                strength = res.level,
                macAddress = res.BSSID,
                frequency = "${res.frequency} MHz",
                timestamp = System.currentTimeMillis(),
                isSuspicious = res.capabilities.contains("WEP") || res.level > -30,
                history = listOf(SignalPoint(System.currentTimeMillis(), res.level)),
                rawData = mapOf("Cap" to res.capabilities, "Freq" to res.frequency.toString())
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun createBluetoothSignal(device: BluetoothDevice, rssi: Int): Signal {
        return Signal(
            id = device.address,
            type = SignalType.BLUETOOTH,
            name = device.name ?: "Unknown Bluetooth",
            strength = rssi,
            macAddress = device.address,
            frequency = "2.4 GHz",
            timestamp = System.currentTimeMillis(),
            isSuspicious = rssi > -40,
            history = listOf(SignalPoint(System.currentTimeMillis(), rssi)),
            rawData = mapOf("Type" to device.type.toString(), "Class" to (device.bluetoothClass?.toString() ?: ""))
        )
    }

    @SuppressLint("MissingPermission")
    private fun getCellSignals(): List<Signal> {
        if (!hasPermissions()) return emptyList()
        val cellInfos = telephonyManager.allCellInfo ?: return emptyList()

        return cellInfos.map { info ->
            // ID təyini zamanı 5G-ni yalnız API 29+ üçün yoxlayırıq
            val id = when {
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q && info is CellInfoNr -> {
                    "5G-${info.hashCode()}"
                }
                info is CellInfoLte -> "LTE-${info.cellIdentity.ci}"
                info is CellInfoGsm -> "GSM-${info.cellIdentity.cid}"
                else -> "CELL-${info.hashCode()}"
            }

            val dbm = when {
                // 5G gücünü yalnız API 29+ cihazlarda oxuyuruq
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q && info is CellInfoNr -> {
                    info.cellSignalStrength.dbm
                }
                info is CellInfoLte -> info.cellSignalStrength.dbm
                info is CellInfoGsm -> info.cellSignalStrength.dbm
                else -> -110
            }

            Signal(
                id = id,
                type = SignalType.CELLULAR,
                name = "Cell Tower (${getCellType(info)})",
                strength = dbm,
                macAddress = "N/A",
                frequency = "Mobile Band",
                timestamp = System.currentTimeMillis(),
                isSuspicious = false,
                history = listOf(SignalPoint(System.currentTimeMillis(), dbm)),
                rawData = mapOf("Registered" to info.isRegistered.toString())
            )
        }
    }

    private fun getCellType(info: CellInfo): String {
        return when {
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q && info is CellInfoNr -> "5G/NR"
            info is CellInfoLte -> "4G/LTE"
            info is CellInfoWcdma -> "3G/WCDMA"
            info is CellInfoGsm -> "2G/GSM"
            else -> "Unknown"
        }
    }


    private fun hasPermissions() = ActivityCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}