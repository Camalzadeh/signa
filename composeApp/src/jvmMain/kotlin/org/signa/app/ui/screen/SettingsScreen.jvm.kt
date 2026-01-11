package org.signa.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.signa.app.ui.theme.GraphColors

@Composable
actual fun PermissionStatusSection() {
    Card(
        colors = CardDefaults.cardColors(containerColor = GraphColors.DeepSpaceBlack.copy(alpha = 0.5f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, GraphColors.StarlightWhite.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "On Desktop, permissions are managed by the operating system settings.",
                color = GraphColors.StarlightWhite,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Status: SYSTEM CONTROLLED",
                color = GraphColors.CyberNeon,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}