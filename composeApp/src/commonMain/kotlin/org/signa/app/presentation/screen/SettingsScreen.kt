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
