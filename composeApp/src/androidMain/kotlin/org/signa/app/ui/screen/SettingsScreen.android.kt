package org.signa.app.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import org.signa.app.ui.theme.GraphColors

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun PermissionStatusSection() {
    val permissions = listOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION to "Fine Location",
        android.Manifest.permission.ACCESS_COARSE_LOCATION to "Coarse Location",
        android.Manifest.permission.ACCESS_WIFI_STATE to "WiFi State",
        android.Manifest.permission.CHANGE_WIFI_STATE to "Change WiFi",
        android.Manifest.permission.BLUETOOTH to "Bluetooth",
        android.Manifest.permission.BLUETOOTH_ADMIN to "Bluetooth Admin",
        android.Manifest.permission.BLUETOOTH_SCAN to "Bluetooth Scan",
        android.Manifest.permission.BLUETOOTH_CONNECT to "Bluetooth Connect"
    )

    val permissionState = rememberMultiplePermissionsState(permissions.map { it.first })

    Card(
        colors = CardDefaults.cardColors(containerColor = GraphColors.DeepSpaceBlack.copy(alpha = 0.5f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, GraphColors.StarlightWhite.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            permissions.forEach { (permission, name) ->
                val isGranted = permissionState.permissions.find { it.permission == permission }?.status?.isGranted ?: false
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            if (!isGranted) {
                                permissionState.launchMultiplePermissionRequest()
                            }
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(name, color = GraphColors.StarlightWhite)
                    Text(
                        if (isGranted) "GRANTED" else "DENIED - TAP TO GRANT",
                        color = if (isGranted) GraphColors.SignalGreen else GraphColors.AlertRed,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}