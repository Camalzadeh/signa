package org.signa.app.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.signa.app.domain.model.Signal
import org.signa.app.presentation.theme.GraphColors
import org.signa.app.presentation.components.GlassyCard

@Composable
fun SignalListScreen(
    signals: List<Signal>,
    onSignalClick: (Signal) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            "DETECTED SIGNALS",
            style = MaterialTheme.typography.headlineMedium,
            color = GraphColors.CyberNeon
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(signals) { signal ->
                SignalListItem(signal, onClick = { onSignalClick(signal) })
            }
        }
    }
}

@Composable
fun SignalListItem(signal: Signal, onClick: () -> Unit) {
    GlassyCard(modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            androidx.compose.ui.Alignment.CenterVertically
        ) {
            Column {
                if (signal.isSuspicious) {
                    Text("⚠️ SUSPICIOUS", color = GraphColors.AlertRed, style = MaterialTheme.typography.labelSmall)
                }
                Text(
                    text = signal.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = GraphColors.StarlightWhite
                )
                Text(
                    text = signal.type.name,
                    style = MaterialTheme.typography.labelSmall,
                    color = GraphColors.CyberNeon
                )
            }
            Text(
                text = "${signal.strength} dBm",
                style = MaterialTheme.typography.titleLarge,
                color = if (signal.strength > -60) GraphColors.SignalGreen else GraphColors.AlertRed.copy(alpha = 0.7f)
            )
        }
    }
}
