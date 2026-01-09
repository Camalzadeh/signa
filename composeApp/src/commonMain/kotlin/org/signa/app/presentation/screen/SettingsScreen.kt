package org.signa.app.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.signa.app.presentation.theme.GraphColors
import org.signa.app.presentation.components.AppFooter

@Composable
expect fun PermissionStatusSection()

@Composable
fun SettingsScreen() {
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

        PermissionStatusSection()
        Spacer(modifier = Modifier.weight(1f))
        AppFooter()
    }
}