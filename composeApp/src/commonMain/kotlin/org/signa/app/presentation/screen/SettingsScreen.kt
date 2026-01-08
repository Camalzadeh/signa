package org.signa.app.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import com.google.accompanist.permissions.isGranted
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.signa.app.presentation.theme.GraphColors
import org.signa.app.presentation.components.AppFooter

@OptIn(com.google.accompanist.permissions.ExperimentalPermissionsApi::class)
@Composable
fun SettingsScreen() {
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

    val permissionState = com.google.accompanist.permissions.rememberMultiplePermissionsState(permissions.map { it.first })

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "SETTINGS",
            style = MaterialTheme.typography.headlineMedium,
            color = GraphColors.CyberNeon
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("PERMISSIONS STATUS", color = GraphColors.CyberNeon, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        
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

        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            "About Creator",
            style = MaterialTheme.typography.titleLarge,
            color = GraphColors.StarlightWhite
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            colors = CardDefaults.cardColors(containerColor = GraphColors.DeepSpaceBlack),
            border = androidx.compose.foundation.BorderStroke(1.dp, GraphColors.NebulaPurple)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Developer: Jamalzadeh", color = GraphColors.StarlightWhite)
                Text("Organization: Graph", color = GraphColors.CyberNeon)
                Text("App Version: 1.0.0", color = GraphColors.StarlightWhite.copy(alpha = 0.5f))
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        AppFooter()
    }
}
