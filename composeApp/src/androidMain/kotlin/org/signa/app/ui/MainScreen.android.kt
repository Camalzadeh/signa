package org.signa.app.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import org.signa.app.ui.theme.GraphColors

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun PermissionGate(onPermissionsGranted: @Composable () -> Unit) {
    val multiplePermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_WIFI_STATE,
            android.Manifest.permission.CHANGE_WIFI_STATE,
            android.Manifest.permission.BLUETOOTH,
            android.Manifest.permission.BLUETOOTH_ADMIN,
            android.Manifest.permission.BLUETOOTH_SCAN,
            android.Manifest.permission.BLUETOOTH_CONNECT
        )
    )

    if (multiplePermissionsState.allPermissionsGranted) {
        onPermissionsGranted()
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                Text("Permissions Required", style = MaterialTheme.typography.headlineMedium, color = GraphColors.AlertRed)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Signa requires Location, WiFi, and Bluetooth permissions.", color = GraphColors.StarlightWhite)
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { multiplePermissionsState.launchMultiplePermissionRequest() },
                    colors = ButtonDefaults.buttonColors(containerColor = GraphColors.CyberNeon)
                ) {
                    Text("Grant Permissions", color = GraphColors.VoidBlack)
                }
            }
        }
    }
}