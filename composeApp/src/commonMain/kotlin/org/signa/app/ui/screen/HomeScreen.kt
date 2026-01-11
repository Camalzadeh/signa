package org.signa.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.signa.app.domain.model.Signal
import org.signa.app.ui.theme.GraphColors
import org.signa.app.ui.components.SignalRadar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(signals: List<Signal>) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { 
                Text(
                    "SIGNA DASHBOARD", 
                    style = MaterialTheme.typography.headlineMedium,
                    color = GraphColors.CyberNeon,
                    letterSpacing = 2.sp
                ) 
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = androidx.compose.ui.graphics.Color.Transparent
            )
        )

        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            SignalRadar(modifier = Modifier.size(300.dp))
            
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 350.dp)) {
                Text(
                    "${signals.size} TARGETS ACTIVE",
                    color = GraphColors.StarlightWhite,
                    style = MaterialTheme.typography.titleLarge
                )
                val suspiciousCount = signals.count { it.isSuspicious }
                if (suspiciousCount > 0) {
                    Text(
                        "$suspiciousCount THREATS DETECTED",
                        color = GraphColors.AlertRed,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
