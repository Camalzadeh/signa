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

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.runtime.*

@Composable
fun SignalListScreen(
    signals: List<Signal>,
    onSignalClick: (Signal) -> Unit
) {
    var filterType by remember { mutableStateOf<String?>(null) }
    var sortBy by remember { mutableStateOf("Strength") }
    
    val filteredSignals = signals.filter { 
        filterType == null || it.type.name == filterType 
    }.sortedWith(
        when (sortBy) {
            "Strength" -> compareByDescending { it.strength }
            "Suspicious" -> compareByDescending { it.isSuspicious }
            else -> compareBy { it.name }
        }
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            "DETECTED SIGNALS",
            style = MaterialTheme.typography.headlineMedium,
            color = GraphColors.CyberNeon
        )
        
        // Filter & Sort Row
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).horizontalScroll(rememberScrollState())) {
            FilterChip(
                selected = filterType == null,
                onClick = { filterType = null },
                label = { Text("All") },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = GraphColors.CyberNeon, selectedLabelColor = GraphColors.VoidBlack, labelColor = GraphColors.StarlightWhite)
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = filterType == "WIFI",
                onClick = { filterType = "WIFI" },
                label = { Text("WiFi") },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = GraphColors.CyberNeon, selectedLabelColor = GraphColors.VoidBlack, labelColor = GraphColors.StarlightWhite)
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = sortBy == "Strength",
                onClick = { sortBy = "Strength" },
                label = { Text("Sort: Strength") },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = GraphColors.CyberNeon, selectedLabelColor = GraphColors.VoidBlack, labelColor = GraphColors.StarlightWhite)
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = sortBy == "Suspicious",
                onClick = { sortBy = "Suspicious" },
                label = { Text("Sort: Alert") },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = GraphColors.AlertRed, selectedLabelColor = GraphColors.VoidBlack, labelColor = GraphColors.StarlightWhite)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredSignals) { signal ->
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
