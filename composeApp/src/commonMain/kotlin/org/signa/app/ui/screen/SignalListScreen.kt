package org.signa.app.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.signa.app.domain.model.Signal
import org.signa.app.ui.components.FilterChipItem
import org.signa.app.ui.theme.GraphColors
import org.signa.app.ui.components.SignalListItem
@Composable
fun SignalListScreen(
    signals: List<Signal>,
    onSignalClick: (Signal) -> Unit
) {
    var filterType by remember { mutableStateOf<String?>(null) }
    var sortBy by remember { mutableStateOf("Priority") }

    val processedSignals = remember(signals, filterType, sortBy) {
        signals.filter { filterType == null || it.type.name == filterType }
            .sortedWith(
                compareByDescending<Signal> { it.isSuspicious }
                    .thenByDescending { it.strength > -50 }
                    .thenByDescending {
                        when (sortBy) {
                            "Strength" -> it.strength.toFloat()
                            else -> 0f
                        }
                    }
                    .thenBy { it.name }
            )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "SIGNAL RADAR",
                    style = MaterialTheme.typography.headlineMedium,
                    color = GraphColors.CyberNeon,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    "Monitoring ${signals.size} active nodes",
                    style = MaterialTheme.typography.labelSmall,
                    color = GraphColors.StarlightWhite.copy(alpha = 0.6f)
                )
            }

            val suspiciousCount = signals.count { it.isSuspicious }
            if (suspiciousCount > 0) {
                Surface(
                    color = GraphColors.AlertRed.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, GraphColors.AlertRed)
                ) {
                    Text(
                        "$suspiciousCount ALERTS",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = GraphColors.AlertRed,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("FILTERS & SORTING", color = GraphColors.StarlightWhite.copy(alpha = 0.4f), style = MaterialTheme.typography.labelSmall)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChipItem(selected = filterType == null, label = "All", onClick = { filterType = null })
            FilterChipItem(selected = filterType == "WIFI", label = "WiFi", onClick = { filterType = "WIFI" })
            FilterChipItem(selected = filterType == "CELLULAR", label = "Cellular", onClick = { filterType = "CELLULAR" })
            FilterChipItem(selected = filterType == "BLUETOOTH", label = "Bluetooth", onClick = { filterType = "BLUETOOTH" })
            FilterChipItem(selected = filterType == "OTHER", label = "Other", onClick = { filterType = "OTHER" })

            VerticalDivider(modifier = Modifier.height(32.dp).padding(horizontal = 4.dp), color = Color.Gray.copy(alpha = 0.3f))

            FilterChipItem(selected = sortBy == "Strength", label = "Sort: Power", onClick = { sortBy = "Strength" })
            FilterChipItem(selected = sortBy == "Priority", label = "Sort: Priority", onClick = { sortBy = "Priority" })
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(processedSignals, key = { it.id }) { signal ->
                SignalListItem(signal, onClick = { onSignalClick(signal) })
            }
        }
    }
}